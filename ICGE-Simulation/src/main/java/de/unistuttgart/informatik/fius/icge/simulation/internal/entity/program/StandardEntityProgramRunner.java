/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.internal.entity.program;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import java.util.concurrent.ForkJoinWorkerThread;

import de.unistuttgart.informatik.fius.icge.log.Logger;
import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;
import de.unistuttgart.informatik.fius.icge.simulation.entity.program.EntityProgramRunner;
import de.unistuttgart.informatik.fius.icge.simulation.entity.program.EntityProgramState;
import de.unistuttgart.informatik.fius.icge.simulation.entity.program.RunningProgramInfo;
import de.unistuttgart.informatik.fius.icge.simulation.exception.CannotRunProgramException;
import de.unistuttgart.informatik.fius.icge.simulation.exception.UncheckedInterruptedException;


/**
 * The standard implementation of {@link EntityProgramRunner}.
 * 
 * @author Tim Neumann
 */
public class StandardEntityProgramRunner implements EntityProgramRunner {
    
    private final StandardEntityProgramRegistry registry;
    
    private final ExecutorService executor;
    
    private final Map<String, EntityProgramRunningInfo> singlePrograms = new HashMap<>();
    private final Map<Entity, EntityProgramRunningInfo> entityPrograms = new HashMap<>();
    
    private ExecutorService createExecutor() {
        final ForkJoinWorkerThreadFactory factory = new ForkJoinWorkerThreadFactory() {
            @Override
            public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
                final ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
                worker.setName("EntityProgramRunnerThread-" + worker.getPoolIndex());
                return worker;
            }
        };
        
        return new ForkJoinPool(Runtime.getRuntime().availableProcessors(), factory, null, false);
    }
    
    /**
     * Create a new StandardEntityProgramRunner.
     * 
     * @param registry
     *     The EntityProgramRegistry to use
     */
    public StandardEntityProgramRunner(final StandardEntityProgramRegistry registry) {
        this.registry = registry;
        this.executor = createExecutor();
    }
    
    private EntityProgramRunningInfo getSingleInstanceProgramInfo(final String programName) {
        if (!this.singlePrograms.containsKey(programName)) {
            if (!this.registry.checkIfProgramHasFactory(programName)) {
                this.singlePrograms.put(programName, new EntityProgramRunningInfo(this.registry.getEntityProgram(programName)));
            }
        }
        return this.singlePrograms.get(programName);
    }
    
    private EntityProgramRunningInfo getProgramInfo(final String programName) {
        final EntityProgramRunningInfo programInfo = this.getSingleInstanceProgramInfo(programName);
        if (programInfo != null) return programInfo;
        if (this.registry.checkIfProgramHasFactory(programName)) {
            return new EntityProgramRunningInfo(this.registry.getEntityProgram(programName));
        }
        throw new NoSuchElementException("No Program registered with the name \"" + programName + "\"!");
    }
    
    @Override
    public EntityProgramState getState(final String program) {
        if (program == null) throw new IllegalArgumentException("Argument is null.");
        EntityProgramRunningInfo singleProgramInstance = this.getSingleInstanceProgramInfo(program);
        if (singleProgramInstance != null) return singleProgramInstance.getState();
        boolean hasFactory = this.registry.checkIfProgramHasFactory(program);
        if (hasFactory) return EntityProgramState.IS_FACTORY;
        throw new IllegalStateException("Program should either have a specific instance or be instantiated by a factory!");
    }
    
    private boolean canRunProgram(final EntityProgramRunningInfo info) {
        return info.getState() == EntityProgramState.NEW;
    }
    
    @Override
    public boolean canRunProgram(final String program) {
        if (program == null) throw new IllegalArgumentException("Argument is null.");
        return this.canRunProgram(this.getProgramInfo(program));
    }
    
    public boolean entityCanRunProgram(final Entity entity) {
        EntityProgramRunningInfo oldInfo = this.entityPrograms.get(entity);
        // only allow new programs to run on the entity if the last finished without exception!
        return (oldInfo == null) || oldInfo.getState().equals(EntityProgramState.FINISHED);
    }
    
    private boolean canRunProgramOn(final EntityProgramRunningInfo info, final Entity entity) {
        if (!this.entityCanRunProgram(entity)) return false;
        if (!this.canRunProgram(info)) return false;
        return info.getProgram().canRunOn(entity);
    }
    
    @Override
    public boolean canRunProgramOn(final String program, final Entity entity) {
        if ((program == null) || (entity == null)) throw new IllegalArgumentException("Argument is null.");
        return this.canRunProgramOn(this.getProgramInfo(program), entity);
    }
    
    @Override
    public void run(final String program, final Entity entity) {
        if ((program == null) || (entity == null)) throw new IllegalArgumentException("Argument is null.");
        
        final var info = this.getProgramInfo(program);
        
        if (!this.canRunProgramOn(info, entity)) throw new CannotRunProgramException();
        
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            try {
                info.getProgram().run(entity);
                info.setState(EntityProgramState.FINISHED);
            } catch (@SuppressWarnings("unused") final UncheckedInterruptedException e) {
                info.setState(EntityProgramState.KILLED);
            } catch (@SuppressWarnings("unused") CancellationException e) {
                //Simulation was stopped.
                //Log would be printed into log panel of new task because of concurrency.
                //I won't bother fixing this now, because log would get cleared immediately anyway.
                //TODO: If a way is added to see/use the log messages of an old simulation we need to fix this
                info.setState(EntityProgramState.KILLED);
            } catch (Exception e) {
                Logger.simout.println("----------------------------------------------");
                Logger.simout.println("The following exception happened in program " + program + " running on entity " + entity.toString());
                e.printStackTrace(Logger.simout);
                Logger.simout.println("----------------------------------------------");
                info.setState(EntityProgramState.KILLED);
            }
            return null;
        }, this.executor);
        
        info.setFuture(future);
        info.setState(EntityProgramState.RUNNING);
        
        // set the running program in the entityMap
        this.entityPrograms.put(entity, info);
    }
    
    @Override
    public RunningProgramInfo getRunningProgramInfo(Entity entity) {
        if (entity == null) throw new IllegalArgumentException("Entity cannot be null!");
        if (
            !this.entityPrograms.containsKey(entity)
        ) throw new NoSuchElementException("No running program for entity " + entity.toString() + "found!");
        return this.entityPrograms.get(entity);
    }
    
    @Override
    public void forceStop() {
        for (final EntityProgramRunningInfo info : this.entityPrograms.values()) {
            info.getFuture().cancel(true);
        }
    }
    
}

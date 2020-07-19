/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.internal.programs;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;

import de.unistuttgart.informatik.fius.icge.simulation.programs.Program;
import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;
import de.unistuttgart.informatik.fius.icge.simulation.exception.UncheckedInterruptedException;


/**
 * The standard runner for {@link Program} instances.
 *
 * @author Fabian Bühler
 */
public class StandardProgramRunner {
    
    private ExecutorService executor;
    
    private final Map<Entity, CompletableFuture<Void>> runningPrograms = new HashMap<>();
    
    /**
     * Standard constructor setting up the executor for the Futures.
     */
    public StandardProgramRunner() {
        this.executor = this.createExecutor();
    }
    
    /**
     * Create an executor that uses named threads for a better debugging experience,
     * 
     * @return the executor service
     */
    private ExecutorService createExecutor() {
        final ForkJoinWorkerThreadFactory factory = pool -> {
            final ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
            worker.setName("ProgramThread-" + worker.getPoolIndex());
            worker.isDaemon();
            return worker;
        };
        
        return new ForkJoinPool(Runtime.getRuntime().availableProcessors(), factory, null, false);
    }
    
    /**
     * Run a program for the given entity inside a completable future.
     * 
     * Only one program can bu run for a each entity.
     * 
     * @param <E>
     *     The subtype of Entity that the Program accepts
     * @param <S>
     *     The type of the Entity to run the program for. Must be a subtype of E
     * @param program
     *     The program to run
     * @param entity
     *     The Entity to run the program for
     */
    public <E extends Entity, S extends E> void run(final Program<E> program, final S entity) {
        if ((program == null) || (entity == null)) throw new IllegalArgumentException("Argument is null.");
        if (this.runningPrograms.containsKey(entity)) {
            if (!this.runningPrograms.get(entity).isDone()) {
                // only throw exception if last program is still running
                throw new IllegalStateException("Already running a program for entity " + entity.toString() + "!");
            }
        }
        
        final CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                program.run(entity);
            } catch (@SuppressWarnings("unused") final UncheckedInterruptedException e) {
                System.out.println("The running program " + program.toString() + " for entity " + entity.toString() + " was stopped.");
            } catch (@SuppressWarnings("unused") final CancellationException e) {
                System.out.println("The running program " + program.toString() + " for entity " + entity.toString() + " was stopped.");
            } catch (final Exception e) {
                System.out.println("----------------------------------------------");
                System.out.println("The following exception happened while running a program for the entity " + entity.toString());
                e.printStackTrace();
                System.out.println("----------------------------------------------");
            }
        }, this.executor);
        
        // set the running program in the entityMap
        this.runningPrograms.put(entity, future);
    }
    
    /**
     * Get the running program as a CompletableFuture.
     *
     * @param entity
     *     the entity to get the program for
     * @return the CompletableFuture in which the program is wrapped (or null)
     */
    public CompletableFuture<Void> getRunningProgram(Entity entity) {
        return this.runningPrograms.get(entity);
    }
    
    /**
     * Stop all running programs.
     */
    public void stopAll() {
        // the completable futures will not be interrupted by cancel(true)
        // see https://github.com/vsilaev/tascalate-concurrent
        for (final CompletableFuture<Void> future : this.runningPrograms.values()) {
            future.cancel(true);
        }
        ExecutorService oldExecutor = this.executor;
        this.executor = this.createExecutor();
        oldExecutor.shutdownNow(); // this interrupts all threads of this executor
        this.runningPrograms.clear();
    }
}

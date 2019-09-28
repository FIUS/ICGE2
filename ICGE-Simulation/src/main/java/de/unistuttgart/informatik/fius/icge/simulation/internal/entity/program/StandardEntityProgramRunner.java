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

import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;
import de.unistuttgart.informatik.fius.icge.simulation.entity.program.EntityProgramRegistry;
import de.unistuttgart.informatik.fius.icge.simulation.entity.program.EntityProgramRunner;
import de.unistuttgart.informatik.fius.icge.simulation.entity.program.EntityProgramState;
import de.unistuttgart.informatik.fius.icge.simulation.exception.CannotRunProgramException;
import de.unistuttgart.informatik.fius.icge.simulation.exception.UncheckedInterruptedException;


/**
 * The standard implementation of {@link EntityProgramRunner}.
 * 
 * @author Tim Neumann
 */
public class StandardEntityProgramRunner implements EntityProgramRunner {
    
    private final EntityProgramRegistry registry;
    
    private final Map<String, EntityProgramRunningInfo> programs = new HashMap<>();
    
    /**
     * Create a new StandardEntityProgramRunner.
     * 
     * @param registry
     *     The EntityProgramRegistry to use
     */
    public StandardEntityProgramRunner(final EntityProgramRegistry registry) {
        this.registry = registry;
    }
    
    private EntityProgramRunningInfo getInfo(final String program) {
        if (!this.programs.containsKey(program)) {
            this.programs.put(program, new EntityProgramRunningInfo(this.registry.getEntityProgram(program)));
        }
        return this.programs.get(program);
    }
    
    @Override
    public EntityProgramState getState(final String program) {
        if (program == null) throw new IllegalArgumentException("Argument is null.");
        return this.getInfo(program).getState();
    }
    
    private boolean canRunProgram(final EntityProgramRunningInfo info) {
        return info.getState() == EntityProgramState.NEW;
    }
    
    @Override
    public boolean canRunProgram(final String program) {
        if (program == null) throw new IllegalArgumentException("Argument is null.");
        return this.canRunProgram(this.getInfo(program));
    }
    
    private boolean canRunProgramOn(final EntityProgramRunningInfo info, final Entity entity) {
        if (!this.canRunProgram(info)) return false;
        return info.getProgram().canRunOn(entity);
    }
    
    @Override
    public boolean canRunProgramOn(final String program, final Entity entity) {
        if ((program == null) || (entity == null)) throw new IllegalArgumentException("Argument is null.");
        return this.canRunProgramOn(this.getInfo(program), entity);
    }
    
    @Override
    public void run(final String program, final Entity entity) {
        if ((program == null) || (entity == null)) throw new IllegalArgumentException("Argument is null.");
        
        final var info = this.getInfo(program);
        
        if (!this.canRunProgramOn(info, entity)) throw new CannotRunProgramException();
        
        final String threadName = "EntityProgramRunner" + "_" + program + "_on_" + entity.toString();
        final Thread thread = new Thread(() -> {
            try {
                info.getProgram().run(entity);
                info.setState(EntityProgramState.FINISHED);
            } catch (@SuppressWarnings("unused") final UncheckedInterruptedException e) {
                info.setState(EntityProgramState.KILLED);
            }
        }, threadName);
        
        info.setThread(thread);
        info.setState(EntityProgramState.RUNNING);
        thread.start();
    }
    
    @Override
    public void forceStop() {
        for (final EntityProgramRunningInfo info : this.programs.values()) {
            info.getThread().interrupt();
        }
    }
    
}

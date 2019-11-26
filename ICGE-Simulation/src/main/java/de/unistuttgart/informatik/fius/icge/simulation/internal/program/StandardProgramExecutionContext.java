/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.internal.program;

import de.unistuttgart.informatik.fius.icge.log.Logger;
import de.unistuttgart.informatik.fius.icge.simulation.entity.ProgrammableEntity;
import de.unistuttgart.informatik.fius.icge.simulation.entity.program.EntityProgram;
import de.unistuttgart.informatik.fius.icge.simulation.entity.program.ProgramExecutionContext;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledFuture;


/**
 * <p>
 * This class encapsulates a shared execution context for EntityProgram instances for entities that are currently on a
 * playfield. Programs are scheduled to run every 500 milliseconds on a shared thread pool with 5 threads.
 * </p>
 */
public class StandardProgramExecutionContext implements ProgramExecutionContext {
    
    private static final long PROGRAM_SCHEDULING_RATE = 500;
    
    private final ScheduledExecutorService                       nativeContext   = Executors.newScheduledThreadPool(5);
    private final Map<ProgrammableEntity<?>, ScheduledFuture<?>> runningPrograms = new HashMap<>();
    
    @Override
    public void add(ProgrammableEntity<?> ent) {
        if (this.runningPrograms.containsKey(ent)) {
            Logger.simout.println("Attempted to run the program of an entity multiple times");
            return;
        }
        @SuppressWarnings("unchecked") // this is the lowest common denominator
        EntityProgram<ProgrammableEntity<?>> program = (EntityProgram<ProgrammableEntity<?>>) ent.attachedProgram();
        if (program == null) {
            return;
        }
        var scheduled = this.nativeContext.scheduleAtFixedRate(() -> program.run(ent), 0, PROGRAM_SCHEDULING_RATE, TimeUnit.MILLISECONDS);
        this.runningPrograms.put(ent, scheduled);
    }
    
    @Override
    public void remove(ProgrammableEntity<?> ent) {
        var scheduled = this.runningPrograms.remove(ent);
        // no need to forcibly interrupt the running process
        scheduled.cancel(false);
    }
}

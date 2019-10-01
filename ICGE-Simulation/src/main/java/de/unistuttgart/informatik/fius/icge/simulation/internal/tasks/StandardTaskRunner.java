/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.internal.tasks;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.FutureTask;

import de.unistuttgart.informatik.fius.icge.simulation.Simulation;
import de.unistuttgart.informatik.fius.icge.simulation.tasks.Task;


/**
 * The standard implementation of {@link TaskRunner}.
 * 
 * @author Tim Neumann
 */
public class StandardTaskRunner {
    
    private final Task       taskToRun;
    private final Simulation sim;
    
    public StandardTaskRunner(final Task taskToRun, final Simulation sim) {
        if ((taskToRun == null) || (sim == null)) throw new IllegalArgumentException("Argument is null.");
        this.taskToRun = taskToRun;
        this.sim = sim;
    }
    
    /**
     * Run the given task and verify the solution.
     * 
     * @return true if the task was completed successfully and the solution could be verified
     */
    public CompletableFuture<Boolean> runTask() {
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(this::executeTask);
        
        return future;
    }
    
    private boolean executeTask() {
        this.taskToRun.prepare(sim);
        this.taskToRun.solve();
        return this.taskToRun.verify();
    }
}

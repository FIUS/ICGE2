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

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;

import de.unistuttgart.informatik.fius.icge.log.Logger;
import de.unistuttgart.informatik.fius.icge.simulation.Simulation;
import de.unistuttgart.informatik.fius.icge.simulation.tasks.Task;


/**
 * The standard runner for {@link Task} instances.
 * 
 * @author Tim Neumann
 */
public class StandardTaskRunner {
    
    private final Task       taskToRun;
    private final Simulation sim;
    
    /**
     * Create a new task runner.
     * 
     * @param taskToRun
     *     The Task instance to run
     * @param sim
     *     The simulation to run this Task instance with
     */
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
        this.taskToRun.prepare(this.sim);
        try {
            this.taskToRun.solve();
        } catch (@SuppressWarnings("unused") CancellationException e) {
            //Simulation was stopped before completion of the task.
            //Log would be printed into log panel of new task because of concurrency.
            //I won't bother fixing this now, because log would get cleared immediately anyway.
            //TODO: If a way is added to see/use the log messages of an old simulation we need to fix this
            return false;
        } catch (Exception e) {
            Logger.simout.println("----------------------------------------------");
            Logger.simout.println("The following exception caused a task failure:");
            e.printStackTrace(Logger.simout);
            Logger.simout.println("----------------------------------------------");
            return false;
        }
        
        boolean verified = this.taskToRun.verify();
        if (verified) {
            Logger.simout.println("----------------------------------------------");
            Logger.simout.println("Task completed successfully");
            Logger.simout.println("----------------------------------------------");
        } else {
            Logger.simout.println("----------------------------------------------");
            Logger.simout.println("Task failed");
            Logger.simout.println("----------------------------------------------");
        }
        return verified;
    }
}

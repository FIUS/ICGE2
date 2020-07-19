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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import de.unistuttgart.informatik.fius.icge.simulation.Simulation;
import de.unistuttgart.informatik.fius.icge.simulation.exception.UncheckedInterruptedException;
import de.unistuttgart.informatik.fius.icge.simulation.tasks.Task;


/**
 * The standard runner for {@link Task} instances.
 *
 * @author Tim Neumann
 */
public class StandardTaskRunner {
    
    private final ExecutorService executor;
    
    private final Task       taskToRun;
    private final Simulation sim;
    
    private CompletableFuture<Boolean> taskResult;
    
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
        final ThreadFactory factory = runnable -> {
            Thread worker = new Thread(runnable, "TaskThread-" + taskToRun.toString());
            return worker;
        };
        
        // only one task can be run per task runner so parallelism of one is ok
        this.executor = Executors.newSingleThreadExecutor(factory);
    }
    
    /**
     * Run the given task
     *
     * @return true if the task was completed successfully, false if an exception was thrown in the run method
     */
    public CompletableFuture<Boolean> runTask() {
        if (this.taskResult != null) return this.taskResult;
        this.taskResult = CompletableFuture.supplyAsync(this::executeTask, this.executor);
        
        return this.taskResult;
    }
    
    private boolean executeTask() {
        try {
            this.taskToRun.run(this.sim);
            return true;
        } catch (@SuppressWarnings("unused") final UncheckedInterruptedException e) {
            // task was interrupted on simulation.stop()
            System.out.println("----------------------------------------------");
            System.out.println("The task " + this.taskToRun.toString() + " was aborted.");
            System.out.println("----------------------------------------------");
            return false;
        } catch (final CancellationException e) {
            //Simulation was stopped before completion of the task.
            System.out.println("----------------------------------------------");
            System.out.println("The task " + this.taskToRun.toString() + " was aborted.");
            e.printStackTrace();
            System.out.println("----------------------------------------------");
            return false;
        } catch (final Exception e) {
            System.out.println("----------------------------------------------");
            System.out.println("The following exception caused the task " + this.taskToRun.toString() + " to fail:");
            e.printStackTrace();
            System.out.println("----------------------------------------------");
            return false;
        }
    }
    
    /**
     * Cancel the completable future and intterupt the underlying thread.
     */
    public void cancel() {
        this.taskResult.cancel(true);
        this.executor.shutdownNow();
    }
}

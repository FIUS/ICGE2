/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.tasks;

import de.unistuttgart.informatik.fius.icge.simulation.Simulation;


/**
 * The interface
 * 
 * @author Tim Neumann
 */
public interface TaskRunner {
    /**
     * Run the given task and verify the solution.
     * 
     * @param taskToRun
     *     The task to run; must not be null; must be possible to be instantiated without an argument
     * @param sim
     *     The simulation to run the task in; must not be null
     * @return true if the task was completed successfully and the solution could be verified
     * @throws IllegalArgumentException
     *     if the argument is null or an error occurs during instantiation
     */
    boolean runTask(Class<? extends Task> taskToRun, Simulation sim);
}

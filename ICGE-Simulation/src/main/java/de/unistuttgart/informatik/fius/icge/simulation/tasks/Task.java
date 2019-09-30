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
 * The interface for a task to be solved by students.
 * 
 * @author Tim Neumann
 */
public interface Task {
    /**
     * Prepare the simulation for this task
     * 
     * @param sim
     *     The simulation this task should be prepared in
     */
    void prepare(Simulation sim);
    
    /**
     * Solve the task
     */
    void solve();
    
    /**
     * Verify that the task was solved correctly.
     * 
     * @return true if the task was solved correctly
     */
    boolean verify();
}

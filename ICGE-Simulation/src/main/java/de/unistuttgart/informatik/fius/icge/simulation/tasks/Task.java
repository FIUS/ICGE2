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
 *
import de.unistuttgart.informatik.fius.icge.simulation.Simulation;
 *
 *
/**
 * The interface for a task to be solved by students.
 *
 * @author Tim Neumann
 */
public interface Task {
    /**
     * Run the task in the given simulation
     *
     * @param sim
     *     The simulation this task should run in
     */
    void run(Simulation sim);
}

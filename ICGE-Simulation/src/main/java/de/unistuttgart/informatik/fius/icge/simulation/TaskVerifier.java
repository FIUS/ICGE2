/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 *
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation;

import de.unistuttgart.informatik.fius.icge.ui.TaskInformation;


/**
 * The interface for a verifier of a task.
 *
 * @author Fabian Bühler
 */
public interface TaskVerifier {
    /**
     * Attach the verifier to a simulation.
     * <p>
     * Must be called by the simulation or the simulation builder.
     *
     * @param sim
     *     the simulation to verify
     */
    void attachToSimulation(Simulation sim);

    /**
     * Verify if the current state of the simulation matches the requirements for the successful completion of the task.
     * <p>
     * This method should update the task information returned by {@link #getTaskInformation}.
     */
    void verify();

    /**
     * Get the current task information.
     * <p>
     * The information returned by this method should not change unless {@link #verify} is called.
     *
     * @return the task information
     */
    TaskInformation getTaskInformation();
}

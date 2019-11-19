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

import de.unistuttgart.informatik.fius.icge.simulation.actions.ActionLog;


/**
 * The interface for the main simulation of the ICGE.
 * 
 * @author Tim Neumann
 */
public interface Simulation {
    /**
     * Get the playfield for this simulation.
     * 
     * @return The playfield used by this simulation
     */
    Playfield getPlayfield();
    
    /**
     * Get the simulation clock for this simulation.
     * 
     * @return the simulation clock used by this simulation
     */
    SimulationClock getSimulationClock();
    
    /**
     * Get the action log for this simulation.
     * 
     * @return the action log used by this siumulation
     */
    ActionLog getActionLog();
    
    /**
     * Initialize the simulation and all its submodules.
     */
    void initialize();
}

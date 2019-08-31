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

import de.unistuttgart.informatik.fius.icge.ui.UiManager;


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
     * Get the ui manager for this simulation.
     * 
     * @return the ui manager used by this simulation
     */
    UiManager getUiManager();
    
    /**
     * Get the tick manager for this simulation.
     * 
     * @return the ui manager used by this simulation
     */
    TickManager getTickManager();
    
    /**
     * Initialize the simulation and all its submodules.
     */
    void initialize();
}

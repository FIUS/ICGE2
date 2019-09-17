/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.internal;

import de.unistuttgart.informatik.fius.icge.simulation.Playfield;
import de.unistuttgart.informatik.fius.icge.simulation.Simulation;
import de.unistuttgart.informatik.fius.icge.simulation.SimulationClock;
import de.unistuttgart.informatik.fius.icge.simulation.internal.playfield.StandardPlayfield;
import de.unistuttgart.informatik.fius.icge.ui.UiManager;


/**
 * The standard implementation of {@link Simulation}
 * 
 * @author Tim Neumann
 */
public class StandardSimulation implements Simulation {
    
    private final UiManager uiManager;
    private final StandardPlayfield playfield;
    private final StandardSimulationClock simulationClock;
    
    /**
     * Creates a new standard simulation with the given parameters.
     * 
     * @param uiManager
     *     The uiManager to use
     * @param playfield
     *     The playfield to use
     * @param simulationClock
     *     The tickManager to use
     */
    public StandardSimulation(UiManager uiManager, StandardPlayfield playfield, StandardSimulationClock simulationClock) {
        this.uiManager = uiManager;
        this.playfield = playfield;
        this.simulationClock = simulationClock;
    }
    
    @Override
    public Playfield getPlayfield() {
        return this.playfield;
    }
    
    @Override
    public void initialize() {
        this.playfield.initialize(this);
        this.simulationClock.initialize(this);
        this.uiManager.start();
    }
    
    /**
     * @return the UiManager for this simulation
     */
    @Override
    public UiManager getUiManager() {
        return this.uiManager;
    }
    
    @Override
    public SimulationClock getSimulationClock() {
        return this.simulationClock;
    }
    
}

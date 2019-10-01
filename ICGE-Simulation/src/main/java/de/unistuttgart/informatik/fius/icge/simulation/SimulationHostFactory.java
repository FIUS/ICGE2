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

import de.unistuttgart.informatik.fius.icge.simulation.internal.StandardSimulationProxy;

import de.unistuttgart.informatik.fius.icge.ui.GameWindow;
import de.unistuttgart.informatik.fius.icge.ui.GameWindowFactory;


/**
 * The factory for creating a simulation host.
 * 
 * @author Tim Neumann
 */
public class SimulationHostFactory {
    
    /**
     * Creates the simulation host.
     * 
     * @return The new simulation host.
     */
    public static SimulationHost createSimulationHost() {
        final StandardSimulationProxy simulationProxy = new StandardSimulationProxy();
        final GameWindow window = GameWindowFactory.createGameWindow(simulationProxy);
        simulationProxy.setGameWindow(window);
        window.start();
        
        return simulationProxy;
    }
}

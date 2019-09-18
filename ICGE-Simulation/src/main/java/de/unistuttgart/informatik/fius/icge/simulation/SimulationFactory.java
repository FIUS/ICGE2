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

import de.unistuttgart.informatik.fius.icge.simulation.internal.StandardSimulation;
import de.unistuttgart.informatik.fius.icge.simulation.internal.StandardSimulationClock;
import de.unistuttgart.informatik.fius.icge.simulation.internal.StandardSimulationProxy;
import de.unistuttgart.informatik.fius.icge.simulation.internal.playfield.StandardPlayfield;
import de.unistuttgart.informatik.fius.icge.ui.UiManager;
import de.unistuttgart.informatik.fius.icge.ui.UiManagerFactory;


/**
 * The factory for creating a Simulation
 *
 * @author Tim Neumann
 */
public class SimulationFactory {
    /**
     * Creates a new Simulation including the initialization of all required submodules.
     *
     * @return The new Simulation.
     */
    public static Simulation createSimulation() {
        StandardSimulationProxy simulationProxy = new StandardSimulationProxy();
        UiManager uiManager = UiManagerFactory.createUiManager(simulationProxy);

        StandardPlayfield playfield = new StandardPlayfield();
        StandardSimulationClock simulationClock = new StandardSimulationClock();
        simulationProxy.setSimulationClock(simulationClock);

        return new StandardSimulation(uiManager, playfield, simulationClock);
    }
}

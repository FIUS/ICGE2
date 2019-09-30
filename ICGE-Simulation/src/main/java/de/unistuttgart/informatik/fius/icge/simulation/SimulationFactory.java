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
import de.unistuttgart.informatik.fius.icge.simulation.internal.entity.program.StandardEntityProgramRegistry;
import de.unistuttgart.informatik.fius.icge.simulation.internal.entity.program.StandardEntityProgramRunner;
import de.unistuttgart.informatik.fius.icge.simulation.internal.playfield.StandardPlayfield;
import de.unistuttgart.informatik.fius.icge.simulation.internal.tasks.StandardTaskRunner;
import de.unistuttgart.informatik.fius.icge.ui.GameWindow;
import de.unistuttgart.informatik.fius.icge.ui.GameWindowFactory;


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
        final StandardSimulationProxy simulationProxy = new StandardSimulationProxy();
        final GameWindow window = GameWindowFactory.createGameWindow(simulationProxy);
        
        final StandardPlayfield playfield = new StandardPlayfield();
        final StandardSimulationClock simulationClock = new StandardSimulationClock();
        simulationProxy.setSimulationClock(simulationClock);
        
        final StandardEntityProgramRegistry entityProgramRegistry = new StandardEntityProgramRegistry();
        final StandardEntityProgramRunner entityProgramRunner = new StandardEntityProgramRunner(entityProgramRegistry);
        final StandardTaskRunner taskRunner = new StandardTaskRunner();
        
        return new StandardSimulation(window, playfield, simulationClock, entityProgramRegistry, entityProgramRunner, taskRunner);
    }
}

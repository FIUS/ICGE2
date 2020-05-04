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

import de.unistuttgart.informatik.fius.icge.simulation.inspection.InspectionManager;
import de.unistuttgart.informatik.fius.icge.simulation.internal.StandardSimulation;
import de.unistuttgart.informatik.fius.icge.simulation.internal.StandardSimulationClock;
import de.unistuttgart.informatik.fius.icge.simulation.internal.actions.StandardActionLog;
import de.unistuttgart.informatik.fius.icge.simulation.internal.entity.StandardEntityTypeRegistry;
import de.unistuttgart.informatik.fius.icge.simulation.internal.entity.program.StandardEntityProgramRegistry;
import de.unistuttgart.informatik.fius.icge.simulation.internal.entity.program.StandardEntityProgramRunner;
import de.unistuttgart.informatik.fius.icge.simulation.internal.playfield.StandardPlayfield;


/**
 * The factory for creating a simulation host.
 *
 * @author Tim Neumann
 */
public class SimulationBuilder {

    private Object taskVerifier;
    private Simulation simulation;

    public void setTaskVerifier(Object taskVerifier) {
        if (this.hasBuiltSimulation()) {
            throw new IllegalStateException("The simulation was already built! Use the methods of the Simulation Object to change its properties.");
        }

        this.taskVerifier = taskVerifier;
    }

    public void buildSimulation() {
        if (this.hasBuiltSimulation()) {
            throw new IllegalStateException("The simulation was already built! Use getBuiltSimulation() to acess the built window.");
        }
        StandardPlayfield playfield = new StandardPlayfield();
        StandardSimulationClock simulationClock = new StandardSimulationClock();

        StandardEntityTypeRegistry entityTypeRegistry = new StandardEntityTypeRegistry();

        StandardEntityProgramRegistry entityProgramRegistry = new StandardEntityProgramRegistry();
        StandardEntityProgramRunner entityProgramRunner = new StandardEntityProgramRunner(entityProgramRegistry);

        StandardActionLog actionLog = new StandardActionLog();

        InspectionManager inspectionManager = new InspectionManager();

        this.simulation = new StandardSimulation(playfield, simulationClock, entityTypeRegistry, entityProgramRegistry, entityProgramRunner, actionLog, inspectionManager);
    }

    public boolean hasBuiltSimulation() {
        return this.simulation != null;
    }

    public Simulation getBuiltSimulation() {
        return this.simulation;
    }
}

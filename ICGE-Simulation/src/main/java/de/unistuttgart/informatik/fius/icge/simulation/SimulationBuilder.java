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
    
    private Simulation simulation;
    
    /**
     * Set the task verifier to use with this simulation.
     *
     * <p>
     * This method can only be called before building the simulation. It is not possible to change it after the
     * simulation has been built.
     * </p>
     *
     * @param taskVerifier
     *     The task verifier to use
     */
    public void setTaskVerifier(final Object taskVerifier) {
        if (
            this.hasBuiltSimulation()
        ) throw new IllegalStateException(
                "The simulation was already built! Use the methods of the Simulation Object to change its properties."
        );
    }
    
    /**
     * Actually build the simulation.
     *
     * <p>
     * This can only be called once for each simulation builder.
     * </p>
     */
    public void buildSimulation() {
        if (
            this.hasBuiltSimulation()
        ) throw new IllegalStateException("The simulation was already built! Use getBuiltSimulation() to acess the built window.");
        final StandardPlayfield playfield = new StandardPlayfield();
        final StandardSimulationClock simulationClock = new StandardSimulationClock();
        
        final StandardEntityTypeRegistry entityTypeRegistry = new StandardEntityTypeRegistry();
        
        final StandardEntityProgramRegistry entityProgramRegistry = new StandardEntityProgramRegistry();
        final StandardEntityProgramRunner entityProgramRunner = new StandardEntityProgramRunner(entityProgramRegistry);
        
        final StandardActionLog actionLog = new StandardActionLog();
        
        final InspectionManager inspectionManager = new InspectionManager();
        
        this.simulation = new StandardSimulation(
                playfield, simulationClock, entityTypeRegistry, entityProgramRegistry, entityProgramRunner, actionLog, inspectionManager
        );
    }
    
    /**
     * Get whether the window has been built.
     * 
     * @return true if and only if the window has been built
     */
    public boolean hasBuiltSimulation() {
        return this.simulation != null;
    }
    
    /**
     * Get the simulation that was built.
     * <p>
     * This method can only be called after {@link #buildSimulation()}.
     * </p>
     *
     * @return The created {@link Simulation}
     */
    public Simulation getBuiltSimulation() {
        if (
            !this.hasBuiltSimulation()
        ) throw new IllegalStateException("The simulation was not yet built! Use buildSimulation() to do that.");
        
        return this.simulation;
    }
}

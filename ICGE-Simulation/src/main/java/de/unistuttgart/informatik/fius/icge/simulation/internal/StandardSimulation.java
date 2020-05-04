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
import de.unistuttgart.informatik.fius.icge.simulation.actions.ActionLog;
import de.unistuttgart.informatik.fius.icge.simulation.entity.EntityTypeRegistry;
import de.unistuttgart.informatik.fius.icge.simulation.entity.program.EntityProgramRegistry;
import de.unistuttgart.informatik.fius.icge.simulation.entity.program.EntityProgramRunner;
import de.unistuttgart.informatik.fius.icge.simulation.inspection.InspectionManager;
import de.unistuttgart.informatik.fius.icge.simulation.internal.actions.StandardActionLog;
import de.unistuttgart.informatik.fius.icge.simulation.internal.entity.StandardEntityTypeRegistry;
import de.unistuttgart.informatik.fius.icge.simulation.internal.entity.program.StandardEntityProgramRegistry;
import de.unistuttgart.informatik.fius.icge.simulation.internal.entity.program.StandardEntityProgramRunner;
import de.unistuttgart.informatik.fius.icge.simulation.internal.playfield.StandardPlayfield;
import de.unistuttgart.informatik.fius.icge.simulation.internal.tasks.StandardTaskRunner;
import de.unistuttgart.informatik.fius.icge.simulation.tasks.Task;
import de.unistuttgart.informatik.fius.icge.ui.GameWindow;
import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy;


/**
 * The standard implementation of {@link Simulation}
 *
 * @author Tim Neumann
 */
public class StandardSimulation implements Simulation {
    
    private final StandardPlayfield             playfield;
    private final StandardSimulationClock       simulationClock;
    private final StandardEntityProgramRegistry entityProgramRegistry;
    private final StandardEntityProgramRunner   entityProgramRunner;
    private final StandardActionLog             actionLog;
    private final StandardEntityTypeRegistry    entityTypeRegistry;
    private final StandardSimulationProxy       simulationProxy;
    
    /**
     * Creates a new standard simulation with the given parameters.
     *
     * @param playfield
     *     The playfield to use
     * @param simulationClock
     *     The simulation clock to use
     * @param entityTypeRegistry
     *     The entityTypeRegistry to use
     * @param entityProgramRegistry
     *     The entityProgramRegistry to use
     * @param entityProgramRunner
     *     The entityProgramRunner to use
     * @param actionLog
     *     The actionLog to use
     * @param inspectionManager
     *     The inspection manager to use
     */
    public StandardSimulation(
            final StandardPlayfield playfield, final StandardSimulationClock simulationClock,
            final StandardEntityTypeRegistry entityTypeRegistry, final StandardEntityProgramRegistry entityProgramRegistry,
            final StandardEntityProgramRunner entityProgramRunner, final StandardActionLog actionLog,
            final InspectionManager inspectionManager
    ) {
        this.playfield = playfield;
        this.simulationClock = simulationClock;
        this.entityProgramRegistry = entityProgramRegistry;
        this.entityProgramRunner = entityProgramRunner;
        this.actionLog = actionLog;
        this.entityTypeRegistry = entityTypeRegistry;
        
        this.playfield.initialize(this);
        
        this.simulationProxy = new StandardSimulationProxy(
                simulationClock, inspectionManager, entityTypeRegistry, playfield, entityProgramRegistry
        );
    }
    
    @Override
    public Playfield getPlayfield() {
        return this.playfield;
    }
    
    @Override
    public SimulationClock getSimulationClock() {
        return this.simulationClock;
    }
    
    @Override
    public EntityProgramRegistry getEntityProgramRegistry() {
        return this.entityProgramRegistry;
    }
    
    @Override
    public EntityProgramRunner getEntityProgramRunner() {
        return this.entityProgramRunner;
    }
    
    @Override
    public ActionLog getActionLog() {
        return this.actionLog;
    }
    
    @Override
    public EntityTypeRegistry getEntityTypeRegistry() {
        return this.entityTypeRegistry;
    }
    
    @Override
    public SimulationProxy getSimulationProxyForWindow() {
        return this.simulationProxy;
    }
    
    @Override
    public void attachToWindow(final GameWindow window) {
        this.getSimulationProxyForWindow().attachToGameWindow(window);
    }
    
    @Override
    public void runTask(final Task taskToRun) {
        new StandardTaskRunner(taskToRun, this).runTask();
    }
}

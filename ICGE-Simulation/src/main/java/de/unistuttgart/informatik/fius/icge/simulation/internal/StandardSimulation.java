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
import de.unistuttgart.informatik.fius.icge.simulation.entity.program.EntityProgramRegistry;
import de.unistuttgart.informatik.fius.icge.simulation.entity.program.EntityProgramRunner;
import de.unistuttgart.informatik.fius.icge.simulation.internal.entity.program.StandardEntityProgramRegistry;
import de.unistuttgart.informatik.fius.icge.simulation.internal.entity.program.StandardEntityProgramRunner;
import de.unistuttgart.informatik.fius.icge.simulation.internal.playfield.StandardPlayfield;
import de.unistuttgart.informatik.fius.icge.simulation.internal.tasks.StandardTaskRunner;
import de.unistuttgart.informatik.fius.icge.simulation.tasks.TaskRunner;
import de.unistuttgart.informatik.fius.icge.ui.GameWindow;


/**
 * The standard implementation of {@link Simulation}
 * 
 * @author Tim Neumann
 */
public class StandardSimulation implements Simulation {
    
    private final GameWindow                    window;
    private final StandardPlayfield             playfield;
    private final StandardSimulationClock       simulationClock;
    private final StandardEntityProgramRegistry entityProgramRegistry;
    private final StandardEntityProgramRunner   entityProgramRunner;
    private final StandardTaskRunner            taskRunner;
    
    /**
     * Creates a new standard simulation with the given parameters.
     * 
     * @param window
     *     The game window to use
     * @param playfield
     *     The playfield to use
     * @param simulationClock
     *     The tickManager to use
     * @param entityProgramRegistry
     *     The entityProgramRegistry to use
     * @param entityProgramRunner
     *     The entityProgramRunner to use
     * @param taskRunner
     *     The taskRunner to use
     */
    public StandardSimulation(
            final GameWindow window, final StandardPlayfield playfield, final StandardSimulationClock simulationClock,
            final StandardEntityProgramRegistry entityProgramRegistry, final StandardEntityProgramRunner entityProgramRunner,
            final StandardTaskRunner taskRunner
    ) {
        this.window = window;
        this.playfield = playfield;
        this.simulationClock = simulationClock;
        this.entityProgramRegistry = entityProgramRegistry;
        this.entityProgramRunner = entityProgramRunner;
        this.taskRunner = taskRunner;
    }
    
    @Override
    public Playfield getPlayfield() {
        return this.playfield;
    }
    
    @Override
    public void initialize() {
        this.playfield.initialize(this);
        this.simulationClock.initialize(this);
        this.window.start();
    }
    
    /**
     * @return the game window for this simulation
     */
    @Override
    public GameWindow getGameWindow() {
        return this.window;
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
    public TaskRunner getTaskRunner() {
        return this.taskRunner;
    }
    
}

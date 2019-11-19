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
import de.unistuttgart.informatik.fius.icge.simulation.internal.actions.StandardActionLog;
import de.unistuttgart.informatik.fius.icge.simulation.internal.playfield.StandardPlayfield;
import de.unistuttgart.informatik.fius.icge.ui.exception.ListenerSetException;
import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy.EntityDrawListener;


/**
 * The standard implementation of {@link Simulation}
 * 
 * @author Tim Neumann
 */
public class StandardSimulation implements Simulation {
    
    private final StandardPlayfield             playfield;
    private final StandardSimulationClock       simulationClock;
    private final StandardActionLog             actionLog;
    
    /**
     * Creates a new standard simulation with the given parameters.
     * 
     * @param playfield
     *     The playfield to use
     * @param simulationClock
     *     The simulation clock to use
     * @param actionLog
     *     The actionLog to use
     */
    public StandardSimulation(
            final StandardPlayfield playfield, final StandardSimulationClock simulationClock,
            final StandardActionLog actionLog
    ) {
        this.playfield = playfield;
        this.simulationClock = simulationClock;
        this.actionLog = actionLog;
    }
    
    @Override
    public Playfield getPlayfield() {
        return this.playfield;
    }
    
    @Override
    public void initialize() {
        this.playfield.initialize(this);
    }
    
    /**
     * Set the entity draw listener.
     * 
     * @param listener
     * 
     * @throws IllegalStateException
     *     If clock is running
     * @throws ListenerSetException
     *     If listener is already set and new listener is not null
     */
    public void setEntityDrawListener(EntityDrawListener listener) {
        this.simulationClock.setEntityDrawListener(listener);
        this.playfield.setEntityDrawListener(listener);
    }
    
    @Override
    public SimulationClock getSimulationClock() {
        return this.simulationClock;
    }
    
    @Override
    public ActionLog getActionLog() {
        return this.actionLog;
    }
}

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
import de.unistuttgart.informatik.fius.icge.simulation.programs.Program;
import de.unistuttgart.informatik.fius.icge.simulation.Simulation;
import de.unistuttgart.informatik.fius.icge.simulation.SimulationClock;
import de.unistuttgart.informatik.fius.icge.simulation.TaskVerifier;
import de.unistuttgart.informatik.fius.icge.simulation.actions.ActionLog;
import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;
import de.unistuttgart.informatik.fius.icge.simulation.entity.EntityTypeRegistry;
import de.unistuttgart.informatik.fius.icge.simulation.inspection.InspectionManager;
import de.unistuttgart.informatik.fius.icge.simulation.internal.actions.StandardActionLog;
import de.unistuttgart.informatik.fius.icge.simulation.internal.entity.StandardEntityTypeRegistry;
import de.unistuttgart.informatik.fius.icge.simulation.internal.playfield.StandardPlayfield;
import de.unistuttgart.informatik.fius.icge.simulation.internal.programs.StandardProgramRunner;
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
    
    private final StandardPlayfield          playfield;
    private final StandardSimulationClock    simulationClock;
    private final StandardActionLog          actionLog;
    private final StandardEntityTypeRegistry entityTypeRegistry;
    private final TaskVerifier               taskVerifier;
    private final StandardSimulationProxy    simulationProxy;
    
    private StandardTaskRunner          runningTask;
    private final StandardProgramRunner programRunner;
    
    /**
     * Creates a new standard simulation with the given parameters.
     *
     * @param playfield
     *     The playfield to use
     * @param simulationClock
     *     The simulation clock to use
     * @param entityTypeRegistry
     *     The entityTypeRegistry to use
     * @param actionLog
     *     The actionLog to use
     * @param inspectionManager
     *     The inspection manager to use
     * @param taskVerifier
     *     the task verifier to use to verify the task completion status
     */
    public StandardSimulation(
            final StandardPlayfield playfield, final StandardSimulationClock simulationClock,
            final StandardEntityTypeRegistry entityTypeRegistry, final StandardActionLog actionLog,
            final InspectionManager inspectionManager, final TaskVerifier taskVerifier
    ) {
        this.playfield = playfield;
        this.simulationClock = simulationClock;
        this.actionLog = actionLog;
        this.entityTypeRegistry = entityTypeRegistry;
        this.taskVerifier = taskVerifier;
        
        this.programRunner = new StandardProgramRunner();
        
        this.playfield.initialize(this);
        
        if (taskVerifier != null) {
            taskVerifier.attachToSimulation(this);
        }
        
        this.simulationProxy = new StandardSimulationProxy(
                this, simulationClock, inspectionManager, entityTypeRegistry, playfield, taskVerifier
        );
    }
    
    @Override
    public Playfield getPlayfield() {
        return this.playfield;
    }
    
    @Override
    public TaskVerifier getTaskVerifier() {
        return this.taskVerifier;
    }
    
    @Override
    public SimulationClock getSimulationClock() {
        return this.simulationClock;
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
        this.attachToWindow(window, false);
    }
    
    @Override
    public void attachToWindow(final GameWindow window, final boolean stopWithWindowClose) {
        this.getSimulationProxyForWindow().attachToGameWindow(window, stopWithWindowClose);
    }
    
    @Override
    public void stop() {
        if (this.runningTask != null) {
            this.runningTask.cancel();
            this.runningTask = null;
        }
        this.programRunner.stopAll();
        this.simulationClock.shutdown(); // stop the clock for good
    }
    
    @Override
    public void runTask(final Task taskToRun) {
        if (this.runningTask != null) {
            throw new IllegalStateException("Cannot run more than 1 Task per Simulation!");
        }
        this.runningTask = new StandardTaskRunner(taskToRun, this);
        this.runningTask.runTask();
    }
    
    @Override
    public <E extends Entity, S extends E> void runProgram(Program<E> program, S entity) {
        this.programRunner.run(program, entity);
    }
}

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

import de.unistuttgart.informatik.fius.icge.simulation.actions.ActionLog;
import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;
import de.unistuttgart.informatik.fius.icge.simulation.entity.EntityTypeRegistry;
import de.unistuttgart.informatik.fius.icge.simulation.programs.Program;
import de.unistuttgart.informatik.fius.icge.simulation.tasks.Task;
import de.unistuttgart.informatik.fius.icge.ui.GameWindow;
import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy;


/**
 * The interface for the main simulation of the ICGE.
 *
 * @author Tim Neumann
 */
public interface Simulation {
    /**
     * Get the playfield for this simulation.
     *
     * @return The playfield used by this simulation
     */
    Playfield getPlayfield();
    
    /**
     * Get the task verifier set for this simulation.
     *
     * @return The task verifier set for this simulation
     */
    TaskVerifier getTaskVerifier();
    
    /**
     * Get the simulation clock for this simulation.
     *
     * @return the simulation clock used by this simulation
     */
    SimulationClock getSimulationClock();
    
    /**
     * Get the entity type registry associated with the simulation host.
     *
     * @return The entity type registry
     */
    EntityTypeRegistry getEntityTypeRegistry();
    
    /**
     * Get the action log for this simulation.
     *
     * @return the action log used by this siumulation
     */
    ActionLog getActionLog();
    
    /**
     * Get the simulation proxy used by the game window to communicate with the simulation.
     * 
     * @return The simulation proxy for the UI to use
     */
    SimulationProxy getSimulationProxyForWindow();
    
    /**
     * Attach this simulation to the given window.
     * 
     * @param window
     *     The window to attach to
     */
    void attachToWindow(GameWindow window);
    
    /**
     * Attach this simulation to the given window.
     * 
     * @param window
     *     The window to attach to
     * @param stopWithWindowClose
     *     If {@code true} the simulation will stop when the attached window is closed
     */
    void attachToWindow(GameWindow window, boolean stopWithWindowClose);
    
    /**
     * Irreversibly stop the simulation and all running background tasks and programs.
     * 
     * Calling this method twice will not throw an exception.
     */
    void stop();
    
    /**
     * Run the given task in the background.
     * 
     * Only one task can be run for a simulation so calling this twice will throw an exception.
     * 
     * @param taskToRun
     *     The task to run
     */
    void runTask(Task taskToRun);
    
    /**
     * Run the given program for the given Entity in the background.
     * 
     * @param <E>
     *     The subtype of Entity that the Program accepts
     * @param <S>
     *     The type of the Entity to run the program for. Must be a subtype of E
     * @param program
     *     The program to run
     * @param entity
     *     The Entity to run the program for
     * 
     */
    <E extends Entity, S extends E> void runProgram(Program<E> program, S entity);
}

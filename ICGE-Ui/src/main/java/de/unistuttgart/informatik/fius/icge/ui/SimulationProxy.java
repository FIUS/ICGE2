/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.ui;

import java.util.Set;


/**
 * The SimulationProxy interface. This is used for communication most between the UI and the simulation.
 * <p>
 * First the {@link #attachToGameWindow(GameWindow)} needs to be called to establish the connection. This should set up
 * all communication channels from the Simulation to the UI.
 * <p>
 * The other methods are communication channels from the UI the Simulation.
 *
 * @author Tobias Wältken, Tim Neumann
 * @version 1.0
 */
public interface SimulationProxy {
    
    /**
     * Attach this simulation proxy to a specific game window.
     *
     * @param gameWindow
     *     The game window to attach to
     * @param stopWithWindowClose
     *     If {@code true} the simulation will stop when the attached window is closed
     */
    void attachToGameWindow(GameWindow gameWindow, boolean stopWithWindowClose);
    
    /**
     * Called when the window is closing.
     */
    void windowClosed();
    
    //
    // Toolbar
    //
    
    /**
     * This is to identify the buttons
     */
    public enum ButtonType {
        /** The play button in the toolbar */
        PLAY,
        /** The step button in the toolbar */
        STEP,
        /** The pause button in the toolbar */
        PAUSE,
        /** The view button in the toolbar */
        VIEW,
        /** The add button in the toolbar */
        ADD,
        /** The sub button in the toolbar */
        SUB
    }
    
    /**
     * This is called when a button is pressed by the user
     *
     * @param type
     *     The type of the pressed button
     */
    void buttonPressed(ButtonType type);
    
    /**
     * This is called if the speed slider is changed by the user
     *
     * @param value
     *     The new selected speed
     */
    void simulationSpeedChange(int value);
    
    /**
     * This gets called when the user changes the selected element
     *
     * @param name
     *     The name of the selected element
     */
    void selectedEntityChanged(String name);
    
    /**
     * This gets called by the user to refresh task status information.
     * <p>
     * Calling this must verify the task.
     */
    void refreshTaskInformation();
    
    //
    // Entity placing
    //
    
    /**
     * Spawn a new entity of the given type at the given position and bind the program to this entity.
     *
     * @param typeName
     *     the entity type to instantiate
     * @param x
     *     coordinate
     * @param y
     *     coordinate
     */
    void spawnEntityAt(String typeName, int x, int y);
    
    /**
     * Clear all entities in the given cell.
     *
     * @param x
     *     coordinate
     * @param y
     *     coordinate
     */
    void clearCell(int x, int y);
    
    //
    // Sidebar - Simulation Tree
    //
    
    /**
     * This function gets called when the user selects a different node
     *
     * @param node
     *     The node with was selected
     */
    void selectedSimulationEntityChange(SimulationTreeNode node);
    
    //
    // Entity Inspector
    //
    
    /**
     * This function gets called when a user changes a value or fires a function in the ui
     *
     * @param name
     *     The name of the setting
     * @param value
     *     The new user selected value
     */
    void entityValueChange(String name, String value);
}

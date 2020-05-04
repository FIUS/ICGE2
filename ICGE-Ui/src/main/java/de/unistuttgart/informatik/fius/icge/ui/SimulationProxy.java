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

import java.util.List;
import java.util.Set;


/**
 * The SimulationProxy interface
 *
 * @author Tobias Wältken
 * @version 1.0
 */
public interface SimulationProxy {

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
        /** The stop button in the toolbar */
        STOP,
        /** The view button in the toolbar */
        VIEW,
        /** The add button in the toolbar */
        ADD,
        /** The sub button in the toolbar */
        SUB
    }

    void attachToGameWindow(GameWindow gameWindow);

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

    //
    // Entity Drawing
    //

    void drawEntities(long tickCount);

    void setDrawables(List<Drawable> drawables);


    //
    // Entity placing
    //

    /**
     * Get a set of registered programs that can be used with an entity of the given type.
     *
     * @param typeName
     *     the name of the entity type
     * @return the set of program identifiers
     */
    Set<String> getAvailableProgramsForEntityType(String typeName);

    /**
     * Spawn a new entity of the given type at the given position and bind the program to this entity.
     *
     * @param typeName
     *     the entity type to instantiate
     * @param x
     *     coordinate
     * @param y
     *     coordinate
     * @param program
     *     program name; use {@code null} to not set a program
     */
    void spawnEntityAt(String typeName, int x, int y, String program);

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

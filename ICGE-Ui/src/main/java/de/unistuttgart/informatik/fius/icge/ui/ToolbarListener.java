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

import java.util.EventListener;

import de.unistuttgart.informatik.fius.icge.ui.internal.SwingPlayfieldDrawer;

/**
 * ToolbarListener Interface to listen to toolbar changes and inputs
 *
 * @author Tobias Wältken
 * @version 1.0
 */
public interface ToolbarListener extends EventListener {

    /**
     * This enum describes the state of the simulation
     */
    public enum SimulationState {
        /** The play state means the user wishes the simulation to be running */
        PLAY,
        /** The pause state means the user wishes the simulation to be paused */
        PAUSE,
        /** The stop state means the user wishes the simulation to be halted and reset */
        STOP
    }

    /**
     * This function is called when the user selects a different simulation state
     *
     * @param state represents the newly selected state
     * @see ToolbarListener.SimulationState
     */
    public void simulationStateChanged(SimulationState state);

    /**
     * This function is called when the user changes the simulation speed
     *
     * @param speed the speed selected by the user which ranges from 0 to 10
     */
    public void simulationSpeedChanged(int speed);


    /**
     * This enum describes the way the {@link PlayfieldDrawer} reacts to mouse input
     *
     * @see SwingPlayfieldDrawer
     */
    public enum InputMode {
        /** The view mode allows the user to move the view port and zoom the map */
        VIEW,
        /** The entity mode allows the user to place, remove and manipulate the entities */
        ENTITY
    }

    /**
     * This function is called when the user requests a different input mode
     *
     * @param mode represents the newly selected input mode
     * @see ToolbarListener.InputMode
     */
    public void inputModeChanged(InputMode mode);
}

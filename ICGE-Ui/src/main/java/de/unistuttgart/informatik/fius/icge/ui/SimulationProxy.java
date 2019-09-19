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
     * The clock button state represents the states of the clock buttons in the ui
     */
    public enum ClockButtonState {
        /** Indicates a running simulation clock */
        PLAYING,
        /** Indicates a stoped simulation clock but a unclean simulation */
        PAUSED,
        /** Indicates a clean simulation */
        STOPPED,
        /** Indicates an error or unavailable clock or simulation */
        BLOCKED
    }
    
    /**
     * The control button state represents the status of the control buttons in the ui
     */
    public enum ControlButtonState {
        /** Indicates the user input is in view mode */
        VIEW,
        /** Indicates the user input is in entity mode */
        ENTITY,
        /** Indicates that the user input is blocked or unavailable */
        BLOCKED
    }
    
    /**
     * The button state listener allows the ui to react to change requests.
     */
    public interface ButtonStateListener {
        
        /**
         * This function changes the enabled state of the clock buttons
         *
         * @param state
         *     the state of the buttons
         */
        void changeButtonState(ClockButtonState state);
        
        /**
         * This function changes the enabled state of the control buttons
         *
         * @param state
         *     the state of the buttons
         */
        void changeButtonState(ControlButtonState state);
    }
    
    /**
     * This function sets the one button state listener and should only be called by the ui itself
     *
     * @param listener
     *     The listener to use.
     */
    void setButtonStateListener(ButtonStateListener listener);
    
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
        /** The entity button in the toolbar */
        ENTITY
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
}

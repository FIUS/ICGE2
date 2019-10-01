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
    // Toolbar - Clock
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
     * This function sets the one button state listener and should only be called by the ui itself. The only way to
     * reset this listener is to explicitly set it to null thus removing the old listener.
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
     * This speed slider listener allows the speed slider to react to requests
     */
    public interface SpeedSliderListener {
        /**
         * Getter function for the currently selected speed
         * 
         * @return Returns the slider position a integer between 0 and 10 (both inclusive)
         */
        int getSpeed();
        
        /**
         * Setter for the position of the slider
         * 
         * @param speed
         *     The new position for the slider a integer between 0 and 10 (both inclusive)
         */
        void setSpeed(int speed);
    }
    
    /**
     * This function sets the one speed slider listener and should only be called by the ui itself. The only way to
     * reset this listener is to explicitly set it to null thus removing the old listener.
     * 
     * @param listener
     *     The listener to use
     */
    void setSpeedSliderListener(SpeedSliderListener listener);
    
    /**
     * This is called if the speed slider is changed by the user
     *
     * @param value
     *     The new selected speed
     */
    void simulationSpeedChange(int value);
    
    //
    // Toolbar - Task
    //
    
    /**
     * The TaskSelectorListener allows the simulation to talk to the task selector
     */
    public interface TaskSelectorListener {
        
        /**
         * Getter function for the currently selected element
         * 
         * @return returns the selected element
         */
        String getSelectedElement();
        
        /**
         * Setter function for all available elements. Use null to clear.
         * 
         * @param elements
         *     The set of new selectable values
         */
        void setElements(Set<String> elements);
    }
    
    /**
     * This function is used to set the one task selector listener and should only be called by the ui itself. The only
     * way to reset this listener is to explicitly set it to null thus removing the old listener.
     * 
     * @param listener
     *     the listener to store
     */
    void setTaskSelectorListener(TaskSelectorListener listener);
    
    /**
     * This function gets called if the selected task changes
     * 
     * @param element
     *     contains the newly selected element
     */
    void selectedTaskChange(String element);
    
    //
    // Toolbar - Controlls
    //
    
    //
    // Entity Drawing
    //
    
    /**
     * The entity draw listener allows the simulation to trigger redraws and set the drawable list.
     */
    public interface EntityDrawListener {
        
        /**
         * Set the current list of Drawables to be rendered.
         *
         * @param drawables
         *     the list of Drawables to render
         */
        void setDrawables(List<Drawable> drawables);
        
        /**
         * (Re-)Draws the playfield.
         * 
         * @param tickCount
         *     The number of the current tick
         */
        void draw(long tickCount);
    }
    
    /**
     * This function is used to set the one task entity draw listener and should only be called by the ui itself. The
     * only way to reset this listener is to explicitly set it to null thus removing the old listener.
     * 
     * @param listener
     *     the listener to store
     */
    void setEntityDrawListener(EntityDrawListener listener);
}

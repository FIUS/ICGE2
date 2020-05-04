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

import de.unistuttgart.informatik.fius.icge.ui.internal.dropdownSelector.DropdownSelector;


/**
 * The toolbar used by a {@link GameWindow} to handle the toolbar.
 *
 * @author Tim Neumann
 * @author Tobias Wältken
 * @version 1.0
 */
public interface Toolbar {
    
    /**
     * The clock button state represents the states of the clock buttons in the ui
     */
    enum ClockButtonState {
        /** Indicates a running simulation clock */
        PLAYING,
        /** Indicates a stoped simulation clock */
        PAUSED,
        /** Indicates an error or unavailable clock or simulation */
        BLOCKED
    }
    
    /**
     * The control button state represents the status of the control buttons in the ui
     */
    enum ControlButtonState {
        /** Indicates the user input is in view mode */
        VIEW,
        /** Indicates the user input is in add mode */
        ADD,
        /** Indicates the user input is in sub mode */
        SUB,
        /** Indicates that the user input is blocked or unavailable */
        BLOCKED
    }
    
    /**
     * Getter function for the user selected entity
     *
     * @return The displayname of the selected entry
     * @see DropdownSelector
     */
    String getCurrentEntity();
    
    /**
     * Append the list of entities with the given entry
     *
     * @param displayName
     *     the name which is displayed for the user
     * @param textureID
     *     the texture which is rendert infront of the display name
     * @see DropdownSelector
     */
    void addEntity(String displayName, String textureID);
    
    /**
     * Set the state of the control buttons.
     * 
     * @param controlButtonState
     *     The new state
     */
    void setControlButtonState(ControlButtonState controlButtonState);
    
    /**
     * Set the state of the clock buttons.
     * 
     * @param clockButtonState
     *     The new state
     */
    void setClockButtonState(ClockButtonState clockButtonState);
    
    /**
     * Get the current position of the speed slider.
     * 
     * @return The position from 0 to 10 (both inclusive)
     */
    int getSpeedSliderPosition();
    
    /**
     * Set the position of the speed slider.
     * 
     * @param position
     *     The new position; can be from 0 to 10 (both inclusive)
     */
    void setSpeedSliderPosition(int position);
    
    /**
     * Get the entity currently selected in the entity selection dropdown.
     * 
     * @return The name of the currently selected entity
     */
    String getCurrentlySelectedEntity();
    
    /**
     * Set the entity selected in the entity selection dropdown.
     * 
     * @param entity
     *     The name of the entity to select
     */
    void setCurrentlySelectedEntity(String entity);
    
    /**
     * Enable the entity selector
     */
    void enableEntitySelector();
    
    /**
     * Disable the entity selector
     */
    void disableEntitySelector();
    
    /**
     * Add a entity to the entity selector drop down.
     *
     * @param name
     *     The name of the new entity.
     * @param textureId
     *     The texture handle of the new entity.
     */
    void addEntityToEntitySelector(String name, String textureId);
}

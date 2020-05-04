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

import de.unistuttgart.informatik.fius.icge.ui.internal.dropdown_selector.DropdownSelector;


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

    void setControlButtonState(ControlButtonState controlButtonState);
    void setClockButtonState(ClockButtonState clockButtonState);

    int getSpeedSliderPosition();
    void setSpeedSliderPosition(int position);

    String getCurrentlySelectedEntity();
    void setCurrentlySelectedEntity(String entity);
    void enableEntitySelector();
    void disableEntitySelector();
    void addEntityToEntitySelector(String name, String textureId);
}

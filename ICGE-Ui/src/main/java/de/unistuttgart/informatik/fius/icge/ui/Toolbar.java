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
 * The toolbar manager user by a {@link UiManager} to handle the toolbar.
 *
 * @author Tim Neumann
 * @author Tobias Wältken
 * @version 1.0
 */
public interface Toolbar {

    /**
     * Getter function for the user selected entity
     *
     * @return The displayname of the selected entry
     * @see DropdownSelector
     */
    public String getCurrentEntity();

    /**
     * Append the list of entities with the given entry
     *
     * @param displayName the name which is displayed for the user
     * @param textureID the texture which is rendert infront of the display name
     * @see DropdownSelector
     */
    public void addEntity(String displayName, String textureID);
}

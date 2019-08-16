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

import java.util.Collection;

/**
 * The toolbar manager user by a {@link UiManager} to handle the toolbar.
 *
 * @author Tim Neumann
 * @author Tobias Wältken
 * @version 1.0
 */
public interface ToolbarManager {

    /**
     * This function adds a {@link ToolbarListener} to the toolbar
     *
     * @param listener the listener to be added
     * @return true (as specified in {@link Collection#add(Object)})
     */
    public boolean addToolbarListener(ToolbarListener listener);

    /**
     * This function removes the given listener from the toolbar
     *
     * @param listener the listener to be removed
     * @return true if this list contained the specified element
     */
    public boolean removeToolbarListener(ToolbarListener listener);

    /**
     * This function deletes all {@link ToolbarListener}s from the toolbar.
     */
    public void clearAllToolbarListeners();
}

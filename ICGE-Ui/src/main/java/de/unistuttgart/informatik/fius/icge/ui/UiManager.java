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
 * The interface for a UI Manager of the ICGE.
 * 
 * @author Tim Neumann
 */
public interface UiManager {
    /**
     * Get the registry, with which to register textures for this UI Manager.
     * 
     * @return The texture registry used by this manager.
     */
    TextureRegistry getTextureRegistry();
    
    /**
     * Get the drawer responsible for drawing the playfield for this UI Manager.
     * 
     * @return The playfield drawer used by this manager.
     */
    PlayfieldDrawer getPlayfieldDrawer();
    
    /**
     * Get the toolbar manager for this UI Manager.
     * 
     * @return The toolbar manager used by this manaer.
     */
    ToolbarManager getToolbarManager();
    
    /**
     * Set the title of the window, in which the ICGE is displayed.
     * 
     * @param title
     *     The title to use.
     */
    void setWindowTitle(String title);
    
    /**
     * Start the UI Manager and all its submodules.
     */
    void start();
}

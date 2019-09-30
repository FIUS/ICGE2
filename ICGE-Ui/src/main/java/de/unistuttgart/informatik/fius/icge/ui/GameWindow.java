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
 * The interface for a game window of the ICGE.
 *
 * @author Tim Neumann
 */
public interface GameWindow {
    /**
     * Get the registry, with which to register textures for this game window.
     *
     * @return The texture registry used by this manager.
     */
    TextureRegistry getTextureRegistry();
    
    /**
     * Get the drawer responsible for drawing the playfield for this game window.
     *
     * @return The playfield drawer used by this manager.
     */
    PlayfieldDrawer getPlayfieldDrawer();
    
    /**
     * Get the toolbar for this game window.
     *
     * @return The toolbar used by this manager.
     */
    Toolbar getToolbar();
    
    /**
     * Get the entity sidebar for this game window
     *
     * @return The entity sidebar used by this manager
     */
    EntitySidebar getEntitySidebar();
    
    /**
     * Get the console for this game window
     *
     * @return The console used by thsi manager
     */
    Console getConsole();
    
    /**
     * Set the title of the window, in which the ICGE is displayed.
     *
     * @param title
     *     The title to use.
     */
    void setWindowTitle(String title);
    
    /**
     * Start the game window and all its submodules.
     */
    void start();
}

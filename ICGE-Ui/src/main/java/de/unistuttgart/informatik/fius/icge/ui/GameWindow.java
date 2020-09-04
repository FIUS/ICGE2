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
     * @return The texture registry used by this window.
     */
    TextureRegistry getTextureRegistry();
    
    /**
     * Get the drawer responsible for drawing the playfield for this game window.
     *
     * @return The playfield drawer used by this window.
     */
    PlayfieldDrawer getPlayfieldDrawer();
    
    /**
     * Get the toolbar for this game window.
     *
     * @return The toolbar used by this window.
     */
    Toolbar getToolbar();
    
    /**
     * Get the entity sidebar for this game window
     *
     * @return The entity sidebar used by this window
     */
    EntitySidebar getEntitySidebar();
    
    /**
     * Get the console for this game window
     *
     * @return The console used by this window
     */
    Console getConsole();
    
    /**
     * Get the task status display for this game window.
     *
     * @return The task status display used by this window
     */
    TaskStatusDisplay getTaskStatusDisplay();
    
    /**
     * Set the title of the window, in which the ICGE is displayed.
     *
     * @param title
     *     The title to use.
     */
    void setWindowTitle(String title);
    
    /**
     * Start the game window and all its submodules.
     *
     * <b>Note:</b> Implementations are not required to perform that start with a synchronous call pattern. When this
     * method returns, the startup may be incomplete.
     */
    void start();
    
    /**
     * Set the simulation proxy instance used by multiple UI components to inform the Simulation of user actions.
     *
     * @param simulationProxy
     *     The instance of simulation proxy to use
     */
    void setSimulationProxy(SimulationProxy simulationProxy);
}

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

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.unistuttgart.informatik.fius.icge.ui.internal.SwingConsole;
import de.unistuttgart.informatik.fius.icge.ui.internal.SwingEntitySidebar;
import de.unistuttgart.informatik.fius.icge.ui.internal.SwingGameWindow;
import de.unistuttgart.informatik.fius.icge.ui.internal.SwingPlayfieldDrawer;
import de.unistuttgart.informatik.fius.icge.ui.internal.SwingTextureRegistry;
import de.unistuttgart.informatik.fius.icge.ui.internal.SwingToolbar;


/**
 * A class for building windows.
 *
 * Each window builder can only build one window.
 *
 * @author Tim Neumann
 */
public class WindowBuilder {
    
    private String     windowTitle = "";
    private boolean    useDoubleBuffering;
    private boolean    syncToScreen;
    private GameWindow window;
    
    /**
     * Set the title of the new window.
     *
     * <p>
     * This method can only be called before building the window. To change the title after building the window, call
     * {@link GameWindow#setWindowTitle(String)} on the resulting window.
     * </p>
     *
     * @param title
     *     The title to set
     */
    public void setTitle(final String title) {
        if (
            this.hasBuiltWindow()
        ) throw new IllegalStateException("The window was already built! Use the methods of the Window Object to change its properties.");
        
        this.windowTitle = title;
    }
    
    /**
     * Set the graphics settings of the playfield drawer.
     * <p>
     * This method can only be called before building the window. To change the title after building the window, call
     * {@link PlayfieldDrawer#setDoubleBuffering(boolean)} and {@link PlayfieldDrawer#setSyncToScreen(boolean)} on the
     * resulting window's playfield drawer.
     * </p>
     *
     * @param useDoubleBuffering
     *     true (default) uses doubleBuffering when rendering changes on the playfield.
     * @param syncToScreen
     *     true (default) actively tries to sync the updated graphics to the screen after rendering changes on the
     *     playfield.
     */
    public void setGraphicsSettings(final boolean useDoubleBuffering, final boolean syncToScreen) {
        if (
            this.hasBuiltWindow()
        ) throw new IllegalStateException("The window was already built! Use the methods of the Window Object to change its properties.");
        
        this.useDoubleBuffering = useDoubleBuffering;
        this.syncToScreen = syncToScreen;
    }
    
    /**
     * Actually build the window.
     *
     * <p>
     * This can only be called once for each window builder.
     * </p>
     */
    public void buildWindow() {
        if (
            this.hasBuiltWindow()
        ) throw new IllegalStateException("The window was already built! Use getBuiltWindow() to acess the built window.");
        // Set window look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("Can't set look and feel because of: " + e.toString());
        }
        
        final SwingTextureRegistry textureRegistry = new SwingTextureRegistry();
        final SwingPlayfieldDrawer playfieldDrawer = new SwingPlayfieldDrawer(textureRegistry);
        final SwingToolbar toolbar = new SwingToolbar(textureRegistry);
        final SwingEntitySidebar entitySidebar = new SwingEntitySidebar(textureRegistry);
        final SwingConsole console = new SwingConsole();
        
        playfieldDrawer.setDoubleBuffering(this.useDoubleBuffering);
        playfieldDrawer.setSyncToScreen(this.syncToScreen);
        
        this.window = new SwingGameWindow(textureRegistry, playfieldDrawer, toolbar, entitySidebar, console);
        if (this.windowTitle != null) {
            this.window.setWindowTitle(this.windowTitle);
        }
    }
    
    /**
     * Get whether the window has been built.
     * 
     * @return true if and only if the window has been built
     */
    public boolean hasBuiltWindow() {
        return this.window != null;
    }
    
    /**
     * Get the window that was built.
     * <p>
     * This method can only be called after {@link #buildWindow()}.
     * </p>
     *
     * @return The created {@link GameWindow}
     */
    public GameWindow getBuiltWindow() {
        if (!this.hasBuiltWindow()) throw new IllegalStateException("The window was not yet built! Use buildWindow() to do that.");
        return this.window;
    }
}

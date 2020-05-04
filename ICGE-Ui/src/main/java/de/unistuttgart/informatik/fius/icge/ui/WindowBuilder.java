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


public class WindowBuilder {

    private String windowTitle = "";
    private boolean useDoubleBuffering;
    private boolean syncToScreen;
    private GameWindow window;

    public void setTitle(String title) {
        if (this.hasBuiltWidow()) {
            throw new IllegalStateException("The window was already built! Use the methods of the Window Object to change its properties.");
        }
        if (title == null) {
            title = "";
        }
        this.windowTitle = title;
    }

    /**
     * Set the graphics settings of the playfield drawer.
     *
     * @param useDoubleBuffering
     *     true (default) uses doubleBuffering when rendering changes on the playfield.
     * @param syncToScreen
     *     true (default) actively tries to sync the updated graphics to the screen after rendering changes on the
     *     playfield.
     */
    public void setGraphicsSettings(boolean useDoubleBuffering, boolean syncToScreen) {
        this.useDoubleBuffering = useDoubleBuffering;
        this.syncToScreen = syncToScreen;
    }

    public void buildWindow() {
        if (this.hasBuiltWidow()) {
            throw new IllegalStateException("The window was already built! Use getBuiltWindow() to acess the built window.");
        }
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
    }

    public boolean hasBuiltWidow() {
        return this.window != null;
    }

    public GameWindow getBuiltWindow() {
        return this.window;
    }
}

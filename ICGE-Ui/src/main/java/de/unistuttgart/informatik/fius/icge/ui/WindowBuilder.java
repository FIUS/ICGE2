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

*

import java.awt.GraphicsEnvironment;
import java.lang.reflect.InvocationTargetException;
import java.awt.Font;*
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;*
import de.unistuttgart.informatik.fius.icge.ui.internal.SwingConsole;
import de.unistuttgart.informatik.fius.icge.ui.internal.SwingEntitySidebar;
import de.unistuttgart.informatik.fius.icge.ui.internal.SwingGameWindow;
import de.unistuttgart.informatik.fius.icge.ui.internal.SwingPlayfieldDrawer;
import de.unistuttgart.informatik.fius.icge.ui.internal.SwingTaskStatusDisplay;
import de.unistuttgart.informatik.fius.icge.ui.internal.SwingTextureRegistry;
import de.unistuttgart.informatik.fius.icge.ui.internal.SwingToolbar;**


/**
 * A class for building windows.
 *
 * Each window builder can only build one window.
 *
 * @author Tim Neumann
 */
public class WindowBuilder {
    *
    private static boolean      isDefaultLookAndFeelUpdated = false;
    private static double       dpiScale;
    private static double  fontScale;*
    private String              windowTitle                 = "";
    private boolean             useDoubleBuffering;
    private boolean             syncToScreen;
    private volatile GameWindow window;*

    /**
     * Create a new WindowBuilder.
     */
    public WindowBuilder() {
        this(WindowBuilder.getDeviceDpiScale());
    }*

    /**
     * Create a new WindowBuilder.
     *
     * @param dpiScale
     *     the scaling factor for high dpi screens, only effective for the very first WindowBuilder instantiation!
     */
    public WindowBuilder(double dpiScale) {
        if (dpiScale < 0.5) throw new IllegalArgumentException("A dpi scale < 0.5 is not supported!");
        if (dpiScale > 3.0) throw new IllegalArgumentException("A dpi scale > 3.0 is not supported!");
 *
        if (!WindowBuilder.isDefaultLookAndFeelUpdated) { // only once
            WindowBuilder.isDefaultLookAndFeelUpdated = true;
            WindowBuilder.dpiScale = dpiScale;
            WindowBuilder.fontScale = ((dpiScale - 1) * 0.75) + 1;
            this.setUiDefaults(dpiScale, WindowBuilder.fontScale);
        }
    }*

    /**
     * Get the scaling factor from the default display device.
     *
     * @return the dpi scale of the default display
     */
    private static double getDeviceDpiScale() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getDefaultTransform()
                .getScaleX();
    }*

    /**
     * Set the UI Manager defaults forlook and feeel and dpi scaling.
     * <p>
     * This method must only be called once. Calling this twice may have undefined behaviur.
     *
     * @param dpiScale
     *     The dpi scaling factor to use to scale all fonts.
     * @param fontScale
     *     The scaling factor to be applied to fonts specifically
     */
    private void setUiDefaults(double dpiScale, double fontScale) {
        // Set window look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            final UIDefaults uidef = UIManager.getLookAndFeelDefaults();
            uidef.forEach((key, value) -> { // scale fonts for highdpi
                if (value instanceof UIDefaults.ActiveValue) {
                    UIDefaults.ActiveValue lazy = (UIDefaults.ActiveValue) value;
                    value = lazy.createValue(UIManager.getDefaults());
                }
                if (value != null && value instanceof Font) {
                    Font font = (Font) value;
                    int scaledFontSize = (int) Math.floor(font.getSize() * fontScale);
                    uidef.put(key, new FontUIResource(font.getName(), font.getStyle(), scaledFontSize));
                }
            });
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("Can't set look and feel because of: " + e.toString());
        }
    }*

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
        if (this.hasBuiltWindow())
            throw new IllegalStateException("The window was already built! Use the methods of the Window Object to change its properties.");
 *
        this.windowTitle = title;
    }*

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
        if (this.hasBuiltWindow())
            throw new IllegalStateException("The window was already built! Use the methods of the Window Object to change its properties.");
 *
        this.useDoubleBuffering = useDoubleBuffering;
        this.syncToScreen = syncToScreen;
    }*

    /**
     * Actually build the window.
     *
     * <p>
     * This can only be called once for each window builder.
     * </p>
     */
    public void buildWindow() {
        if (this.hasBuiltWindow())
            throw new IllegalStateException("The window was already built! Use getBuiltWindow() to acess the built window.");
        try {
            SwingUtilities.invokeAndWait(this::buildWindowInternal);
        } catch (final InterruptedException | InvocationTargetException e) {
            // TODO better exception handling
            System.err.println("Can't build the window because of: " + e.toString());
        }
    }*

    /**
     * Build the window.
     * <p>
     * This method must be called in the swing UI thread!
     */
    private void buildWindowInternal() {
        final SwingTextureRegistry textureRegistry = new SwingTextureRegistry();
        final SwingPlayfieldDrawer playfieldDrawer = new SwingPlayfieldDrawer(textureRegistry, WindowBuilder.dpiScale);
        final SwingToolbar toolbar = new SwingToolbar(textureRegistry, WindowBuilder.dpiScale);
        final SwingEntitySidebar entitySidebar = new SwingEntitySidebar(textureRegistry, WindowBuilder.dpiScale);
        final SwingConsole console = new SwingConsole(WindowBuilder.fontScale);
        final SwingTaskStatusDisplay taskStatus = new SwingTaskStatusDisplay(WindowBuilder.fontScale);
 *
        playfieldDrawer.setDoubleBuffering(this.useDoubleBuffering);
        playfieldDrawer.setSyncToScreen(this.syncToScreen);
 *
        this.window = new SwingGameWindow(textureRegistry, playfieldDrawer, toolbar, entitySidebar, console, taskStatus);
        if (this.windowTitle != null) {
            this.window.setWindowTitle(this.windowTitle);
        }
    }*

    /**
     * Get whether the window has been built.
     *
     * @return true if and only if the window has been built
     */
    public boolean hasBuiltWindow() {
        return this.window != null;
    }*

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

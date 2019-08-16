/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 *
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.ui.internal;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import de.unistuttgart.informatik.fius.icge.ui.PlayfieldDrawer;
import de.unistuttgart.informatik.fius.icge.ui.TextureRegistry;
import de.unistuttgart.informatik.fius.icge.ui.ToolbarManager;
import de.unistuttgart.informatik.fius.icge.ui.UiManager;


/**
 * An implementation of {@link UiManager} using java swing.
 *
 * @author Tim Neumann
 */
public class SwingUIManager extends JFrame implements UiManager {

    /**
     * generated
     */
    private static final long          serialVersionUID = -7215617949088643819L;
    private final SwingTextureRegistry textureRegistry;
    private final SwingPlayfieldDrawer playfieldDrawer;
    private final SwingToolbarManager  toolbarManager;

    /**
     * Create a new Swing UI Manager using the given submodules.
     *
     * @param textureRegistry
     *     The {@link TextureRegistry} to use.
     * @param playfieldDrawer
     *     The {@link PlayfieldDrawer} to use.
     * @param toolbarManager
     *     The {@link ToolbarManager} to use.
     */
    public SwingUIManager(
            final SwingTextureRegistry textureRegistry, final SwingPlayfieldDrawer playfieldDrawer, final SwingToolbarManager toolbarManager
    ) {
        this.textureRegistry = textureRegistry;
        this.playfieldDrawer = playfieldDrawer;
        this.toolbarManager = toolbarManager;
    }

    @Override
    public TextureRegistry getTextureRegistry() {
        return this.textureRegistry;
    }

    @Override
    public PlayfieldDrawer getPlayfieldDrawer() {
        return this.playfieldDrawer;
    }

    @Override
    public ToolbarManager getToolbarManager() {
        return this.toolbarManager;
    }

    @Override
    public void start() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.playfieldDrawer.initialize(this);
        this.getContentPane().add(BorderLayout.CENTER, this.playfieldDrawer);
        //this.toolbarManager.initialize(this);
        this.getContentPane().add(BorderLayout.NORTH, this.toolbarManager);
        this.pack();
        this.setVisible(true);
    }

    @Override
    public void setWindowTitle(final String title) {
        this.setTitle(title);
    }

}

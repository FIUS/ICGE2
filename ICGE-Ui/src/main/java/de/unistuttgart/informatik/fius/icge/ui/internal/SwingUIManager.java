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
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import de.unistuttgart.informatik.fius.icge.ui.EntitySidebar;
import de.unistuttgart.informatik.fius.icge.ui.PlayfieldDrawer;
import de.unistuttgart.informatik.fius.icge.ui.TextureRegistry;
import de.unistuttgart.informatik.fius.icge.ui.Toolbar;
import de.unistuttgart.informatik.fius.icge.ui.UiManager;


/**
 * An implementation of {@link UiManager} using java swing.
 *
 * @author Tim Neumann
 * @author Tobias Wältken
 * @version 1.0
 */
public class SwingUIManager extends JFrame implements UiManager {
    private static final long serialVersionUID = -7215617949088643819L;

    private final SwingTextureRegistry textureRegistry;
    private final SwingToolbar toolbarManager;
    private final SwingEntitySidebar entitySidebar;
    private final SwingPlayfieldDrawer playfieldDrawer;

    /**
     * Create a new Swing UI Manager using the given submodules.
     *
     * @param textureRegistry
     *     The {@link TextureRegistry} to use.
     * @param playfieldDrawer
     *     The {@link PlayfieldDrawer} to use.
     */
    public SwingUIManager(
            final SwingTextureRegistry textureRegistry,
            final SwingPlayfieldDrawer playfieldDrawer
    ) {
        this.textureRegistry = textureRegistry;
        this.playfieldDrawer = playfieldDrawer;
        this.toolbarManager = new SwingToolbar(this.textureRegistry);
        this.entitySidebar = new SwingEntitySidebar();
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
    public Toolbar getToolbarManager() {
        return this.toolbarManager;
    }

    @Override
    public EntitySidebar getEntitySidebar() {
        return this.entitySidebar;
    }

    @Override
    public void setWindowTitle(final String title) {
        this.setTitle(title);
    }

    @Override
    public void start() {
        // init jFrame
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.playfieldDrawer.initialize(this);

        // setup toolbar
        this.getContentPane().add(BorderLayout.NORTH, this.toolbarManager);

        // setup main split pane with playfield and entity sidebar
        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                this.playfieldDrawer, this.entitySidebar);
        this.getContentPane().add(BorderLayout.CENTER, sp);

        // finalize jFrame
        this.pack();
        this.setVisible(true);
    }
}

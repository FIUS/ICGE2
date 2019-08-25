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

import javax.swing.JComponent;
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
    private final SwingPlayfieldDrawer playfieldDrawer;
    private final Toolbar toolbar;
    private final EntitySidebar entitySidebar;

    /**
     * Create a new Swing UI Manager using the given submodules.
     *
     * @param textureRegistry The {@link TextureRegistry} to use.
     * @param playfieldDrawer The {@link PlayfieldDrawer} to use.
     * @param toolbar The {@link Toolbar} to use.
     * @param entitySidebar The {@link EntitySidebar} to use.
     */
    public SwingUIManager(
            final SwingTextureRegistry textureRegistry,
            final SwingPlayfieldDrawer playfieldDrawer,
            final Toolbar toolbar,
            final EntitySidebar entitySidebar
    ) {
        this.textureRegistry = textureRegistry;
        this.playfieldDrawer = playfieldDrawer;
        this.toolbar = toolbar;
        this.entitySidebar = entitySidebar;
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
    public Toolbar getToolbar() {
        return this.toolbar;
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
    @SuppressWarnings ("unused") // Suppress unused warnings on 'ClassCastException e'
    public void start() {
        // init jFrame
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.playfieldDrawer.initialize(this);

        // setup toolbar
        try {
            this.getContentPane().add(BorderLayout.NORTH, (JComponent) this.toolbar);
        } catch (ClassCastException e) {/* Simply don't add toolbar if it isn't a JComponent */}

        // setup main split pane with playfield and entity sidebar
        try {
            JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                    this.playfieldDrawer, (JComponent) this.entitySidebar);
            this.getContentPane().add(BorderLayout.CENTER, sp);
        } catch (ClassCastException e) {
            // Only add the playfield drawer if the sidebar is not a JComponent
            this.getContentPane().add(BorderLayout.CENTER, this.playfieldDrawer);
        }

        // finalize jFrame
        this.pack();
        this.setVisible(true);
    }
}

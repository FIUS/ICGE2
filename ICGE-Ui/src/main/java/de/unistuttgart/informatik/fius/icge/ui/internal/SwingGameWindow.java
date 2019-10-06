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

import static javax.swing.SwingConstants.CENTER;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import de.unistuttgart.informatik.fius.icge.log.Logger;
import de.unistuttgart.informatik.fius.icge.ui.Console;
import de.unistuttgart.informatik.fius.icge.ui.EntitySidebar;
import de.unistuttgart.informatik.fius.icge.ui.PlayfieldDrawer;
import de.unistuttgart.informatik.fius.icge.ui.SimulationTreeNode;
import de.unistuttgart.informatik.fius.icge.ui.TextureRegistry;
import de.unistuttgart.informatik.fius.icge.ui.Toolbar;
import de.unistuttgart.informatik.fius.icge.ui.GameWindow;


/**
 * An implementation of {@link GameWindow} using java swing.
 *
 * @author Tim Neumann
 * @author Tobias Wältken
 * @version 1.0
 */
public class SwingGameWindow extends JFrame implements GameWindow {
    private static final long serialVersionUID = -7215617949088643819L;
    
    private final SwingTextureRegistry textureRegistry;
    private final SwingPlayfieldDrawer playfieldDrawer;
    private final Toolbar              toolbar;
    private final EntitySidebar        entitySidebar;
    private final Console              console;
    
    /**
     * Create a new Swing game window using the given submodules.
     *
     * @param textureRegistry
     *     The {@link TextureRegistry} to use.
     * @param playfieldDrawer
     *     The {@link PlayfieldDrawer} to use.
     * @param toolbar
     *     The {@link Toolbar} to use.
     * @param entitySidebar
     *     The {@link EntitySidebar} to use.
     * @param console
     *     The {@link Console} to use.
     */
    public SwingGameWindow(
            final SwingTextureRegistry textureRegistry, final SwingPlayfieldDrawer playfieldDrawer, final Toolbar toolbar,
            final EntitySidebar entitySidebar, final Console console
    ) {
        this.textureRegistry = textureRegistry;
        this.playfieldDrawer = playfieldDrawer;
        this.toolbar = toolbar;
        this.entitySidebar = entitySidebar;
        this.console = console;
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
    public Console getConsole() {
        return this.console;
    }
    
    @Override
    public void setWindowTitle(final String title) {
        this.setTitle(title);
    }
    
    @Override
    @SuppressWarnings("unused") // Suppress unused warnings on 'ClassCastException e'
    public void start() {
        // init jFrame
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.playfieldDrawer.initialize();
        
        // convert toolbar
        JComponent toolbarComponent;
        try {
            toolbarComponent = (JComponent) this.toolbar;
        } catch (ClassCastException | NullPointerException e) {
            toolbarComponent = new JLabel("Toolbar not valid!", UIManager.getIcon("OptionPane.warningIcon"), CENTER);
        }
        
        // convert sidebar
        JComponent sidebarComponent;
        try {
            sidebarComponent = (JComponent) this.entitySidebar;
        } catch (ClassCastException | NullPointerException e) {
            sidebarComponent = new JLabel(UIManager.getIcon("OptionPane.warningIcon"), CENTER);
        }
        
        // convert console
        JComponent consoleComponent;
        try {
            consoleComponent = (JComponent) this.console;
        } catch (ClassCastException | NullPointerException e) {
            consoleComponent = new JLabel("Console not valid!", UIManager.getIcon("OptionPane.warningIcon"), CENTER);
        }
        
        // connect logger to console
        Logger.addSimulationOutputStream(this.console.getSimulationOutputStream());
        Logger.addOutOutputStream(this.console.getSystemOutputStream());
        Logger.addErrorOutputStream(this.console.getSystemOutputStream());
        
        // setup JFrame layout
        this.getContentPane().add(BorderLayout.NORTH, toolbarComponent);
        JSplitPane jsp1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.playfieldDrawer, consoleComponent);
        jsp1.setOneTouchExpandable(true);
        jsp1.setResizeWeight(0.8);
        JSplitPane jsp2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jsp1, sidebarComponent);
        jsp2.setOneTouchExpandable(true);
        this.getContentPane().add(BorderLayout.CENTER, jsp2);
        
        // finalize jFrame
        this.pack();
        this.setVisible(true);
    }
}

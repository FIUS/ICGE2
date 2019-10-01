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
        this.addTempTestDataToEntitySidebar(); //FIXME Remove
        
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
    
    //FIXME remove
    private void addTempTestDataToEntitySidebar() {
        SimulationTreeNode rootNode = new SimulationTreeNode("root", "Simulation", "", false);
        
        SimulationTreeNode players = new SimulationTreeNode("1", "Players", "", false);
        players.appendChild(new SimulationTreeNode("1:0", "Neo", ""));
        players.appendChild(new SimulationTreeNode("1:1", "Trinity", ""));
        players.appendChild(new SimulationTreeNode("1:2", "Morpheus", ""));
        rootNode.appendChild(players);
        
        SimulationTreeNode pills = new SimulationTreeNode("2", "Pills", "", false);
        pills.appendChild(new SimulationTreeNode("2:0", "Red-Pill <2,1>", ""));
        pills.appendChild(new SimulationTreeNode("2:1", "Red-Pill <3,1>", ""));
        pills.appendChild(new SimulationTreeNode("2:2", "Red-Pill <4,1>", ""));
        pills.appendChild(new SimulationTreeNode("2:3", "Blue-Pill <1,4>", ""));
        pills.appendChild(new SimulationTreeNode("2:4", "Blue-Pill <2,4>", ""));
        pills.appendChild(new SimulationTreeNode("2:5", "Blue-Pill <3,4>", ""));
        pills.appendChild(new SimulationTreeNode("2:6", "Blue-Pill <4,4>", ""));
        pills.appendChild(new SimulationTreeNode("2:7", "Blue-Pill <5,4>", ""));
        pills.appendChild(new SimulationTreeNode("2:8", "Blue-Pill <6,4>", ""));
        rootNode.appendChild(pills);
        
        SimulationTreeNode walls = new SimulationTreeNode("3", "Walls", "", false);
        walls.appendChild(new SimulationTreeNode("3:0", "Wall <0,0>", ""));
        walls.appendChild(new SimulationTreeNode("3:1", "Wall <1,0>", ""));
        walls.appendChild(new SimulationTreeNode("3:2", "Wall <2,0>", ""));
        walls.appendChild(new SimulationTreeNode("3:3", "Wall <3,0>", ""));
        walls.appendChild(new SimulationTreeNode("3:4", "Wall <4,0>", ""));
        walls.appendChild(new SimulationTreeNode("3:5", "Wall <5,0>", ""));
        walls.appendChild(new SimulationTreeNode("3:6", "Wall <6,0>", ""));
        walls.appendChild(new SimulationTreeNode("3:7", "Wall <7,0>", ""));
        walls.appendChild(new SimulationTreeNode("3:8", "Wall <0,1>", ""));
        walls.appendChild(new SimulationTreeNode("3:9", "Wall <0,2>", ""));
        walls.appendChild(new SimulationTreeNode("3:10", "Wall <0,3>", ""));
        walls.appendChild(new SimulationTreeNode("3:11", "Wall <0,4>", ""));
        walls.appendChild(new SimulationTreeNode("3:12", "Wall <0,5>", ""));
        walls.appendChild(new SimulationTreeNode("3:13", "Wall <1,5>", ""));
        walls.appendChild(new SimulationTreeNode("3:14", "Wall <2,5>", ""));
        walls.appendChild(new SimulationTreeNode("3:15", "Wall <3,5>", ""));
        walls.appendChild(new SimulationTreeNode("3:16", "Wall <4,5>", ""));
        walls.appendChild(new SimulationTreeNode("3:17", "Wall <5,5>", ""));
        walls.appendChild(new SimulationTreeNode("3:18", "Wall <6,5>", ""));
        walls.appendChild(new SimulationTreeNode("3:19", "Wall <7,5>", ""));
        walls.appendChild(new SimulationTreeNode("3:20", "Wall <7,1>", ""));
        walls.appendChild(new SimulationTreeNode("3:21", "Wall <7,2>", ""));
        walls.appendChild(new SimulationTreeNode("3:22", "Wall <7,3>", ""));
        walls.appendChild(new SimulationTreeNode("3:23", "Wall <7,4>", ""));
        walls.appendChild(new SimulationTreeNode("3:24", "Wall <7,5>", ""));
        rootNode.appendChild(walls);
        
        this.entitySidebar.setSimulationTreeRootNode(rootNode);
    }
}

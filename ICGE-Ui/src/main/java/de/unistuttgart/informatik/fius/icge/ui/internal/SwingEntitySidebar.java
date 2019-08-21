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

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.DefaultMutableTreeNode;

import de.unistuttgart.informatik.fius.icge.ui.EntitySidebar;

/**
 * A swing implementation of the EntitySidebar
 *
 * @author Tobias Wältken
 * @version 1.0
 */
public class SwingEntitySidebar extends JPanel implements EntitySidebar {
    private static final long serialVersionUID = -4409545257025298208L;

    /** The root node of the entity list */
    public DefaultMutableTreeNode rootNode;
    /** The hierarchical list of all entities */
    public JTree entityList;

    /**
     * The default constructor
     */
    public SwingEntitySidebar() {
        this.setupRootNode();

        this.entityList = new JTree(this.rootNode);
        this.entityList.setShowsRootHandles(false);
        this.entityList.setRootVisible(true);
        this.entityList.expandRow(2);
        this.entityList.expandRow(1);

        JScrollPane pane = new JScrollPane(this.entityList);
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.setLayout(new BorderLayout());
        this.add(pane, BorderLayout.CENTER);
    }

    private void setupRootNode() {
        this.rootNode = new DefaultMutableTreeNode("Simulation");

        DefaultMutableTreeNode player = new DefaultMutableTreeNode("Player");
        player.add(new DefaultMutableTreeNode("Neo"));
        player.add(new DefaultMutableTreeNode("Trinity"));
        player.add(new DefaultMutableTreeNode("Morpheus"));
        this.rootNode.add(player);

        DefaultMutableTreeNode pills = new DefaultMutableTreeNode("Pills");
        pills.add(new DefaultMutableTreeNode("Red-Pill"));
        pills.add(new DefaultMutableTreeNode("Red-Pill"));
        pills.add(new DefaultMutableTreeNode("Red-Pill"));
        pills.add(new DefaultMutableTreeNode("Blue-Pill"));
        pills.add(new DefaultMutableTreeNode("Blue-Pill"));
        pills.add(new DefaultMutableTreeNode("Blue-Pill"));
        pills.add(new DefaultMutableTreeNode("Blue-Pill"));
        pills.add(new DefaultMutableTreeNode("Blue-Pill"));
        pills.add(new DefaultMutableTreeNode("Blue-Pill"));
        this.rootNode.add(pills);

        DefaultMutableTreeNode walls = new DefaultMutableTreeNode("Walls");
        walls.add(new DefaultMutableTreeNode("Wall"));
        walls.add(new DefaultMutableTreeNode("Wall"));
        walls.add(new DefaultMutableTreeNode("Wall"));
        walls.add(new DefaultMutableTreeNode("Wall"));
        walls.add(new DefaultMutableTreeNode("Wall"));
        walls.add(new DefaultMutableTreeNode("Wall"));
        walls.add(new DefaultMutableTreeNode("Wall"));
        walls.add(new DefaultMutableTreeNode("Wall"));
        walls.add(new DefaultMutableTreeNode("Wall"));
        walls.add(new DefaultMutableTreeNode("Wall"));
        walls.add(new DefaultMutableTreeNode("Wall"));
        walls.add(new DefaultMutableTreeNode("Wall"));
        walls.add(new DefaultMutableTreeNode("Wall"));
        walls.add(new DefaultMutableTreeNode("Wall"));
        walls.add(new DefaultMutableTreeNode("Wall"));
        this.rootNode.add(walls);
    }
}

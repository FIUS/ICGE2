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
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import de.unistuttgart.informatik.fius.icge.ui.EntityInspectorEntry;
import de.unistuttgart.informatik.fius.icge.ui.EntitySidebar;
import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy;
import de.unistuttgart.informatik.fius.icge.ui.SimulationTreeNode;


/**
 * A swing implementation of the EntitySidebar
 *
 * @author Tobias Wältken
 * @version 1.0
 */
public class SwingEntitySidebar extends JPanel implements EntitySidebar {
    private static final long serialVersionUID = -4409545257025298208L;
    
    /** The simulation proxy */
    private SimulationProxy            simulationProxy;
    /** The texture registry */
    private final SwingTextureRegistry textureRegistry;
    
    /** The root node of the entity list */
    public SimulationTreeNode   rootNode;
    /** The hierarchical list of all entities */
    public JTree                entityList;
    /** The model of the JTree component */
    public DefaultTreeModel     entityListModel;
    /** The entity inspector in the sidebar */
    public SwingEntityInspector entityInspector;
    
    /**
     * The default constructor
     *
     * @param textureRegistry
     *     The texture registry the textures and icons are loaded from
     * @param dpiScale
     *     The scaling value to adjust for highdpi screens
     */
    public SwingEntitySidebar(final SwingTextureRegistry textureRegistry, final double dpiScale) {
        // class setup
        this.textureRegistry = textureRegistry;
        
        // JTree setup
        this.entityListModel = new DefaultTreeModel(null, true);
        this.entityList = new JTree(this.entityListModel);
        this.entityList.setShowsRootHandles(false);
        this.entityList.setRootVisible(true);
        this.entityList.setEnabled(false);
        //TODO write custom entity renderer for the JTree
        this.entityList.addTreeSelectionListener(arg0 -> {
            if (SwingEntitySidebar.this.entityList.getLastSelectedPathComponent() == null) {
                if (SwingEntitySidebar.this.simulationProxy != null) {
                    SwingEntitySidebar.this.simulationProxy.selectedSimulationEntityChange(null);
                }
                return;
            }
            
            if (SwingEntitySidebar.this.simulationProxy != null) {
                SwingEntitySidebar.this.simulationProxy.selectedSimulationEntityChange(
                        (SimulationTreeNode) ((DefaultMutableTreeNode) SwingEntitySidebar.this.entityList.getLastSelectedPathComponent())
                                .getUserObject()
                );
            }
        });
        
        // Entity inspector setup
        this.entityInspector = new SwingEntityInspector(this.textureRegistry);
        
        // Sidebar layout
        final JScrollPane pane = new JScrollPane(this.entityList);
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        final JSplitPane jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pane, this.entityInspector);
        jsp.setOneTouchExpandable(true);
        jsp.setResizeWeight(0.4);
        this.setLayout(new BorderLayout());
        this.add(jsp, BorderLayout.CENTER);
    }
    
    /**
     * TODO better doc
     *
     * @param simulationProxy
     *     The simulation proxy to use
     */
    public void setSimulationProxy(final SimulationProxy simulationProxy) {
        if (this.simulationProxy != null) throw new IllegalStateException("SimulationProxy is already set and cannot be overwritten!");
        
        this.simulationProxy = simulationProxy;
    }
    
    @Override
    public void setSimulationTreeRootNode(final SimulationTreeNode treeNode) {
        this.rootNode = treeNode;
        this.entityListModel.setRoot(SwingEntitySidebar.generateDefaultMutableTreeNodeFromSimulationTreeNode(this.rootNode));
        this.updateSimulationTree();
    }
    
    private void getExpandedTreePaths(final List<TreePath> expanded, final TreePath path) {
        if (path == null) return;
        for (final Enumeration<TreePath> e = this.entityList.getExpandedDescendants(path); e.hasMoreElements();) {
            final TreePath p = e.nextElement();
            expanded.add(p);
            this.getExpandedTreePaths(expanded, p);
        }
    }
    
    private TreePath getRootPath() {
        final Object root = this.entityList.getModel().getRoot();
        if (root == null) return null;
        return new TreePath(root);
    }
    
    @Override
    public void updateSimulationTree() {
        final TreePath lastSelected = this.entityList.getSelectionPath();
        final List<TreePath> expanded = new ArrayList<>();
        this.getExpandedTreePaths(expanded, this.getRootPath());
        SwingEntitySidebar.updateTreeNodeChildren((DefaultMutableTreeNode) this.entityList.getModel().getRoot());
        this.entityListModel.reload();
        this.entityList.addSelectionPath(lastSelected);
        for (final TreePath p : expanded) {
            this.entityList.expandPath(p);
        }
    }
    
    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        this.entityList.setEnabled(enabled);
    }
    
    @Override
    public void setEntityInspectorEntries(final EntityInspectorEntry[] entries) {
        this.entityInspector.updateEntityInspectorEntries(entries);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 800);
    }
    
    private static void updateTreeNodeChildren(final DefaultMutableTreeNode node) {
        if ((node == null) || !node.getAllowsChildren()) return;
        final SimulationTreeNode data = (SimulationTreeNode) node.getUserObject();
        
        final List<DefaultMutableTreeNode> toRemove = new ArrayList<>();
        final List<SimulationTreeNode> toAdd = new ArrayList<>();
        
        outer: for (final SimulationTreeNode child : data.getChildren()) {
            for (final Enumeration<TreeNode> e = node.children(); e.hasMoreElements();) {
                final DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) e.nextElement();
                if (childNode.getUserObject().equals(child)) {
                    continue outer;
                }
            }
            toAdd.add(child);
        }
        
        outer: for (final Enumeration<TreeNode> e = node.children(); e.hasMoreElements();) {
            final DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) e.nextElement();
            for (final SimulationTreeNode child : data.getChildren()) {
                if (childNode.getUserObject().equals(child)) {
                    continue outer;
                }
            }
            toRemove.add(childNode);
        }
        
        for (final DefaultMutableTreeNode child : toRemove) {
            node.remove(child);
        }
        
        for (final SimulationTreeNode child : toAdd) {
            node.add(SwingEntitySidebar.generateDefaultMutableTreeNodeFromSimulationTreeNode(child));
        }
        
        for (final Enumeration<TreeNode> e = node.children(); e.hasMoreElements();) {
            final DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) e.nextElement();
            SwingEntitySidebar.updateTreeNodeChildren(childNode);
        }
    }
    
    /**
     * Recursively generate a {@link DefaultMutableTreeNode} from a {@link SimulationTreeNode}
     *
     * @param node
     *     The {@link SimulationTreeNode} the Tree structure is generated from
     * @return Returns the corresponding {@link DefaultMutableTreeNode}
     */
    private static DefaultMutableTreeNode generateDefaultMutableTreeNodeFromSimulationTreeNode(final SimulationTreeNode node) {
        final DefaultMutableTreeNode returnNode = new DefaultMutableTreeNode(node);
        
        if (node.isLeaf()) {
            returnNode.setAllowsChildren(false);
        } else {
            node.forEachChild(
                    (final SimulationTreeNode childNode) -> returnNode.add(SwingEntitySidebar.generateDefaultMutableTreeNodeFromSimulationTreeNode(childNode))
            );
        }
        
        return returnNode;
    }
    
    @Override
    public SimulationTreeNode getSimulationTreeSelectedElement() {
        return (SimulationTreeNode) ((DefaultMutableTreeNode) SwingEntitySidebar.this.entityList.getLastSelectedPathComponent())
                .getUserObject();
    }
    
    @Override
    public void setSimulationTreeSelectedElement(final SimulationTreeNode node) {
        //TODO implement
    }
    
    @Override
    public void enableSimulationTree() {
        SwingEntitySidebar.this.entityList.setEnabled(true);
    }
    
    @Override
    public void disbaleSimulationTree() {
        SwingEntitySidebar.this.entityList.setEnabled(false);
        SwingEntitySidebar.this.entityListModel.setRoot(null);
    }
    
    @Override
    public void setEntityInspectorName(final String name) {
        SwingEntitySidebar.this.entityInspector.setName(name);
    }
    
    @Override
    public String getEntityInspectorName() {
        return SwingEntitySidebar.this.entityInspector.getName();
    }
    
    @Override
    public void enableEntityInspector() {
        SwingEntitySidebar.this.entityInspector.setEnabled(true);
    }
    
    @Override
    public void disableEntityInspector() {
        SwingEntitySidebar.this.entityInspector.setEnabled(false);
    }
}

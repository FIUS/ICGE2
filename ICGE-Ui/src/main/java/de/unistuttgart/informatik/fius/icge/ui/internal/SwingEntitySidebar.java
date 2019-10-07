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
import java.util.Objects;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import de.unistuttgart.informatik.fius.icge.ui.EntityInspectorEntry;
import de.unistuttgart.informatik.fius.icge.ui.EntitySidebar;
import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy;
import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy.EntityInspectorListener;
import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy.SimulationTreeListener;
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
    private final SimulationProxy      simulationProxy;
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
    
    private EntityInspectorEntry[] lastEntries;
    
    /**
     * The default constructor
     * 
     * @param simulationProxy
     *     The simulation proxy to use
     * @param textureRegistry
     *     The texture registry the textures and icons are loaded from
     */
    public SwingEntitySidebar(final SimulationProxy simulationProxy, final SwingTextureRegistry textureRegistry) {
        // class setup
        this.simulationProxy = simulationProxy;
        this.textureRegistry = textureRegistry;
        
        // JTree setup
        this.entityListModel = new DefaultTreeModel(null, true);
        this.entityList = new JTree(this.entityListModel);
        this.entityList.setShowsRootHandles(false);
        this.entityList.setRootVisible(true);
        this.entityList.setEnabled(false);
        //TODO write custom entity renderer for the JTree
        this.entityList.addTreeSelectionListener(new TreeSelectionListener() {
            
            @Override
            public void valueChanged(TreeSelectionEvent arg0) {
                if (SwingEntitySidebar.this.entityList.getLastSelectedPathComponent() == null) {
                    SwingEntitySidebar.this.simulationProxy.selectedSimulationEntityChange(null);
                    return;
                }
                
                SwingEntitySidebar.this.simulationProxy.selectedSimulationEntityChange(
                        (SimulationTreeNode) ((DefaultMutableTreeNode) SwingEntitySidebar.this.entityList.getLastSelectedPathComponent())
                                .getUserObject()
                );
            }
        });
        
        // Proxy setup
        this.simulationProxy.setSimulationTreeListener(new SimulationTreeListener() {
            
            @Override
            public SimulationTreeNode getSelectedElement() {
                return (SimulationTreeNode) ((DefaultMutableTreeNode) SwingEntitySidebar.this.entityList.getLastSelectedPathComponent())
                        .getUserObject();
            }
            
            @Override
            public void setSelectedElement(SimulationTreeNode node) {
                //TODO implement
            }
            
            @Override
            public void setRootNode(SimulationTreeNode rootNode) {
                SwingEntitySidebar.this.setSimulationTreeRootNode(rootNode);
            }
            
            @Override
            public void updateSimulationTree() {
                SwingEntitySidebar.this.updateSimulationTree();
            }
            
            @Override
            public void enable() {
                SwingEntitySidebar.this.entityList.setEnabled(true);
            }
            
            @Override
            public void disable() {
                SwingEntitySidebar.this.entityList.setEnabled(false);
                SwingEntitySidebar.this.entityListModel.setRoot(null);
            }
        });
        
        // Entity inspector setup
        this.entityInspector = new SwingEntityInspector(this.textureRegistry);
        this.simulationProxy.setEntityInspectorListener(new EntityInspectorListener() {
            
            @Override
            public void setName(String name) {
                SwingEntitySidebar.this.entityInspector.setName(name);
            }
            
            @Override
            public void setEntityEntries(EntityInspectorEntry[] entries) {
                SwingEntitySidebar.this.setEntityInspectorEntries(entries);
            }
            
            @Override
            public String getName() {
                return SwingEntitySidebar.this.entityInspector.getName();
            }
            
            @Override
            public void enable() {
                SwingEntitySidebar.this.entityInspector.setEnabled(true);
            }
            
            @Override
            public void disable() {
                SwingEntitySidebar.this.entityInspector.setEnabled(false);
            }
        });
        
        // Sidebar layout
        JScrollPane pane = new JScrollPane(this.entityList);
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        JSplitPane jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pane, this.entityInspector);
        jsp.setOneTouchExpandable(true);
        jsp.setResizeWeight(0.4);
        this.setLayout(new BorderLayout());
        this.add(jsp, BorderLayout.CENTER);
    }
    
    @Override
    public void setSimulationTreeRootNode(SimulationTreeNode treeNode) {
        this.rootNode = treeNode;
        this.entityListModel.setRoot(generateDefaultMutableTreeNodeFromSimulationTreeNode(this.rootNode));
        this.updateSimulationTree();
    }
    
    private void getExpanededTreePaths(List<TreePath> expanded, TreePath path) {
        if (path == null) return;
        for (Enumeration<TreePath> e = this.entityList.getExpandedDescendants(path); e.hasMoreElements();) {
            TreePath p = e.nextElement();
            expanded.add(p);
            getExpanededTreePaths(expanded, p);
        }
    }
    
    private TreePath getRootPath() {
        Object root = entityList.getModel().getRoot();
        if (root == null) return null;
        return new TreePath(root);
    }
    
    @Override
    public void updateSimulationTree() {
        TreePath lastSelected = this.entityList.getSelectionPath();
        List<TreePath> expanded = new ArrayList<>();
        getExpanededTreePaths(expanded, getRootPath());
        updateTreeNodeChildren((DefaultMutableTreeNode) this.entityList.getModel().getRoot());
        this.entityListModel.reload();
        this.entityList.addSelectionPath(lastSelected);
        for (TreePath p : expanded) {
            this.entityList.expandPath(p);
        }
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.entityList.setEnabled(enabled);
    }
    
    @Override
    public void setEntityInspectorEntries(EntityInspectorEntry[] entries) {
        if (Objects.equals(this.lastEntries, entries)) {
            return;
        }
        this.lastEntries = entries;
        
        this.entityInspector.clearUIElements();
        
        for (EntityInspectorEntry entry : entries) {
            this.entityInspector.addUIElement(entry.getName(), entry.getType(), entry.getValue(), (id, value) -> {
                entry.runCallback(value);
                this.simulationProxy.entityValueChange(id, value);
            });
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 800);
    }
    
    private static void updateTreeNodeChildren(DefaultMutableTreeNode node) {
        if (node == null || !node.getAllowsChildren()) return;
        SimulationTreeNode data = (SimulationTreeNode) node.getUserObject();
        
        List<DefaultMutableTreeNode> toRemove = new ArrayList<>();
        List<SimulationTreeNode> toAdd = new ArrayList<>();
        
        outer: for (SimulationTreeNode child : data.getChildren()) {
            for (Enumeration<TreeNode> e = node.children(); e.hasMoreElements();) {
                DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) e.nextElement();
                if (childNode.getUserObject().equals(child)) {
                    continue outer;
                }
            }
            toAdd.add(child);
        }
        
        outer: for (Enumeration<TreeNode> e = node.children(); e.hasMoreElements();) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) e.nextElement();
            for (SimulationTreeNode child : data.getChildren()) {
                if (childNode.getUserObject().equals(child)) {
                    continue outer;
                }
            }
            toRemove.add(childNode);
        }
        
        for (DefaultMutableTreeNode child : toRemove) {
            node.remove(child);
        }
        
        for (SimulationTreeNode child : toAdd) {
            node.add(generateDefaultMutableTreeNodeFromSimulationTreeNode(child));
        }
        
        for (Enumeration<TreeNode> e = node.children(); e.hasMoreElements();) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) e.nextElement();
            updateTreeNodeChildren(childNode);
        }
    }
    
    /**
     * Recursively generate a {@link DefaultMutableTreeNode} from a {@link SimulationTreeNode}
     *
     * @param node
     *     The {@link SimulationTreeNode} the Tree structure is generated from
     * @return Returns the corresponding {@link DefaultMutableTreeNode}
     */
    private static DefaultMutableTreeNode generateDefaultMutableTreeNodeFromSimulationTreeNode(SimulationTreeNode node) {
        DefaultMutableTreeNode returnNode = new DefaultMutableTreeNode(node);
        
        if (node.isLeaf()) returnNode.setAllowsChildren(false);
        else node.forEachChild(
                (SimulationTreeNode childNode) -> returnNode.add(generateDefaultMutableTreeNodeFromSimulationTreeNode(childNode))
        );
        
        return returnNode;
    }
}

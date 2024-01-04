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

/**
 * A sidebar showing all current entities
 *
 * @author Tobias Wältken
 * @version 1.0
 */
public interface EntitySidebar {

    /**
     * Setter function to attach a SimulationTree to the entity sidebar.
     *
     * @param treeNode
     *     The root node of the simulation tree.
     */
    void setSimulationTreeRootNode(SimulationTreeNode treeNode);

    /**
     * This function should be called if the simulation tree changes.
     */
    void updateSimulationTree();

    /**
     * This function deletes all currently created ui elements and creates a new inspector
     *
     * @param entries
     *     The entries to create the inspector from
     */
    void setEntityInspectorEntries(EntityInspectorEntry[] entries);

    /**
     * Get the selected element of the simulation tree.
     *
     * @return the currently selected SimulationTreeNode
     */
    SimulationTreeNode getSimulationTreeSelectedElement();

    /**
     * Set the selected element of the simulation tree.
     *
     * @param node
     *     The SimulationTreeNode to select
     */
    void setSimulationTreeSelectedElement(SimulationTreeNode node);

    /**
     * Enable the simulation tree.
     */
    void enableSimulationTree();

    /**
     * Disable the simulation tree.
     */
    void disbaleSimulationTree();

    /**
     * Set the name of the entity inspector.
     *
     * @param name
     *     The name to set
     */
    void setEntityInspectorName(String name);

    /**
     * Get the current name of the entity inspector.
     *
     * @return The name
     */
    String getEntityInspectorName();

    /**
     * Enable the entity inspector.
     */
    void enableEntityInspector();

    /**
     * Disable the entity inspector.
     */
    void disableEntityInspector();
}

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
    public void setSimulationTreeRootNode(SimulationTreeNode treeNode);
    
    /**
     * This function should be called if the simulation tree changes.
     */
    public void updateSimulationTree();
}

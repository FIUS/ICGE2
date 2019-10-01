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

import de.unistuttgart.informatik.fius.icge.ui.SimulationTreeNode;


/**
 * InternalSimulationTreeNode
 * 
 * @author Tobias Wältken
 * @version 1.0
 */
public class InternalSimulationTreeNode extends SimulationTreeNode {
    
    /**
     * This protected constructor is used to generate SimulationTreeNodes from MutableTreeNodeData. Keep in mind that
     * MutableTreeNodeData contains no child information thus it is ALWAYS treated as a leaf node.
     * 
     * @param data
     *     The data node the info is extracted from
     */
    protected InternalSimulationTreeNode(MutableTreeNodeData data) {
        super(data.getElementId(), data.getDisplayText(), data.getTextureId());
    }
}

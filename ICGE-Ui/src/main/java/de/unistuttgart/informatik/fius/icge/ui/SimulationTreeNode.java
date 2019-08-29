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


import java.util.ArrayList;
import java.util.function.Consumer;


/**
 * A SimulationTreeNode is a data container used to build the simulation tree
 * which is shown in the entity sidebar to allow the user to see and easily
 * select entities in the simulation.
 *
 * @author Tobias Wältken
 * @version 1.0
 */
public class SimulationTreeNode {

    private ArrayList<SimulationTreeNode> children;

    private String elementId;
    private String displayText;
    private String textureId;

    /**
     * Default constructor
     *
     * @param elementId A non visible id used to better identify the slected entity.
     * @param displayText The name of the entity which is displayed to the user.
     * @param textureId The id of the texture which is rendered infront of the display text.
     */
    public SimulationTreeNode(String elementId, String displayText, String textureId) {
        this.children = new ArrayList<>();

        this.elementId = elementId;
        this.displayText = displayText;
        this.textureId = textureId;
    }

    /**
     * Getter for the element id the non visible string to better identify the
     * selected entity.
     *
     * @return Returns a String
     */
    public String getElementId() {
        return this.elementId;
    }

    /**
     * Getter for the display text which is the name of the entity which is
     * displayed to the user.
     *
     * @return Returns a String
     */
    public String getDisplayText() {
        return this.displayText;
    }

    /**
     * Getter for the texture id the identifier of the texture which is rendered
     * infront of the display text.
     *
     * @return Returns a String
     */
    public String getTextureId() {
        return this.textureId;
    }

    /**
     * Appends a child node to this node
     *
     * @param node The node to append
     * @return Returns true if action was successfull
     */
    public boolean appendChild(SimulationTreeNode node) {
        return this.children.add(node);
    }

    /**
     * Runs a function for each added child node with the child node as a parameter
     *
     * @param lamda The function to run on every child node
     */
    public void forEachChild(Consumer<SimulationTreeNode> lamda) {
        for (SimulationTreeNode child : this.children) {
            lamda.accept(child);
        }
    }

    /**
     * Function to check if the node is a leaf node or has children.
     *
     * @return Returns false if it is a leaf node.
     */
    public boolean hasChildren() {
        return !this.children.isEmpty();
    }
}

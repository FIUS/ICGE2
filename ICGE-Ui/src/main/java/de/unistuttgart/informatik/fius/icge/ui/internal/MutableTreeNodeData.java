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

/**
 * MutableTreeNodeData
 */
public class MutableTreeNodeData {
    
    private String elementId;
    private String displayText;
    private String textureId;
    
    /**
     * Default Constructor
     *
     * @param elementId
     *     A non visible id used to better identify the slected entity.
     * @param displayText
     *     The name of the entity which is displayed to the user.
     * @param textureId
     *     The id of the texture which is rendered infront of the display text.
     */
    public MutableTreeNodeData(String elementId, String displayText, String textureId) {
        this.elementId = elementId;
        this.displayText = displayText;
        this.textureId = textureId;
    }
    
    /**
     * Getter for the element id the non visible string to better identify the selected entity.
     *
     * @return Returns a String
     */
    public String getElementId() {
        return this.elementId;
    }
    
    /**
     * Getter for the display text which is the name of the entity which is displayed to the user.
     *
     * @return Returns a String
     */
    public String getDisplayText() {
        return this.displayText;
    }
    
    /**
     * Getter for the texture id the identifier of the texture which is rendered infront of the display text.
     *
     * @return Returns a String
     */
    public String getTextureId() {
        return this.textureId;
    }
    
    @Override
    public String toString() {
        return this.displayText;
    }
}

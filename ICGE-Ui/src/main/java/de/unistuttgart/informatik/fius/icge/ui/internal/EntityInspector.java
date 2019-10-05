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

import java.util.ArrayList;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.unistuttgart.informatik.fius.icge.ui.EntityInspectorEntry;
import de.unistuttgart.informatik.fius.icge.ui.internal.SwingTextureRegistry;


/**
 * EntityInspector
 */
public class EntityInspector extends JPanel {
    private static final long          serialVersionUID = 1L;
    
    /** The texture registry */
    private final SwingTextureRegistry textureRegistry;

    private GridBagConstraints gbc;

    /**
     * Default constructor
     * 
     * @param textureRegistry
     *     The texture registry to get the images from
     */
    public EntityInspector(final SwingTextureRegistry textureRegistry) {
        this.textureRegistry = textureRegistry;

        this.setLayout(new GridBagLayout());
        this.gbc = new GridBagConstraints();
        this.gbc.fill = GridBagConstraints.HORIZONTAL;
        this.gbc.gridx = 0;
        this.gbc.gridy = 0;

        addUIElement("Test", "string", "value");
    }

    public void addUIElement(String name, String type, String value) {
        this.add(new JLabel(name + ": "), this.gbc);
        this.gbc.gridx = 1;

        switch (type) {
            case "string":
                this.add(new JTextField(value), this.gbc);
                break;
            default:
                this.add(new JLabel(value), this.gbc);
        }
        
        this.gbc.gridx = 0;
        this.gbc.gridy += 1;
    }
}

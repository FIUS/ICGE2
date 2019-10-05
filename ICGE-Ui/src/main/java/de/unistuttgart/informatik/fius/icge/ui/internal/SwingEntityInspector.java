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
import java.util.function.BiConsumer;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.unistuttgart.informatik.fius.icge.ui.internal.SwingTextureRegistry;


/**
 * EntityInspector
 */
public class SwingEntityInspector extends JPanel {
    private static final long serialVersionUID = 1L;
    
    /** The texture registry */
    private final SwingTextureRegistry textureRegistry;
    
    private GridBagConstraints gbc;
    
    /** The name wich is displayed at the top */
    private String name;
    
    /**
     * Default constructor
     * 
     * @param textureRegistry
     *     The texture registry to get the images from
     */
    public SwingEntityInspector(final SwingTextureRegistry textureRegistry) {
        this.textureRegistry = textureRegistry;
        this.name = "";
        
        this.setLayout(new GridBagLayout());
        this.gbc = new GridBagConstraints();
        this.gbc.fill = GridBagConstraints.HORIZONTAL;
        this.gbc.gridx = 0;
        this.gbc.gridy = 0;
    }
    
    /**
     * Getter for the inspector name wich is displayed at the top
     * 
     * @return the name
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Setter for the inspector name
     * 
     * @param name
     *     The new name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Add a entry to the ui editor
     * 
     * @param name
     *     The name of the setting, which is displayed infromt
     * @param type
     *     The type of the setting
     * @param value
     *     The default/start value of the element
     * @param callback
     *     The callback run on value change
     */
    public void addUIElement(String name, String type, String value, BiConsumer<String, String> callback) {
        this.add(new JLabel(name + ": "), this.gbc);
        this.gbc.gridx = 1;
        
        switch (type) {
            case "string":
                JTextField textField = new JTextField(value);
                textField.addActionListener(ae -> callback.accept(name, textField.getText()));
                this.add(textField, this.gbc);
                break;
            case "function":
                JButton button = new JButton("call");
                button.addActionListener(ae -> callback.accept(name, ""));
                this.add(button, this.gbc);
                break;
            default:
                this.add(new JLabel(value), this.gbc);
        }
        
        this.gbc.gridx = 0;
        this.gbc.gridy += 1;
    }
    
    /**
     * Clears the entity editor
     */
    public void clearUIElements() {
        //TODO implement
    }
}

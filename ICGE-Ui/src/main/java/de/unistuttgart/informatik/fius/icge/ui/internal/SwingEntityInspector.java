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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.NumberFormat;
import java.util.function.BiConsumer;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;


/**
 * EntityInspector
 */
public class SwingEntityInspector extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private final GridBagConstraints gbc;
    
    /** The name at the top */
    private final JLabel title;
    
    /** The JPanel containing all the ui elements */
    private JPanel inspector;
    
    /** The user warning at the bottom */
    private final JLabel warning;
    
    /**
     * Default constructor
     * 
     * @param textureRegistry
     *     The texture registry to get the images from
     */
    public SwingEntityInspector(final SwingTextureRegistry textureRegistry) {
        this.setLayout(new BorderLayout());
        
        this.title = new JLabel("Empty");
        this.add(this.title, BorderLayout.PAGE_START);
        
        this.inspector = new JPanel();
        this.inspector.setLayout(new GridBagLayout());
        this.add(this.inspector, BorderLayout.CENTER);
        
        this.warning = new JLabel("Pause the simulation to modify an entity!");
        this.warning.setVisible(false);
        this.add(this.warning, BorderLayout.PAGE_END);
        
        this.gbc = new GridBagConstraints();
        this.gbc.fill = GridBagConstraints.HORIZONTAL;
        this.gbc.gridx = 0;
        this.gbc.gridy = 0;
        
        this.setEnabled(false);
        this.warning.setVisible(false);
    }
    
    /**
     * Getter for the inspector name wich is displayed at the top
     * 
     * @return the name
     */
    @Override
    public String getName() {
        return this.title.getText();
    }
    
    /**
     * Setter for the inspector name
     * 
     * @param name
     *     The new name
     */
    @Override
    public void setName(final String name) {
        this.title.setText(name);
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
    public void addUIElement(final String name, final String type, final String value, final BiConsumer<String, String> callback) {
        this.inspector.add(new JLabel(name + ": "), this.gbc);
        this.gbc.gridx = 1;
        
        switch (type) {
            case "integer": {
                final NumberFormatter formatter = new NumberFormatter(NumberFormat.getInstance());
                formatter.setValueClass(Integer.class);
                formatter.setMinimum(Integer.MIN_VALUE);
                formatter.setMaximum(Integer.MAX_VALUE);
                formatter.setAllowsInvalid(false);
                final JFormattedTextField field = new JFormattedTextField(formatter);
                field.addActionListener(ae -> callback.accept(name, field.getText()));
                this.inspector.add(field, this.gbc);
            }
                break;
            case "long": {
                final NumberFormatter formatter = new NumberFormatter(NumberFormat.getInstance());
                formatter.setValueClass(Long.class);
                formatter.setMinimum(Long.MIN_VALUE);
                formatter.setMaximum(Long.MAX_VALUE);
                formatter.setAllowsInvalid(false);
                final JFormattedTextField field = new JFormattedTextField(formatter);
                field.addActionListener(ae -> callback.accept(name, field.getText()));
                this.inspector.add(field, this.gbc);
            }
                break;
            case "string": {
                final JTextField field = new JTextField(value);
                field.addActionListener(ae -> callback.accept(name, field.getText()));
                this.inspector.add(field, this.gbc);
            }
                break;
            case "function":
                final JButton button = new JButton("call");
                button.addActionListener(ae -> callback.accept(name, ""));
                this.inspector.add(button, this.gbc);
                break;
            default:
                this.inspector.add(new JLabel(value), this.gbc);
        }
        
        this.gbc.gridx = 0;
        this.gbc.gridy += 1;
        
        this.revalidate();
        this.repaint();
    }
    
    /**
     * Clears the entity editor
     */
    public void clearUIElements() {
        this.remove(this.inspector);
        this.inspector = new JPanel();
        this.inspector.setLayout(new GridBagLayout());
        this.add(this.inspector, BorderLayout.CENTER);
        
        this.gbc.gridx = 0;
        this.gbc.gridy = 0;
        
        this.revalidate();
        this.repaint();
    }
    
    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        
        this.title.setEnabled(enabled);
        SwingEntityInspector.setEnabledState(this.inspector, enabled);
        this.warning.setVisible(!enabled);
    }
    
    /**
     * Recursive method to enable and disable a JPanel and its children
     * 
     * @param panel
     *     The panel to traverse
     * @param state
     *     The state to set its enabled state to
     */
    public static void setEnabledState(final JPanel panel, final boolean state) {
        panel.setEnabled(state);
        
        for (final Component component : panel.getComponents()) {
            if (component instanceof JPanel) {
                SwingEntityInspector.setEnabledState((JPanel) component, state);
            }
            component.setEnabled(state);
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 300);
    }
}

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
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.NumberFormatter;

import de.unistuttgart.informatik.fius.icge.ui.EntityInspectorEntry;


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
    
    private Object                 updateLock = new Object();
    private EntityInspectorEntry[] currentEntries;
    private List<Consumer<String>> uiValueUpdaters;
    
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
     * Update the entity inspector to reflect the new Values.
     *
     * @param entries
     *     the new list of entries to display
     */
    public void updateEntityInspectorEntries(final EntityInspectorEntry[] entries) {
        if (this.canUpdateValuesOnly(entries)) {
            // only update values
            SwingUtilities.invokeLater(() -> {
                synchronized (this.updateLock) {
                    // check preconditions
                    List<Consumer<String>> valueUpdaters = this.uiValueUpdaters;
                    if (valueUpdaters == null) return; // TODO throw exeptions here?
                    if (valueUpdaters.size() != entries.length) return;
                    
                    // update values
                    for (int i = 0; i < entries.length; i++) {
                        valueUpdaters.get(i).accept(entries[i].getValue());
                    }
                    
                    // change to new entries list
                    this.currentEntries = entries;
                    
                    // update ui
                    this.revalidate();
                    this.repaint();
                }
            });
            return;
        }
        
        // completely update the view
        SwingUtilities.invokeLater(() -> {
            synchronized (this.updateLock) {
                // clear old elements first, then update current entries list
                this.clearUIElements();
                
                // change to new entries list
                this.uiValueUpdaters = null;
                this.currentEntries = entries; // only do this after clearing old elements
                
                // add new ui elements
                List<Consumer<String>> newUiValueUpdaters = new ArrayList<>(entries.length);
                for (int i = 0; i < entries.length; i++) {
                    newUiValueUpdaters.add(this.addUIElement(entries[i], i));
                }
                
                // set new value updaters
                this.uiValueUpdaters = newUiValueUpdaters;
                
                // update ui
                this.revalidate();
                this.repaint();
            }
        });
    }
    
    /**
     * Check if the new list of entries matches the current entries structurally.
     *
     * @param entries
     *     The ne list of entries to check against currentEntries
     * @return true iff only the values need to be updated
     */
    private boolean canUpdateValuesOnly(final EntityInspectorEntry[] entries) {
        if (this.currentEntries == null) return false;
        if (this.currentEntries.length != entries.length) return false;
        for (int i = 0; i < entries.length; i++) {
            EntityInspectorEntry oldEntry = this.currentEntries[i];
            EntityInspectorEntry newEntry = entries[i];
            if (!oldEntry.getName().equals(newEntry.getName())) return false;
            if (!oldEntry.getType().equals(newEntry.getType())) return false;
            // only value can differ for updates without complete redraws!
        }
        return true;
    }
    
    /**
     * Callback to change the value of an entry.
     * 
     * This indirect callback is used to avoid changing the callback on the ui element when only the values of the
     * entries have changed.
     * 
     * @param index
     *     the index of the entry to update
     * @param name
     *     the name of the entry to update (additional safeguard)
     * @param value
     *     the new value to set for the entry
     */
    private void updateValueOnEntity(int index, String name, String value) {
        if (this.currentEntries == null || index >= this.currentEntries.length) return;
        EntityInspectorEntry entry = this.currentEntries[index];
        if (!entry.getName().equals(name)) return;
        entry.runCallback(value);
    }
    
    /**
     * Add a entry to the ui editor
     * 
     * @param entry
     *     The entry to add a ui element for
     * @param index
     *     The index of the entry in currentEntries
     * @return A consumer that updates the newly added ui element to the given string value
     */
    private Consumer<String> addUIElement(final EntityInspectorEntry entry, final int index) {
        
        final String name = entry.getName();
        this.inspector.add(new JLabel(name + ": "), this.gbc);
        this.gbc.gridx = 1;
        
        Consumer<String> updateUiValueCallback = (newValue) -> {
            /* Default is to update nothing. */};
        
        switch (entry.getType()) {
            case "integer": {
                final NumberFormatter formatter = new NumberFormatter(NumberFormat.getInstance());
                formatter.setValueClass(Integer.class);
                formatter.setMinimum(Integer.MIN_VALUE);
                formatter.setMaximum(Integer.MAX_VALUE);
                formatter.setAllowsInvalid(false);
                final JFormattedTextField field = new JFormattedTextField(formatter);
                field.addActionListener(ae -> this.updateValueOnEntity(index, name, field.getText()));
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
                field.addActionListener(ae -> this.updateValueOnEntity(index, name, field.getText()));
                this.inspector.add(field, this.gbc);
            }
                break;
            case "string": {
                final JTextField field = new JTextField(entry.getValue());
                updateUiValueCallback = (newValue) -> field.setText(newValue);
                field.addActionListener(ae -> this.updateValueOnEntity(index, name, field.getText()));
                this.inspector.add(field, this.gbc);
            }
                break;
            case "function":
                final JButton button = new JButton("call");
                button.addActionListener(ae -> this.updateValueOnEntity(index, name, ""));
                this.inspector.add(button, this.gbc);
                break;
            default:
                JLabel label = new JLabel(entry.getValue());
                updateUiValueCallback = (newValue) -> label.setText(newValue);
                this.inspector.add(label, this.gbc);
        }
        
        this.gbc.gridx = 0;
        this.gbc.gridy += 1;
        
        return updateUiValueCallback;
    }
    
    /**
     * Clears the entity editor
     */
    private void clearUIElements() {
        this.remove(this.inspector);
        this.inspector = new JPanel();
        this.inspector.setLayout(new GridBagLayout());
        this.add(this.inspector, BorderLayout.CENTER);
        
        this.gbc.gridx = 0;
        this.gbc.gridy = 0;
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
    // set to private to avoid having to check for event dispatch thread on public API
    private static void setEnabledState(final JPanel panel, final boolean state) {
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

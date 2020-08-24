/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.ui.internal.dropdown;

import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.unistuttgart.informatik.fius.icge.ui.internal.SwingTextureRegistry;


/**
 * The DropdownSelector is a JComboBox based drop down menu to select a string
 *
 * @author Tobias Wältken
 * @version 1.0
 */
public class DropdownSelector extends JPanel {
    private static final long serialVersionUID = -3898035206502504991L;
    
    /**
     * This class represents a single entry of the selector
     *
     * @author Tobias Wältken
     * @version 1.0
     */
    public class DropdownEntry {
        /** The name of the entry which is shown to the user */
        public String displayName;
        /** The texture id of the icon which is rendert infront of the display name */
        public String textureID;
        
        /**
         * The empty constructor
         */
        public DropdownEntry() {
            this.displayName = "";
            this.textureID = "";
        }
        
        /**
         * This constructor is used for text only entries
         *
         * @param name
         *     the display name
         */
        public DropdownEntry(final String name) {
            this.displayName = name;
            this.textureID = "";
        }
        
        /**
         * The default constructor
         *
         * @param name
         *     the display name shown to the user
         * @param texture
         *     the texture id of the icon infront of the display text
         */
        public DropdownEntry(final String name, final String texture) {
            this.displayName = name;
            this.textureID = texture;
        }
    }
    
    /** The texture registry */
    private final SwingTextureRegistry textureRegistry;
    
    private final JLabel                                    label;
    private final JComboBox<DropdownSelector.DropdownEntry> comboBox;
    
    /** The data model of the DropdownSelector */
    private final DefaultComboBoxModel<DropdownEntry> model;
    
    /**
     * Constructor of the DropdownSelector
     *
     * @param textureRegistry
     *     The texture registry the textures and icons are loaded from
     * @param header
     *     The header text which is displayed above the combobox
     */
    public DropdownSelector(final SwingTextureRegistry textureRegistry, final String header) {
        this.textureRegistry = textureRegistry;
        
        this.label = new JLabel(header + ": ");
        this.comboBox = new JComboBox<>();
        
        this.model = new DefaultComboBoxModel<>();
        this.comboBox.setModel(this.model);
        this.comboBox.setRenderer(new DropdownItemRenderer(this.textureRegistry));
        this.comboBox.setEditor(new DropdownItemEditor(this.textureRegistry));
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(this.label);
        this.add(this.comboBox);
    }
    
    /**
     * Getter for the currently selected entry
     *
     * @return returns the current {@link DropdownEntry}
     */
    public DropdownEntry getCurrentEntry() {
        return (DropdownEntry) this.comboBox.getEditor().getItem();
    }
    
    /**
     * Setter for the currently selected entry
     * 
     * WARNING: This is not checked if it is available it can be ANY entry
     * 
     * @param entry
     *     The entry to set as selected
     */
    public void setCurrentEntry(final DropdownEntry entry) {
        this.comboBox.getEditor().setItem(entry);
    }
    
    /**
     * This function adds an entry to the selector
     *
     * @param entries
     *     1 to n entries to be appended
     */
    public void addEntry(final DropdownEntry... entries) {
        for (final DropdownEntry entry : entries) {
            this.model.addElement(entry);
            
            if (getCurrentEntry() == null)
                this.setCurrentEntry(entry);
        }
    }
    
    /**
     * This function removes all entries from the dropdown menu
     */
    public void removeAllEntries() {
        this.model.removeAllElements();
    }
    
    /**
     * Adds a listener which reacts to the selection and deselection of items
     * 
     * @param listener
     *     The listener which is added
     */
    public void addSelectionListener(final ItemListener listener) {
        this.comboBox.addItemListener(listener);
    }
    
    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        this.comboBox.setEnabled(enabled);
        this.label.setEnabled(enabled);
    }
}

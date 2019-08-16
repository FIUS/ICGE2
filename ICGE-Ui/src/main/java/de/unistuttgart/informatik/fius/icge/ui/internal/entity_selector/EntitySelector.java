/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 *
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.ui.internal.entity_selector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import de.unistuttgart.informatik.fius.icge.ui.internal.SwingTextureRegistry;

/**
 * The EntitySelector is a JComboBox based drop down menu to select a game entity
 *
 * @author Tobias Wältken
 * @version 1.0
 */
public class EntitySelector extends JComboBox<EntitySelector.EntityEntry> {
    private static final long serialVersionUID = -3898035206502504991L;

    /** The texture registry */
    private final SwingTextureRegistry textureRegistry;

    /**
     * This class represents a single entry of the selector
     *
     * @author Tobias Wältken
     * @version 1.0
     */
    public class EntityEntry {
        /** The name of the entry which is shown to the user */
        public String displayName;
        /** The texture id of the icon which is rendert infront of the display name */
        public String textureID;

        /**
         * The empty constructor
         */
        public EntityEntry() {
            this.displayName = "";
            this.textureID = "";
        }

        /**
         * This constructor is used for text only entries
         *
         * @param name the display name
         */
        public EntityEntry(String name) {
            this.displayName = name;
            this.textureID = "";
        }

        /**
         * The default constructor
         *
         * @param name the display name shown to the user
         * @param texture the texture id of the icon infront of the display text
         */
        public EntityEntry(String name, String texture) {
            this.displayName = name;
            this.textureID = texture;
        }
    }

    /** The data model of the EntitySelector */
    private DefaultComboBoxModel<EntityEntry> model;

    /**
     * Constructor of the EntitySelector
     *
     * @param textureRegistry The texture registry the textures and icons are loaded from
     */
    public EntitySelector(SwingTextureRegistry textureRegistry) {
        this.textureRegistry = textureRegistry;

        this.model = new DefaultComboBoxModel<>();
        this.setModel(this.model);
        this.setRenderer(new EntityItemRenderer(this.textureRegistry));
        this.setEditor(new EntityItemEditor(this.textureRegistry));
    }

    /**
     * Getter for the currently selected entry
     *
     * @return returns the current {@link EntityEntry}
     */
    public EntityEntry getCurrentEntry() {
        return (EntityEntry)this.getEditor().getItem();
    }

    /**
     * This function adds an entry to the selector
     *
     * @param entries 1 to n entries to be appended
     */
    public void addEntry(EntityEntry ... entries) {
        for (EntityEntry entry : entries)
            this.model.addElement(entry);
    }
}

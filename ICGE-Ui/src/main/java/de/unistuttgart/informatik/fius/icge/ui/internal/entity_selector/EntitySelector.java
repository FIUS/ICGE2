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

/**
 * EntitySelector
 *
 * @author Tobias Wältken
 * @version 1.0
 */
public class EntitySelector extends JComboBox<EntitySelector.EntityEntry> {
    private static final long serialVersionUID = -3898035206502504991L;

    public class EntityEntry {
        public String displayName;
        public String textureID;

        public EntityEntry() {
            this.displayName = "";
            this.textureID = "";
        }

        public EntityEntry(String name) {
            this.displayName = name;
            this.textureID = "";
        }

        public EntityEntry(String name, String texture) {
            this.displayName = name;
            this.textureID = texture;
        }
    }

    private DefaultComboBoxModel<EntityEntry> model;

    public EntitySelector() {
        this.model = new DefaultComboBoxModel<>();
        this.setModel(this.model);
        this.setRenderer(new EntityItemRenderer());
        this.setEditor(new EntityItemEditor());
    }

    public void addEntry(EntityEntry ... entries) {
        for (EntityEntry entry : entries)
            this.model.addElement(entry);
    }

    public EntityEntry getCurrentEntry() {
        return (EntityEntry)this.getEditor().getItem();
    }
}

/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 *
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.ui.internal.dropdown_selector;

import static javax.swing.SwingConstants.LEFT;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicComboBoxEditor;

import de.unistuttgart.informatik.fius.icge.ui.internal.SwingTextureRegistry;
import de.unistuttgart.informatik.fius.icge.ui.internal.dropdown_selector.DropdownSelector.DropdownEntry;

/**
 * DropdownItemEditor
 *
 * @author Tobias Wältken
 * @version 1.0
 */
class DropdownItemEditor extends BasicComboBoxEditor {

    /** The texture registry */
    private final SwingTextureRegistry textureRegistry;

    private JPanel panel;
    private JLabel labelItem;
    private DropdownEntry selectedValue;

    /**
     * Constructor for the DropdownItemEditor
     *
     * @param textureRegistry The texture registry the textures and icons are loaded from
     */
    public DropdownItemEditor(SwingTextureRegistry textureRegistry) {
        this.textureRegistry = textureRegistry;

        this.panel = new JPanel();
        this.panel.setBackground(Color.BLUE);
        this.panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.insets = new Insets(2, 5, 2, 2);

        this.labelItem = new JLabel();
        this.labelItem.setOpaque(false);
        this.labelItem.setHorizontalAlignment(LEFT);
        this.labelItem.setForeground(Color.WHITE);

        this.panel.add(this.labelItem, constraints);
    }

    @Override
    public Component getEditorComponent() {
        return this.panel;
    }

    @Override
    public Object getItem() {
        return this.selectedValue;
    }

    @Override
    public void setItem(Object item) {
        if (item == null) return;

        this.selectedValue = (DropdownEntry) item;
        this.labelItem.setText(this.selectedValue.displayName);

        if (!this.selectedValue.textureID.equals("")) {
            this.labelItem.setIcon(new ImageIcon(
                this.textureRegistry.getTextureForHandle(this.selectedValue.textureID).getTexture()
            ));
        }
    }
}

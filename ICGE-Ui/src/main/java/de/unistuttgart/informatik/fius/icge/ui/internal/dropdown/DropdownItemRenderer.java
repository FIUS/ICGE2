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

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import de.unistuttgart.informatik.fius.icge.ui.internal.SwingTextureRegistry;
import de.unistuttgart.informatik.fius.icge.ui.internal.dropdown.DropdownSelector.DropdownEntry;


/**
 * DropdownItemRenderer
 *
 * @author Tobias Wältken
 * @version 1.0
 */
class DropdownItemRenderer extends JPanel implements ListCellRenderer<DropdownSelector.DropdownEntry> {
    private static final long serialVersionUID = 2930839533138981414L;
    
    /** The texture registry */
    private final SwingTextureRegistry textureRegistry;
    
    private final JLabel labelItem;
    
    /**
     * Constructor for the DropdownItemRenderer
     *
     * @param textureRegistry
     *     The texture registry the textures and icons are loaded from
     */
    public DropdownItemRenderer(final SwingTextureRegistry textureRegistry) {
        this.textureRegistry = textureRegistry;
        
        this.labelItem = new JLabel();
        this.setLayout(new GridBagLayout());
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.insets = new Insets(2, 2, 2, 2);
        
        this.labelItem.setOpaque(true);
        this.labelItem.setHorizontalAlignment(SwingConstants.LEFT);
        
        this.add(this.labelItem, constraints);
        this.setBackground(Color.LIGHT_GRAY);
    }
    
    @Override
    public Component getListCellRendererComponent(
            final JList<? extends DropdownEntry> list, final DropdownEntry value, final int index, final boolean isSelected,
            final boolean cellHasFocus
    ) {
        if (value == null) {
            this.labelItem.setText("∅");
        } else {
            this.labelItem.setText(value.displayName);
            
            if (!value.textureID.equals("")) {
                this.labelItem.setIcon(new ImageIcon(this.textureRegistry.getTextureForHandle(value.textureID).getTexture()));
            }
        }
        
        return this;
    }
}

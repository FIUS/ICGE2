/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 *
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.ui;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.unistuttgart.informatik.fius.icge.ui.internal.SwingPlayfieldDrawer;
import de.unistuttgart.informatik.fius.icge.ui.internal.SwingTextureRegistry;
import de.unistuttgart.informatik.fius.icge.ui.internal.SwingUIManager;


/**
 * The factory for creating a UIManager
 *
 * @author Tim Neumann
 */
public class UiManagerFactory {

    /**
     * Creates a new Ui Manager including the initialization of all required submodules.
     *
     * @return The new Ui Manager.
     */
    public static UiManager createUiManager() {
        // Set window look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        final SwingTextureRegistry textureRegistry = new SwingTextureRegistry();
        final SwingPlayfieldDrawer playfieldDrawer = new SwingPlayfieldDrawer();

        return new SwingUIManager(textureRegistry, playfieldDrawer);
    }
}

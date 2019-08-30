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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.unistuttgart.informatik.fius.icge.ui.internal.SwingConsole;
import de.unistuttgart.informatik.fius.icge.ui.internal.SwingEntitySidebar;
import de.unistuttgart.informatik.fius.icge.ui.internal.SwingPlayfieldDrawer;
import de.unistuttgart.informatik.fius.icge.ui.internal.SwingTextureRegistry;
import de.unistuttgart.informatik.fius.icge.ui.internal.SwingToolbar;
import de.unistuttgart.informatik.fius.icge.ui.internal.SwingUIManager;


/**
 * Test class for the {@link SwingUIManager}
 *
 * @author Tim Neumann
 */
class SwingUiManagerUiTest {

    private SwingUIManager uiManager;

    /**
     * Setup the uiManager
     */
    @BeforeEach
    public void setup() {
        final SwingTextureRegistry textureRegistry = new SwingTextureRegistry();
        final SwingPlayfieldDrawer playfieldDrawer = new SwingPlayfieldDrawer();
        final SwingToolbar toolbar = new SwingToolbar(textureRegistry);
        final SwingEntitySidebar entitySidebar = new SwingEntitySidebar();
        final SwingConsole console = new SwingConsole();

        this.uiManager = new SwingUIManager(textureRegistry, playfieldDrawer, toolbar, entitySidebar, console);
    }

    /**
     * Test {@link SwingUIManager#start()}
     */
    @Test
    void testStart() {
        this.uiManager.start();
        Assertions.assertTrue(this.uiManager.isVisible(), "JFrame should be visisble");
    }
}

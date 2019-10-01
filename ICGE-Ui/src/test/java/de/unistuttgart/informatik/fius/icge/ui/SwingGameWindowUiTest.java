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
import de.unistuttgart.informatik.fius.icge.ui.internal.SwingGameWindow;


/**
 * Test class for the {@link SwingGameWindow}
 *
 * @author Tim Neumann
 */
class SwingGameWindowUiTest {
    
    private SwingGameWindow window;
    
    /**
     * Setup the game window
     */
    @BeforeEach
    public void setup() {
        final MinimalSimulationProxy simulationProxy = new MinimalSimulationProxy();
        final SwingTextureRegistry textureRegistry = new SwingTextureRegistry();
        final SwingPlayfieldDrawer playfieldDrawer = new SwingPlayfieldDrawer(simulationProxy, textureRegistry);
        final SwingToolbar toolbar = new SwingToolbar(simulationProxy, textureRegistry);
        final SwingEntitySidebar entitySidebar = new SwingEntitySidebar(simulationProxy, textureRegistry);
        final SwingConsole console = new SwingConsole();
        
        this.window = new SwingGameWindow(textureRegistry, playfieldDrawer, toolbar, entitySidebar, console);
    }
    
    /**
     * Test {@link SwingGameWindow#start()}
     */
    @Test
    void testStart() {
        this.window.start();
        Assertions.assertTrue(this.window.isVisible(), "JFrame should be visisble");
    }
}

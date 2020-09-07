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

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import de.unistuttgart.informatik.fius.icge.ui.internal.SwingConsole;
import de.unistuttgart.informatik.fius.icge.ui.internal.SwingEntitySidebar;
import de.unistuttgart.informatik.fius.icge.ui.internal.SwingGameWindow;
import de.unistuttgart.informatik.fius.icge.ui.internal.SwingPlayfieldDrawer;
import de.unistuttgart.informatik.fius.icge.ui.internal.SwingTaskStatusDisplay;
import de.unistuttgart.informatik.fius.icge.ui.internal.SwingTextureRegistry;
import de.unistuttgart.informatik.fius.icge.ui.internal.SwingToolbar;


/**
 * Test class for the {@link SwingGameWindow}
 *
 * @author Tim Neumann
 */
class SwingGameWindowUiTest {
    
    private SwingGameWindow window;
    
    /**
     * Setup the game window
     * 
     * @throws Exception
     *     When anything goes wrong
     */
    @BeforeEach
    public void setup() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            final SwingTextureRegistry textureRegistry = new SwingTextureRegistry();
            final SwingPlayfieldDrawer playfieldDrawer = new SwingPlayfieldDrawer(textureRegistry, 1);
            final SwingToolbar toolbar = new SwingToolbar(textureRegistry, 1);
            final SwingEntitySidebar entitySidebar = new SwingEntitySidebar(textureRegistry, 1);
            final SwingConsole console = new SwingConsole(1);
            final SwingTaskStatusDisplay taskStatus = new SwingTaskStatusDisplay(1);
            
            this.window = new SwingGameWindow(textureRegistry, playfieldDrawer, toolbar, entitySidebar, console, taskStatus);
        });
    }
    
    /**
     * Test {@link SwingGameWindow#start()}
     */
    @Disabled
    @Test
    void testStart() {
        this.window.start();
        try {
            SwingUtilities.invokeAndWait(() -> {
                // wait for event queue to catch up
            });
        } catch (InterruptedException | InvocationTargetException e) {
            // nothing to do here, we're waiting for Swing events to finish
        }
        Assertions.assertTrue(this.window.isVisible(), "JFrame should be visisble");
    }
}

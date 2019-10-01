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
import org.junit.jupiter.api.Test;


/**
 * Test class for {@link GameWindowFactory}.
 *
 * @author Tim Neumann
 */
class GameWindowFactoryTest {
    
    /**
     * Test method for
     * {@link de.unistuttgart.informatik.fius.icge.ui.GameWindowFactory#createGameWindow(SimulationProxy)}.
     */
    @Test
    void testCreateGameWindow() {
        @SuppressWarnings("deprecation")
        final MinimalSimulationProxy simulationProxy = new MinimalSimulationProxy();
        final GameWindow window = GameWindowFactory.createGameWindow(simulationProxy);
        Assertions.assertNotNull(window);
        Assertions.assertNotNull(window.getPlayfieldDrawer());
        Assertions.assertNotNull(window.getTextureRegistry());
        Assertions.assertNotNull(window.getToolbar());
    }
    
}

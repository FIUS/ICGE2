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
 * Test class for {@link UiManagerFactory}.
 *
 * @author Tim Neumann
 */
class UiManagerFactoryTest {

    /**
     * Test method for {@link de.unistuttgart.informatik.fius.icge.ui.UiManagerFactory#createUiManager(SimulationProxy)}.
     */
    @Test
    void testCreateUiManager() {
        final UiManager manager = UiManagerFactory.createUiManager(new SimulationProxy(){

            @Override
            public void setButtonStateListener(ButtonStateListener listener) {
                // TODO Auto-generated method stub
            }

            @Override
            public void buttonPressed(ButtonType type) {
                // TODO Auto-generated method stub
            }

            @Override
            public void simulationSpeedChange(int value) {
                // TODO Auto-generated method stub
            }
        });
        Assertions.assertNotNull(manager);
        Assertions.assertNotNull(manager.getPlayfieldDrawer());
        Assertions.assertNotNull(manager.getTextureRegistry());
        Assertions.assertNotNull(manager.getToolbar());
    }

}

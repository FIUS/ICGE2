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
        final GameWindow window = GameWindowFactory.createGameWindow(new SimulationProxy() {
            
            @Override
            public void setButtonStateListener(final ButtonStateListener listener) {
                // TODO Auto-generated method stub
            }
            
            @Override
            public void buttonPressed(final ButtonType type) {
                // TODO Auto-generated method stub
            }
            
            @Override
            public void simulationSpeedChange(final int value) {
                // TODO Auto-generated method stub
            }
            
            @Override
            public void setTaskSelectorListener(TaskSelectorListener listener) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void selectedTaskChange(String element) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setSpeedSliderListener(SpeedSliderListener listener) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setEntityDrawListener(EntityDrawListener listener) {
                // TODO Auto-generated method stub
                
            }
        });
        Assertions.assertNotNull(window);
        Assertions.assertNotNull(window.getPlayfieldDrawer());
        Assertions.assertNotNull(window.getTextureRegistry());
        Assertions.assertNotNull(window.getToolbar());
    }
    
}

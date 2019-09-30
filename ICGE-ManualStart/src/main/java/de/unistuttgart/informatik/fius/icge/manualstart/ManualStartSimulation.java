/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.manualstart;

import de.unistuttgart.informatik.fius.icge.simulation.Position;
import de.unistuttgart.informatik.fius.icge.simulation.Simulation;
import de.unistuttgart.informatik.fius.icge.simulation.SimulationFactory;
import de.unistuttgart.informatik.fius.icge.simulation.entity.BasicEntity;
import de.unistuttgart.informatik.fius.icge.ui.TextureRegistry;
import de.unistuttgart.informatik.fius.icge.ui.GameWindow;


/**
 * A class to manually start the renderer.
 *
 * @author Tim Neumann
 */
public class ManualStartSimulation {
    private static String textureHandleWall;
    
    /**
     * @param args
     *     the command line arguments. Not used.
     */
    public static void main(final String[] args) {
        final Simulation sim = SimulationFactory.createSimulation();
        prepareGameWindow(sim.getGameWindow());
        sim.initialize();
        sim.getPlayfield().addEntity(new Position(3, 4), new Wall());
        sim.getSimulationClock().start();
    }
    
    private static void prepareGameWindow(GameWindow window) {
        // load textures
        final TextureRegistry tr = window.getTextureRegistry();
        textureHandleWall = tr.loadTextureFromResource("textures/wall-default.png", ManualStartSimulation.class::getResourceAsStream);
        window.setWindowTitle("Manual simulation start");
    }
    
    private static class Wall extends BasicEntity {
        
        @Override
        protected String getTextureHandle() {
            return textureHandleWall;
        }
        
        @Override
        protected int getZPosition() {
            return 0;
        }
        
    }
}

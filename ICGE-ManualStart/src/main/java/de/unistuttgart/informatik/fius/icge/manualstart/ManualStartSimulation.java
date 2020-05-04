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

import de.unistuttgart.informatik.fius.icge.simulation.Simulation;
import de.unistuttgart.informatik.fius.icge.simulation.SimulationBuilder;
import de.unistuttgart.informatik.fius.icge.ui.GameWindow;
import de.unistuttgart.informatik.fius.icge.ui.TextureRegistry;
import de.unistuttgart.informatik.fius.icge.ui.WindowBuilder;


/**
 * A class to manually start the renderer.
 *
 * @author Tim Neumann
 */
public class ManualStartSimulation {
    
    private static String textureHandleWall;
    private static String textureHandleCoin;
    private static String animated;
    
    /**
     * @param args
     *     the command line arguments. Not used.
     */
    public static void main(final String[] args) {
        final WindowBuilder wb = new WindowBuilder();
        wb.setTitle("Window Builder start!");
        wb.buildWindow();
        wb.setGraphicsSettings(false, true);
        final GameWindow w = wb.getBuiltWindow();
        
        ManualStartSimulation.prepareTextures(w.getTextureRegistry());
        TestEntity.TEXTURE_HANDLE = ManualStartSimulation.animated;
        
        final SimulationBuilder sb = new SimulationBuilder();
        sb.buildSimulation();
        final Simulation sim = sb.getBuiltSimulation();
        
        w.start();
        sim.attachToWindow(w);
        sim.runTask(new TestTask());
    }
    
    private static void prepareTextures(final TextureRegistry tr) {
        ManualStartSimulation.textureHandleWall = tr
                .loadTextureFromResource("textures/wall-default.png", ManualStartSimulation.class::getResourceAsStream);
        ManualStartSimulation.textureHandleCoin = tr
                .loadTextureFromResource("textures/coin-default.png", ManualStartSimulation.class::getResourceAsStream);
        ManualStartSimulation.animated = tr.createAnimatedTexture(true);
        tr.addAnimationFrameToTexture(ManualStartSimulation.animated, ManualStartSimulation.textureHandleWall, 3);
        tr.addAnimationFrameToTexture(ManualStartSimulation.animated, ManualStartSimulation.textureHandleCoin, 3);
    }
}

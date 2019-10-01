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
import de.unistuttgart.informatik.fius.icge.simulation.SimulationHost;
import de.unistuttgart.informatik.fius.icge.simulation.SimulationHostFactory;
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
    private static String textureHandleCoin;
    private static String animated;
    
    /**
     * @param args
     *     the command line arguments. Not used.
     */
    public static void main(final String[] args) {
        final SimulationHost host = SimulationHostFactory.createSimulationHost();
        prepareTextures(host.getTextureRegistry());
        TestEntity.TEXTURE_HANDLE = animated;
        host.getTaskRegistry().registerTask("Test Task", new TestTask());
    }
    
    private static void prepareTextures(TextureRegistry tr) {
        textureHandleWall = tr.loadTextureFromResource("textures/wall-default.png", ManualStartSimulation.class::getResourceAsStream);
        textureHandleCoin = tr.loadTextureFromResource("textures/coin-default.png", ManualStartSimulation.class::getResourceAsStream);
        animated = tr.createAnimatedTexture(true);
        tr.addAnimationFrameToTexture(animated, textureHandleWall, 3);
        tr.addAnimationFrameToTexture(animated, textureHandleCoin, 3);
    }
}

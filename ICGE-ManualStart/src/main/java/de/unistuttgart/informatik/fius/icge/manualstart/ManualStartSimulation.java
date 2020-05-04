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
        WindowBuilder wb = new WindowBuilder();
        wb.setTitle("Window Builder start!");
        wb.buildWindow();
        wb.setGraphicsSettings(false, true);
        GameWindow w = wb.getBuiltWindow();

        prepareTextures(w.getTextureRegistry());
        TestEntity.TEXTURE_HANDLE = animated;

        SimulationBuilder sb = new SimulationBuilder();
        sb.buildSimulation();
        Simulation sim = sb.getBuiltSimulation();

        w.start();
        sim.attachToWindow(w);
        sim.runTask(new TestTask());

    }

    private static void prepareTextures(TextureRegistry tr) {
        textureHandleWall = tr.loadTextureFromResource("textures/wall-default.png", ManualStartSimulation.class::getResourceAsStream);
        textureHandleCoin = tr.loadTextureFromResource("textures/coin-default.png", ManualStartSimulation.class::getResourceAsStream);
        animated = tr.createAnimatedTexture(true);
        tr.addAnimationFrameToTexture(animated, textureHandleWall, 3);
        tr.addAnimationFrameToTexture(animated, textureHandleCoin, 3);
    }
}

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

import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.informatik.fius.icge.ui.BasicDrawable;
import de.unistuttgart.informatik.fius.icge.ui.Drawable;
import de.unistuttgart.informatik.fius.icge.ui.GameWindow;
import de.unistuttgart.informatik.fius.icge.ui.GameWindowFactory;
import de.unistuttgart.informatik.fius.icge.ui.TextureRegistry;
import de.unistuttgart.informatik.fius.icge.ui.WindowBuilder;


/**
 * A class to manually start the ui.
 *
 * @author Tim Neumann
 */
public class ManualStartUi {

    /**
     * Main entry point of the program
     *
     * @param args
     *     the command line arguments. Not used.
     */
    public static void main(final String[] args) {
        newStyleBuilder();
    }

    private static void newStyleBuilder() {
        WindowBuilder wb = new WindowBuilder();
        wb.setTitle("Window Builder start!");
        wb.buildWindow();
        GameWindow w = wb.getBuiltWindow();
        w.start();
    }

    /**
     * Todo remove after refactor is complete.
     */
    private static void oldStyleFactory() {
        final MinimalSimulationProxy simulationProxy = new MinimalSimulationProxy();
        final GameWindow window = GameWindowFactory.createGameWindow(simulationProxy);

        // load textures
        final TextureRegistry tr = window.getTextureRegistry();
        final String wallTexture = tr.loadTextureFromResource("textures/wall-default.png", ManualStartUi.class::getResourceAsStream);

        // generate playfield
        final ArrayList<Drawable> drawables = new ArrayList<>(
                List.of(new BasicDrawable(-1, -1, 0, wallTexture), new BasicDrawable(5, 5, 0, wallTexture))
        );
        window.getPlayfieldDrawer().setDrawables(drawables);

        window.start();
        window.setWindowTitle("Manual start");
    }
}

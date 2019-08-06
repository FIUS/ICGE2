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

import java.util.ArrayList;
import java.util.List;

/**
 * A class to manually start the renderer.
 *
 * @author Tim Neumann
 */
public class ManualStart {

    /**
     * @param args
     *     the command line arguments. Not used.
     */
    public static void main(String[] args) {
        UiManager manager = UiManagerFactory.createUiManager();

        // load textures
        TextureRegistry tr = manager.getTextureRegistry();
        String wallTexture = tr.loadTextureFromResource("/wall/wall-default.png");

        // generate playfield
        ArrayList<Drawable> drawables = new ArrayList<>(List.of(
            new Drawable(-1, -1, 0, wallTexture),
            new Drawable(5, 5, 0, wallTexture)
        ));
        manager.getPlayfieldDrawer().setDrawables(drawables);

        manager.start();
        manager.setWindowTitle("Manual start");
    }
}

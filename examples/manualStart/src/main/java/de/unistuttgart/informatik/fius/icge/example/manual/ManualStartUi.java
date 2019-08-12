/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.example.manual;

import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.informatik.fius.icge.ui.Drawable;
import de.unistuttgart.informatik.fius.icge.ui.BasicDrawable;
import de.unistuttgart.informatik.fius.icge.ui.TextureRegistry;
import de.unistuttgart.informatik.fius.icge.ui.UiManager;
import de.unistuttgart.informatik.fius.icge.ui.UiManagerFactory;


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
        final UiManager manager = UiManagerFactory.createUiManager();
        
        // load textures
        final TextureRegistry tr = manager.getTextureRegistry();
        final String wallTexture = tr.loadTextureFromResource("/textures/wall-default.png", ManualStartUi.class::getResourceAsStream);
        
        // generate playfield
        final ArrayList<Drawable> drawables = new ArrayList<>(
                List.of(new BasicDrawable(-1, -1, 0, wallTexture), new BasicDrawable(5, 5, 0, wallTexture))
        );
        manager.getPlayfieldDrawer().setDrawables(drawables);
        
        manager.start();
        manager.setWindowTitle("Manual start");
    }
}

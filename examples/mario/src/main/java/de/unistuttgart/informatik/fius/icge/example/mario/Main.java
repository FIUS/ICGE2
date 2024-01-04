/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 *
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.example.mario;

import de.unistuttgart.informatik.fius.icge.example.mario.tasks.Solution1;
import de.unistuttgart.informatik.fius.icge.example.mario.tasks.Solution2;
import de.unistuttgart.informatik.fius.icge.simulation.SimulationHost;
import de.unistuttgart.informatik.fius.icge.simulation.SimulationHostFactory;
import de.unistuttgart.informatik.fius.icge.ui.TextureRegistry;


/**
 * Main class of the example
 *
 * @author Tim Neumann
 */
public class Main {
    
    /**
     * The main entry point of the example
     * 
     * @param args
     *     the command line args; not used
     */
    public static void main(final String[] args) {
        final SimulationHost host = SimulationHostFactory.createSimulationHost();
        prepareTextures(host.getTextureRegistry());
        
        host.getTaskRegistry().registerTask("Solution 1", new Solution1());
        host.getTaskRegistry().registerTask("Solution 2", new Solution2());
    }
    
    private static void prepareTextures(final TextureRegistry tr) {
        // load textures
        for (final Texture texture : Texture.values()) {
            texture.load(tr);
        }
    }
}

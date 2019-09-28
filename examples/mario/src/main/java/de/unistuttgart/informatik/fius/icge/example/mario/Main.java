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
import de.unistuttgart.informatik.fius.icge.simulation.Simulation;
import de.unistuttgart.informatik.fius.icge.simulation.SimulationFactory;
import de.unistuttgart.informatik.fius.icge.ui.TextureRegistry;
import de.unistuttgart.informatik.fius.icge.ui.UiManager;


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
        final Simulation sim = SimulationFactory.createSimulation();
        Main.prepareUiManager(sim.getUiManager());
        
        sim.initialize();
        
        sim.getTaskRunner().runTask(Solution1.class, sim);
    }
    
    private static void prepareUiManager(final UiManager manager) {
        // load textures
        final TextureRegistry tr = manager.getTextureRegistry();
        for (final Texture texture : Texture.values()) {
            texture.load(tr);
        }
        manager.setWindowTitle("Manual simulation start");
    }
}

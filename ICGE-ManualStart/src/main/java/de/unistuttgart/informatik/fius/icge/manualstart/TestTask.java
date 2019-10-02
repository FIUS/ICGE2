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
import de.unistuttgart.informatik.fius.icge.simulation.tasks.Task;


/**
 * Test task for manual start.
 */
public class TestTask implements Task {
    
    private Simulation sim;
    
    @Override
    public void prepare(Simulation sim) {
        this.sim = sim;
        sim.getPlayfield().addEntity(new Position(3, 4), new TestEntity());
    }
    
    @Override
    public void solve() {
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
        }
    }
    
    @Override
    public boolean verify() {
        return true;
    }
}

/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.example.mario.tasks;

import de.unistuttgart.informatik.fius.icge.example.mario.WalkingProgram;


/**
 * The example solution for Task1
 * 
 * @author Tim Neumann
 */
public class Solution1 extends Task1 {
    
    @Override
    public void solve() {
        final String walkingProgramName = "Walking";
        
        this.sim.getEntityProgramRegistry().registerEntityProgram(walkingProgramName, new WalkingProgram());
        
        this.sim.getSimulationClock().start(); //This can be done via UI in the future.
        
        this.sim.getEntityProgramRunner().run(walkingProgramName, this.walkingMario);
        
        while (true) {
            this.spinningMario.turnClockWise();
        }
    }
    
}

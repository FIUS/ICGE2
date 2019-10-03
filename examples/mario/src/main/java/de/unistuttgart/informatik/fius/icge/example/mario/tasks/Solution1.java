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
        final String walkingProgramName1 = "Walking1";
        final String walkingProgramName2 = "Walking2";
        final String walkingProgramName3 = "Walking3";
        final String walkingProgramName4 = "Walking4";
        
        this.sim.getEntityProgramRegistry().registerEntityProgram(walkingProgramName1, new WalkingProgram());
        this.sim.getEntityProgramRegistry().registerEntityProgram(walkingProgramName2, new WalkingProgram());
        this.sim.getEntityProgramRegistry().registerEntityProgram(walkingProgramName3, new WalkingProgram());
        this.sim.getEntityProgramRegistry().registerEntityProgram(walkingProgramName4, new WalkingProgram());
        
        this.sim.getEntityProgramRunner().run(walkingProgramName1, this.walkingMario);
        this.sim.getEntityProgramRunner().run(walkingProgramName2, this.walkingMario);
        this.sim.getEntityProgramRunner().run(walkingProgramName3, this.walkingMario);
        this.sim.getEntityProgramRunner().run(walkingProgramName4, this.walkingMario);
        
        while (true) {
            this.spinningMario.turnClockWise();
        }
    }
    
}

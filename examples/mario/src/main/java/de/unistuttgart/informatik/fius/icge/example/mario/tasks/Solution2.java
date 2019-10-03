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

/**
 * The solution for {@link Task2}
 * 
 * @author Tim Neumann
 */
public class Solution2 extends Task2 {
    
    @Override
    public void solve() {
        mario.move();
        mario.move();
        mario.turnClockWise();
        mario.turnClockWise();
        mario.move();
        mario.move();
    }
    
}

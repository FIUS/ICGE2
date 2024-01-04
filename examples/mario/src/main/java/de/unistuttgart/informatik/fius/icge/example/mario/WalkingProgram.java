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

import java.util.List;

import de.unistuttgart.informatik.fius.icge.example.mario.entity.Coin;
import de.unistuttgart.informatik.fius.icge.example.mario.entity.Mario;
import de.unistuttgart.informatik.fius.icge.example.mario.entity.MarioProgram;


/**
 * A program walking around mario a bit
 *
 * @author Tim Neumann
 */
public class WalkingProgram extends MarioProgram {
    
    @Override
    public void run(final Mario mario) {
        while (true) {
            if (mario.canMove()) {
                mario.move();
            } else {
                mario.turnClockWise();
                mario.turnClockWise();
                final List<Coin> droppableCoins = mario.getCurrentlyDroppableEntities(Coin.class, false);
                final List<Coin> collectableCoins = mario.getCurrentlyCollectableEntities(Coin.class, false);
                
                if (!droppableCoins.isEmpty()) {
                    mario.drop(droppableCoins.get(0));
                } else if (!collectableCoins.isEmpty()) {
                    mario.collect(collectableCoins.get(0));
                }
            }
        }
    }
    
}

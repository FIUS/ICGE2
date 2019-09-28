/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.example.mario.entity;

import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;
import de.unistuttgart.informatik.fius.icge.simulation.entity.program.EntityProgram;


/**
 * A program for mario
 * 
 * @author Tim Neumann
 */
public abstract class MarioProgram implements EntityProgram {
    
    /**
     * Run this mario program on the given mario
     * 
     * @param mario
     *     The mario to run this program on
     */
    public abstract void run(Mario mario);
    
    @Override
    public void run(final Entity entity) {
        if (entity instanceof Mario) {
            this.run((Mario) entity);
        } else throw new IllegalArgumentException("Cannot run on that entity.");
    }
    
    @Override
    public boolean canRunOn(final Entity entity) {
        if (entity instanceof Mario) return true;
        return false;
    }
}

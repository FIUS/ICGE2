/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.entity.program;

import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;


/**
 * A program to be executed by an entity
 * 
 * @author Tim Neumann
 */
public interface EntityProgram {
    
    /**
     * Run this program on the given entity.
     * 
     * @param entity
     *     The entity to run this program on
     * @throws IllegalArgumentException
     *     if this program cannot run on the given entity
     */
    void run(Entity entity);
    
    /**
     * Check whether this program could run on the given entity.
     * 
     * @param entity
     *     The entity to check
     * @return true if this program could run on that entity
     */
    boolean canRunOn(Entity entity);
}

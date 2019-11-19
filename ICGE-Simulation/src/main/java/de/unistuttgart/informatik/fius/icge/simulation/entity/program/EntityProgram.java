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
 * @author Clemens Lieb
 */
public interface EntityProgram<T extends Entity> {
    
    /**
     * Run this program on the given entity.
     * 
     * @param entity
     *     The entity to run this program on
     */
    void run(T entity);
}

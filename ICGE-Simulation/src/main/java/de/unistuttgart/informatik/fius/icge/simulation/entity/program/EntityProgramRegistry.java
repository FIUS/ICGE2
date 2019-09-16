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

import java.util.Set;

import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;


/**
 * The registry for all {@link EntityProgram}s.
 * 
 * @author Tim Neumann
 */
public interface EntityProgramRegistry {
    /**
     * Register an entity program for the given entity type.
     * 
     * @param program
     *     the info about the program to register
     */
    void registerEntityProgram(EntityProgramInfo program);
    
    /**
     * Get all registered entity programs.
     * 
     * @return A set of all registered entity programs.
     */
    Set<EntityProgramInfo> getEntityPrograms();
    
    /**
     * Get all entity programs, which could run on the given entity.
     * 
     * @param entity
     *     the entity to get the programs for
     * @return a set of all info objects for entity programs which could be run on this entity
     */
    Set<EntityProgramInfo> getProgramsForEntity(Entity entity);
}

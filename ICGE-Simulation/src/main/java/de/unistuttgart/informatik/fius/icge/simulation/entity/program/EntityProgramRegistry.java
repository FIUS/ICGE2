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

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Supplier;

import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;
import de.unistuttgart.informatik.fius.icge.simulation.exception.ElementExistsException;


/**
 * The registry for all {@link EntityProgram}s.
 * 
 * @author Tim Neumann
 */
public interface EntityProgramRegistry {
    /**
     * Register an entity program with the given name.
     * 
     * @param name
     *     the name of the program; must not be null; must be unique
     * 
     * @param program
     *     the program to register; must not be null
     * 
     * @throws IllegalArgumentException
     *     if an argument is null
     * @throws ElementExistsException
     *     if the name is already used
     */
    void registerEntityProgram(String name, EntityProgram program);
    
    /**
     * Register many entity programs of the same type with the given name.
     * 
     * @param name
     *     the name for these programs; must not be null; must be unique
     * 
     * @param programGenerator
     *     a generator for the type of program; must not be null; must provide non null programs
     * 
     * @throws IllegalArgumentException
     *     if an argument is null
     * 
     * @throws ElementExistsException
     *     if the name is already used
     */
    void registerManyEntityProgram(String name, Supplier<EntityProgram> programGenerator);
    
    /**
     * Get the names of all registered entity programs.
     * 
     * @return a set of the names of all registered entity programs
     */
    Set<String> getPrograms();
    
    /**
     * Get the names of all entity programs, which could run on the given entity.
     * 
     * @param entity
     *     the entity to get the programs for; must not be null
     * @return a set of the names of all entity programs which could be run on this entity
     * 
     * @throws IllegalArgumentException
     *     if an argument is null
     */
    Set<String> getProgramsForEntity(Entity entity);
    
    /**
     * Get the entity program for a given name.
     * 
     * @param name
     *     The name to get the program for; must not be null
     * @return The program for the given name
     * @throws IllegalArgumentException
     *     if an argument is null
     * @throws NoSuchElementException
     *     if the given name is not registered
     */
    EntityProgram getEntityProgram(String name);
}

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

import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;
import de.unistuttgart.informatik.fius.icge.simulation.exception.CannotRunProgramException;


/**
 * A class to run {@link EntityProgram}s.
 * 
 * @author Tim Neumann
 */
public interface EntityProgramRunner {
    
    /**
     * Get the state of an entity program.
     * 
     * @param program
     *     The name of program to get the state of; must be a registered program in the {@link EntityProgramRegistry}
     * @return The state of the given program
     * @throws NoSuchElementException
     *     if the program is not registered in the {@link EntityProgramRegistry}
     * @throws IllegalArgumentException
     *     if an argument is null
     */
    EntityProgramState getState(String program);
    
    /**
     * Check whether the given program can run.
     * 
     * @param program
     *     The name of program to check; must be a registered program in the {@link EntityProgramRegistry}
     * 
     * @return true if the program can run
     * @throws NoSuchElementException
     *     if the program is not registered in the {@link EntityProgramRegistry}
     * @throws IllegalArgumentException
     *     if an argument is null
     */
    boolean canRunProgram(String program);
    
    /**
     * Check whether the given program can be run to the given entity.
     * <p>
     * First checks {@link #canRunProgram(String)} and then {@link EntityProgram#canRunOn(Entity)}.
     * </p>
     * 
     * @param program
     *     The name of the program to check; must be a registered program in the {@link EntityProgramRegistry}
     * @param entity
     *     The entity to check; must not be null
     * @return true if the program can run on that entity
     * @throws NoSuchElementException
     *     if the program is not registered in the {@link EntityProgramRegistry}
     * @throws IllegalArgumentException
     *     if an argument is null
     */
    boolean canRunProgramOn(String program, Entity entity);
    
    /**
     * Run an entity program on an entity.
     * 
     * @param program
     *     The program to run; must be a registered program in the {@link EntityProgramRegistry}; must be able to run on
     *     the entity now
     * @param entity
     *     The entity to run the program on
     * @throws IllegalArgumentException
     *     if an argument is null
     * @throws NoSuchElementException
     *     if the program is not registered in the {@link EntityProgramRegistry}
     * @throws CannotRunProgramException
     *     if the program is not able to run on the entity now
     * @throws RuntimeException
     *     if an exception occurred in the entity program
     */
    void run(String program, Entity entity);
    
    /**
     * Force stop all running entity programs.
     * <p>
     * This does not block until successful shutdown
     * </p>
     */
    void forceStop();
}

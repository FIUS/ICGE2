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

import de.unistuttgart.informatik.fius.icge.simulation.entity.ProgrammableEntity;


/**
 * <p>
 * Enables access to a ProgramExecutionContext that automatically schedules {@link EntityProgram EntityPrograms} for
 * execution. All implementations of the context expect that the caller retains ownership of the entity and will
 * {@link #remove(ProgrammableEntity)} it if the attached program should no longer run.
 * </p>
 * <p>
 * This notably implies that removing an entity from a PlayingField should result in said entity also being removed from
 * the Execution context it was associated with.
 * </p>
 */
public interface ProgramExecutionContext {
    
    /**
     * Adds the given entity to the program execution context, automatically scheduling it's program for execution.
     */
    void add(ProgrammableEntity<?> ent);
    
    /**
     * Removes the given entity from the program execution context, terminating the program it's running as soon as
     * possible.
     */
    void remove(ProgrammableEntity<?> ent);
}

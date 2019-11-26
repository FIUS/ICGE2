/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.entity;

import de.unistuttgart.informatik.fius.icge.simulation.entity.program.EntityProgram;


/**
 * Describes an Entity that can be programmed using an {@link EntityProgram} that takes the entity instance it's
 * attached to as argument. To enable typesafety in the EntityProgram, a generic type argument can specific the
 * inheriting type to allow for argument covariance.
 * 
 * @param <T>
 *     The type of the entity passed to the program, generally the most specific type known at compile-time. This type
 *     is usually the inheritor.
 */
public interface ProgrammableEntity<T extends ProgrammableEntity<?>> extends Entity {
    
    /**
     * @return the optional program attached to this specific entity
     */
    EntityProgram<T> attachedProgram();
    
    /**
     * Defines the program this entity is running.
     * 
     * @param program
     *     An EntityProgram matching this entity
     */
    void defineProgram(EntityProgram<T> program);
}

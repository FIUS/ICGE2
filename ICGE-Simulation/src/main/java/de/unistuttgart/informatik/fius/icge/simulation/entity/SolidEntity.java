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

/**
 * A solid entity, through which a movable entity cannot move.
 * 
 * @author Tim Neumann
 */
public interface SolidEntity extends Entity {
    
    /**
     * Get whether this entity is really solid at the moment.
     * <p>
     * This can be used to make entities, that are not solid all the time.
     * </p>
     * 
     * @return true if and only if this entity is currently solid
     */
    default boolean isCurrentlySolid() {
        return true;
    }
}

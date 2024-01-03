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
 * An entity, which can be collected by {@link EntityCollector}.
 *
 * @author Tim Neumann
 */
public interface CollectableEntity extends Entity {
    /**
     * Get whether this entity is really collectable at the moment.
     * <p>
     * This can be used to make entities, that are not collectable all the time.
     * </p>
     *
     * @return true if and only if this entity is currently collectable
     */
    default boolean isCurrentlyCollectable() {
        return true;
    }
}

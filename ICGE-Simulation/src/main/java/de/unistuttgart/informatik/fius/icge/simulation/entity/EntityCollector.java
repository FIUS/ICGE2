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

import de.unistuttgart.informatik.fius.icge.simulation.Position;
import de.unistuttgart.informatik.fius.icge.simulation.exception.CannotCollectEntityException;
import de.unistuttgart.informatik.fius.icge.simulation.exception.CannotDropEntityException;


/**
 * An entity which can collect {@link CollectableEntity}.
 * 
 * @author Tim Neumann
 */
public interface EntityCollector {
    
    /**
     * Check if this can collect and drop the given collectable entity type.
     * 
     * @param type
     *     The type to check; must not be null
     * @return true if this entity collector can collect or drop the given entity type
     */
    boolean canCarry(Class<? extends CollectableEntity> type);
    
    /**
     * Collect the given entity.
     * 
     * @param entity
     *     The entity to collect; must not be null; must be collectable by this
     * @throws CannotCollectEntityException
     *     if the given entity cannot be collected right now for any reason
     * @throws IllegalArgumentException
     *     if an argument is null
     */
    void collect(CollectableEntity entity);
    
    /**
     * Drop the given entity.
     * 
     * @param entity
     *     The entity to drop;must not be null; must be droppable by this
     * @param pos
     *     The position to drop the entity at; must not be null
     * @throws CannotDropEntityException
     *     if the given entity cannot be dropped right now for any reason
     * @throws IllegalArgumentException
     *     if an argument is null
     */
    void drop(CollectableEntity entity, Position pos);
    
}

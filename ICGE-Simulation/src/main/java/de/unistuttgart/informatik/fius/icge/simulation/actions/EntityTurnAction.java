/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.actions;

import de.unistuttgart.informatik.fius.icge.simulation.Direction;
import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;


/**
 * An entity action for when the entity turned
 * 
 * @author Tim Neumann
 */
public class EntityTurnAction extends EntityAction {
    
    private final Direction from;
    private final Direction to;
    
    /**
     * Create an entity turn action.
     * 
     * @param tickNumber
     *     the tick the action happened at
     * @param entity
     *     the entity that caused the action
     * @param from
     *     the direction the entity was facing before the turn
     * @param to
     *     the direction the entity is facing after the turn
     */
    public EntityTurnAction(long tickNumber, Entity entity, Direction from, Direction to) {
        super(tickNumber, entity);
        this.from = from;
        this.to = to;
    }
    
    /**
     * @return the direction the entity was facing before the turn
     */
    public Direction from() {
        return this.from;
    }
    
    /**
     * @return the direction the entity is facing after the turn
     */
    public Direction to() {
        return this.to;
    }
    
    @Override
    public String getDescription() {
        return this.getEntity() + " turned from " + this.from() + " to " + this.to();
    }
}

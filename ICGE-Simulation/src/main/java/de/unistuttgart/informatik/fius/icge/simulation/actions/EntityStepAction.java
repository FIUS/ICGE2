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

import de.unistuttgart.informatik.fius.icge.simulation.Position;
import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;


/**
 * An entity action for when the entity took a step
 * 
 * @author Tim Neumann
 */
public class EntityStepAction extends EntityMoveAction {
    
    /**
     * Create an entity step action.
     * 
     * @param tickNumber
     *     the tick the action happened at
     * @param entity
     *     the entity that caused the action
     * @param from
     *     the position the entity was at before the move
     * @param to
     *     the position the entity is after the move
     */
    public EntityStepAction(final long tickNumber, final Entity entity, final Position from, final Position to) {
        super(tickNumber, entity, from, to);
    }
    
    @Override
    public String getDescription() {
        return this.getEntity() + " stepped from " + this.from() + " to " + this.to();
    }
}

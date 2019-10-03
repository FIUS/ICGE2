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
 * An entity action for when the entity was just teleported on the playfield
 * 
 * @author Tim Neumann
 */
public class EntityTeleportAction extends EntityMoveAction {
    /**
     * Create an entity teleport action.
     * 
     * @param tickNumber
     *     the tick the action happened at
     * @param entity
     *     the entity that caused the action
     * @param from
     *     the position the entity was at before the move
     * @param to
     *     the position the entity is after the moveEntityStepAction
     */
    public EntityTeleportAction(long tickNumber, Entity entity, Position from, Position to) {
        super(tickNumber, entity, from, to);
    }
    
    @Override
    public String getDescription() {
        return this.getEntity() + " teleported from " + this.from() + " to " + this.to();
    }
}

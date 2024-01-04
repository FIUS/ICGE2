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
 * An entity action for when a entity moved.
 *
 * @author Tim Neumann
 * @see EntityStepAction
 * @see EntityTeleportAction
 */
public abstract class EntityMoveAction extends EntityAction {

    private final Position from;
    private final Position to;

    /**
     * Create an entity move action.
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
    public EntityMoveAction(final long tickNumber, final Entity entity, final Position from, final Position to) {
        super(tickNumber, entity);
        this.from = from;
        this.to = to;
    }

    /**
     * @return the position the entity was at before the move
     */
    public Position from() {
        return this.from;
    }

    /**
     * @return the position the entity is after the move
     */
    public Position to() {
        return this.to;
    }
}

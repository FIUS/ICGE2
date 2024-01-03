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
import de.unistuttgart.informatik.fius.icge.simulation.entity.CollectableEntity;
import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;


/**
 * An entity action for when an entity drops another entity.
 *
 * @author Tim Neumann
 */
public class EntityDropAction extends EntityAction {

    private final CollectableEntity dropped;
    private final Position          dropperPos;
    private final Position          dropppedPos;

    /**
     * Create an entity drop action.
     *
     * @param tickNumber
     *     the tick the action happened at
     * @param entity
     *     the entity that caused the action
     * @param dropped
     *     the entity that was dropped
     * @param dropperPos
     *     the position the dropper was at while collecting
     * @param droppedPos
     *     the position the dropped entity was dropped to
     *
     */
    public EntityDropAction(
            final long tickNumber, final Entity entity, final CollectableEntity dropped, final Position dropperPos,
            final Position droppedPos
    ) {
        super(tickNumber, entity);
        this.dropped = dropped;
        this.dropppedPos = droppedPos;
        this.dropperPos = dropperPos;
    }

    /**
     * @return the entity that was dropped
     */
    public CollectableEntity getDroppedEntity() {
        return this.dropped;
    }

    /**
     * @return the position the dropped entity was dropped to
     */
    public Position getDroppedEntityPosition() {
        return this.dropppedPos;
    }

    /**
     * @return the position the dropper was at while dropping
     */
    public Position getDropperPosition() {
        return this.dropperPos;
    }

    @Override
    public String getDescription() {
        return this.getEntity() + " (at " + this.getDropperPosition() + ") dropped " + this.getDroppedEntity() + " at "
                + this.getDroppedEntityPosition();
    }

}

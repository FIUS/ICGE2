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

*

import de.unistuttgart.informatik.fius.icge.simulation.Position;
import de.unistuttgart.informatik.fius.icge.simulation.entity.CollectableEntity;
import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;**


/**
 * An entity action for when an entity collects another entity.
 *
 * @author Tim Neumann
 */
public class EntityCollectAction extends EntityAction {
 *
    private final CollectableEntity collected;
    private final Position          collectorPos;
    private final Position          collectedPos;
 *
    /**
     * Create an entity collect action.
     *
     * @param tickNumber
     *     the tick the action happened at
     * @param entity
     *     the entity that caused the action
     * @param collected
     *     the entity that was collected
     * @param collectorPos
     *     the position the collector was at while collecting
     * @param collectedPos
     *     the position the collected entity was at while being collected
     *
     */
    public EntityCollectAction(
            final long tickNumber, final Entity entity, final CollectableEntity collected, final Position collectorPos,
            final Position collectedPos
    ) {
        super(tickNumber, entity);
        this.collected = collected;
        this.collectedPos = collectedPos;
        this.collectorPos = collectorPos;
    }
 *
    /**
     * @return the entity that was collected
     */
    public CollectableEntity getCollectedEntity() {
        return this.collected;
    }
 *
    /**
     * @return the position the collected entity was at while being collected
     */
    public Position getCollectedEntityPosition() {
        return this.collectedPos;
    }
 *
    /**
     * @return the position the collector was at while collecting
     */
    public Position getCollectorPosition() {
        return this.collectorPos;
    }
 *
    @Override
    public String getDescription() {
        return this.getEntity() + " (at " + this.getCollectorPosition() + ") collected " + this.getCollectedEntity() + " at "
                + this.getCollectedEntityPosition();
    }
 *
}

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

import de.unistuttgart.informatik.fius.icge.simulation.Playfield;
import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;**


/**
 * An entity action for when the entity is removed from a play field
 *
 * @author Tim Neumann
 */
public class EntityDespawnAction extends EntityAction {
 *
    private final Playfield playfield;
 *
    /**
     * Create an entity despawn action
     *
     * @param tickNumber
     *     the tick the action happened at
     * @param entity
     *     the entity that caused the action
     * @param playfield
     *     the playfield the entity was removed from
     */
    public EntityDespawnAction(final long tickNumber, final Entity entity, final Playfield playfield) {
        super(tickNumber, entity);
        this.playfield = playfield;
    }
 *
    /**
     * @return the playfield the entity was removed from
     */
    public Playfield getPlayfield() {
        return this.playfield;
    }
 *
    @Override
    public String getDescription() {
        return this.getEntity() + " despawned from " + this.getPlayfield();
    }
}

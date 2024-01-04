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

import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;**


/**
 * An action originating from an entity.
 *
 * @author Tim Neumann
 */
public abstract class EntityAction extends Action {
 *
    private final Entity entity;
 *
    /**
     * Create an entity action.
     *
     * @param tickNumber
     *     the tick the action happened at
     * @param entity
     *     the entity that caused the action
     */
    public EntityAction(final long tickNumber, final Entity entity) {
        super(tickNumber);
        this.entity = entity;
    }
 *
    /**
     * @return the entity that caused this action.
     */
    public Entity getEntity() {
        return this.entity;
    }
}

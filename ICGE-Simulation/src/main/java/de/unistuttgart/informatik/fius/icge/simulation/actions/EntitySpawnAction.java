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

import de.unistuttgart.informatik.fius.icge.simulation.Playfield;
import de.unistuttgart.informatik.fius.icge.simulation.Position;
import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;


/**
 * An entity action for when the entity is added to a play field
 * 
 * @author Tim Neumann
 */
public class EntitySpawnAction extends EntityAction {
    
    private final Playfield playfield;
    private final Position  position;
    
    /**
     * Create an entity spawn action.
     * 
     * @param tickNumber
     *     the tick the action happened at
     * @param entity
     *     the entity that caused the action
     * @param playfield
     *     the playfield the entity was added to
     * @param position
     *     the position the entity was added at
     */
    public EntitySpawnAction(final long tickNumber, final Entity entity, final Playfield playfield, final Position position) {
        super(tickNumber, entity);
        this.playfield = playfield;
        this.position = position;
    }
    
    /**
     * @return the playfield the entity was added to
     */
    public Playfield getPlayfield() {
        return this.playfield;
    }
    
    /**
     * @return the position the entity was added at
     */
    public Position getPosition() {
        return this.position;
    }
    
    @Override
    public String getDescription() {
        return this.getEntity() + " spawned in " + this.getPlayfield() + " at " + this.getPosition();
    }
}

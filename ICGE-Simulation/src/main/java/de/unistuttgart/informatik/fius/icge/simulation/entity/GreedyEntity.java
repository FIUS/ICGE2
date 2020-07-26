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

import java.util.List;

import de.unistuttgart.informatik.fius.icge.simulation.Position;
import de.unistuttgart.informatik.fius.icge.simulation.actions.Action;
import de.unistuttgart.informatik.fius.icge.simulation.actions.EntityCollectAction;
import de.unistuttgart.informatik.fius.icge.simulation.actions.EntityDropAction;
import de.unistuttgart.informatik.fius.icge.simulation.exception.CannotCollectEntityException;
import de.unistuttgart.informatik.fius.icge.simulation.exception.CannotDropEntityException;
import de.unistuttgart.informatik.fius.icge.simulation.exception.EntityNotOnFieldException;


/**
 * A movable entity collector.
 * 
 * @author Tim Neumann
 */
public abstract class GreedyEntity extends MovableEntity implements EntityCollector {
    
    /** A lock to synchronize all operations involving the entities inventory. */
    protected final Object inventoryOperationLock = new Object();
    
    private final Inventory inventory = new Inventory();
    
    /**
     * @return the inventory of this greedy entity
     */
    public Inventory getInventory() {
        return this.inventory;
    }
    
    @Override
    public boolean canCarry(final Class<? extends CollectableEntity> type) {
        return true;
    }
    
    /**
     * Get all the currently collectable entities of the given type
     * 
     * @param <T>
     *     The generic type to return the entities as
     * @param type
     *     The type of entity to get; must not be null
     * @param includeSubclasses
     *     Whether to include the subclasses of the given type
     * @return A list of currently collectable entities matching the type
     */
    public <T extends CollectableEntity> List<T> getCurrentlyCollectableEntities(final Class<T> type, final boolean includeSubclasses) {
        return this.getPlayfield().getEntitiesOfTypeAt(this.getPosition(), type, includeSubclasses);
    }
    
    @Override
    public void collect(final CollectableEntity entity) {
        if (entity == null) throw new IllegalArgumentException("An argument is null.");
        Position myPos;
        Position otherPos;
        
        synchronized (this.inventoryOperationLock) {
            try {
                myPos = this.getPosition();
                otherPos = entity.getPosition();
            } catch (final EntityNotOnFieldException e) {
                throw new CannotCollectEntityException(e);
            }
            
            if (!myPos.equals(otherPos)) throw new CannotCollectEntityException("Not on my field");
            
            this.getSimulation().getPlayfield().removeEntity(entity);
            this.getInventory().add(entity);
            
            final Action action = new EntityCollectAction(
                    this.getSimulation().getSimulationClock().getLastTickNumber(), this, entity, myPos, otherPos
            );
            this.getSimulation().getActionLog().logAction(action);
        }
    }
    
    /**
     * Get all the currently droppable entities of the given type
     * 
     * @param <T>
     *     The generic type to return the entities as
     * @param type
     *     The type of entity to get; must not be null
     * @param includeSubclasses
     *     Whether to include the subclasses of the given type
     * @return A list of currently collectable entities matching the type
     */
    public <T extends CollectableEntity> List<T> getCurrentlyDroppableEntities(final Class<T> type, final boolean includeSubclasses) {
        return this.getInventory().get(type, includeSubclasses);
    }
    
    @Override
    public void drop(final CollectableEntity entity, final Position pos) {
        if ((entity == null) || (pos == null)) throw new IllegalArgumentException("An argument is null.");
        Position myPos;
        synchronized (this.inventoryOperationLock) {
            try {
                myPos = this.getPosition();
            } catch (final EntityNotOnFieldException e) {
                throw new CannotCollectEntityException(e);
            }
            
            if (!myPos.equals(pos)) throw new CannotCollectEntityException("Not on my field");
            
            this.getInventory().remove(entity);
            this.getPlayfield().addEntity(pos, entity);
            
            final Action action = new EntityDropAction(
                    this.getSimulation().getSimulationClock().getLastTickNumber(), this, entity, myPos, pos
            );
            this.getSimulation().getActionLog().logAction(action);
        }
    }
    
    /**
     * Drop the given entity at the current position.
     * 
     * @param entity
     *     The entity to drop;must not be null; must be droppable by this *
     * @throws CannotDropEntityException
     *     if the given entity cannot be dropped right now for any reason
     * @throws IllegalArgumentException
     *     if an argument is null
     */
    public void drop(final CollectableEntity entity) {
        this.drop(entity, this.getPosition());
    }
    
}

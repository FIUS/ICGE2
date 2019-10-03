/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.internal.playfield;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.unistuttgart.informatik.fius.icge.simulation.Playfield;
import de.unistuttgart.informatik.fius.icge.simulation.Position;
import de.unistuttgart.informatik.fius.icge.simulation.Simulation;
import de.unistuttgart.informatik.fius.icge.simulation.actions.EntityDespawnAction;
import de.unistuttgart.informatik.fius.icge.simulation.actions.EntityMoveAction;
import de.unistuttgart.informatik.fius.icge.simulation.actions.EntitySpawnAction;
import de.unistuttgart.informatik.fius.icge.simulation.actions.EntityTeleportAction;
import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;
import de.unistuttgart.informatik.fius.icge.simulation.entity.SolidEntity;
import de.unistuttgart.informatik.fius.icge.simulation.exception.EntityAlreadyOnFieldExcpetion;
import de.unistuttgart.informatik.fius.icge.simulation.exception.EntityNotOnFieldException;
import de.unistuttgart.informatik.fius.icge.simulation.internal.StandardSimulation;
import de.unistuttgart.informatik.fius.icge.ui.Drawable;
import de.unistuttgart.informatik.fius.icge.ui.ListenerSetException;
import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy.EntityDrawListener;


/**
 * The standard implementation of {@link Playfield}
 * 
 * @author Tim Neumann
 */
public class StandardPlayfield implements Playfield {
    private WeakReference<Simulation> sim;
    
    private final Map<Position, PlayfieldCell> cells           = new HashMap<>();
    private final Map<Entity, Position>        entityPositions = new HashMap<>();
    
    private EntityDrawListener drawer;
    
    /**
     * Initialize the playfield for the given simulation
     * 
     * @param simulation
     *     the parent simulation
     */
    public void initialize(final StandardSimulation simulation) {
        this.sim = new WeakReference<>(simulation);
        simulation.getSimulationClock().registerPostTickListener(count -> {
            this.drawEntities();
            return true;
        });
    }
    
    /**
     * Set the entity draw listener.
     * 
     * @param listener
     * 
     * @throws ListenerSetException
     *     If listener is already set and new listener is not null
     */
    public void setEntityDrawListener(EntityDrawListener listener) {
        if ((this.drawer == null) || (listener == null)) {
            this.drawer = listener;
            if (listener != null) {
                this.drawEntities();
            }
        } else throw new ListenerSetException();
    }
    
    /**
     * @throws IllegalStateException
     *     if this playfield is not part of any simulation
     */
    @Override
    public Simulation getSimulation() {
        if (this.sim == null) throw new IllegalStateException("This playfield is not part of any simulation.");
        final Simulation simulation = this.sim.get();
        if (simulation == null) throw new IllegalStateException("This playfield is not part of any simulation.");
        return simulation;
    }
    
    /**
     * Converts all entities to drawables and sends them to the playfield drawer.
     */
    public void drawEntities() {
        final List<Drawable> drawables = new ArrayList<>();
        for (final Entity entity : this.getAllEntities()) {
            drawables.add(entity.getDrawInformation());
        }
        if (this.drawer != null) {
            this.drawer.setDrawables(drawables);
        }
    }
    
    @Override
    public List<Entity> getAllEntities() {
        return this.getAllEntitiesOfType(Entity.class, true);
    }
    
    @Override
    public <T extends Entity> List<T> getAllEntitiesOfType(final Class<? extends T> type, final boolean includeSubclasses) {
        if (type == null) throw new IllegalArgumentException("The given type is null.");
        final List<T> result = new ArrayList<>();
        for (final PlayfieldCell cell : this.cells.values()) {
            result.addAll(cell.get(type, includeSubclasses));
        }
        return result;
    }
    
    @Override
    public List<Entity> getEntitiesAt(final Position pos) {
        if (pos == null) throw new IllegalArgumentException("The given pos is null.");
        return this.getEntitiesOfTypeAt(pos, Entity.class, true);
    }
    
    @Override
    public <T extends Entity> List<T> getEntitiesOfTypeAt(
            final Position pos, final Class<? extends T> type, final boolean includeSubclasses
    ) {
        if (type == null) throw new IllegalArgumentException("The given type is null.");
        if (pos == null) throw new IllegalArgumentException("The given pos is null.");
        final List<T> result = new ArrayList<>();
        final PlayfieldCell cell = this.cells.get(pos);
        if ((cell != null) && cell.getPosition().equals(pos)) {
            result.addAll(cell.get(type, includeSubclasses));
        }
        return result;
    }
    
    private void addEntityToCell(final Position pos, final Entity entity) {
        PlayfieldCell cell = this.cells.get(pos);
        if (cell == null) {
            cell = new PlayfieldCell(pos);
            this.cells.put(pos, cell);
        }
        cell.add(entity);
    }
    
    private void removeEntityFromCell(final Position pos, final Entity entity) {
        final PlayfieldCell cell = this.cells.get(pos);
        if ((cell == null) || !cell.contains(entity)) // TODO decide if this should throw an Exception
            return; // cell is already empty...
        cell.remove(entity);
        if (cell.isEmpty()) {
            this.cells.remove(pos, cell);
        }
    }
    
    @Override
    public void addEntity(final Position pos, final Entity entity) {
        if (pos == null) throw new IllegalArgumentException("The given pos is null.");
        if (entity == null) throw new IllegalArgumentException("The given entity is null.");
        
        if (
            this.entityPositions.containsKey(entity)
        ) throw new EntityAlreadyOnFieldExcpetion("The given entity" + entity + "is already on this playfield.");
        
        this.entityPositions.put(entity, pos);
        
        this.addEntityToCell(pos, entity);
        
        this.getSimulation().getActionLog()
                .logAction(new EntitySpawnAction(this.getSimulation().getSimulationClock().getLastTickNumber(), entity, this, pos));
        
        entity.initOnPlayfield(this);
        
        this.drawEntities();
    }
    
    @Override
    public void moveEntity(final Entity entity, final Position pos) {
        this.moveEntity(entity, pos, null);
    }
    
    @Override
    public void moveEntity(Entity entity, Position pos, EntityMoveAction action) {
        if (pos == null) throw new IllegalArgumentException("The given pos is null.");
        if (entity == null) throw new IllegalArgumentException("The given entity is null.");
        if (
            !this.entityPositions.containsKey(entity)
        ) throw new EntityNotOnFieldException("The given entity" + entity + "is not on this playfield.");
        
        EntityMoveAction actionToLog = action;
        
        final Position oldPos = this.entityPositions.get(entity);
        
        if (actionToLog == null) {
            actionToLog = new EntityTeleportAction(this.getSimulation().getSimulationClock().getLastTickNumber(), entity, oldPos, pos);
        } else {
            if (!actionToLog.getEntity().equals(entity)) throw new IllegalArgumentException("Given action wasn't caused by given entity.");
            if (
                !actionToLog.from().equals(oldPos)
            ) throw new IllegalArgumentException("Given action does not start at current position of given entity.");
            if (!actionToLog.to().equals(pos)) throw new IllegalArgumentException("Given action does not end at given pos.");
        }
        
        this.removeEntityFromCell(oldPos, entity);
        this.addEntityToCell(pos, entity);
        this.entityPositions.put(entity, pos);
        
        this.getSimulation().getActionLog().logAction(actionToLog);
        
        this.drawEntities();
    }
    
    @Override
    public void removeEntity(final Entity entity) {
        if (entity == null) throw new IllegalArgumentException("The given entity is null.");
        if (
            !this.entityPositions.containsKey(entity)
        ) throw new EntityNotOnFieldException("The given entity" + entity + "is not on this playfield.");
        
        final Position pos = this.entityPositions.get(entity);
        this.removeEntityFromCell(pos, entity);
        this.entityPositions.remove(entity, pos);
        
        this.getSimulation().getActionLog()
                .logAction(new EntityDespawnAction(this.getSimulation().getSimulationClock().getLastTickNumber(), entity, this));
        
        this.drawEntities();
    }
    
    @Override
    public Position getEntityPosition(final Entity entity) {
        if (entity == null) throw new IllegalArgumentException("The given entity is null.");
        final Position pos = this.entityPositions.get(entity);
        if (pos == null) throw new EntityNotOnFieldException("The given entity" + entity + "is not on this playfield.");
        return pos;
    }
    
    @Override
    public boolean containsEntity(final Entity entity) {
        if (entity == null) throw new IllegalArgumentException("The given entity is null.");
        return this.entityPositions.containsKey(entity);
    }
    
    @Override
    public boolean isSolidEntityAt(final Position pos) {
        final List<SolidEntity> solidEntitiesAtPos = this.getEntitiesOfTypeAt(pos, SolidEntity.class, true);
        for (final SolidEntity entity : solidEntitiesAtPos) {
            if (entity.isCurrentlySolid()) return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
    }
}

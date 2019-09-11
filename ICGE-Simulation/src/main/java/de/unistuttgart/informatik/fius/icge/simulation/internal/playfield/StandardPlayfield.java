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
import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;
import de.unistuttgart.informatik.fius.icge.simulation.entity.SolidEntity;
import de.unistuttgart.informatik.fius.icge.simulation.exception.EntityAlreadyOnFieldExcpetion;
import de.unistuttgart.informatik.fius.icge.simulation.exception.EntityNotOnFieldException;
import de.unistuttgart.informatik.fius.icge.simulation.internal.StandardSimulation;
import de.unistuttgart.informatik.fius.icge.ui.Drawable;


/**
 * The standard implementation of {@link Playfield}
 * 
 * @author Tim Neumann
 */
public class StandardPlayfield implements Playfield {
    private WeakReference<Simulation> sim;
    
    private final Map<Position, PlayfieldCell> cells           = new HashMap<>();
    private final Map<Entity, Position>        entityPositions = new HashMap<>();
    
    /**
     * Initialize the playfield for the given simulation
     * 
     * @param simulation
     *     the parent simulation
     */
    public void initialize(StandardSimulation simulation) {
        this.sim = new WeakReference<>(simulation);
        simulation.getSimulationClock().registerPostTickListener(count -> {
            drawEntities();
            return true;
        });
    }
    
    /**
     * @throws IllegalStateException
     *     if this playfield is not part of any simulation
     */
    @Override
    public Simulation getSimulation() {
        if (this.sim == null) throw new IllegalStateException("This playfield is not part of any simulation.");
        Simulation simulation = this.sim.get();
        if (simulation == null) throw new IllegalStateException("This playfield is not part of any simulation.");
        return simulation;
    }
    

    /**
     * Converts all entities to drawables and sends them to the playfield drawer.
     */
    public void drawEntities() {
        List<Drawable> drawables = new ArrayList<>();
        for (Entity entity : this.getAllEntities()) {
            drawables.add(entity.getDrawInformation());
        }
        this.getSimulation().getUiManager().getPlayfieldDrawer().setDrawables(drawables);
    }
    
    @Override
    public List<Entity> getAllEntities() {
        return this.getAllEntitiesOfType(Entity.class, true);
    }
    
    @Override
    public <T extends Entity> List<T> getAllEntitiesOfType(Class<? extends T> type, boolean includeSubclasses) {
        if (type == null) throw new IllegalArgumentException("The given type is null.");
        List<T> result = new ArrayList<>();
        for (PlayfieldCell cell : this.cells.values()) {
            result.addAll(cell.getEntities(type, includeSubclasses));
        }
        return result;
    }
    
    @Override
    public List<Entity> getEntitiesAt(Position pos) {
        if (pos == null) throw new IllegalArgumentException("The given pos is null.");
        return getEntitiesOfTypeAt(pos, Entity.class, true);
    }
    
    @Override
    public <T extends Entity> List<T> getEntitiesOfTypeAt(Position pos, Class<? extends T> type, boolean includeSubclasses) {
        if (type == null) throw new IllegalArgumentException("The given type is null.");
        if (pos == null) throw new IllegalArgumentException("The given pos is null.");
        List<T> result = new ArrayList<>();
        PlayfieldCell cell = this.cells.get(pos);
        if (cell != null && cell.getPosition().equals(pos)) {
            result.addAll(cell.getEntities(type, includeSubclasses));
        }
        return result;
    }
    
    private void addEntityToCell(Position pos, Entity entity) {
        PlayfieldCell cell = this.cells.get(pos);
        if (cell == null) {
            cell = new PlayfieldCell(pos);
            this.cells.put(pos, cell);
        }
        cell.addEntity(entity);
    }
    
    private void removeEntityFromCell(Position pos, Entity entity) {
        PlayfieldCell cell = this.cells.get(pos);
        if (cell == null || !cell.contains(entity)) {
            // TODO decide if this should throw an Exception
            return; // cell is already empty...
        }
        cell.remove(entity);
        if (cell.isEmpty()) {
            this.cells.remove(pos, cell);
        }
    }
    
    @Override
    public void addEntity(Position pos, Entity entity) {
        if (pos == null) throw new IllegalArgumentException("The given pos is null.");
        if (entity == null) throw new IllegalArgumentException("The given entity is null.");
        
        if (this.entityPositions.containsKey(entity)) {
            throw new EntityAlreadyOnFieldExcpetion("The given entity" + entity + "is already on this playfield.");
        }
        
        this.entityPositions.put(entity, pos);
        
        this.addEntityToCell(pos, entity);
        
        entity.initOnPlayfield(this);
    }
    
    @Override
    public void moveEntity(Entity entity, Position pos) {
        if (pos == null) throw new IllegalArgumentException("The given pos is null.");
        if (entity == null) throw new IllegalArgumentException("The given entity is null.");
        if (
            !this.entityPositions.containsKey(entity)
        ) throw new EntityNotOnFieldException("The given entity" + entity + "is not on this playfield.");
        
        Position oldPos = this.entityPositions.get(entity);
        this.removeEntityFromCell(oldPos, entity);
        this.addEntityToCell(pos, entity);
        this.entityPositions.put(entity, pos);
    }
    
    @Override
    public void removeEntity(Entity entity) {
        if (entity == null) throw new IllegalArgumentException("The given entity is null.");
        if (
            !this.entityPositions.containsKey(entity)
        ) throw new EntityNotOnFieldException("The given entity" + entity + "is not on this playfield.");
        
        Position pos = this.entityPositions.get(entity);
        this.removeEntityFromCell(pos, entity);
        this.entityPositions.remove(entity, pos);
    }
    
    @Override
    public Position getEntityPosition(Entity entity) {
        if (entity == null) throw new IllegalArgumentException("The given entity is null.");
        Position pos = this.entityPositions.get(entity);
        if (pos == null) throw new EntityNotOnFieldException("The given entity" + entity + "is not on this playfield.");
        return pos;
    }
    
    @Override
    public boolean containsEntity(Entity entity) {
        if (entity == null) throw new IllegalArgumentException("The given entity is null.");
        return this.entityPositions.containsKey(entity);
    }
    
    @Override
    public boolean isSolidEntityAt(Position pos) {
        List<SolidEntity> solidEntitiesAtPos = this.getEntitiesOfTypeAt(pos, SolidEntity.class, true);
        for (SolidEntity entity : solidEntitiesAtPos) {
            if (entity.isCurrentlySolid()) {
                return true;
            }
        }
        return false;
    }
}

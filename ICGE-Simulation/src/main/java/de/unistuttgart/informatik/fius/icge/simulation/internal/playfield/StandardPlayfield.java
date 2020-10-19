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
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
import de.unistuttgart.informatik.fius.icge.ui.SimulationTreeNode;
import de.unistuttgart.informatik.fius.icge.ui.exception.ListenerSetException;


/**
 * The standard implementation of {@link Playfield}
 *
 * @author Tim Neumann
 */
public class StandardPlayfield implements Playfield {
    private WeakReference<Simulation> sim;
    
    private final Object playfieldLock = new Object();
    
    private final Map<Position, PlayfieldCell> cells           = new HashMap<>();
    private final Map<Entity, Position>        entityPositions = new HashMap<>();
    
    private SimulationTreeNode simualtionTreeRootNode;
    
    private BiConsumer<SimulationTreeNode, Entity> simulationTreeEntityAddedListener;
    private Consumer<SimulationTreeNode>           simulationTreeEntityRemovedListener;
    
    private Consumer<List<Drawable>> drawablesChangedListener;
    
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
        
        this.simualtionTreeRootNode = new SimulationTreeNode("root", "Entities", "", false);
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
            try {
                drawables.add(entity.getDrawInformation());
            } catch (@SuppressWarnings("unused") final EntityNotOnFieldException e) {
                //Entity has been removed from the field while this loop was running.
                //Just don't draw it and ignore the exception.
            }
        }
        try {
            if (this.drawablesChangedListener != null) {
                this.drawablesChangedListener.accept(drawables);
            }
        } catch (@SuppressWarnings("unused") final IllegalStateException e) {
            //If we are not attached to a simultion we do not need to draw anything
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
        synchronized (this.playfieldLock) {
            for (final PlayfieldCell cell : this.cells.values()) {
                result.addAll(cell.get(type, includeSubclasses));
            }
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
        synchronized (this.playfieldLock) {
            final PlayfieldCell cell = this.cells.get(pos);
            if ((cell != null) && cell.getPosition().equals(pos)) {
                result.addAll(cell.get(type, includeSubclasses));
            }
        }
        return result;
    }
    
    private void addEntityToCell(final Position pos, final Entity entity) {
        synchronized (this.playfieldLock) {
            PlayfieldCell cell = this.cells.get(pos);
            if (cell == null) {
                cell = new PlayfieldCell(pos);
                this.cells.put(pos, cell);
            }
            cell.add(entity);
        }
    }
    
    private void removeEntityFromCell(final Position pos, final Entity entity) {
        synchronized (this.playfieldLock) {
            final PlayfieldCell cell = this.cells.get(pos);
            if ((cell == null) || !cell.contains(entity)) {
                // TODO decide if this should throw an Exception
                return; // cell is already empty...
            }
            cell.remove(entity);
            if (cell.isEmpty()) {
                this.cells.remove(pos, cell);
            }
        }
    }
    
    @Override
    public void addEntity(final Position pos, final Entity entity) {
        if (pos == null) throw new IllegalArgumentException("The given pos is null.");
        if (entity == null) throw new IllegalArgumentException("The given entity is null.");
        
        synchronized (this.playfieldLock) {
            if (
                this.entityPositions.containsKey(entity)
            ) throw new EntityAlreadyOnFieldExcpetion("The given entity" + entity + "is already on this playfield.");
            
            this.entityPositions.put(entity, pos);
            
            this.addEntityToCell(pos, entity);
            
            this.getSimulation().getActionLog()
                    .logAction(new EntitySpawnAction(this.getSimulation().getSimulationClock().getLastTickNumber(), entity, this, pos));
            
            entity.initOnPlayfield(this);
            
            this.addEntityToSimulationTree(entity);
            this.drawEntities();
        }
    }
    
    private SimulationTreeNode findNodeForEntity(final Entity entity, final boolean create) {
        final List<Class<?>> classHiera = new ArrayList<>();
        {
            Class<?> clazz = entity.getClass();
            do {
                classHiera.add(0, clazz);
                clazz = clazz.getSuperclass();
            } while (Entity.class.isAssignableFrom(clazz));
        }
        SimulationTreeNode node = this.simualtionTreeRootNode;
        
        hieraLoop: for (final Class<?> clazz : classHiera) {
            for (final SimulationTreeNode child : node.getChildren()) {
                if (child.getElementId().equals(clazz.getName())) {
                    node = child;
                    continue hieraLoop;
                }
            }
            if (create) {
                //TODO: get texture from EntityTypeRegistry
                final SimulationTreeNode child = new SimulationTreeNode(clazz.getName(), clazz.getSimpleName(), "", false);
                node.appendChild(child);
                node = child;
            } else return null;
        }
        return node;
    }
    
    private void addEntityToSimulationTree(final Entity entity) {
        final SimulationTreeNode newNode = new SimulationTreeNode(
                Integer.toHexString(entity.hashCode()), entity.toString(), entity.getDrawInformation().getTextureHandle()
        );
        this.findNodeForEntity(entity, true).appendChild(newNode);
        
        if (this.simulationTreeEntityAddedListener != null) {
            // TODO check for bugs with simulation tree when window is reattached or attached late
            this.simulationTreeEntityAddedListener.accept(newNode, entity);
        }
    }
    
    @Override
    public void moveEntity(final Entity entity, final Position pos) {
        this.moveEntity(entity, pos, null);
    }
    
    @Override
    public void moveEntity(final Entity entity, final Position pos, final EntityMoveAction action) {
        if (pos == null) throw new IllegalArgumentException("The given pos is null.");
        if (entity == null) throw new IllegalArgumentException("The given entity is null.");
        
        synchronized (this.playfieldLock) {
            if (
                !this.entityPositions.containsKey(entity)
            ) throw new EntityNotOnFieldException("The given entity" + entity + "is not on this playfield.");
            
            EntityMoveAction actionToLog = action;
            
            final Position oldPos = this.entityPositions.get(entity);
            
            if (actionToLog == null) {
                actionToLog = new EntityTeleportAction(this.getSimulation().getSimulationClock().getLastTickNumber(), entity, oldPos, pos);
            } else {
                if (
                    !actionToLog.getEntity().equals(entity)
                ) throw new IllegalArgumentException("Given action wasn't caused by given entity.");
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
    }
    
    private void removeEntityFromSimulationTree(final Entity entity) {
        final SimulationTreeNode node = this.findNodeForEntity(entity, false);
        
        if (node == null) return;
        
        for (final SimulationTreeNode child : node.getChildren()) {
            if (child.getElementId().equals(Integer.toHexString(entity.hashCode()))) {
                node.removeChild(child);
                if (this.simulationTreeEntityRemovedListener != null) {
                    // TODO check for bugs with simulation tree when window is reattached or attached late
                    this.simulationTreeEntityRemovedListener.accept(child);
                }
            }
        }
    }
    
    @Override
    public void removeEntity(final Entity entity) {
        if (entity == null) throw new IllegalArgumentException("The given entity is null.");
        
        synchronized (this.playfieldLock) {
            if (
                !this.entityPositions.containsKey(entity)
            ) throw new EntityNotOnFieldException("The given entity" + entity + "is not on this playfield.");
            
            final Position pos = this.entityPositions.get(entity);
            this.removeEntityFromCell(pos, entity);
            this.entityPositions.remove(entity, pos);
            
            this.getSimulation().getActionLog()
                    .logAction(new EntityDespawnAction(this.getSimulation().getSimulationClock().getLastTickNumber(), entity, this));

            entity.removeFromPlayfield();
            
            this.removeEntityFromSimulationTree(entity);
            
            this.drawEntities();
        }
    }
    
    @Override
    public Position getEntityPosition(final Entity entity) {
        if (entity == null) throw new IllegalArgumentException("The given entity is null.");
        synchronized (this.playfieldLock) {
            final Position pos = this.entityPositions.get(entity);
            if (pos == null) throw new EntityNotOnFieldException("The given entity" + entity + "is not on this playfield.");
            return pos;
        }
    }
    
    @Override
    public boolean containsEntity(final Entity entity) {
        if (entity == null) throw new IllegalArgumentException("The given entity is null.");
        synchronized (this.playfieldLock) {
            return this.entityPositions.containsKey(entity);
        }
    }
    
    @Override
    public boolean isSolidEntityAt(final Position pos) {
        final List<SolidEntity> solidEntitiesAtPos = this.getEntitiesOfTypeAt(pos, SolidEntity.class, true);
        for (final SolidEntity entity : solidEntitiesAtPos) {
            if (entity.isCurrentlySolid()) return true;
        }
        return false;
    }
    
    /**
     * @return the root node of the simulation tree
     */
    public SimulationTreeNode getSimulationTree() {
        return this.simualtionTreeRootNode;
    }
    
    /**
     * Set the listener for when an entity is added to the simulation tree.
     *
     * @param listener
     *     the listener to set
     */
    public void setSimulationTreeEntityAddedListener(final BiConsumer<SimulationTreeNode, Entity> listener) {
        if ((this.simulationTreeEntityAddedListener == null) || (listener == null)) {
            this.simulationTreeEntityAddedListener = listener;
        } else throw new ListenerSetException();
    }
    
    /**
     * Remove the listener for when an entity is added to the simulation tree.
     */
    public void removeSimulationTreeEntityAddedListener() {
        this.simulationTreeEntityAddedListener = null;
    }
    
    /**
     * Set the listener for when an entity is removed from the simulation tree.
     *
     * @param listener
     *     the listener to set
     */
    public void setSimulationTreeEntityRemovedListener(final Consumer<SimulationTreeNode> listener) {
        if ((this.simulationTreeEntityRemovedListener == null) || (listener == null)) {
            this.simulationTreeEntityRemovedListener = listener;
        } else throw new ListenerSetException();
    }
    
    /**
     * Remove the listener for when an entity is removed from the simulation tree.
     */
    public void removeSimulationTreeEntityRemovedListener() {
        this.simulationTreeEntityRemovedListener = null;
    }
    
    /**
     * Set the listener for when the drawables on the playfield changed. This listener is responsible for informing the
     * UI.
     *
     * @param listener
     *     the listener to set
     */
    public void setDrawablesChangedListener(final Consumer<List<Drawable>> listener) {
        if ((this.drawablesChangedListener == null) || (listener == null)) {
            this.drawablesChangedListener = listener;
        } else throw new ListenerSetException();
    }
    
    /**
     * Remove the listener for when the drawables on the playfield changed. This listener is responsible for informing
     * the UI.
     */
    public void removeDrawablesChangedListener() {
        this.drawablesChangedListener = null;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(this.hashCode());
    }
}

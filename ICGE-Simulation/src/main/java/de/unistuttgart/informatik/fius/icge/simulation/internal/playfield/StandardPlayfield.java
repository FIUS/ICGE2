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

import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.informatik.fius.icge.simulation.Playfield;
import de.unistuttgart.informatik.fius.icge.simulation.Position;
import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;
import de.unistuttgart.informatik.fius.icge.simulation.exception.EntityAlreadyOnFieldExcpetion;
import de.unistuttgart.informatik.fius.icge.simulation.exception.EntityNotOnFieldException;
import de.unistuttgart.informatik.fius.icge.simulation.internal.StandardSimulation;
import de.unistuttgart.informatik.fius.icge.ui.Drawable;
import de.unistuttgart.informatik.fius.icge.ui.PlayfieldDrawer;


/**
 * The standard implementation of {@link Playfield}
 * 
 * @author Tim Neumann
 */
public class StandardPlayfield implements Playfield {
    private PlayfieldDrawer drawer;
    
    //TODO: Improve data structure. Tim already has an idea with a matrix of linked lists and a hashmap of weak refs. 
    private final List<PlayfieldCell> cells = new ArrayList<>();
    
    /**
     * Initialize the playfield for the given simulation
     * 
     * @param simulation
     *     the parent simulation
     */
    public void initialize(StandardSimulation simulation) {
        this.drawer = simulation.getUiManager().getPlayfieldDrawer();
    }
    
    /**
     * Converts all entities to drawables and sends them to the playfield drawer.
     */
    public void drawEntities() {
        List<Drawable> drawables = new ArrayList<>();
        for (Entity entity : this.getAllEntities()) {
            drawables.add(entity.getDrawInformation());
        }
        this.drawer.setDrawables(drawables);
        this.drawer.draw();
    }
    
    @Override
    public List<Entity> getAllEntities() {
        return this.getAllEntitiesOfType(Entity.class, true);
    }
    
    @Override
    public <T extends Entity> List<T> getAllEntitiesOfType(Class<? extends T> type, boolean includeSubclasses) {
        if (type == null) throw new IllegalArgumentException("The given type is null.");
        List<T> result = new ArrayList<>();
        for (PlayfieldCell cell : this.cells) {
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
        for (PlayfieldCell cell : this.cells) {
            if (cell.getPosition().equals(pos)) {
                result.addAll(cell.getEntities(type, includeSubclasses));
            }
        }
        return result;
    }
    
    @Override
    public void addEntity(Position pos, Entity entity) {
        if (pos == null) throw new IllegalArgumentException("The given pos is null.");
        if (entity == null) throw new IllegalArgumentException("The given entity is null.");
        
        PlayfieldCell cellToAddIn = null;
        for (PlayfieldCell cell : this.cells) {
            if (cell.getPosition().equals(pos)) {
                cellToAddIn = cell;
            }
            if (cell.contains(entity)) {
                throw new EntityAlreadyOnFieldExcpetion("The given entity" + entity + "is already on this playfield.");
            }
        }
        

        if (cellToAddIn == null) {
            cellToAddIn = new PlayfieldCell(pos);
            this.cells.add(cellToAddIn);
        }
        
        cellToAddIn.addEntity(entity);
        entity.initOnPlayfield(this);
    }
    
    @Override
    public Position getEntityPosition(Entity entity) {
        if (entity == null) throw new IllegalArgumentException("The given entity is null.");
        for (PlayfieldCell cell : this.cells) {
            if (cell.contains(entity)) return cell.getPosition();
        }
        throw new EntityNotOnFieldException("The given entity" + entity + "is not on this playfield.");
    }
    
    @Override
    public boolean containsEntity(Entity entity) {
        if (entity == null) throw new IllegalArgumentException("The given entity is null.");
        for (PlayfieldCell cell : this.cells) {
            if (cell.contains(entity)) return true;
        }
        return false;
    }
}

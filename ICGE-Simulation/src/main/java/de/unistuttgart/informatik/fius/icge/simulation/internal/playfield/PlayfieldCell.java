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
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import de.unistuttgart.informatik.fius.icge.simulation.Position;
import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;


/**
 * Represents one cell on the playfield.
 * 
 * @author Tim Neumann
 */
public class PlayfieldCell {
    private final Position pos;
    
    private final HashMap<Class<? extends Entity>, List<Entity>> entities;
    
    /**
     * Create a new playfield cell,
     * 
     * @param pos
     *     The position of the cell
     */
    public PlayfieldCell(Position pos) {
        this.pos = pos;
        this.entities = new HashMap<>();
    }
    
    /**
     * @return the position of this cell
     */
    public Position getPosition() {
        return this.pos;
    }
    
    /**
     * Get the relevant list for the given entity type.
     * 
     * @param type
     *     The entity type to get the list for
     * @return an Optional with the list if it exists, otherwise an empty Optional
     */
    private <T extends Entity> Optional<List<T>> getRelevantListGeneric(Class<? extends T> type) {
        List<? extends Entity> list = this.entities.get(type);
        //We need this one unchecked cast.
        //But nothing can happen here, because the HashMap always contains lists of the type of the key
        //And therefore we have here a list of the type which is the parameter, which extends T
        @SuppressWarnings("unchecked")
        List<T> listOfCorrectType = (List<T>) list;
        return Optional.ofNullable(listOfCorrectType);
    }
    
    /**
     * Get the relevant list for the given entity.
     * 
     * @param e
     *     The entity to get the list for
     * @return an Optional with the list if it exists, otherwise an empty Optional
     */
    private Optional<List<Entity>> getRelevantList(Entity e) {
        var type = e.getClass();
        
        return getRelevantListGeneric(type);
    }
    
    /**
     * Get the relevant list for the given entity and create it if it is not there.
     * 
     * @param e
     *     The entity to get the list for
     * @return The list for the given entity
     */
    private List<Entity> getRelevantListAndCreate(Entity e) {
        var type = e.getClass();
        if (this.entities.containsKey(type)) {
            return this.entities.get(type);
        }
        
        List<Entity> list = new ArrayList<>();
        this.entities.put(type, list);
        return list;
    }
    
    /**
     * Add the given entity to this field.
     * 
     * @param e
     *     The entity to add
     */
    public void addEntity(Entity e) {
        this.getRelevantListAndCreate(e).add(e);
    }
    
    /**
     * Check whether this cell contains the given entity.
     * 
     * @param e
     *     The entity to check for
     * @return Whether this cell contains the given entity
     */
    public boolean contains(Entity e) {
        var opt = getRelevantList(e);
        if (opt.isEmpty()) return false;
        return opt.get().contains(e);
    }
    
    /**
     * Return {@code true} iff the cell contains no entities.
     * 
     * @return {@code true} if empty
     */
    public boolean isEmpty() {
        return this.entities.isEmpty();
    }
    
    /**
     * Remove the given entity from this cell.
     * 
     * @param e
     *     The entity to remove
     * @return Whether this cell contained the given entity
     */
    public boolean remove(Entity e) {
        var opt = getRelevantList(e);
        if (opt.isEmpty()) return false;
        return opt.get().remove(e);
    }
    
    /**
     * Get all entities in this cell of the given type.
     * 
     * @param <T>
     *     The generic type to return the entities as
     * @param type
     *     The type of the entities to get
     * @param includeSubclasses
     *     Whether to include the subclasses of the given type
     * @return A list of all matching entities
     */
    public <T extends Entity> List<T> getEntities(Class<? extends T> type, boolean includeSubclasses) {
        if (!includeSubclasses) {
            return this.<T>getRelevantListGeneric(type).orElse(List.of());
        }
        
        List<T> result = new ArrayList<>();
        
        for (Class<? extends Entity> storedType : this.entities.keySet()) {
            if (type.isAssignableFrom(storedType)) {
                Class<? extends T> storedTypeAsSubOfRequested = storedType.asSubclass(type);
                List<T> listForStoredType = this.<T>getRelevantListGeneric(storedTypeAsSubOfRequested).orElse(List.of());
                result.addAll(listForStoredType);
            }
        }
        
        return result;
    }
    
    /**
     * @return a list of stored types.
     */
    public Set<Class<? extends Entity>> getStoredTypes() {
        return this.entities.keySet();
    }
}

/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 *
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation;

import java.util.List;

import de.unistuttgart.informatik.fius.icge.simulation.actions.EntityMoveAction;
import de.unistuttgart.informatik.fius.icge.simulation.actions.EntityTeleportAction;
import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;
import de.unistuttgart.informatik.fius.icge.simulation.exception.EntityAlreadyOnFieldExcpetion;
import de.unistuttgart.informatik.fius.icge.simulation.exception.EntityNotOnFieldException;
import de.unistuttgart.informatik.fius.icge.simulation.exception.EntityOnAnotherFieldException;


/**
 * The interface for the playfield of the simulation
 *
 * @author Tim Neumann
 */
public interface Playfield {

    /**
     * @return the simulation this playfield is part of
     */
    Simulation getSimulation();

    /**
     * Get a list of all entities on this playfield.
     *
     * @return A list of all entities
     */
    List<Entity> getAllEntities();

    /**
     * Get a list of all entities matching the given type on this playfield.
     *
     * @param <T>
     *     The generic type to return the entities as
     * @param type
     *     The type of entity to get; must <b>not</b> be <b>null</b>
     * @param includeSubclasses
     *     Whether to include the subclasses of the given type
     * @return A list of all matching entities
     * @throws IllegalArgumentException
     *     if the given type is null
     */
    <T extends Entity> List<T> getAllEntitiesOfType(Class<? extends T> type, boolean includeSubclasses);

    /**
     * Get a list of all entities at the given position on this playfield.
     *
     * @param pos
     *     The position at which to get the entities; must <b>not</b> be <b>null</b>
     * @return A list of all entities at that position
     * @throws IllegalArgumentException
     *     if the given pos is null
     */
    List<Entity> getEntitiesAt(final Position pos);

    /**
     * Get a list of all entities matching the given type at the given position on this playfield.
     *
     * @param <T>
     *     The generic type to return the entities as
     * @param pos
     *     The position at which to get the entities; must <b>not</b> be <b>null</b>
     * @param type
     *     The type of entity to get; must <b>not</b> be <b>null</b>
     * @param includeSubclasses
     *     Whether to include the subclasses of the given type
     * @return A list of all matching entities
     * @throws IllegalArgumentException
     *     if the given pos or type is null
     */
    <T extends Entity> List<T> getEntitiesOfTypeAt(final Position pos, Class<? extends T> type, boolean includeSubclasses);

    /**
     * Add a given entity to this simulation at a given position on this playfield.
     *
     * @param pos
     *     The position to add the entity; must <b>not</b> be <b>null</b>
     * @param entity
     *     The entity to add; must <b>not</b> be <b>on the field</b>; must <b>not</b> be <b>null</b>
     *
     * @throws EntityAlreadyOnFieldExcpetion
     *     if the given entity has been added to this playfield before
     * @throws EntityOnAnotherFieldException
     *     if the entity is already on another field
     * @throws IllegalArgumentException
     *     if the given pos or entity is null
     */
    void addEntity(Position pos, Entity entity);

    /**
     * Move a entity of this simulation to a given position on this playfield.
     * <p>
     * This causes a {@link EntityTeleportAction}.
     * </p>
     *
     * @param entity
     *     The entity to move; <b>must</b> be <b>on the field</b>; must <b>not</b> be <b>null</b>
     * @param pos
     *     The position to move the entity to; must <b>not</b> be <b>null</b>
     * @throws EntityNotOnFieldException
     *     if the given entity is not in this playfield
     * @throws IllegalArgumentException
     *     if the given pos or entity is null
     */
    void moveEntity(Entity entity, Position pos);

    /**
     * Move a entity of this simulation to a given position on this playfield.
     *
     * @param entity
     *     The entity to move; <b>must</b> be <b>on the field</b>; must <b>not</b> be <b>null</b>
     * @param pos
     *     The position to move the entity to; must <b>not</b> be <b>null</b>
     * @param action
     *     An action describing this move; will be logged by this method; if null causes a {@link EntityTeleportAction}
     * @throws EntityNotOnFieldException
     *     if the given entity is not in this playfield
     * @throws IllegalArgumentException
     *     if the given pos or entity is null
     * @throws IllegalArgumentException
     *     if the given action match with the other arguments
     */
    void moveEntity(Entity entity, Position pos, EntityMoveAction action);

    /**
     * Remove a entity of this simulation from this playfield.
     *
     * @param entity
     *     The entity to add; <b>must</b> be <b>on the field</b>; must <b>not</b> be <b>null</b>
     * @throws EntityNotOnFieldException
     *     if the given entity is not in this playfield
     * @throws IllegalArgumentException
     *     if the given entity is null
     */
    void removeEntity(Entity entity);

    /**
     * Get the position of the specified entity on the playfield.
     *
     * @param entity
     *     The entity to get the position of; <b>must</b> be <b>on the field</b>; must <b>not</b> be <b>null</b>
     * @return The position of the given entity
     * @throws EntityNotOnFieldException
     *     if the given entity is not in this playfield
     * @throws IllegalArgumentException
     *     if the given entity is null
     */
    Position getEntityPosition(Entity entity);

    /**
     * Check whether the specified entity is on this playfield.
     *
     * @param entity
     *     The entity to check; must <b>not</b> be <b>null</b>
     * @return whether the given entity is on this playfield
     * @throws IllegalArgumentException
     *     if the given entity is null
     */
    boolean containsEntity(Entity entity);

    /**
     * Check whether a solid entity is at the given position.
     *
     * @param pos
     *     The position to check
     * @return true if and only if a solid entity is at that position
     */
    boolean isSolidEntityAt(Position pos);
}

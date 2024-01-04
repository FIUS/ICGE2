/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 *
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.tools;

import java.util.function.Supplier;

import de.unistuttgart.informatik.fius.icge.simulation.Playfield;
import de.unistuttgart.informatik.fius.icge.simulation.Position;
import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;


/**
 * A tool to modify a playfield and place entities on it.
 *
 * @author Tim Neumann
 */
public class PlayfieldModifier {
    private final Playfield playfield;

    /**
     * Create a new playfield modifier for the given playfield.
     *
     * @param playfield
     *     The playfield to create the modifier for
     */
    public PlayfieldModifier(final Playfield playfield) {
        this.playfield = playfield;
    }

    /**
     * Place the given entity at the given position on the playfield.
     *
     * @param entity
     *     The entity to place
     * @param position
     *     The position to place the entity at
     */
    public void placeEntityAt(final Entity entity, final Position position) {
        this.playfield.addEntity(position, entity);
    }

    /**
     * Place the given number of entities supplied by the given factory at the given location.
     *
     * @param entityFactory
     *     The factory to get the entities from
     * @param count
     *     The number of entities to place
     * @param position
     *     The position to place the entities at
     */
    public void placeMultipleEntitiesAt(final Supplier<? extends Entity> entityFactory, final int count, final Position position) {
        for (int i = 0; i < count; i++) {
            placeEntityAt(entityFactory.get(), position);
        }
    }

    /**
     * Place an entity supplied by the given factory at each of the given positions
     *
     * @param entityFactory
     *     The factory to get the entities from
     * @param positions
     *     A list of positions to place the entities at
     */
    public void placeEntityAtEachPosition(final Supplier<? extends Entity> entityFactory, Iterable<Position> positions) {
        for (Position pos : positions) {
            placeEntityAt(entityFactory.get(), pos);
        }
    }
}

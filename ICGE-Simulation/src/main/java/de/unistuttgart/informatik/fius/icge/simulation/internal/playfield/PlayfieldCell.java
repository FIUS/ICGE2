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

import de.unistuttgart.informatik.fius.icge.simulation.MultiTypedList;
import de.unistuttgart.informatik.fius.icge.simulation.Position;
import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;


/**
 * Represents one cell on the playfield.
 * 
 * @author Tim Neumann
 */
public class PlayfieldCell extends MultiTypedList<Entity> {
    private final Position pos;
    
    /**
     * Create a new playfield cell,
     * 
     * @param pos
     *     The position of the cell
     */
    public PlayfieldCell(final Position pos) {
        super();
        this.pos = pos;
    }
    
    /**
     * @return the position of this cell
     */
    public Position getPosition() {
        return this.pos;
    }
}

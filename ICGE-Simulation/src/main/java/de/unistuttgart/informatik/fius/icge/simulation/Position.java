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

/**
 * Represents a position on the playfield.
 * <p>
 * Objects of this class are immutable
 * </p>
 * 
 * @author Tim Neumann
 */
public class Position {
    private final int x;
    private final int y;
    
    /**
     * Create a new position from the given parameters.
     * 
     * @param x
     *     The x coordinate for the new position
     * @param y
     *     The y coordinate for the new position
     */
    public Position(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * @return the x coordinate of this position
     */
    public int getX() {
        return this.x;
    }
    
    /**
     * @return the y coordinate of this position
     */
    public int getY() {
        return this.y;
    }
}

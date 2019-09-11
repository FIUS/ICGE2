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
 * The four fundamental directions possible in the simulation.
 * <p>
 * {@link #NORTH} is at the top of the screen ({@link Position#getY()} getting smaller)
 * </p>
 * <p>
 * {@link #EAST} is at the right of the screen ({@link Position#getX()} getting bigger)
 * </p>
 * <p>
 * {@link #SOUTH} is at the bottom of the screen ({@link Position#getY()} getting bigger)
 * </p>
 * <p>
 * {@link #WEST} is at the left of the screen ({@link Position#getX()} getting smaller)
 * </p>
 * 
 * @see Position
 * 
 * @author Tim Neumann
 */
public enum Direction {
    /** the north direction; at the top of the screen ({@link Position#getY()} getting smaller) */
    NORTH,
    /** The east direction; at the right of the screen ({@link Position#getX()} getting bigger) */
    EAST,
    /** The south direction; at the bottom of the screen ({@link Position#getY()} getting bigger) */
    SOUTH,
    /** The west direction; at the left of the screen ({@link Position#getX()} getting smaller) */
    WEST;
    
    /**
     * Get the direction that is next in a clock wise rotation
     * 
     * @return The next direction
     */
    public Direction clockWiseNext() {
        return Direction.values()[(this.ordinal() + 1) % 4];
    }
}

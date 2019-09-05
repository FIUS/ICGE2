/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.entity;

/**
 * The four fundamental directions possible in the simulation.
 * 
 * @author Tim Neumann
 */
public enum Direction {
    /** the north direction */
    NORTH,
    /** The east direction */
    EAST,
    /** The south direction */
    SOUTH,
    /** The west direction */
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

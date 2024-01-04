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

import java.util.Arrays;


/**
 * Represents a position on the playfield.
 * <p>
 * Objects of this class are immutable
 * </p>
 *
 * <p>
 * x is the row; negative towards the top ({@link Direction#NORTH}) and positive towards the bottom
 * ({@link Direction#SOUTH})
 * </p>
 * <p>
 * y is the column; negative towards the left ({@link Direction#WEST}) and positive towards the right
 * ({@link Direction#EAST})
 * </p>
 *
 * @see Direction
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

    /**
     * Get the adjacent position in the given direction.
     *
     * @param direction
     *     The direction to get the adjacent position in
     * @return The position adjacent to this in the given direction
     */
    public Position adjacentPosition(final Direction direction) {
        switch (direction) {
            case EAST:
                return new Position(this.getX() + 1, this.getY());
            case NORTH:
                return new Position(this.getX(), this.getY() - 1);
            case SOUTH:
                return new Position(this.getX(), this.getY() + 1);
            case WEST:
                return new Position(this.getX() - 1, this.getY());
            default:
                throw new IllegalArgumentException("Unknown direction");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Position)) return false;
        final Position p = (Position) o;
        return (this.x == p.getX()) && (this.y == p.getY());
    }

    @Override
    public int hashCode() {
        final int[] numbers = { this.x, this.y };
        return Arrays.hashCode(numbers);
    }

    @Override
    public String toString() {
        return "(x=" + this.x + ", y=" + this.y + ")";
    }
}

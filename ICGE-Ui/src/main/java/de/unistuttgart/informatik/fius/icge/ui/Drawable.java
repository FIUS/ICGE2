/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 *
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.ui;

/**
 * A class containing all information needed to draw an object onto the playfield by a {@link PlayfieldDrawer}.
 *
 * @author Tim Neumann
 */
public class Drawable implements Comparable<Drawable>{

    /**
     * The x coordinate of the drawable.
     *
     * The coordinate system is based on cells where fractionals denote positions between cells.
     */
    public final double x;

    /**
     * The y coordinate of the drawable.
     *
     * The coordinate system is based on cells where fractionals denote positions between cells.
     */
    public final double y;

    /**
     * The z value of the drawable.
     *
     * The z value is used to decide the drawing order of Drawables in the same cell.
     */
    public final int z;

    /**
     * The handle of the texture for this drawable.
     *
     * The texture must be registered in the TextureRegistry.
     */
    public final String textureHandle;

    /**
     * Create a new Drawable.
     *
     * @param x coordinate
     * @param y coordinate
     * @param z value
     * @param textureHandle
     */
    public Drawable(double x, double y, int z, String textureHandle) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.textureHandle = textureHandle;
    }

    @Override
    public int compareTo(Drawable o) {
        double compareResult = 0;
        compareResult = this.z - o.z;
        if (compareResult == 0) {
            compareResult = this.x - o.x;
        }
        if (compareResult == 0) {
            compareResult = this.y - o.y;
        }
        if (compareResult < 0) {
            return -1;
        }
        if (compareResult > 0) {
            return 1;
        }
        return this.textureHandle.compareTo(o.textureHandle);
    }
}

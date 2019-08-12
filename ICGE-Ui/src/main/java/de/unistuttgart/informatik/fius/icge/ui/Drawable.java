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
public interface Drawable extends Comparable<Drawable> {
    
    /**
     * Get the x coordinate of the drawable.
     *
     * The coordinate system is based on cells where fractionals denote positions between cells.
     */
    double getX();
    
    /**
     * Get the y coordinate of the drawable.
     *
     * The coordinate system is based on cells where fractionals denote positions between cells.
     */
    double getY();
    
    /**
     * Get the z value of the drawable.
     *
     * The z value is used to decide the drawing order of Drawables in the same cell.
     */
    int getZ();
    
    /**
     * Get the handle of the texture for this drawable.
     *
     * The texture must be registered in the TextureRegistry.
     */
    String getTextureHandle();
    
    /**
     * Return wether this Drawable can be drawn tiled if multiple are present in the same cell.
     * 
     * @return true if the Drawable is tilable
     */
    default boolean isTilable() {
        return true;
    }
    
    @Override
    default int compareTo(final Drawable o) {
        double compareResult = 0;
        compareResult = this.getZ() - o.getZ();
        if (compareResult == 0) {
            compareResult = this.getX() - o.getX();
        }
        if (compareResult == 0) {
            compareResult = this.getY() - o.getY();
        }
        if (compareResult < 0) return -1;
        if (compareResult > 0) return 1;
        return this.getTextureHandle().compareTo(o.getTextureHandle());
    }
}

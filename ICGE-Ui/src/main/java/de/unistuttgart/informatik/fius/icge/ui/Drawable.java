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
 * A interface representing an object that can be drawn onto the playfield by a {@link PlayfieldDrawer}.
 *
 * @author Tim Neumann
 */
public interface Drawable {

    /**
     * Get the x coordinate of the drawable.
     *
     * The coordinate system is based on cells where fractionals denote positions between cells.
     *
     * @return the x coordinate.
     */
    public double getX();

    /**
     * Get the y coordinate of the drawable.
     *
     * The coordinate system is based on cells where fractionals denote positions between cells.
     *
     * @return the y coordinate.
     */
    public double getY();

    /**
     * Get the z coordinate of the drawable.
     *
     * The z coordinate is used as a tie braker if two drawables are in the same cell.
     *
     * @return the z coordinate.
     */
    public int getZ();

    /**
     * Get the handle of the texture for this drawable.
     *
     * The texture must be registered in the TextureRegistry.
     *
     * @return the texture handle
     */
    public String getTextureHandle();
}

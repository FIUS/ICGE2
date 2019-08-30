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
public class BasicDrawable implements Drawable {
    
    /**
     * The x coordinate of the drawable.
     *
     * The coordinate system is based on cells where fractionals denote positions between cells.
     */
    private final double x;
    
    /**
     * The y coordinate of the drawable.
     *
     * The coordinate system is based on cells where fractionals denote positions between cells.
     */
    private final double y;
    
    /**
     * The z value of the drawable.
     *
     * The z value is used to decide the drawing order of Drawables in the same cell.
     */
    private final int z;
    
    /**
     * The handle of the texture for this drawable.
     *
     * The texture must be registered in the TextureRegistry.
     */
    private final String textureHandle;
    
    /**
     * Create a new Drawable.
     *
     * @param x
     *     coordinate
     * @param y
     *     coordinate
     * @param z
     *     value
     * @param textureHandle
     */
    public BasicDrawable(final double x, final double y, final int z, final String textureHandle) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.textureHandle = textureHandle;
    }
    
    @Override
    public double getX() {
        return this.x;
    }
    
    @Override
    public double getY() {
        return this.y;
    }
    
    @Override
    public int getZ() {
        return this.z;
    }
    
    @Override
    public String getTextureHandle() {
        return this.textureHandle;
    }
}

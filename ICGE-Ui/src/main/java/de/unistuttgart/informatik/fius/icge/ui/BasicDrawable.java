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
     * The amount.
     *
     * The amount is used to draw multiple of the same drawable in the same cell by tiling or by diplaying a number on top of the Texture.
     */
    private int amount;
    
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
     *     the handle of the texture used for this drawable
     */
    public BasicDrawable(final double x, final double y, final int z, final String textureHandle) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.amount = 1;
        this.textureHandle = textureHandle;
    }
    
    /**
     * Create a new Drawable.
     *
     * @param x
     *     coordinate
     * @param y
     *     coordinate
     * @param z
     *     value
     * @param amount
     * @param textureHandle
     *     the handle of the texture used for this drawable
     */
    public BasicDrawable(final double x, final double y, final int z, int amount, final String textureHandle) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.amount = amount;
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

    @Override
    public int getAmount() {
        return this.amount;
    }

    @Override
    public void setAmount(int amount) {
        this.amount=amount;
    }

    @Override
    public void incrementAmount() {
        this.amount++;
    }
}

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
 * A class containing all information needed to draw a moving object onto the playfield by a {@link PlayfieldDrawer}.
 *
 * @author Fabian Bühler
 */
public class AnimatedDrawable implements Drawable {
    
    private final double xStart;
    private final double yStart;
    private final double xEnd;
    private final double yEnd;
    
    private final int z;
    
    private final String textureHandle;
    
    private final long   tickStart;
    private final long   tickEnd;
    private final double duration;
    
    private long currentTick = 0;
    
    /**
     * Create a new animated Drawable.
     *
     * @param tickStart
     *     start tick
     * @param xStart
     *     start x coordinate
     * @param yStart
     *     start y coordinate
     * @param tickEnd
     *     end tick
     * @param xEnd
     *     end x coordinate
     * @param yEnd
     *     end y coordinate
     * @param z
     *     value
     * @param textureHandle
     */
    public AnimatedDrawable(
            final long tickStart, final double xStart, final double yStart, final long tickEnd, final double xEnd, final double yEnd,
            final int z, final String textureHandle
    ) {
        if (tickEnd < tickStart) {
            throw new IllegalArgumentException("End tick must be after start tick!");
        }
        this.tickStart = tickStart;
        this.xStart = xStart;
        this.yStart = yStart;
        this.tickEnd = tickEnd;
        this.xEnd = xEnd;
        this.yEnd = yEnd;
        this.duration = tickEnd - tickStart;
        this.z = z;
        this.textureHandle = textureHandle;
    }
    
    @Override
    public double getX() {
        if (this.currentTick <= this.tickStart) {
            return this.xStart;
        }
        if (this.currentTick >= this.tickEnd) {
            return this.xEnd;
        }
        double completion = (this.currentTick - this.tickStart) / this.duration;
        return (this.xEnd - this.xStart) * completion + this.xStart;
    }
    
    @Override
    public double getY() {
        if (this.currentTick <= this.tickStart) {
            return this.yStart;
        }
        if (this.currentTick >= this.tickEnd) {
            return this.yEnd;
        }
        double completion = (this.currentTick - this.tickStart) / this.duration;
        return (this.yEnd - this.yStart) * completion + this.yStart;
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
    public boolean isTilable() {
        return false;
    }
    
    @Override
    public boolean isAnimated() {
        return this.currentTick <= this.tickEnd;
    }
}

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

*
/**
 * A class containing all information needed to draw a moving object onto the playfield by a {@link PlayfieldDrawer}.
 *
 * @author Fabian Bühler
 */
public class AnimatedDrawable implements Drawable {
 *
    private final double xStart;
    private final double yStart;
    private final double xEnd;
    private final double yEnd;
 *
    private final int z;
 *
    private final String textureHandle;
 *
    private final long tickStart;
    private final long tickEnd;
    private final long duration;
 *
    private long currentTick = 0;
 *
    /**
     * Create a new animated Drawable.
     *
     * @param tickStart
     *     start tick
     * @param xStart
     *     start x coordinate
     * @param yStart
     *     start y coordinate
     * @param duration
     *     duration in ticks
     * @param xEnd
     *     end x coordinate
     * @param yEnd
     *     end y coordinate
     * @param z
     *     value
     * @param textureHandle
     *     the handle of the texture used for this drawable
     */
    public AnimatedDrawable(
            final long tickStart, final double xStart, final double yStart, final long duration, final double xEnd, final double yEnd,
            final int z, final String textureHandle
    ) {
        if (duration <= 0) throw new IllegalArgumentException("Animation must not have a negative duration");
        this.tickStart = tickStart;
        this.xStart = xStart;
        this.yStart = yStart;
        this.tickEnd = tickStart + duration;
        this.xEnd = xEnd;
        this.yEnd = yEnd;
        this.duration = duration;
        this.z = z;
        this.textureHandle = textureHandle;
    }
 *
    @Override
    public void setCurrentTick(final long renderTick) {
        this.currentTick = renderTick;
    }
 *
    @Override
    public double getX() {
        if (this.currentTick <= this.tickStart) return this.xStart;
        if (this.currentTick >= this.tickEnd) return this.xEnd;
        final double completion = (this.currentTick - this.tickStart) / (double) this.duration;
        return ((this.xEnd - this.xStart) * completion) + this.xStart;
    }
 *
    @Override
    public double getY() {
        if (this.currentTick <= this.tickStart) return this.yStart;
        if (this.currentTick >= this.tickEnd) return this.yEnd;
        final double completion = (this.currentTick - this.tickStart) / (double) this.duration;
        return ((this.yEnd - this.yStart) * completion) + this.yStart;
    }
 *
    @Override
    public int getZ() {
        return this.z;
    }
 *
    @Override
    public String getTextureHandle() {
        return this.textureHandle;
    }
 *
    @Override
    public boolean isTilable() {
        return false;
    }
 *
    @Override
    public boolean isAnimated() {
        return this.currentTick <= this.tickEnd;
    }
 *
    /**
     * Get's {@link #xStart xStart}
     *
     * @return xStart
     */
    public double getxStart() {
        return this.xStart;
    }
 *
    /**
     * Get's {@link #yStart yStart}
     *
     * @return yStart
     */
    public double getyStart() {
        return this.yStart;
    }
 *
    /**
     * Get's {@link #xEnd xEnd}
     *
     * @return xEnd
     */
    public double getxEnd() {
        return this.xEnd;
    }
 *
    /**
     * Get's {@link #yEnd yEnd}
     *
     * @return yEnd
     */
    public double getyEnd() {
        return this.yEnd;
    }
 *
    /**
     * Get's {@link #tickStart tickStart}
     *
     * @return tickStart
     */
    public long getTickStart() {
        return this.tickStart;
    }
 *
    /**
     * Get's {@link #tickEnd tickEnd}
     *
     * @return tickEnd
     */
    public long getTickEnd() {
        return this.tickEnd;
    }
 *
    /**
     * Get's {@link #duration duration}
     *
     * @return duration
     */
    public long getDuration() {
        return this.duration;
    }
}

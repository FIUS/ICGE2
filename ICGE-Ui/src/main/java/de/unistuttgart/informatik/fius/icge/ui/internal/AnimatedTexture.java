/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.ui.internal;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;


/**
 * Class containing multiple textures of an animation.
 * 
 * @author Fabian Bühler
 */
public class AnimatedTexture implements Texture {
    
    private final SwingTextureRegistry      registry;
    private final ArrayList<AnimationFrame> animationFrames;
    private long                            duration;
    private final boolean                   loop;
    
    /**
     * Create a new animated Texture.
     * 
     * @param registry
     *     the texture registry.
     * @param loop
     *     set to true if the animation should loop
     */
    public AnimatedTexture(final SwingTextureRegistry registry, final boolean loop) {
        this.registry = registry;
        this.animationFrames = new ArrayList<>();
        this.duration = 0;
        this.loop = loop;
    }
    
    /**
     * Add a texture at the end of the animation.
     * 
     * @param frameTexture
     *     the texture handle of the texture to add.
     * @param frames
     *     the number of frames the texture should show
     */
    public void addAnimationFrame(final String frameTexture, final long frames) {
        if (frames <= 0) throw new IllegalArgumentException("The number of frames can not be negative or 0!");
        final long last = this.duration;
        final Texture texture = this.registry.getTextureForHandle(frameTexture);
        this.duration += frames;
        final AnimationFrame frame = new AnimationFrame(last, last + (frames - 1), texture);
        this.animationFrames.add(frame);
    }
    
    /**
     * Get the texture for the current frame.
     * 
     * @param frame
     *     the current frame
     * @return the texture for the frame
     */
    public Texture getTextureForTick(final long frame) {
        return this.getTextureForTick(frame, 0);
    }
    
    /**
     * Get the texture for the current frame.
     * 
     * @param frame
     *     the current frame
     * @param animationStart
     *     offset for the start frame of the animation
     * @return the texture for the frame
     */
    private Texture getTextureForTick(final long frame, final long animationStart) {
        if (this.duration == 0) throw new IllegalStateException("AnimatedTexture is empty!");
        
        long animationFrame = frame - animationStart;
        if (this.loop) {
            animationFrame = animationFrame % this.duration;
            // animation frame could be negative if animation is yet to start
            if (animationFrame < 0) {
                animationFrame += this.duration;
            }
        }
        if (animationFrame <= 0) // return first animation frame
            return this.animationFrames.get(0).texture;
        if (animationFrame >= (this.duration - 1)) // return last animation frame
            return this.animationFrames.get(this.animationFrames.size() - 1).texture;
        for (final AnimationFrame animFrame : this.animationFrames) {
            if ((animFrame.startFrame <= animationFrame) && (animFrame.endFrame >= animationFrame)) return animFrame.texture;
        }
        throw new IllegalStateException("This can only happen if the start and end frames of the animation frames were set wrong!");
    }
    
    @Override
    public Image getTexture(final long frame) {
        return this.getTextureForTick(frame).getTexture();
    }
    
    @Override
    public void drawTexture(final long frame, final Graphics g, final int x, final int y, final int width, final int height) {
        this.getTextureForTick(frame).drawTexture(frame, g, x, y, width, height);
    }
    
    private class AnimationFrame {
        private final long    startFrame;
        private final long    endFrame;
        private final Texture texture;
        
        private AnimationFrame(final long startFrame, final long endFrame, final Texture texture) {
            this.startFrame = startFrame;
            this.endFrame = endFrame;
            this.texture = texture;
        }
    }
}

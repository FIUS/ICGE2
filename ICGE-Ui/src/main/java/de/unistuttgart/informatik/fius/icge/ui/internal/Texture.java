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


/**
 * Class containing a texture image and methods to draw this image.
 * 
 * @author Fabian Bühler
 */
public class Texture {
    
    private final Image texture;
    
    /**
     * Create a new Texture.
     * 
     * @param texture
     *     the image to use as texture.
     */
    public Texture(Image texture) {
        this.texture = texture;
    }
    
    /**
     * Get the texture image.
     * 
     * @return the image
     */
    public Image getTexture() {
        return this.texture;
    }
    
    /**
     * Draw the texture onto the screen.
     * 
     * @param g
     *     the graphics object used to draw the image
     * @param x
     *     x coordinate of the image
     * @param y
     *     y coordinate of the image
     * @param size
     *     texture size (for quadratic textures)
     */
    public void drawTexture(Graphics g, int x, int y, int size) {
        this.drawTexture(g, x, y, size, size);
    }
    
    /**
     * Draw the texture onto the screen.
     * 
     * @param g
     *     the graphics object used to draw the image
     * @param x
     *     x coordinate of the image
     * @param y
     *     y coordinate of the image
     * @param width
     *     texture width
     * @param height
     *     texture height
     */
    public void drawTexture(Graphics g, int x, int y, int width, int height) {
        g.drawImage(this.texture, x, y, width, height, null);
    }
}

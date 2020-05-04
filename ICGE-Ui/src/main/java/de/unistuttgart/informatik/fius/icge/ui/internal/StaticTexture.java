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
public class StaticTexture implements Texture {
    
    private final Image texture;
    
    /**
     * Create a new Texture.
     * 
     * @param texture
     *     the image to use as texture.
     */
    public StaticTexture(final Image texture) {
        this.texture = texture;
    }
    
    @Override
    public Image getTexture(final long frame) {
        return this.texture;
    }
    
    @Override
    public void drawTexture(final long frame, final Graphics g, final int x, final int y, final int width, final int height) {
        g.drawImage(this.texture, x, y, width, height, null);
    }
}

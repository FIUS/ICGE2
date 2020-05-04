/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.entity;

import de.unistuttgart.informatik.fius.icge.ui.BasicDrawable;


/**
 * Same as {@link BasicDrawable} but won't tile.
 */
public class UntilableDrawable extends BasicDrawable {
    
    /**
     * Create a new untilable Drawable.
     * 
     * @param x
     * @param y
     * @param z
     * @param textureHandle
     */
    public UntilableDrawable(final double x, final double y, final int z, final String textureHandle) {
        super(x, y, z, textureHandle);
    }
    
    @Override
    public boolean isTilable() {
        return false;
    }
}

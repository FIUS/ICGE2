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
 * Exception thrown when a texture file or resource could not be found.
 */
public class TextureNotFoundException extends RuntimeException {
    
    /**
     * generated
     */
    private static final long serialVersionUID = -6554875504029045378L;
    
    /**
     * Construct a new {@code TextureNotFoundException} with the given message.
     *
     * @param message
     *     error message
     */
    public TextureNotFoundException(final String message) {
        super(message);
    }
    
    /**
     * Construct a new {@code TextureNotFoundException} with the given message.
     *
     * @param message
     *     error message
     * @param e
     *     throwable cause
     */
    public TextureNotFoundException(final String message, final Throwable e) {
        super(message, e);
    }
}

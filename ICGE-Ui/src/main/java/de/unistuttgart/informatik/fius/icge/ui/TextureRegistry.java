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

import java.awt.Image;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.function.Function;


/**
 * The interface for a texture registry used by a {@link UiManager}.
 *
 * @author Tim Neumann
 */
public interface TextureRegistry {
    
    /**
     * Load an image from an internal resource as texture and return the handle to the texture.
     *
     * <p>
     * The image will only be loaded once and subsequent loads only return the existing texture handle.
     * </p>
     *
     * @param resourceName
     *     the name of the texture resource
     * @param resourceProvider
     *     a function providing the input stream for the resource with the name, given to it as the parameter.
     *     <p>
     *     A typical implementation is <code>&lt;name of class&gt;.class::getResourceAsStream</code>
     *     </p>
     * @return the handle to retrieve the texture
     *
     * @throws TextureNotFoundException
     */
    String loadTextureFromResource(String resourceName, final Function<String, InputStream> resourceProvider);
    
    /**
     * Load an image from a file path as texture and return the handle to the texture.
     *
     * The image will only be loaded once and subsequent loads only return the existing texture handle.
     *
     * @param filePath
     *     the path to the texture image file
     * @return the handle to retrieve the texture
     *
     * @throws TextureNotFoundException
     */
    String loadTextureFromFile(String filePath);
}

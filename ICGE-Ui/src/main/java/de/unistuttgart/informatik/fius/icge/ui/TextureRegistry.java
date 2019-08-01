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
import java.util.NoSuchElementException;;

/**
 * The interface for a texture registry used by a {@link UiManager}.
 *
 * @author Tim Neumann
 */
public interface TextureRegistry {

    /**
     * Load an image from a internal resource as texture and return the handle to the texture.
     *
     * The image will only be loaded once and subsequent loads only return the existing texture handle.
     *
     * @param resourceName the name of the texture resource
     * @return the handle to retrieve the texture
     *
     * @throws TextureNotFoundException
     */
    public String loadTextureFromResource(String resourceName);

    /**
     * Load an image from a file path as texture and return the handle to the texture.
     *
     * The image will only be loaded once and subsequent loads only return the existing texture handle.
     *
     * @param filePath the path to the texture image file
     * @return the handle to retrieve the texture
     *
     * @throws TextureNotFoundException
     */
    public String loadTextureFromFile(String filePath);

    /**
     * Get the texture for the given texture handle.
     *
     * @param handle the texture handle
     * @return the texture for the handle
     *
     * @throws NoSuchElementException
     */
    public Image getTextureForHandle(String handle);
}

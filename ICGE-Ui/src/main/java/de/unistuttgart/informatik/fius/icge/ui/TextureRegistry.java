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

import java.io.InputStream;
import java.util.function.Function;

import de.unistuttgart.informatik.fius.icge.ui.exception.TextureNotFoundException;


/**
 * The interface for a texture registry used by a {@link GameWindow}.
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
     *     the texture could not be loaded from the given resource location
     */
    String loadTextureFromResource(String resourceName, final Function<String, InputStream> resourceProvider);

    /**
     * Create a new animated texture with the animation length in render ticks.
     *
     * Use {@link #addAnimationFrameToTexture} to add animation frames to the animated texture.
     *
     * @param loop
     *     if true the animation will loop after the last frame
     * @return the handle to retrieve the texture
     */
    String createAnimatedTexture(boolean loop);

    /**
     * Add a animation frame to an animated texture.
     *
     * @param animatedTexture
     *     the animated texture to add the animation frame to
     * @param frameTexture
     *     the texture to add as animation frame
     * @param frames
     *     the number of frames to show this texture
     */
    void addAnimationFrameToTexture(String animatedTexture, String frameTexture, long frames);

    /**
     * Check if a texture is animated.
     *
     * @param textureHandle
     *     the texture to check
     * @return true iff the texture is animated
     */
    boolean isTextureAnimated(String textureHandle);

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
     *     the texture could not be loaded from the given file path
     */
    String loadTextureFromFile(String filePath);
}

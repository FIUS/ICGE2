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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.imageio.ImageIO;

import de.unistuttgart.informatik.fius.icge.ui.TextureNotFoundException;
import de.unistuttgart.informatik.fius.icge.ui.TextureRegistry;


/**
 * An implementation of {@link TextureRegistry} using java swing.
 *
 * @author Tim Neumann
 */
public class SwingTextureRegistry implements TextureRegistry {
    private final Map<String, String> resourceToHandle = new HashMap<>();
    private final Map<String, String> pathToHandle     = new HashMap<>();
    private final Map<String, Texture>  handleToTexture  = new HashMap<>();
    
    /**
     * Load a texture from a local (ui module) resource.
     * 
     * @param resourceName
     *     The name of the resource
     * @return The resource handle.
     * @see #loadTextureFromResource(String, Function)
     */
    public String loadTextureFromResource(final String resourceName) {
        return loadTextureFromResource(resourceName, SwingTextureRegistry.class::getResourceAsStream);
    }
    
    @Override
    public String loadTextureFromResource(final String resourceName, final Function<String, InputStream> resourceProvider) {
        if (this.resourceToHandle.containsKey(resourceName)) return this.resourceToHandle.get(resourceName);
        try (InputStream input = resourceProvider.apply(resourceName)) {
            final BufferedImage texture = ImageIO.read(input);
            this.resourceToHandle.put(resourceName, resourceName);
            this.handleToTexture.put(resourceName, new Texture(texture));
            return resourceName;
        } catch (IllegalArgumentException | IOException e) {
            throw new TextureNotFoundException("The requested Resource could not be loaded!", e);
        }
    }
    
    @Override
    public String loadTextureFromFile(final String filePath) {
        final Path resolvedPath = Path.of(filePath).toAbsolutePath();
        final String textureHandle = resolvedPath.toString();
        if (this.pathToHandle.containsKey(textureHandle)) return this.pathToHandle.get(textureHandle);
        try {
            final File textureFile = resolvedPath.toFile();
            final BufferedImage texture = ImageIO.read(textureFile);
            this.pathToHandle.put(textureHandle, textureHandle);
            this.handleToTexture.put(textureHandle, new Texture(texture));
        } catch (IllegalArgumentException | IOException e) {
            throw new TextureNotFoundException("The requested path could not be loaded!", e);
        }
        return textureHandle;
    }
    
    /**
     * Get the texture for the given texture handle.
     *
     * @param handle
     *     the texture handle
     * @return the texture for the handle
     *
     * @throws NoSuchElementException
     */
    public Texture getTextureForHandle(final String handle) {
        return this.handleToTexture.get(handle);
    }
}

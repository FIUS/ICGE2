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
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import javax.imageio.ImageIO;

import de.unistuttgart.informatik.fius.icge.ui.TextureNotFoundException;
import de.unistuttgart.informatik.fius.icge.ui.TextureRegistry;


/**
 * An implementation of {@link TextureRegistry} using java swing.
 *
 * @author Tim Neumann
 * @author Tobias Wältken
 * @version 1.0
 */
public class SwingTextureRegistry implements TextureRegistry {
    private final Map<String, String>  resourceToHandle = new HashMap<>();
    private final Map<String, String>  pathToHandle     = new HashMap<>();
    private final Set<String>          animatedTextures = new HashSet<>();
    private final Map<String, Texture> handleToTexture  = new HashMap<>();
    
    /**
     * Default constructor
     */
    public SwingTextureRegistry() {
        StaticUiTextures.load(this);
    }
    
    /**
     * Generate a new unique texture handle.
     * 
     * @return uuid texture handle
     */
    private static String generateNewTextureHandle() {
        return UUID.randomUUID().toString();
    }
    
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
            final String textureHandle = "resource://" + resourceName;
            this.resourceToHandle.put(resourceName, textureHandle);
            this.handleToTexture.put(textureHandle, new StaticTexture(texture));
            return textureHandle;
        } catch (IllegalArgumentException | IOException e) {
            throw new TextureNotFoundException("The requested Resource could not be loaded!", e);
        }
    }
    
    @Override
    public String loadTextureFromFile(final String filePath) {
        final Path resolvedPath = Path.of(filePath).toAbsolutePath();
        final String fullPath = resolvedPath.toString();
        final String textureHandle = "file://" + fullPath;
        if (this.pathToHandle.containsKey(fullPath)) return this.pathToHandle.get(fullPath);
        try {
            final File textureFile = resolvedPath.toFile();
            final BufferedImage texture = ImageIO.read(textureFile);
            this.pathToHandle.put(fullPath, textureHandle);
            this.handleToTexture.put(textureHandle, new StaticTexture(texture));
        } catch (IllegalArgumentException | IOException e) {
            throw new TextureNotFoundException("The requested path could not be loaded!", e);
        }
        return textureHandle;
    }
    
    @Override
    public String createAnimatedTexture(boolean loop) {
        String textureHandle = SwingTextureRegistry.generateNewTextureHandle();
        
        AnimatedTexture animTexture = new AnimatedTexture(this, loop);
        this.handleToTexture.put(textureHandle, animTexture);
        this.animatedTextures.add(textureHandle);
        
        return textureHandle;
    }
    
    @Override
    public void addAnimationFrameToTexture(String animatedTexture, String frameTexture, long frames) {
        try {
            AnimatedTexture animated = (AnimatedTexture) this.getTextureForHandle(animatedTexture);
            animated.addAnimationFrame(frameTexture, frames);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Texture handle was not a handle for an animated texture!", e);
        }
    }
    
    @Override
    public boolean isTextureAnimated(String textureHandle) {
        return this.animatedTextures.contains(textureHandle);
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

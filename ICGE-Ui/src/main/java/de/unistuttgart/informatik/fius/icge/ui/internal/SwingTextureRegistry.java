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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

import de.unistuttgart.informatik.fius.icge.ui.TextureNotFoundException;
import de.unistuttgart.informatik.fius.icge.ui.TextureRegistry;


/**
 * An implementation of {@link TextureRegistry} using java swing.
 *
 * @author Tim Neumann
 */
public class SwingTextureRegistry implements TextureRegistry {
    private Map<String, String> resourceToHandle = new HashMap<>();
    private Map<String, String> pathToHandle     = new HashMap<>();
    private Map<String, Image>  handleToTexture  = new HashMap<>();

    @Override
    public String loadTextureFromResource(String resourceName) {
        if (this.resourceToHandle.containsKey(resourceName)) {
            return this.resourceToHandle.get(resourceName);
        }
        try (InputStream input = SwingTextureRegistry.class.getResourceAsStream("resourceName")) {
            BufferedImage texture = ImageIO.read(input);
            this.resourceToHandle.put(resourceName, resourceName);
            this.handleToTexture.put(resourceName, texture);
            return resourceName;
        } catch (IllegalArgumentException | IOException e) {
            throw new TextureNotFoundException("The requested Resource could not be loaded!", e);
        }
    }

    @Override
    public String loadTextureFromFile(String filePath) {
        Path resolvedPath = Path.of(filePath).toAbsolutePath();
        String textureHandle = resolvedPath.toString();
        if (this.pathToHandle.containsKey(textureHandle)) {
            return this.pathToHandle.get(textureHandle);
        }
        try {
            File textureFile = resolvedPath.toFile();
            BufferedImage texture = ImageIO.read(textureFile);
            this.pathToHandle.put(textureHandle, textureHandle);
            this.handleToTexture.put(textureHandle, texture);
        } catch (IllegalArgumentException | IOException e) {
            throw new TextureNotFoundException("The requested path could not be loaded!", e);
        }
        return textureHandle;
    }

    @Override
    public Image getTextureForHandle(String handle) {
        return this.handleToTexture.get(handle);
    }
}

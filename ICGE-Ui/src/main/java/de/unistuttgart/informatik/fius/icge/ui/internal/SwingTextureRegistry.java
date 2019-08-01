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
import java.util.HashMap;
import java.util.Map;

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
        // TODO load image
        return resourceName;
    }

    @Override
    public String loadTextureFromFile(String filePath) {
        // TODO load image
        return filePath;
    }

    @Override
    public Image getTextureForHandle(String handle) {
        return this.handleToTexture.get(handle);
    }
}

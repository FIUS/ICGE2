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

import de.unistuttgart.informatik.fius.icge.ui.TextureRegistry;

/**
 * SwingTextureStorage
 *
 * @author Tobias Wältken
 * @version 1.0
 */
public abstract class SwingTextureStorage {

    public static String playIcon;
    public static String pauseIcon;
    public static String stopIcon;

    public static void init(TextureRegistry registry) {
        SwingTextureStorage.playIcon = registry.loadTextureFromResource(
            "textures/play.png",
            TextureRegistry.class::getResourceAsStream
        );
        SwingTextureStorage.pauseIcon = registry.loadTextureFromResource(
            "textures/pause.png",
            TextureRegistry.class::getResourceAsStream
        );
        SwingTextureStorage.stopIcon = registry.loadTextureFromResource(
            "textures/stop.png",
            TextureRegistry.class::getResourceAsStream
        );
    }
}

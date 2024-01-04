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
 * The SwingTextureStorage contains all icon ids needed by swing ui components
 *
 * @author Tobias Wältken
 * @version 1.0
 */
public abstract class StaticUiTextures {

    /** A play icon */
    public static String playIcon;
    /** A step icon */
    public static String stepIcon;
    /** A pause icon */
    public static String pauseIcon;
    /** A stop icon */
    @Deprecated
    public static String stopIcon;

    /** A arrow icon */
    public static String arrowIcon;
    /** A add icon */
    public static String addIcon;
    /** A sub icon */
    public static String subIcon;

    /**
     * This function loads all textures into the given texture registry and stores the icon ids
     *
     * @param registry
     *     The registry to load the icons in
     */
    public static void load(final TextureRegistry registry) {
        StaticUiTextures.playIcon = registry.loadTextureFromResource("textures/play.png", TextureRegistry.class::getResourceAsStream);
        StaticUiTextures.stepIcon = registry.loadTextureFromResource("textures/step.png", TextureRegistry.class::getResourceAsStream);
        StaticUiTextures.pauseIcon = registry.loadTextureFromResource("textures/pause.png", TextureRegistry.class::getResourceAsStream);
        StaticUiTextures.stopIcon = registry.loadTextureFromResource("textures/stop.png", TextureRegistry.class::getResourceAsStream);

        StaticUiTextures.arrowIcon = registry.loadTextureFromResource("textures/arrow.png", TextureRegistry.class::getResourceAsStream);
        StaticUiTextures.addIcon = registry.loadTextureFromResource("textures/add.png", TextureRegistry.class::getResourceAsStream);
        StaticUiTextures.subIcon = registry.loadTextureFromResource("textures/sub.png", TextureRegistry.class::getResourceAsStream);
    }
}

/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.example.mario;

import de.unistuttgart.informatik.fius.icge.simulation.Direction;
import de.unistuttgart.informatik.fius.icge.ui.TextureRegistry;
import de.unistuttgart.informatik.fius.icge.ui.exception.TextureNotFoundException;


/**
 * The enum for all builtin mario textures.
 * 
 * @author Tim Neumann
 */
public enum Texture {
    /** The "missing texture" texture */
    MISSING("missing.png"),
    /** The normal wall texture */
    WALL("wall/wall-default.png"),
    /** The default mario textures */
    MARIO("mario/mario-east-0.png", "mario/mario-south-0.png", "mario/mario-west-0.png", "mario/mario-north-0.png"),
    /** The coin texture */
    COIN("coin/coin-default.png");
    
    //for directional textures this is east
    private final TextureInfo info;
    
    private TextureInfo southInfo = null;
    
    private TextureInfo westInfo = null;
    
    private TextureInfo northInfo = null;
    
    Texture(final String textureLocation) {
        this.info = new TextureInfo(textureLocation);
    }
    
    Texture(
            final String textureEastLocation, final String textureSouthLocation, final String textureWestLocation,
            final String textureNorthLocation
    ) {
        this.info = new TextureInfo(textureEastLocation);
        this.southInfo = new TextureInfo(textureSouthLocation);
        this.westInfo = new TextureInfo(textureWestLocation);
        this.northInfo = new TextureInfo(textureNorthLocation);
    }
    
    private void load(final TextureRegistry registry, final TextureInfo infoToLoad) {
        try {
            infoToLoad.textureHandle = registry
                    .loadTextureFromResource("textures/" + infoToLoad.textureLocation, Texture.class::getResourceAsStream);
        } catch (final TextureNotFoundException e) {
            System.out.println("Could not find texture:");
            e.printStackTrace();
            infoToLoad.textureHandle = registry
                    .loadTextureFromResource("textures/" + MISSING.info.textureLocation, Texture.class::getResourceAsStream);
        }
    }
    
    /**
     * Load this texture with the given registry
     * 
     * @param registry
     *     The registry to load the texture with.
     * @throws TextureNotFoundException
     *     if neither the correct nor the "missing texture" texture can be found
     */
    public void load(final TextureRegistry registry) {
        this.load(registry, this.info);
        if (this.southInfo != null) {
            this.load(registry, this.southInfo);
        }
        if (this.westInfo != null) {
            this.load(registry, this.westInfo);
        }
        if (this.northInfo != null) {
            this.load(registry, this.northInfo);
        }
    }
    
    /**
     * Get the handle of this texture.
     * <p>
     * The texture must be loaded for this. ({@link #load(TextureRegistry)} needs to have been called before)
     * </p>
     * 
     * @return the texture handle of this
     * @throws IllegalStateException
     *     if the texture was not loaded before.
     */
    public String getHandle() {
        if (this.info.textureHandle == null) throw new IllegalStateException("Need to be loaded first");
        return this.info.textureHandle;
    }
    
    /**
     * Get the handle of this texture for the given direction.
     * <p>
     * The texture must be loaded for this. ({@link #load(TextureRegistry)} needs to have been called before)
     * </p>
     * <p>
     * If no texture is available for this direction, the default texture is returned.
     * </p>
     * 
     * @param direction
     *     the to get the handle for.
     * @return the correct texture handle
     * @throws IllegalStateException
     *     if the texture was not loaded before.
     */
    public String getHandle(final Direction direction) {
        if (this.info.textureHandle == null) throw new IllegalStateException("Need to be loaded first");
        switch (direction) {
            case EAST:
                break;
            case SOUTH:
                if (this.southInfo != null) return this.southInfo.textureHandle;
                break;
            case WEST:
                if (this.westInfo != null) return this.westInfo.textureHandle;
                break;
            case NORTH:
                if (this.northInfo != null) return this.northInfo.textureHandle;
                break;
            default:
                break;
        }
        return this.info.textureHandle;
    }
    
    private class TextureInfo {
        final String textureLocation;
        String       textureHandle;
        
        TextureInfo(final String location) {
            this.textureLocation = location;
        }
    }
}

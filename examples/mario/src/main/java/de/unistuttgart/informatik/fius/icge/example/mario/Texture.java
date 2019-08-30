/*
 * ICGE-Example-Mario
 *
 * TODO: Project Beschreibung
 *
 * @author Tim Neumann
 * @version 1.0.0
 *
 */
package de.unistuttgart.informatik.fius.icge.example.mario;

import de.unistuttgart.informatik.fius.icge.ui.TextureNotFoundException;
import de.unistuttgart.informatik.fius.icge.ui.TextureRegistry;

/**
 * The enum for all builtin mario textures.
 * 
 * @author Tim Neumann
 */
public enum Texture {
    /** The "missing texture" texture */
    MISSING("missing.png"),
    /** The normal wall texture */
    WALL("wall/wall-default.png");
    private final String textureLocation;
    private String textureHandle = null;

    Texture(String textureLocation) {
        this.textureLocation = textureLocation;
    }

    /**
     * Load this texture with the given registry
     * 
     * @param registry
     *            The registry to load the texture with.
     * @throws TextureNotFoundException
     *             if neither the correct nor the "missing texture" texture can be found
     */
    public void load(TextureRegistry registry) {
        try {
            this.textureHandle = registry.loadTextureFromResource("textures/" + this.textureLocation, Texture.class::getResourceAsStream);
        } catch (TextureNotFoundException e) {
            this.textureHandle = registry
                    .loadTextureFromResource("textures/" + MISSING.textureLocation, Texture.class::getResourceAsStream);
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
     *             if the texture was not loaded before.
     */
    public String getHandle() {
        if (this.textureHandle == null) throw new IllegalStateException("Need to be loaded first");
        return this.textureHandle;
    }
}

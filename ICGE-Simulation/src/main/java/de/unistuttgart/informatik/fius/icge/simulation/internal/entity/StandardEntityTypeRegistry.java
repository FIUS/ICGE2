/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 *
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.internal.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;
import de.unistuttgart.informatik.fius.icge.simulation.entity.EntityTypeRegistry;
import de.unistuttgart.informatik.fius.icge.simulation.exception.ElementExistsException;
import de.unistuttgart.informatik.fius.icge.ui.exception.ListenerSetException;


/**
 * Standard EntityTypeRegistry implementation.
 *
 * @author Fabian Bühler
 */
public class StandardEntityTypeRegistry implements EntityTypeRegistry {

    private final Map<String, Supplier<? extends Entity>> typeToEntityFactory = new HashMap<>();
    private final Map<String, String>                     typeToTextureHandle = new HashMap<>();

    private EntityRegisteredListener entityRegisteredListener;

    @Override
    public void registerEntityType(final String typeName, final String textureHandle, final Class<? extends Entity> entityType) {
        if (entityType == null) throw new IllegalArgumentException("Entity type class object cannot be null!");
        final Supplier<? extends Entity> entityFactory = () -> {
            try {
                return entityType.getDeclaredConstructor().newInstance();
            } catch (
                    NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e
            ) {
                e.printStackTrace();
                // could not instantiate a new entity
                return null;
            }
        };
        this.registerEntityType(typeName, textureHandle, entityFactory);
    }

    @Override
    public synchronized void registerEntityType(
            final String typeName, final String textureHandle, final Supplier<? extends Entity> entityFactory
    ) {
        if ((typeName == null) || typeName.equals("")) throw new IllegalArgumentException("Type name cannot be null or empty!");
        if ((textureHandle == null) || textureHandle.equals(""))
            throw new IllegalArgumentException("Texture handle cannot be null or empty!");
        if (entityFactory == null) throw new IllegalArgumentException("Entity factory cannot be null!");
        if (entityFactory.get() == null) throw new IllegalArgumentException("Unable to instantiate a new entity!");
        if (this.typeToEntityFactory.containsKey(typeName)) throw new ElementExistsException();
        this.typeToEntityFactory.put(typeName, entityFactory);
        this.typeToTextureHandle.put(typeName, textureHandle);

        if (this.entityRegisteredListener != null) {
            this.entityRegisteredListener.entityWasRegistered(typeName, textureHandle);
        }
    }

    @Override
    public Set<String> getRegisteredEntityTypes() {
        return this.typeToEntityFactory.keySet();
    }

    @Override
    public String getTextureHandleOfEntityType(final String typeName) {
        if ((typeName == null) || typeName.equals("")) throw new IllegalArgumentException("Type name cannot be null or empty!");
        return this.typeToTextureHandle.get(typeName);
    }

    @Override
    public Entity getNewEntity(final String typeName) {
        if ((typeName == null) || typeName.equals("")) throw new IllegalArgumentException("Type name cannot be null or empty!");
        final Supplier<? extends Entity> entityFactory = this.typeToEntityFactory.get(typeName);
        if (entityFactory == null) return null;
        return entityFactory.get();
    }

    /**
     * Set an entity selector listener that gets informed about all entity types added.
     *
     * @param listener
     *     the listener to set; use null to remove listener
     * @throws ListenerSetException
     *     if the listener is already set and the provided listener is not {@code null}.
     */
    public synchronized void setEntityRegisteredListener(final EntityRegisteredListener listener) {
        if ((this.entityRegisteredListener == null) || (listener == null)) {
            if (listener != null) {
                this.typeToEntityFactory.keySet().stream().forEachOrdered((typeName) -> {
                    listener.entityWasRegistered(typeName, this.typeToTextureHandle.get(typeName));
                });
            }
            this.entityRegisteredListener = listener;
        } else throw new ListenerSetException();
    }

    /**
     * Remove the set entity selector listener that gets informed about all entity types added.
     */
    public synchronized void removeEntityRegisteredListener() {
        this.entityRegisteredListener = null;
    }

    /**
     * The interface for when an entity is registered
     */
    public interface EntityRegisteredListener {
        /**
         * Called when an entity was registered at the registry.
         *
         * @param entityName
         *     The name of the new entity
         * @param textureHandle
         *     The texture handle of the new entity
         */
        void entityWasRegistered(String entityName, String textureHandle);
    }
}

/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.entity;

import java.util.Set;
import java.util.function.Supplier;

import de.unistuttgart.informatik.fius.icge.simulation.exception.ElementExistsException;


/**
 * The interface of the EntityTypeRegistry.
 *
 * @author Fabian Bühler
 */
public interface EntityTypeRegistry {
    
    /**
     * Register a new entity type by name.
     * <p>
     * This method attempts to build a {@code Supplier} function out of the provided class object. The {@code Supplier}
     * function will be called once to test its functionality.
     *
     * @param typeName
     *     the name of the entity type
     * @param textureHandle
     *     the texture handle to use as an icon for this type
     * @param entityType
     *     the class object of the entity type
     * @throws IllegalArgumentException
     *     if an argument is null
     * @throws IllegalArgumentException
     *     if the built {@code Supplier} function could not instantiate an entity
     * @throws ElementExistsException
     *     if the name is already used
     */
    void registerEntityType(final String typeName, final String textureHandle, Class<? extends Entity> entityType);
    
    /**
     * Register a new entity type by name.
     * <p>
     * The entityFactory will be called once to test its functionality.
     *
     * @param typeName
     *     the name of the entity type
     * @param textureHandle
     *     the texture handle to use as an icon for this type
     * @param entityFactory
     *     supplier function to create new instances of this entity type
     * @throws IllegalArgumentException
     *     if an argument is null
     * @throws IllegalArgumentException
     *     if the entityFactory could not instantiate an entity
     * @throws ElementExistsException
     *     if the name is already used
     */
    void registerEntityType(final String typeName, final String textureHandle, Supplier<? extends Entity> entityFactory);
    
    /**
     * Get a set of all currently registered entity types.
     *
     * @return registered entity types
     */
    Set<String> getRegisteredEntityTypes();
    
    /**
     * Get the texture handle of a registered entity type.
     *
     * @param typeName
     *     the name of the registered entity type
     * @return the texture handle for the given entity type
     * @throws IllegalArgumentException
     *     if an argument is null
     */
    String getTextureHandleOfEntityType(final String typeName);
    
    /**
     * Get a new Entity instance of a registered entity type.
     *
     * @param typeName
     *     the name of the registered entity type
     * @return the newly created Entity instance
     * @throws IllegalArgumentException
     *     if an argument is null
     */
    Entity getNewEntity(final String typeName);
}

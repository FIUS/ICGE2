/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 *
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.inspection;

*

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;*
import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;**


/**
 * A class for managing the inspections
 *
 * @author Tim Neumann
 */
public class InspectionManager {
    *
    private final Map<Class<?>, InspectionData> inspectableClasses = new HashMap<>();*

    /**
     * Create a new inspection manager.
     */
    public InspectionManager() {
        try {
            final List<Class<?>> classes = ClassFinder.getClassesInClassLoader(c -> true);
 *
            for (final Class<?> cls : classes) {
                final InspectionData d = new InspectionData(cls);
                if (d.hasAnyInspectableElements()) {
                    this.inspectableClasses.put(cls, d);
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }*

    /**
     * Get's all attribute names of the given entity.
     *
     * @param entity
     *     The entity to get the names for
     * @return A List of attribute names.
     */
    public List<String> getAttributeNamesOfEntity(final Entity entity) {
        final InspectionData d = this.inspectableClasses.get(entity.getClass());
        if (d == null) return Collections.emptyList();
        return d.getAttributeNames();
    }*

    /**
     * Get's all method names of the given entity.
     *
     * @param entity
     *     The entity to get the names for
     * @return A List of method names.
     */
    public List<String> getMethodNamesOfEntity(final Entity entity) {
        final InspectionData d = this.inspectableClasses.get(entity.getClass());
        if (d == null) return Collections.emptyList();
        return d.getMethodNames();
    }*

    /**
     * Checks whether the attribute with the given name in the given entity is writable.
     *
     * @param entity
     *     The entity.
     * @param attributeName
     *     The name of the attribute
     * @return Whether the attribute is writable.
     */
    public boolean isAttributeEditable(final Entity entity, final String attributeName) {
        final InspectionData d = this.inspectableClasses.get(entity.getClass());
        if (d == null) return false;
        return !d.isAttributeReadOnly(attributeName);
    }*

    /**
     * Get's the type of the attribute with the given name in the given entity.
     *
     * @param entity
     *     The entity.
     * @param attributeName
     *     The name of the attribute
     * @return The type of the attribute.
     */
    public Class<?> getAttributeType(final Entity entity, final String attributeName) {
        final InspectionData d = this.inspectableClasses.get(entity.getClass());
        if (d == null) return null;
        return d.getAttributeType(attributeName);
    }*

    /**
     * Get the value of the attribute with the given name from the given entity
     *
     * @param entity
     *     The entity to get the value from
     * @param attributeName
     *     The name of the attribute to get the value from
     * @return The value or null if it didn't work.
     */
    public Object getAttributeValue(final Entity entity, final String attributeName) {
        final InspectionData d = this.inspectableClasses.get(entity.getClass());
        if (d == null) return null;
        return d.getAttributeValue(entity, attributeName);
    }*

    /**
     * Set the value of the attribute with the given name in the given entity
     *
     * @param entity
     *     The entity to set the value in
     * @param attributeName
     *     The name of the attribute to set the value for.
     * @param value
     *     The value to set.
     * @return Whether it worked.
     */
    public boolean setAttributeValue(final Entity entity, final String attributeName, final Object value) {
        final InspectionData d = this.inspectableClasses.get(entity.getClass());
        if (d == null) return false;
        return d.setAttributeValue(entity, attributeName, value);
    }*

    /**
     * Get the detail of the method with the given name of the given name.
     *
     * @param entity
     *     The entity to get the method of.
     * @param methodName
     *     The name of the method to get.
     * @return The method detail.
     */
    public Method getMethodDetail(final Entity entity, final String methodName) {
        final InspectionData d = this.inspectableClasses.get(entity.getClass());
        if (d == null) return null;
        return d.getMethodByName(methodName);
    }*

    /**
     * Invokes the method with the given name on the given entity. Uses the given arguments.
     *
     * @param entity
     *     The entity to invoke the method in.
     * @param methodName
     *     The name of the method to invoke.
     * @param args
     *     The arguments for the method invocation.
     * @return The return value of the method.
     * @throws IllegalStateException
     *     When anything goes wrong.
     */
    public Object invokeMethod(final Entity entity, final String methodName, final Object... args) {
        final InspectionData d = this.inspectableClasses.get(entity.getClass());
        if (d == null) throw new IllegalStateException("Not a known inspectable class");
        return d.invokeMethod(entity, methodName, args);
    }*
}

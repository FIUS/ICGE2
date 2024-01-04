/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 *
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * A list which can contain and return multiple types.
 *
 * @author Tim Neumann
 * @param <P>
 *     The parent type of all types contained in this list.
 */
public class MultiTypedList<P> {

    private final HashMap<Class<? extends P>, List<P>> items = new HashMap<>();

    /**
     * Get the relevant list for the given type.
     *
     * @param type
     *     The type to get the list for
     * @return an Optional with the list if it exists, otherwise an empty Optional
     */
    private <T extends P> Optional<List<T>> getRelevantListGeneric(final Class<? extends T> type) {
        final List<? extends P> list = this.items.get(type);
        //We need this one unchecked cast.
        //But nothing can happen here, because the HashMap always contains lists of the type of the key
        //And therefore we have here a list of the type which is the parameter, which extends T
        @SuppressWarnings("unchecked")
        final var listOfCorrectType = (List<T>) list;
        return Optional.ofNullable(listOfCorrectType);
    }

    private Class<? extends P> getClass(final P o) {
        //Actually a cast should not be necessary here.
        //Should be totally safe, because o is of type P, so o.getClass() should extend P.
        @SuppressWarnings("unchecked")
        final var type = (Class<? extends P>) o.getClass();
        return type;
    }

    /**
     * Get the relevant list for the given object.
     *
     * @param o
     *     The object to get the list for
     * @return an Optional with the list if it exists, otherwise an empty Optional
     */
    private Optional<List<P>> getRelevantList(final P o) {
        return this.getRelevantListGeneric(this.getClass(o));
    }

    /**
     * Get the relevant list for the given entity and create it if it is not there.
     *
     * @param o
     *     The object to get the list for
     * @return The list for the given object
     */
    private List<P> getRelevantListAndCreate(final P o) {
        final var type = this.getClass(o);
        if (this.items.containsKey(type)) return this.items.get(type);

        final List<P> list = new ArrayList<>();
        this.items.put(type, list);
        return list;
    }

    /**
     * Add the given object to this list.
     *
     * @param o
     *     The object to add
     */
    public synchronized void add(final P o) {
        this.getRelevantListAndCreate(o).add(o);
    }

    /**
     * Check whether this list contains the given object.
     *
     * @param o
     *     The object to check for
     * @return Whether this list contains the given object
     */
    public synchronized boolean contains(final P o) {
        final var opt = this.getRelevantList(o);
        if (opt.isEmpty()) return false;
        return opt.get().contains(o);
    }

    /**
     * Return {@code true} iff the list contains no objects.
     *
     * @return {@code true} if empty
     */
    public synchronized boolean isEmpty() {
        return this.items.isEmpty();
    }

    /**
     * Remove the given object from this list.
     *
     * @param o
     *     The object to remove
     * @return Whether this cell contained the given object
     */
    public synchronized boolean remove(final P o) {
        final var opt = this.getRelevantList(o);
        if (opt.isEmpty()) return false;
        return opt.get().remove(o);
    }

    /**
     * Get all objects in this list of the given type.
     *
     * @param <T>
     *     The generic type to return the objects as
     * @param type
     *     The type of the objects to get
     * @param includeSubclasses
     *     Whether to include the subclasses of the given type
     * @return A list of all matching objects
     */
    public synchronized <T extends P> List<T> get(final Class<? extends T> type, final boolean includeSubclasses) {
        if (!includeSubclasses) return this.<T>getRelevantListGeneric(type).orElse(List.of());

        final List<T> result = new ArrayList<>();

        for (final Class<? extends P> storedType : this.items.keySet()) {
            if (type.isAssignableFrom(storedType)) {
                final Class<? extends T> storedTypeAsSubOfRequested = storedType.asSubclass(type);
                final List<T> listForStoredType = this.<T>getRelevantListGeneric(storedTypeAsSubOfRequested).orElse(List.of());
                result.addAll(listForStoredType);
            }
        }

        return result;
    }

    /**
     * @return a list of stored types.
     */
    public synchronized Set<Class<? extends P>> getStoredTypes() {
        return this.items.keySet();
    }
}

/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.tasks;

import java.util.NoSuchElementException;
import java.util.Set;

import de.unistuttgart.informatik.fius.icge.simulation.exception.ElementExistsException;


/**
 * The interface of the TaskRegistry.
 *
 * @author Fabian Bühler
 */
public interface TaskRegistry {
    
    /**
     * Register a Task with the given name.
     * 
     * @param name
     *     The name of the task; must not be null; must be unique
     * @param task
     *     the task to register; must not be null
     * 
     * @throws IllegalArgumentException
     *     if an argument is null
     * @throws ElementExistsException
     *     if the name is already used
     */
    public void registerTask(String name, Task task);
    
    /**
     * Get the names of all registered tasks.
     * 
     * @return a set of the names of all registered tasks
     */
    public Set<String> getTaskNameSet();
    
    /**
     * Get the task for a given name.
     * 
     * @param name
     *     The name to get the task for; must not be null
     * @return The task for the given name
     * @throws IllegalArgumentException
     *     if an argument is null
     * @throws NoSuchElementException
     *     if the given name is not registered
     */
    public Task getTask(String name);
}

/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.internal.tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.unistuttgart.informatik.fius.icge.simulation.exception.ElementExistsException;
import de.unistuttgart.informatik.fius.icge.simulation.tasks.Task;
import de.unistuttgart.informatik.fius.icge.simulation.tasks.TaskRegistry;
import de.unistuttgart.informatik.fius.icge.ui.exception.ListenerSetException;
import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy.TaskSelectorListener;


/**
 * Standard implementation of the task registry.
 * 
 * @author Fabian Bühler
 */
public class StandardTaskRegistry implements TaskRegistry {
    
    private Map<String, Task> taskMap = new HashMap<>();
    
    private TaskSelectorListener changeListener;
    
    @Override
    public void registerTask(String name, Task task) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null!");
        }
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null!");
        }
        if (this.taskMap.containsKey(name)) {
            throw new ElementExistsException("A task with the name \"" + name + "\" already exists");
        }
        this.taskMap.put(name, task);
        if (this.changeListener != null) {
            this.changeListener.setElements(this.getTaskNameSet());
        }
    }
    
    @Override
    public Set<String> getTaskNameSet() {
        return this.taskMap.keySet();
    }
    
    @Override
    public Task getTask(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null!");
        }
        return this.taskMap.get(name);
    }
    
    /**
     * Sets the changeListener.
     * 
     * @param listener
     *     the listener to set; use null to remove listener
     * @throws ListenerSetException
     *     if the listener is already set and the provided listener is not {@code null}.
     */
    public void setTaskSelectorListener(TaskSelectorListener listener) {
        if ((this.changeListener == null) || (listener == null)) {
            this.changeListener = listener;
            this.changeListener.enable();
        } else throw new ListenerSetException();
    }
    
}

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

import java.lang.reflect.InvocationTargetException;

import de.unistuttgart.informatik.fius.icge.simulation.Simulation;
import de.unistuttgart.informatik.fius.icge.simulation.tasks.Task;
import de.unistuttgart.informatik.fius.icge.simulation.tasks.TaskRunner;


/**
 * The standard implementation of {@link TaskRunner}.
 * 
 * @author Tim Neumann
 */
public class StandardTaskRunner implements TaskRunner {
    
    @Override
    public boolean runTask(final Class<? extends Task> taskToRun, final Simulation sim) {
        if ((taskToRun == null) || (sim == null)) throw new IllegalArgumentException("Argument is null.");
        
        try {
            final Task task = taskToRun.getDeclaredConstructor().newInstance();
            task.prepare(sim);
            task.solve();
            return task.verify();
        } catch (
                InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e
        ) {
            throw new IllegalArgumentException("Failed to instantiate.", e);
        }
    }
}

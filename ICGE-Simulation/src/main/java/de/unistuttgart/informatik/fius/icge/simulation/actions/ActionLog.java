/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.actions;

import java.util.List;

import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;


/**
 * A log for all {@link Action}s.
 * 
 * @author Tim Neumann
 */
public interface ActionLog {
    
    /**
     * Get all actions which were logged.
     * <p>
     * Warning: No order is guaranteed.
     * </p>
     * 
     * @return A list of all actions logged
     */
    List<Action> getAllActions();
    
    /**
     * Get all actions which were logged of the given type.
     * 
     * @param <T>
     *     The generic type to return the actions as
     * @param type
     *     The type of the actions to get
     * @param includeSubclasses
     *     Whether to include the subclasses of the given type
     * @return A list of all matching actions
     */
    <T extends Action> List<T> getActionsOfType(final Class<? extends T> type, final boolean includeSubclasses);
    
    /**
     * Get all actions which were logged and were caused by the given entity.
     * 
     * @param entity
     *     The entity to get actions for
     * @return A list of all matching actions
     */
    List<EntityAction> getAllActionsOfEntity(Entity entity);
    
    /**
     * Get all actions which were logged of the given type and were caused by the given entity.
     * 
     * @param <T>
     *     The generic type to return the actions as
     * @param entity
     *     The entity to get actions for
     * @param type
     *     The type of the actions to get
     * @param includeSubclasses
     *     Whether to include the subclasses of the given type
     * @return A list of all matching actions
     */
    <T extends EntityAction> List<T> getActionsOfTypeOfEntity(
            Entity entity, final Class<? extends T> type, final boolean includeSubclasses
    );
    
    /**
     * Log an action.
     * 
     * @param actionToLog
     *     The action to log
     */
    void logAction(Action actionToLog);
    
    /**
     * Set ActionLog console output (default: true)
     *
     * @param enable
     *     true: log to console, false: do not log to console
     */
    void setConsoleOutput(boolean enable);
    
    /**
     * Get ActionLog console output status (true: log to console, false: do not log to console)
     */
    boolean getConsoleOutput();
    
}

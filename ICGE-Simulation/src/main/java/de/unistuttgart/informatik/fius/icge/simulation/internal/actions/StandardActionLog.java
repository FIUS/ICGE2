/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.internal.actions;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.unistuttgart.informatik.fius.icge.log.Logger;
import de.unistuttgart.informatik.fius.icge.simulation.MultiTypedList;
import de.unistuttgart.informatik.fius.icge.simulation.actions.Action;
import de.unistuttgart.informatik.fius.icge.simulation.actions.ActionLog;
import de.unistuttgart.informatik.fius.icge.simulation.actions.EntityAction;
import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;


/**
 * The standard implementation of {@link ActionLog}.
 * 
 * @author Tim Neumann
 */
public class StandardActionLog implements ActionLog {
    
    private MultiTypedList<Action> actions = new MultiTypedList<>();
    
    private Map<Entity, MultiTypedList<EntityAction>> entityActions = new HashMap<>();
    
    @Override
    public List<Action> getAllActions() {
        return this.actions.get(Action.class, true);
    }
    
    @Override
    public <T extends Action> List<T> getActionsOfType(Class<? extends T> type, boolean includeSubclasses) {
        return this.actions.get(type, includeSubclasses);
    }
    
    @Override
    public List<EntityAction> getAllActionsOfEntity(Entity entity) {
        MultiTypedList<EntityAction> list = this.entityActions.get(entity);
        if (list == null) return Collections.emptyList();
        return list.get(EntityAction.class, true);
    }
    
    @Override
    public <T extends EntityAction> List<T> getActionsOfTypeOfEntity(Entity entity, Class<? extends T> type, boolean includeSubclasses) {
        MultiTypedList<EntityAction> list = this.entityActions.get(entity);
        if (list == null) return Collections.emptyList();
        return list.get(type, includeSubclasses);
    }
    
    @Override
    public void logAction(Action actionToLog) {
        if (actionToLog instanceof EntityAction) {
            EntityAction entityAction = ((EntityAction) actionToLog);
            Entity entity = entityAction.getEntity();
            if (!this.entityActions.containsKey(entity)) {
                this.entityActions.put(entity, new MultiTypedList<>());
            }
            this.entityActions.get(entity).add(entityAction);
        }
        this.actions.add(actionToLog);
        Logger.simout.println(actionToLog.getDescription());
    }
    
}

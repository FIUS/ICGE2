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

    private final Object actionsLock       = new Object();
    private final Object entityActionsLock = new Object();

    private final MultiTypedList<Action> actions = new MultiTypedList<>();

    private final Map<Entity, MultiTypedList<EntityAction>> entityActions = new HashMap<>();

    private boolean consoleLog = true;

    @Override
    public List<Action> getAllActions() {
        synchronized (this.actionsLock) {
            return this.actions.get(Action.class, true);
        }
    }

    @Override
    public <T extends Action> List<T> getActionsOfType(final Class<? extends T> type, final boolean includeSubclasses) {
        synchronized (this.actionsLock) {
            return this.actions.get(type, includeSubclasses);
        }
    }

    @Override
    public List<EntityAction> getAllActionsOfEntity(final Entity entity) {
        synchronized (this.entityActionsLock) {
            final MultiTypedList<EntityAction> list = this.entityActions.get(entity);
            if (list == null) return Collections.emptyList();
            return list.get(EntityAction.class, true);
        }
    }

    @Override
    public <T extends EntityAction> List<T> getActionsOfTypeOfEntity(
            final Entity entity, final Class<? extends T> type, final boolean includeSubclasses
    ) {
        synchronized (this.entityActionsLock) {
            final MultiTypedList<EntityAction> list = this.entityActions.get(entity);
            if (list == null) return Collections.emptyList();
            return list.get(type, includeSubclasses);
        }
    }

    @Override
    public void logAction(final Action actionToLog) {
        if (actionToLog instanceof EntityAction) {
            final EntityAction entityAction = ((EntityAction) actionToLog);
            final Entity entity = entityAction.getEntity();
            synchronized (this.entityActionsLock) {
                if (!this.entityActions.containsKey(entity)) {
                    this.entityActions.put(entity, new MultiTypedList<>());
                }
                this.entityActions.get(entity).add(entityAction);
            }
        }
        synchronized (this.actionsLock) {
            this.actions.add(actionToLog);
        }
        if (this.consoleLog) {
            System.out.println(actionToLog.getDescription());
        }
    }

    @Override
    public void setConsoleOutput(boolean enable) {
        this.consoleLog = enable;
    }

    @Override
    public boolean getConsoleOutput() {
        return this.consoleLog;
    }
}

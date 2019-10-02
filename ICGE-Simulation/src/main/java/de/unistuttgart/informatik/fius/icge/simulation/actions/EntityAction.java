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

import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;


/**
 * A action originating from an entity.
 * 
 * @author Tim Neumann
 */
public interface EntityAction extends Action {
    /**
     * @return the entity that caused this action.
     */
    Entity getEntity();
}

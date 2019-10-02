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

/**
 * A action which happens during the execution of a simulation.
 * 
 * @author Tim Neumann
 */
public interface Action {
    /**
     * @return the description of this action
     */
    String getDescription();
    
    /**
     * @return the number of the tick the action happened
     */
    int getTickNumber();
}

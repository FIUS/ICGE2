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
 * An action which happens during the execution of a simulation.
 *
 * @author Tim Neumann
 */
public abstract class Action {

    private final long tickNumber;

    /**
     * Create an action.
     *
     * @param tickNumber
     *     the tick the action happened at
     */
    public Action(final long tickNumber) {
        this.tickNumber = tickNumber;
    }

    /**
     * @return the description of this action
     */
    public abstract String getDescription();

    /**
     * @return the number of the simulation tick the action happened
     */
    public long getTickNumber() {
        return this.tickNumber;
    }

    @Override
    public String toString() {
        return this.getDescription();
    }
}

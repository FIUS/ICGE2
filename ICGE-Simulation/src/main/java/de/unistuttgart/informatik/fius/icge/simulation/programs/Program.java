/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 *
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.programs;

import de.unistuttgart.informatik.fius.icge.simulation.Simulation;
import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;


/**
 * The interface for programs that operate on entities.
 *
 * Programs can be run by using {@link Simulation#runProgram}.
 *
 * @param <E>
 *     The type of entities this program can operate on.
 */
public interface Program<E extends Entity> {
    /**
     * The entry method of the program.
     *
     * @param entity
     *     The entity this program operates on.
     */
    void run(E entity);
}

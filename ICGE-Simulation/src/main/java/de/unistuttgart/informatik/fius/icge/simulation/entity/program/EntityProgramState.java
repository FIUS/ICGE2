/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.entity.program;

/**
 * The state of a entity program
 * 
 * @author Tim Neumann
 */
public enum EntityProgramState {
    /** When the program has a program factory that creates new instances of this program. */
    IS_FACTORY,
    /** When the entity program is new and was not started yet. */
    NEW,
    /** When the entity program is currently running. */
    RUNNING,
    /** When the entity program finished nominally. */
    FINISHED,
    /** When the entity program was killed by the {@link EntityProgramRunner}. */
    KILLED
}

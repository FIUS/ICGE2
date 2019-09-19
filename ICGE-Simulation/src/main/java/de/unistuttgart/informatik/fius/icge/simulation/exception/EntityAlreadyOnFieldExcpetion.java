/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.exception;

/**
 * This exception indicates that an action is illegal, because the subject entity is already in on the relevant
 * playfield.
 * 
 * @author Tim Neumann
 */
public class EntityAlreadyOnFieldExcpetion extends SimulationExcpetion {
    /**
     * generated
     */
    private static final long serialVersionUID = 3177203739377160043L;
    
    /**
     * Constructs a new EntityAlreadyOnFieldExcpetion with {@code null} as its detail message. The cause is not
     * initialized, and may subsequently be initialized by a call to {@link #initCause}.
     */
    public EntityAlreadyOnFieldExcpetion() {
        super();
    }
    
    /**
     * Constructs a new EntityAlreadyOnFieldExcpetion with the specified detail message. The cause is not initialized,
     * and may subsequently be initialized by a call to {@link #initCause}.
     *
     * @param message
     *     the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public EntityAlreadyOnFieldExcpetion(final String message) {
        super(message);
    }
    
    /**
     * Constructs a new EntityAlreadyOnFieldExcpetion with the specified detail message and cause.
     *
     * @param message
     *     the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause
     *     the cause (which is saved for later retrieval by the {@link #getCause()} method). (A {@code null} value is
     *     permitted, and indicates that the cause is nonexistent or unknown.)
     * @since 1.4
     */
    public EntityAlreadyOnFieldExcpetion(final String message, final Throwable cause) {
        super(message, cause);
    }
}

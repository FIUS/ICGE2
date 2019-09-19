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
 * This exception indicates that an entity program cannot be run.
 * 
 * @author Tim Neumann
 */
public class CannotCollectEntityException extends SimulationExcpetion {
    
    /**
     * generated
     */
    private static final long serialVersionUID = 6758366706458330276L;
    
    /**
     * Constructs a new CannotCollectEntityException with {@code null} as its detail message. The cause is not
     * initialized, and may subsequently be initialized by a call to {@link #initCause}.
     */
    public CannotCollectEntityException() {
        super();
    }
    
    /**
     * Constructs a new CannotCollectEntityException with the specified detail message. The cause is not initialized,
     * and may subsequently be initialized by a call to {@link #initCause}.
     *
     * @param message
     *     the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public CannotCollectEntityException(final String message) {
        super(message);
    }
    
    /**
     * Constructs a new CannotCollectEntityException with the specified cause and a detail message of
     * {@code (cause==null ? null : cause.toString())} (which typically contains the class and detail message of
     * {@code cause}). This constructor is useful for runtime exceptions that are little more than wrappers for other
     * throwables.
     *
     * @param cause
     *     the cause (which is saved for later retrieval by the {@link #getCause()} method). (A {@code null} value is
     *     permitted, and indicates that the cause is nonexistent or unknown.)
     * @since 1.4
     */
    public CannotCollectEntityException(final Throwable cause) {
        super(cause);
    }
    
    /**
     * Constructs a new CannotCollectEntityException with the specified detail message and cause.
     *
     * @param message
     *     the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause
     *     the cause (which is saved for later retrieval by the {@link #getCause()} method). (A {@code null} value is
     *     permitted, and indicates that the cause is nonexistent or unknown.)
     * @since 1.4
     */
    public CannotCollectEntityException(final String message, final Throwable cause) {
        super(message, cause);
    }
}

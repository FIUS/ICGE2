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
 * This exception indicates, that a entity could not move.
 * <p>
 * Possible reasons are: solid entity in the way
 * </p>
 *
 * @author Tim Neumann
 */
public class IllegalMoveException extends SimulationExcpetion {
    /**
     * generated
     */
    private static final long serialVersionUID = -4781899414191823139L;

    /**
     * Constructs a new IllegalMoveException with {@code null} as its detail message. The cause is not initialized, and
     * may subsequently be initialized by a call to {@link #initCause}.
     */
    public IllegalMoveException() {
        super();
    }

    /**
     * Constructs a new IllegalMoveException with the specified detail message. The cause is not initialized, and may
     * subsequently be initialized by a call to {@link #initCause}.
     *
     * @param message
     *     the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public IllegalMoveException(final String message) {
        super(message);
    }

    /**
     * Constructs a new IllegalMoveException with the specified detail message and cause. *
     *
     * @param message
     *     the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause
     *     the cause (which is saved for later retrieval by the {@link #getCause()} method). (A {@code null} value is
     *     permitted, and indicates that the cause is nonexistent or unknown.)
     * @since 1.4
     */
    public IllegalMoveException(final String message, final Throwable cause) {
        super(message, cause);
    }
}

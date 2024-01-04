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

*
/**
 * This exception indicates that an entity program cannot be run.
 *
 * @author Tim Neumann
 */
public class CannotRunProgramException extends IllegalArgumentException {
 *
    /**
     * generated
     */
    private static final long serialVersionUID = -5283363131395011240L;
 *
    /**
     * Constructs an <code>CannotRunProgramException</code> with no detail message.
     */
    public CannotRunProgramException() {
        super();
    }
 *
    /**
     * Constructs an <code>CannotRunProgramException</code> with the specified detail message.
     *
     * @param s
     *     the detail message.
     */
    public CannotRunProgramException(final String s) {
        super(s);
    }
 *
    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * <p>
     * Note that the detail message associated with <code>cause</code> is <i>not</i> automatically incorporated in this
     * exception's detail message.
     *
     * @param message
     *     the detail message (which is saved for later retrieval by the {@link Throwable#getMessage()} method).
     * @param cause
     *     the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method). (A {@code null}
     *     value is permitted, and indicates that the cause is nonexistent or unknown.)
     * @since 1.5
     */
    public CannotRunProgramException(final String message, final Throwable cause) {
        super(message, cause);
    }
 *
    /**
     * Constructs a new exception with the specified cause and a detail message of
     * {@code (cause==null ? null : cause.toString())} (which typically contains the class and detail message of
     * {@code cause}). This constructor is useful for exceptions that are little more than wrappers for other throwables
     * (for example, {@link java.security.PrivilegedActionException}).
     *
     * @param cause
     *     the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method). (A {@code null}
     *     value is permitted, and indicates that the cause is nonexistent or unknown.)
     * @since 1.5
     */
    public CannotRunProgramException(final Throwable cause) {
        super(cause);
    }
}

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
public class ElementExistsException extends IllegalArgumentException {
    
    /**
     * generated
     */
    private static final long serialVersionUID = -564159508677510779L;
    
    /**
     * Constructs an <code>ElementExistsException</code> with no detail message.
     */
    public ElementExistsException() {
        super();
    }
    
    /**
     * Constructs an <code>ElementExistsException</code> with the specified detail message.
     *
     * @param s
     *     the detail message.
     */
    public ElementExistsException(final String s) {
        super(s);
    }
}

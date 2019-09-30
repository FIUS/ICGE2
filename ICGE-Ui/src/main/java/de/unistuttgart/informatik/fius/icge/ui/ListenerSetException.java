/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.ui;

/**
 * The ListenerSetException is thrown if a set listener function in the SimulationProxy fails
 * 
 * @author Tobias Wältken
 * @version 1.0
 */
public class ListenerSetException extends IllegalArgumentException {
    private static final long serialVersionUID = -2620520061340158420L;
    
    /**
     * Default constructor
     */
    public ListenerSetException() {
        super("Can't set the given listener!");
    }
}

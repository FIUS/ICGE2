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

import de.unistuttgart.informatik.fius.icge.simulation.SimulationClock;


/**
 * This exception if thrown if a {@link SimulationClock} is already running
 *
 * @author Tobias Wältken
 * @version 1.0
 */
public class TimerAlreadyRunning extends IllegalStateException {
    private static final long serialVersionUID = 1715637086603245424L;
    
    /**
     * Default constructor
     */
    public TimerAlreadyRunning() {
        super("The Timer is already running, thus can't be started.");
    }
}

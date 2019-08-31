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


import java.io.OutputStream;


/**
 * A Console to show text output of the software
 *
 * @author Tobias Wältken
 * @version 1.0
 */
public interface Console {

    /**
     * Getter for the output stream of the simulation console
     *
     * @return Returns a output stream
     */
    public OutputStream getSimulationOutputStream();

    /**
     * Getter for the output stream of the system console
     *
     * @return Returns a output stream
     */
    public OutputStream getSystemOutputStream();
}

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

import java.util.concurrent.CompletableFuture;


/**
 * Info object holding a running program.
 * 
 * @author Fabian Bühler
 */
public interface RunningProgramInfo {
    
    /**
     * @return the state of this object; cannot be null
     */
    public EntityProgramState getState();
    
    /**
     * @return the future running the program
     */
    public CompletableFuture<Void> getFuture();
    
}

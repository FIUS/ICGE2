/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.entity;

import de.unistuttgart.informatik.fius.icge.simulation.Position;
import de.unistuttgart.informatik.fius.icge.ui.Drawable;


/**
 * The interface for an entity in the simulation of the ICGE.
 * 
 * @author Tim Neumann
 */
public interface Entity {
    /**
     * @return the position of this entity
     */
    Position getPosition();
    
    /**
     * @return the information required to draw this entity
     */
    Drawable getDrawInformation();
}

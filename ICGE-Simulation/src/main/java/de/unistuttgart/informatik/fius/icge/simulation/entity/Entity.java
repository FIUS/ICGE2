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

import de.unistuttgart.informatik.fius.icge.simulation.Playfield;
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
     * @return the information required to draw this entity; must <b>not</b> be <b>null</b>
     */
    Drawable getDrawInformation();
    
    /**
     * Method to initialize this entity after being added to the playfield.
     * <p>
     * This method needs to be called by the playfield directly after the entity was added.
     * </p>
     * 
     * @param playfield
     *     The playfield this entity was added to; must <b>not</b> be <b>null</b>
     * @throws IllegalArgumentException
     *     if the given playfield is null
     */
    void initOnPlayfield(Playfield playfield);
    
    /**
     * Method to tell the entity that it was removed from the playfield.
     * <p>
     * This Method must only be called by the playfield.
     * This method needs to be called by the playfield directly after the entity was removed.
     * </p>
     */
    void removeFromPlayfield();
}

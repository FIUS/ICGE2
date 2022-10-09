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
import de.unistuttgart.informatik.fius.icge.simulation.exception.EntityOnAnotherFieldException;
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
     * @return the amount of this entity
     */
    int getAmount();
    
    /**
     * @param amount 
     * 
     */
    void setAmount(int amount);
    
    /**
     * 
     */
    void incrementAmount();
    
    /**
     * 
     */
    void decrementAmount();
    
    /**
     * @return the information required to draw this entity; must <b>not</b> be <b>null</b>
     */
    Drawable getDrawInformation();
    
    /**
     * Method to initialize this entity after being added to the playfield.
     * <p>
     * This method should not be called from anywhere other than the playfield implementation.
     * </p>
     * <p>
     * This method needs to be called by the playfield directly before adding the entity to the field.
     * </p>
     * 
     * @param playfield
     *     The playfield this entity was added to; must <b>not</b> be <b>null</b>
     * 
     * @throws IllegalArgumentException
     *     if the given playfield is null
     * @throws EntityOnAnotherFieldException
     *     if the entity is already on a field
     */
    void initOnPlayfield(Playfield playfield);
    
}

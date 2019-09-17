/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.example.mario.entity;

import de.unistuttgart.informatik.fius.icge.example.mario.Texture;
import de.unistuttgart.informatik.fius.icge.simulation.entity.MovableEntity;


/**
 * The mario entity
 * 
 * @author Tim Neumann
 */
public class Mario extends MovableEntity {
    
    @Override
    protected String getTextureHandle() {
        return Texture.MARIO.getHandle(this.getLookingDirection());
    }
    
    @Override
    protected int getZPosition() {
        return 10;
    }
    
}

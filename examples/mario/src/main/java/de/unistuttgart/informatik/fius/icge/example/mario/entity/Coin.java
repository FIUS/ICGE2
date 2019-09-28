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
import de.unistuttgart.informatik.fius.icge.simulation.entity.BasicEntity;
import de.unistuttgart.informatik.fius.icge.simulation.entity.CollectableEntity;


/**
 * A coin
 * 
 * @author Tim Neumann
 */
public class Coin extends BasicEntity implements CollectableEntity {
    
    @Override
    protected String getTextureHandle() {
        return Texture.COIN.getHandle();
    }
    
    @Override
    protected int getZPosition() {
        return 2;
    }
    
}

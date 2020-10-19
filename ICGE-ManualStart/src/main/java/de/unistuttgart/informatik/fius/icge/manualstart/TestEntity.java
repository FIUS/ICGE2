/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.manualstart;

import de.unistuttgart.informatik.fius.icge.simulation.entity.GreedyEntity;


/**
 * Basic test entity.
 */
public class TestEntity extends GreedyEntity {
    
    /**
     * The texture handle used for the test entity.
     */
    public static String TEXTURE_HANDLE;
    
    @Override
    protected String getTextureHandle() {
        return TestEntity.TEXTURE_HANDLE;
    }
    
    @Override
    protected int getZPosition() {
        return 0;
    }
}

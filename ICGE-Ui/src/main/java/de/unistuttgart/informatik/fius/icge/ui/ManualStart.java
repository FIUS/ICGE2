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
 * A class to manually start the renderer.
 * 
 * @author Tim Neumann
 */
public class ManualStart {
    
    /**
     * @param args
     *     the command line arguments. Not used.
     */
    public static void main(String[] args) {
        UiManager manager = UiManagerFactory.createUiManager();
        manager.start();
    }
}

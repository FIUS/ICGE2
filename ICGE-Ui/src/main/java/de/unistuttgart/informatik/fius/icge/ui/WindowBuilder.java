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

public class WindowBuilder {
    
    private String  windowTitle    = "";
    private Window  window;
    private boolean hasBuiltWindow = false;
    
    public WindowBuilder() {
        
    }
    
    public void setTitle(String title) {
        if (title == null) {
            title = "";
        }
        this.windowTitle = title;
    }
    
    public void buildWindow() {
        this.hasBuiltWindow = true;
    }
    
    public Window getBuiltWindow() {
        return this.window;
    }
}

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

import java.util.function.Consumer;


/**
 * EntityInspectorEntry
 */
public class EntityInspectorEntry {
    
    private final String name;
    
    private final String type;
    
    private final String value;
    
    private final Consumer<String> callback;
    
    /**
     * Default constructor
     * 
     * @param name
     *     The name of the setting
     * @param type
     *     The type of the setting
     * @param value
     *     The start value of the setting
     * @param callback
     *     The labda wich gets called when the value changes
     */
    public EntityInspectorEntry(final String name, final String type, final String value, final Consumer<String> callback) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.callback = callback;
    }
    
    /**
     * Getter for the name
     * 
     * @return returns the name
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Getter for the type
     * 
     * @return returns the type
     */
    public String getType() {
        return this.type;
    }
    
    /**
     * Getter for the value
     * 
     * @return returns the value
     */
    public String getValue() {
        return this.value;
    }
    
    /**
     * This function runs the callback of the item
     * 
     * @param value
     *     The argument of the callback
     */
    public void runCallback(String value) {
        this.callback.accept(value);
    }
}

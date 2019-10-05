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

import java.util.function.BiConsumer;

/**
 * EntityInspectorEntry
 */
public class EntityInspectorEntry {

    private final String name;

    private final String type;

    private final String value;

    private final BiConsumer<String, String> callback;

    public EntityInspectorEntry(final String name, final String type, final String value, final BiConsumer<String, String> callback) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.callback = callback;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }

    public void runCallback(String id, String value) {
        this.callback.accept(id, value);
    }
}

/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 *
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.ui.internal;

/**
 * Enum to identify different standard styles for the ConsoleBufferedOutputStream
 *
 * @author David Ruff
 */
public enum OutputStyle {

    /** Standard Style for text from stdout */
    STANDARD,

    /** Style for text from stderr (e.g. errors) */
    ERROR
}

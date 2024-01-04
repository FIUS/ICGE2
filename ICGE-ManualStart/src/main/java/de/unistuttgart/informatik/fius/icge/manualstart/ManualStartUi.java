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

import de.unistuttgart.informatik.fius.icge.ui.GameWindow;
import de.unistuttgart.informatik.fius.icge.ui.WindowBuilder;


/**
 * A class to manually start the ui.
 *
 * @author Tim Neumann
 */
public class ManualStartUi {

    /**
     * Main entry point of the program
     *
     * @param args
     *     the command line arguments. Not used.
     */
    public static void main(final String[] args) {
        ManualStartUi.newStyleBuilder();
    }

    private static void newStyleBuilder() {
        final WindowBuilder wb = new WindowBuilder();
        wb.setTitle("Window Builder start!");
        wb.buildWindow();
        final GameWindow w = wb.getBuiltWindow();
        w.start();
    }
}

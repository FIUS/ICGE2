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

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import de.unistuttgart.informatik.fius.icge.ui.Console;

/**
 * SwingConsole
 */
public class SwingConsole extends JTabbedPane implements Console {

    public SwingConsole() {
        super(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        addTab("TextArea", new JScrollPane(new JTextArea()));
    }
}

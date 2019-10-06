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

import java.awt.Font;
import java.awt.Dimension;
import java.io.OutputStream;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultCaret;

import de.unistuttgart.informatik.fius.icge.ui.Console;


/**
 * The SwingConsole allows the user to see text output from the system or the simulation itself.
 *
 * @author Tobias Wältken
 * @version 1.0
 */
public class SwingConsole extends JTabbedPane implements Console {
    private static final long serialVersionUID = 5100186594058483257L;
    
    private JTextArea simulationConsole;
    private JTextArea systemConsole;
    
    private ConsoleBufferedOutputStream simulationOutputStream;
    private ConsoleBufferedOutputStream systemOutputStream;
    
    /**
     * Default constructor
     */
    public SwingConsole() {
        super(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        {   // Setup simulation console
            this.simulationConsole = new JTextArea();
            this.simulationConsole.setEditable(false);
            this.simulationConsole.setFont(new Font("monospaced", Font.PLAIN, 12));
            final DefaultCaret caret = (DefaultCaret) this.simulationConsole.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            this.simulationOutputStream = new ConsoleBufferedOutputStream(this.simulationConsole);
        }
        {   // Setup system console
            this.systemConsole = new JTextArea();
            this.systemConsole.setEditable(false);
            this.systemConsole.setFont(new Font("monospaced", Font.PLAIN, 12));
            final DefaultCaret caret = (DefaultCaret) this.systemConsole.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            this.systemOutputStream = new ConsoleBufferedOutputStream(this.systemConsole);
        }
        
        // Add consoles to the TabbedPane
        this.addTab("Simulation", new JScrollPane(this.simulationConsole));
        this.addTab("System", new JScrollPane(this.systemConsole));
        this.addTab("Notes", new JScrollPane(new JTextArea("Your place for non permanent notes!\n")));
    }
    
    @Override
    public OutputStream getSimulationOutputStream() {
        return this.simulationOutputStream;
    }
    
    @Override
    public OutputStream getSystemOutputStream() {
        return this.systemOutputStream;
    }
    
    @Override
    public void clearSimulationConsole() {
        this.simulationConsole.setText("");
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 200);
    }
}

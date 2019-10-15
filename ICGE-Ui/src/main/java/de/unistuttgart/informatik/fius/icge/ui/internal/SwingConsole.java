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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.OutputStream;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import de.unistuttgart.informatik.fius.icge.ui.Console;


/**
 * The SwingConsole allows the user to see text output from the system or the simulation itself.
 *
 * @author Tobias Wältken
 * @version 1.0
 */
public class SwingConsole extends JTabbedPane implements Console {
    private static final long serialVersionUID = 5100186594058483257L;
    
    private JTextPane simulationConsole;
    private StyledDocument simulationDocument;
    private JTextArea systemConsole;
    
    private ConsoleBufferedOutputStream simulationOutputStream;
    private ConsoleBufferedOutputStream systemOutputStream;
    
    /**
     * Default constructor
     */
    public SwingConsole() {
        super(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

        Font standadFont = new Font("monospaced", Font.PLAIN, 12);
        {   // Setup simulation console
            this.simulationDocument = new DefaultStyledDocument();
            this.simulationConsole = new JTextPane(simulationDocument);
            this.simulationConsole.setEditable(false);
            this.simulationConsole.setFont(standadFont);
            final DefaultCaret caret = (DefaultCaret) this.simulationConsole.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            //this.simulationOutputStream = new ConsoleBufferedOutputStream(this.simulationConsole);
        }
        {   // Setup system console
            this.systemConsole = new JTextArea();
            this.systemConsole.setEditable(false);
            this.systemConsole.setFont(standadFont);
            final DefaultCaret caret = (DefaultCaret) this.systemConsole.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            this.systemOutputStream = new ConsoleBufferedOutputStream(this.systemConsole);
        }
        // Add consoles to the TabbedPane
        this.addTab("Simulation", new JScrollPane(this.simulationConsole));
        this.addTab("System", new JScrollPane(this.systemConsole));
        this.addTab("Notes", new JScrollPane(new JTextArea("Your place for non permanent notes!\n")));
        StyleContext context = new StyleContext();
        Style style = context.addStyle("test", null);
        Style styleRed = context.addStyle("red", style);
        StyleConstants.setForeground(styleRed,Color.red);
        
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

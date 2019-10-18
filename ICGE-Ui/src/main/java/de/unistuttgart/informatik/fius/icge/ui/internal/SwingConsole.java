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

import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;

import de.unistuttgart.informatik.fius.icge.ui.Console;


/**
 * The SwingConsole allows the user to see text output from the system or the simulation itself.
 *
 * @author Tobias Wältken
 * @author David Ruff
 * @version 1.0
 */
public class SwingConsole extends JTabbedPane implements Console {
    private static final long serialVersionUID = 5100186594058483257L;
    
    private JTextPane simulationConsole;
    private JTextPane systemConsole;
    
    private ConsoleBufferedOutputStream simulationOutputStream;
    private ConsoleBufferedOutputStream simulationErrorStream;
    private ConsoleBufferedOutputStream systemOutputStream;
    private ConsoleBufferedOutputStream systemErrorStream;
    
    private StringBuffer simulationInputBuffer;
    private Style        simulationInputStyle;
    
    private final int maxRowCount = 3;
    
    /**
     * Default constructor
     */
    public SwingConsole() {
        super(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        
        Font standardFont = new Font("monospaced", Font.PLAIN, 12);
        {   // Setup simulation console
            this.simulationConsole = new JTextPane(new DefaultStyledDocument());
            this.simulationConsole.setEditable(false);
            this.simulationConsole.setFont(standardFont);
            final DefaultCaret caret = (DefaultCaret) this.simulationConsole.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            this.simulationOutputStream = new ConsoleBufferedOutputStream(this.simulationConsole, OutputStyle.STANDARD,this);
            this.simulationErrorStream = new ConsoleBufferedOutputStream(this.simulationConsole, OutputStyle.ERROR,this);
        }
        {   // Setup system console
            this.systemConsole = new JTextPane(new DefaultStyledDocument());
            this.systemConsole.setEditable(false);
            this.systemConsole.setFont(standardFont);
            final DefaultCaret caret = (DefaultCaret) this.systemConsole.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            this.systemOutputStream = new ConsoleBufferedOutputStream(this.systemConsole, OutputStyle.STANDARD,this);
            this.systemErrorStream = new ConsoleBufferedOutputStream(this.systemConsole, OutputStyle.ERROR,this);
            
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
    public OutputStream getSimulationErrorStream() {
        return this.simulationErrorStream;
    }
    
    @Override
    public OutputStream getSystemOutputStream() {
        return this.systemOutputStream;
    }
    
    @Override
    public OutputStream getSystemErrorStream() {
        return this.systemErrorStream;
    }
    
    @Override
    public void clearSimulationConsole() {
        this.simulationConsole.setText("");
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 200);
    }
    
    void write(final char character, Style inputStyle, JTextPane textPane) {
        StringBuffer inputbuffer;
        Style bufferStyle;
        if(textPane == this.simulationConsole){
            inputbuffer = this.simulationInputBuffer;
            bufferStyle = this.simulationInputStyle;
        }else{
            throw new RuntimeException();
        } 
        if (inputbuffer.length() != 0 && bufferStyle != inputStyle) {
            flush(textPane);
        }
        this.simulationInputStyle = inputStyle;
        if (character != '\r') {
            inputbuffer.append(character);
        }
    }
    
    void flush(JTextPane textPane){
        StringBuffer inputbuffer;
        Style bufferStyle;
        if(textPane == this.simulationConsole){
            inputbuffer = this.simulationInputBuffer;
            bufferStyle = this.simulationInputStyle;
        }else{
            throw new RuntimeException();
        }
        try {
            textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(),inputbuffer.toString(), bufferStyle);
        } catch (BadLocationException e) {
            throw new RuntimeException("Bad insert Location: ", e);
        }
    }
    
    
    
}

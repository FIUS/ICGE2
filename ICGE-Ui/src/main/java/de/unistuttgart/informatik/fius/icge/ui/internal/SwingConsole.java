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
import java.io.IOException;

import javax.swing.JTextPane;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;

import de.unistuttgart.informatik.fius.icge.log.Logger;
import de.unistuttgart.informatik.fius.icge.ui.Console;


/**
 * The SwingConsole allows the user to see text output from the system or the simulation itself.
 *
 * @author Tobias Wältken
 * @author David Ruff
 * @author Fabian Bühler
 * @version 2.0
 */
public class SwingConsole extends JTextPane implements Console {
    private static final long serialVersionUID = 5100186594058483257L;
    
    private ConsoleBufferedOutputStream systemOutputStream;
    private ConsoleBufferedOutputStream systemErrorStream;
    
    /**
     * Default constructor
     */
    public SwingConsole() {
        super(new DefaultStyledDocument());
        
        final Font standardFont = new Font("monospaced", Font.PLAIN, 12);
        
        this.setEditable(false);
        this.setFont(standardFont);
        final DefaultCaret caret = (DefaultCaret) this.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        this.systemOutputStream = new ConsoleBufferedOutputStream(this, OutputStyle.STANDARD);
        this.systemErrorStream = new ConsoleBufferedOutputStream(this, OutputStyle.ERROR);
        
        Logger.addOutOutputStream(this.systemOutputStream);
        Logger.addErrorOutputStream(this.systemErrorStream);
    }
    
    @Override
    public void clearConsole() {
        this.setText("");
    }
    
    /**
     * Detach the internal output streams from {@code Logger.out} and {@code Logger.error}.
     */
    public void cleanup() {
        Logger.removeOutOutputStream(this.systemOutputStream);
        Logger.removeErrorOutputStream(this.systemErrorStream);
        try { // stop timers in ConsoleBufferedOutputStreams
            this.systemOutputStream.close();
            this.systemErrorStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

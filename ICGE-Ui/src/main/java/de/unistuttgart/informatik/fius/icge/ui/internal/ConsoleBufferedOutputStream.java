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

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;


/**
 * The ConsoleBufferedOutputStream allows other streams and classes to write text into a {@link JTextArea}
 *
 * @author Tobias Wältken David Ruff
 * @version 1.0
 */
public class ConsoleBufferedOutputStream extends OutputStream {
    
    private final JTextPane textPane;
    private final StyledDocument document;
    
    
    /**
     * Default Constructor
     *
     * @param textPane
     *     The text pane to place the stream data into
     */
    public ConsoleBufferedOutputStream(final JTextPane textPane) {
        this.textPane = textPane;
        this.document = textPane.getStyledDocument();
    }
    
    @Override
    public void flush() throws IOException {
        super.flush();
    }
    
    @Override
    public void write(final int character) throws IOException {
        try{
            Style style = this.document.getStyle("normal");
            this.document.insertString(this.document.getLength(),""+ (char) character,style);
        }
        catch(BadLocationException e){
            throw new IOException("Bad insert Location");
        }
    }
}

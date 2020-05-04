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
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;


/**
 * The ConsoleBufferedOutputStream allows other streams and classes to write text into a {@link JTextPane}
 *
 * @author Tobias Wältken
 * @author David Ruff
 * @version 1.0
 */
public class ConsoleBufferedOutputStream extends OutputStream {
    
    //TODO add actual buffer to avoid overflowing the textarea and cause lag
    private final JTextPane textPane;
    private final Style     style;
    
    /**
     * Default Constructor
     *
     * @param textPane
     *     The text pane to place the stream data into
     * @param style
     *     The style type to be used for the output
     */
    public ConsoleBufferedOutputStream(final JTextPane textPane, final OutputStyle style) {
        this.textPane = textPane;
        this.style = this.textPane.addStyle(style.toString(), null);
        
        switch (style) {
            case STANDARD:
                break;
            case ERROR:
                StyleConstants.setForeground(this.style, Color.red);
                break;
            default:
                throw new UnsupportedOperationException("With stye type " + style.toString());
        }
    }
    
    @Override
    public void flush() throws IOException {
        super.flush();
    }
    
    @Override
    public void write(final int character) throws IOException {
        try {
            this.textPane.getStyledDocument()
                    .insertString(this.textPane.getStyledDocument().getLength(), "" + (char) character, this.style);
        } catch (final BadLocationException e) {
            throw new IOException("Bad insert Location: ", e);
        }
    }
}

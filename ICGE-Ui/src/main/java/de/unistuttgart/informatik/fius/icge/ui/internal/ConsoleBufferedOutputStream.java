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
import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


/**
 * The ConsoleBufferedOutputStream allows other streams and classes to write text into a {@link JTextPane}.
 * <p>
 * The individual character writes are buffered and flushed to the {@link JTextPane} on three conditions:
 * <p>
 * 1. The last character is a newline
 * <p>
 * 2. The current line buffer contains {@code MAX_BUFFER_LENGTH} characters
 * <p>
 * 3. The {@link Timer} gives a signal to flush the line buffer
 * <p>
 * The {@link Timer} is stopped when closing the stream.
 *
 * @author Tobias Wältken
 * @author David Ruff
 * @author Fabian Bühler
 * @version 2.0
 */
public class ConsoleBufferedOutputStream extends OutputStream {
    
    /** The maximum length of the line buffer. */
    static final int MAX_BUFFER_LENGTH = 1024;
    
    private final Timer timer;
    
    //TODO add actual buffer to avoid overflowing the textarea and cause lag
    private final JTextPane     textPane;
    private final Style         style;
    private final StringBuilder lineBuffer;
    
    /**
     * Default Constructor
     *
     * @param textPane
     *     The text pane to place the stream data into
     * @param style
     *     The style type to be used for the output
     */
    public ConsoleBufferedOutputStream(final JTextPane textPane, final OutputStyle style) {
        this.lineBuffer = new StringBuilder(MAX_BUFFER_LENGTH * 2);
        
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
        
        this.timer = new Timer(500, (event) -> {
            try {
                // flush the line buffer in regular intervalls
                this.flushLineBufferToTextPane();
            } catch (final IOException e) {
                // do not handle exeption during regular buffer flush to avoid exception loops
            }
        });
        this.timer.setCoalesce(true);
        this.timer.start(); // start timer after everything is initialized
    }
    
    @Override
    public void flush() throws IOException {
        super.flush();
        this.flushLineBufferToTextPane();
    }
    
    @Override
    public void close() throws IOException {
        super.close();
        this.timer.stop();
    }
    
    @Override
    public void write(final int character) throws IOException {
        final char symbol = (char) character;
        final boolean newline = symbol == '\n'; // should catch CR/LF and LF line endings
        
        synchronized (this.lineBuffer) {
            this.lineBuffer.append(symbol);
        }
        
        if (newline || this.lineBuffer.length() >= MAX_BUFFER_LENGTH) {
            this.flushLineBufferToTextPane();
        }
    }
    
    private void flushLineBufferToTextPane() throws IOException {
        if (this.lineBuffer.length() == 0) { // fast exit as default without costly synchronized
            return; // nothing to flush in the line buffer
        }
        
        String newText;
        synchronized (this.lineBuffer) { // stringBuilder is not threadsafe
            // get the current buffer and reset linebuffer 
            newText = this.lineBuffer.toString();
            this.lineBuffer.setLength(0);
        }
        
        if (newText.length() > 0) { // new null check because previous check may be obsolete now
            // print line to text pane
            try {
                final StyledDocument content = this.textPane.getStyledDocument();
                synchronized (this.textPane) {
                    content.insertString(content.getLength(), newText, this.style);
                }
            } catch (final BadLocationException e) {
                throw new IOException("Bad insert Location: ", e);
            }
        }
    }
}

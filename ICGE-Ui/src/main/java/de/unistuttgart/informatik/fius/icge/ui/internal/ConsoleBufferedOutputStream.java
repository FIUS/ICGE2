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
import javax.swing.SwingUtilities;
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
    static final int  DEFAULT_MAX_BUFFER_LENGTH = 4096;
    private final int maxBufferLength;
    
    private final Timer timer;
    
    //TODO add actual buffer to avoid overflowing the textarea and cause lag
    private final JTextPane     textPane;
    private final Style         style;
    private final StringBuilder lineBuffer;
    
    /**
     * Default Constructor
     * <p>
     * This should only be called from the swing ui thread
     *
     * @param textPane
     *     The text pane to place the stream data into
     * @param style
     *     The style type to be used for the output
     */
    public ConsoleBufferedOutputStream(final JTextPane textPane, final OutputStyle style) {
        this(textPane, style, DEFAULT_MAX_BUFFER_LENGTH);
    }
    
    /**
     * Constructor with maxBufferLenght included
     * 
     * @param textPane
     *     The text pane to place the stream data into
     * @param style
     *     The style type to be used for the output
     * @param maxBufferLength
     *     the maximum length of the text buffer before it is flushed. The buffer is also flushed every 32ms. Choosing a
     *     small value may result in one output stream being cut of by the messages from another.
     */
    public ConsoleBufferedOutputStream(final JTextPane textPane, final OutputStyle style, int maxBufferLength) {
        this.maxBufferLength = maxBufferLength;
        this.lineBuffer = new StringBuilder(maxBufferLength * 2);
        
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
        
        this.timer = new Timer(32, (event) -> {
            try {
                // flush the line buffer in regular intervalls
                this.flushLineBufferToTextPane();
            } catch (@SuppressWarnings("unused") final IOException e) {
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
        
        synchronized (this.lineBuffer) {
            this.lineBuffer.append(symbol);
        }
        
        if (this.lineBuffer.length() >= this.maxBufferLength) {
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
            SwingUtilities.invokeLater(() -> {
                try {
                    final StyledDocument content = this.textPane.getStyledDocument();
                    synchronized (this.textPane) {
                        content.insertString(content.getLength(), newText, this.style);
                    }
                } catch (final BadLocationException e) {
                    e.printStackTrace();
                }
            });
        }
    }
    
    /**
     * Get's {@link #maxBufferLength maxBufferLength}
     * 
     * @return maxBufferLength
     */
    public int getMaxBufferLength() {
        return this.maxBufferLength;
    }
    
}

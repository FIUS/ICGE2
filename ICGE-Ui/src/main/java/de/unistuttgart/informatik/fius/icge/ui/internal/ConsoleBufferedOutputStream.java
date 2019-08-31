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

import javax.swing.JTextArea;

/**
 * The ConsoleBufferedOutputStream allows other streams and classes to write
 * text into a {@link JTextArea}
 *
 * @author Tobias Wältken
 * @version 1.0
 */
public class ConsoleBufferedOutputStream extends OutputStream {

    private JTextArea textArea;

    //TODO add actual buffer to avoid overflowing the textarea and cause lag

    /**
     * Default Constructor
     *
     * @param textArea The text area to place the stream data into
     */
    public ConsoleBufferedOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void flush() throws IOException {
        super.flush();
    }

    @Override
    public void write(int arg0) throws IOException {
        this.textArea.append("" + (char) arg0);
    }
}

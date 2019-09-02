/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 *
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.log;


import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;


/**
 * A Output stream that allows multiple other output streams to attach
 * themselves to this one.
 *
 * @author Tobias Wältken
 * @version 1.0
 */
public class OutputStreamMultiplier extends OutputStream {

    private ArrayList<OutputStream> listenerStreams;

    /**
     * Default Constructor
     */
    public OutputStreamMultiplier() {
        this.listenerStreams = new ArrayList<>();
    }

    /**
     * Add a output stream to also recieve everything this stream recieves
     *
     * @param listenerStream The stream to add
     * @return Returns True if successfull
     */
    public boolean addOutputStream(OutputStream listenerStream) {
        return this.listenerStreams.add(listenerStream);
    }

    /**
     * Remove a output stream from recieving everything this stream recieves
     *
     * @param listenerStream The stream to remove
     * @return Returns True if successfull
     */
    public boolean removeOutputStream(OutputStream listenerStream) {
        return this.listenerStreams.remove(listenerStream);
    }

    /**
     * Clears all connected output streams
     */
    public void clearOutputStreams() {
        this.listenerStreams.clear();
    }

    @Override
    public void flush() throws IOException {
        super.flush();

        for (OutputStream listenerStream : this.listenerStreams)
            listenerStream.flush();
    }

    @Override
    public void write(int arg0) throws IOException {
        for (OutputStream listenerStream : this.listenerStreams)
            listenerStream.write(arg0);
    }
}

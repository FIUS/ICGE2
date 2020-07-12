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

import java.io.OutputStream;
import java.io.PrintStream;


/**
 * Helper class that intercepts {@code System.out} and {@code System.err} and allows to attach multiple streams to
 * capture the outputs of intercepted streams.
 *
 * @author Tim Neumann
 * @author Tobias Wältken
 * @author Fabian Bühler
 * @version 2.0
 */
public abstract class Logger {
    
    /** The main logger printing to {@link System#out} */
    public static PrintStream out;
    /** The error logger printing to {@link System#err} */
    public static PrintStream error;
    
    private static OutputStreamMultiplier outStream;
    private static OutputStreamMultiplier errorStream;
    
    // This block intercepts {@link System.out} and {@link System.err}
    static {
        Logger.outStream = new OutputStreamMultiplier();
        Logger.outStream.addOutputStream(System.out);
        Logger.out = new PrintStream(Logger.outStream);
        System.setOut(Logger.out);
        
        Logger.errorStream = new OutputStreamMultiplier();
        Logger.errorStream.addOutputStream(System.err);
        Logger.error = new PrintStream(Logger.errorStream);
        System.setErr(Logger.error);
    }
    
    /**
     * Function to add a {@link OutputStream} to the out logger
     *
     * @param stream
     *     The {@link OutputStream} to add
     * @return Returns true if successfull
     * @see OutputStreamMultiplier#addOutputStream(OutputStream)
     */
    public static boolean addOutOutputStream(final OutputStream stream) {
        return Logger.outStream.addOutputStream(stream);
    }
    
    /**
     * Function to remove a {@link OutputStream} from the out logger
     *
     * @param stream
     *     The {@link OutputStream} to remove
     * @return Returns true if successfull
     * @see OutputStreamMultiplier#removeOutputStream(OutputStream)
     */
    public static boolean removeOutOutputStream(final OutputStream stream) {
        return Logger.outStream.removeOutputStream(stream);
    }
    
    /**
     * Clears the out OutputStreams
     *
     * @see OutputStreamMultiplier#clearOutputStreams()
     */
    public static void clearOutOutputStream() {
        Logger.outStream.clearOutputStreams();
    }
    
    /**
     * Function to add a {@link OutputStream} to the error logger
     *
     * @param stream
     *     The {@link OutputStream} to add
     * @return Returns true if successfull
     * @see OutputStreamMultiplier#addOutputStream(OutputStream)
     */
    public static boolean addErrorOutputStream(final OutputStream stream) {
        return Logger.errorStream.addOutputStream(stream);
    }
    
    /**
     * Function to remove a {@link OutputStream} from the error logger
     *
     * @param stream
     *     The {@link OutputStream} to remove
     * @return Returns true if successfull
     * @see OutputStreamMultiplier#removeOutputStream(OutputStream)
     */
    public static boolean removeErrorOutputStream(final OutputStream stream) {
        return Logger.errorStream.removeOutputStream(stream);
    }
    
    /**
     * Clears the error OutputStreams
     *
     * @see OutputStreamMultiplier#clearOutputStreams()
     */
    public static void clearErrorOutputStream() {
        Logger.errorStream.clearOutputStreams();
    }
}

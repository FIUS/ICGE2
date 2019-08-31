/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation;

import java.util.function.Function;


/**
 * The interface of the manager, that produces the right amount of game and render ticks.
 * 
 * @author Tim Neumann
 */
public interface TickManager {
    
    /**
     * The number of render ticks per one simulation tick.
     */
    public static final int RENDER_TICKS_PER_SIMULATION_TICK = 8;
    
    /**
     * The render period, that is set by default.
     */
    public static final int DEFAULT_RENDER_TICK_PERIOD = 125;
    
    /**
     * Set the period of the render ticks. The game ticks will have this period times
     * {@value #RENDER_TICKS_PER_SIMULATION_TICK}.
     * 
     * @param millis
     *     The new period in milliseconds.
     */
    public void setPeriod(int millis);
    
    /**
     * Get the period of the render ticks.
     * 
     * @return The number of milliseconds per render tick period.
     */
    public int getRenderTickPeriod();
    
    /**
     * Get the period of the game ticks.
     * 
     * @return The number of milliseconds per game tick period.
     */
    public int getGameTickPeriod();
    
    /**
     * Start ticking.
     */
    public void start();
    
    /**
     * Stop ticking.
     * <p>
     * Ticking can be started again with {@link #start()}
     * <p>
     */
    public void stop();
    
    /**
     * Register a listener for simulation ticks.
     * <p>
     * The listener get's the current tick count as an argument and must return whether it wants to continue to listen.
     * </p>
     * 
     * @param listener
     *     The listener to be called.
     */
    public void registerTickListener(Function<Long, Boolean> listener);
}
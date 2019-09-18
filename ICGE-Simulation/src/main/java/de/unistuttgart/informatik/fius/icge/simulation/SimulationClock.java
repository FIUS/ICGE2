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

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;


/**
 * The simulation clock is responsible for producing the game and render ticks.
 * 
 * @author Tim Neumann
 */
public interface SimulationClock {
    
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
     * </p>
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
    
    /**
     * Register a listener for the end of simulation ticks.
     * <p>
     * The listener get's the current tick count as an argument and must return whether it wants to continue to listen.
     * </p>
     * 
     * @param listener
     *     The listener to be called.
     */
    public void registerPostTickListener(Function<Long, Boolean> listener);
    
    /**
     * @return the number of the last simulation tick
     */
    long getLastTickNumber();
    
    /**
     * Schedule an operation, to happen during the given tick. This method will block until that tick. Then the tick
     * processing will halt until the given end of operation is completed.
     * <p>
     * If the given tick is in the past, the operation will be scheduled for the next tick.
     * </p>
     * 
     * @param tick
     *     The absolute number of the tick at which the operation will be run
     * @param endOfOperation
     *     Tick processing will be halted until this future is completed; must not complete exceptionally
     * @throws IllegalStateException
     *     if the end of operation completes exceptionally
     */
    void scheduleOperationAtTick(long tick, CompletableFuture<Void> endOfOperation);
    
    /**
     * Schedule an operation, to happen during the tick a given number of ticks in the future. This method will block
     * until that tick. Then the tick processing will halt until the given end of operation is completed.
     * <p>
     * If the given number of ticks is less or equal to one, the operation is scheduled for the next tick.
     * </p>
     *
     * @param ticks
     *     The number of ticks until the tick, for which to schedule the operation
     * @param endOfOperation
     *     Tick processing will be halted until this future is completed; must not complete exceptionally
     * @throws IllegalStateException
     *     if the end of operation completes exceptionally
     */
    void scheduleOperationInTicks(long ticks, CompletableFuture<Void> endOfOperation);
    
    /**
     * Schedule an operation, to happen during the next tick. This method will block until that tick. Then the tick
     * processing will halt until the given end of operation is completed.
     * 
     * @param endOfOperation
     *     Tick processing will be halted until this future is completed; must not complete exceptionally
     * @throws IllegalStateException
     *     if the end of operation completes exceptionally
     */
    void scheduleOperationAtNextTick(CompletableFuture<Void> endOfOperation);
}

/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 *
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.internal;

*

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;*
import de.unistuttgart.informatik.fius.icge.simulation.SimulationClock;
import de.unistuttgart.informatik.fius.icge.simulation.exception.TimerAlreadyRunning;
import de.unistuttgart.informatik.fius.icge.simulation.exception.UncheckedInterruptedException;
import de.unistuttgart.informatik.fius.icge.ui.exception.ListenerSetException;**


/**
 * The standard implementation of {@link SimulationClock}
 *
 * @author Tim Neumann
 * @author Tobias Wältken
 * @version 1.0
 */
public class StandardSimulationClock implements SimulationClock {
    private final Object tickListenerLock = new Object();*
    private final List<Function<Long, Boolean>> tickListeners;
    private final List<Function<Long, Boolean>> postTickListeners;*
    private final Set<CompletableFuture<Void>> operationBoundaries;*
    private Consumer<Long>                      animationTickListener;
    private StateChangeListener stateChangeListener;*
    private TimerTask                           task;
    private final Timer timer;*
    private volatile long tickCount;*
    /**
     * Setting this to true will signal the clock that it is in shutdown mode.
     * <p>
     * This causes tick processing to abort at appropriate points.
     * </p>
     * <p>
     * This causes most methods to return immediately without throwing an exception. This decision was made because due
     * to concurrency some methods may still be called after a shutdown without the caller doing anything wrong or
     * having any sensible to reaction to this.
     * </p>
     *
     */
    private volatile boolean shuttingDown;*
    private int period;*

    /**
     * Default constructor
     */
    public StandardSimulationClock() {
        this.tickListeners = new ArrayList<>();
        this.postTickListeners = new ArrayList<>();
        this.timer = new Timer("STM-TickTimer", true);
        this.tickCount = -1;
        this.period = SimulationClock.DEFAULT_RENDER_TICK_PERIOD;
        this.shuttingDown = false;
        this.operationBoundaries = Collections.synchronizedSet(new HashSet<>());
    }*

    /**
     * This internal start function actually starts the timer but does not notify the simulation proxy. If you don't
     * know what you do use {@link SimulationClock#start()}
     */
    public synchronized void startInternal() {
        if (this.isRunning()) throw new TimerAlreadyRunning();
        if (this.shuttingDown) return;
 *
        this.task = new TimerTask() {
 *
            @Override
            public void run() {
                StandardSimulationClock.this.tick();
            }
        };
        this.timer.schedule(this.task, 0, this.period);
    }*

    /**
     * This internal stop function actually stops the timer but does not notify the simulation proxy. If you don't know
     * what you do use {@link SimulationClock#stop()}
     */
    public synchronized void stopInternal() {
        if (this.isRunning()) {
            this.task.cancel();
        }
        this.task = null;
    }*

    /**
     * Shuts down this clock.
     * <p>
     * Shutting down includes stopping the clock and canceling all scheduled operations as well as stopping to wait for
     * the completion of running ones.
     * </p>
     * <p>
     * Most methods of this clock will just return immediately after this method is called. This includes register
     * listener methods, scheduleOperation methods and all methods controlling the state of the clock except for
     * stopInternal.
     * </p>
     *
     * @see #shuttingDown <b>shuttingDown</b> for more information and the reason for all methods returning
     */
    public synchronized void shutdown() {
        if (this.shuttingDown) return;
        this.shuttingDown = true;
        this.stop();
        for (final var boundary : Set.copyOf(this.operationBoundaries)) {
            boundary.cancel(true);
        }
    }*

    @Override
    public synchronized void setPeriod(final int millis) {
        this.period = millis;
 *
        if (this.isRunning()) {
            this.stop();
            this.start();
        }
    }*

    @Override
    public int getRenderTickPeriod() {
        return this.period;
    }*

    @Override
    public int getGameTickPeriod() {
        return this.period * SimulationClock.RENDER_TICKS_PER_SIMULATION_TICK;
    }*

    @Override
    public boolean isRunning() {
        return this.task != null;
    }*

    @Override
    public synchronized void start() {
        if (this.stateChangeListener != null) {
            this.stateChangeListener.clockStarted();
        }
 *
        this.startInternal();
    }*

    @Override
    public synchronized void stop() {
        if (this.stateChangeListener != null) {
            this.stateChangeListener.clockPaused();
        }
 *
        this.stopInternal();
    }*

    @Override
    public synchronized void step() {
        if (this.isRunning()) throw new TimerAlreadyRunning();
        if (this.shuttingDown) return;
 *
        new Thread(() -> {
            StandardSimulationClock.this.tickCount = ((StandardSimulationClock.this.tickCount
                    - (StandardSimulationClock.this.tickCount % 8)) + 7);
            StandardSimulationClock.this.tick();
        }, "single-step").start();
    }*

    /**
     * Process a tick
     */
    private void tick() {
        synchronized (this.tickListenerLock) {
            //Don't process tick when shutting down.
            if (this.shuttingDown) return;
            this.tickCount++;
            if ((this.tickCount % SimulationClock.RENDER_TICKS_PER_SIMULATION_TICK) == 0) {
                this.tickSimulation(this.tickCount / SimulationClock.RENDER_TICKS_PER_SIMULATION_TICK);
            }
            //Don't continue to process tick when shutting down.
            if (this.shuttingDown) return;
            if (this.animationTickListener != null) {
                this.animationTickListener.accept(this.tickCount);
            }
        }
    }*

    /**
     * Process a simulation tick
     *
     * @param tickNumber
     *     The number of the simulation tick since the start of the clock.
     */
    private void tickSimulation(final long tickNumber) {
        for (final var listener : List.copyOf(this.tickListeners)) {
            //Don't continue to process tick when shutting down.
            if (this.shuttingDown) return;
            if (!listener.apply(tickNumber)) {
                this.tickListeners.remove(listener);
            }
        }
 *
        for (final var listener : List.copyOf(this.postTickListeners)) {
            //Don't continue to process tick when shutting down.
            if (this.shuttingDown) return;
            if (!listener.apply(tickNumber)) {
                this.postTickListeners.remove(listener);
            }
        }
    }*

    /**
     * Set the animation tick listener, that gets called every animation tick and is responsible for informing the UI.
     *
     * @param listener
     *     the listener to set; use null to remove listener
     * @throws ListenerSetException
     *     if the listener is already set and the provided listener is not {@code null}.
     */
    public void setAnimationTickListener(final Consumer<Long> listener) {
        if ((this.animationTickListener == null) || (listener == null)) {
            this.animationTickListener = listener;
        } else throw new ListenerSetException();
    }*

    /**
     * Set the state change listener, that gets called when the clock get's started or paused through public API and is
     * responsible for informing the UI.
     *
     * @param listener
     *     the listener to set; use null to remove listener
     * @throws ListenerSetException
     *     if the listener is already set and the provided listener is not {@code null}.
     */
    public void setStateChangeListener(final StateChangeListener listener) {
        if ((this.stateChangeListener == null) || (listener == null)) {
            this.stateChangeListener = listener;
        } else throw new ListenerSetException();
    }*

    /**
     * Remove the state change listener, that gets called when the clock get's started or paused through public API and
     * is responsible for informing the UI.
     */
    public void removeStateChangeListener() {
        this.stateChangeListener = null;
    }*

    @Override
    public void registerTickListener(final Function<Long, Boolean> listener) {
        if (this.shuttingDown) return;
        synchronized (this.tickListenerLock) {
            this.tickListeners.add(listener);
        }
    }*

    @Override
    public void registerPostTickListener(final Function<Long, Boolean> listener) {
        if (this.shuttingDown) return;
        synchronized (this.tickListenerLock) {
            this.postTickListeners.add(listener);
        }
    }*

    @Override
    public long getLastTickNumber() {
        //not rounding is intended here as we'd need floor and casting is the same as floor for positive integers
        return this.tickCount / SimulationClock.RENDER_TICKS_PER_SIMULATION_TICK;
    }*

    @Override
    public long getLastRenderTickNumber() {
        return this.tickCount;
    }*

    @Override
    public void scheduleOperationAtTick(final long tick, final CompletableFuture<Void> endOfOperation) {
        if (this.shuttingDown) return;
        this.operationBoundaries.add(endOfOperation);
        final CompletableFuture<Void> startOfOperation = new CompletableFuture<>();
        this.operationBoundaries.add(startOfOperation);
        this.registerTickListener(tickNumber -> {
            if (tickNumber >= tick) {
                startOfOperation.complete(null);
                try {
                    endOfOperation.get();
                    this.operationBoundaries.remove(endOfOperation);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                } catch (final CancellationException e) {
                    //When shutting down this is expected
                    if (!this.shuttingDown) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
            return true;
        });
        try {
            startOfOperation.get();
            this.operationBoundaries.remove(startOfOperation);
        } catch (final InterruptedException e) {
            throw new UncheckedInterruptedException(e);
        } catch (final ExecutionException e) {
            //Should not happen as this future does never execute but is simply completed manually.
            e.printStackTrace();
        }
    }*

    @Override
    public void scheduleOperationInTicks(final long ticks, final CompletableFuture<Void> endOfOperation) {
        this.scheduleOperationAtTick(this.getLastTickNumber() + ticks, endOfOperation);
    }*

    @Override
    public void scheduleOperationAtNextTick(final CompletableFuture<Void> endOfOperation) {
        this.scheduleOperationInTicks(1, endOfOperation);
    }*

    /**
     * The interface for a listener listening for simulation clock starts and stops. The listener is only informed when
     * the state change is caused from the public API, not from UI interaction.
     */
    public interface StateChangeListener {
        /**
         * The clock was started.
         */
        void clockStarted();
 *
        /**
         * The clock was paused/stopped.
         */
        void clockPaused();
    }
}

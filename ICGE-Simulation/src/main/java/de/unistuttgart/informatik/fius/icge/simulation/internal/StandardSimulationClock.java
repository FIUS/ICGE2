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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import de.unistuttgart.informatik.fius.icge.simulation.SimulationClock;
import de.unistuttgart.informatik.fius.icge.simulation.exception.TimerAlreadyRunning;
import de.unistuttgart.informatik.fius.icge.simulation.exception.UncheckedInterruptedException;
import de.unistuttgart.informatik.fius.icge.ui.ListenerSetException;
import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy.ButtonType;
import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy.EntityDrawListener;


/**
 * The standard implementation of {@link SimulationClock}
 *
 * @author Tim Neumann
 * @author Tobias Wältken
 * @version 1.0
 */
public class StandardSimulationClock implements SimulationClock {
    
    private StandardSimulationProxy simulationProxy;
    
    private EntityDrawListener drawer;
    
    private final Object tickListenerLock = new Object();
    
    private final List<Function<Long, Boolean>> tickListeners;
    private final List<Function<Long, Boolean>> postTickListeners;
    
    private TimerTask   task;
    private final Timer timer;
    
    private volatile long tickCount;
    
    private int period;
    
    /**
     * Default constructor
     */
    public StandardSimulationClock() {
        this.tickListeners = new ArrayList<>();
        this.postTickListeners = new ArrayList<>();
        this.timer = new Timer("STM-TickTimer");
        this.tickCount = -1;
        this.period = SimulationClock.DEFAULT_RENDER_TICK_PERIOD;
    }
    
    /**
     * Set the entity draw listener.
     * 
     * @param listener
     * 
     * @throws IllegalStateException
     *     If clock is running
     * @throws ListenerSetException
     *     If listener is already set and new listener is not null
     */
    public void setEntityDrawListener(EntityDrawListener listener) {
        if (this.isRunning()) {
            throw new IllegalStateException("Draw listener can only be set when clock is stopped or paused!");
        }
        if ((this.drawer == null) || (listener == null)) {
            this.drawer = listener;
        } else throw new ListenerSetException();
    }
    
    /**
     * Setter function to set the simulation proxy which is notified about ui changes
     *
     * @param simulationProxy
     *     The proxy to set
     */
    public void setSimulationProxy(final StandardSimulationProxy simulationProxy) {
        this.simulationProxy = simulationProxy;
    }
    
    /**
     * This internal start function actually starts the timer but does not notify the simulation proxy. If you don't
     * know what you do use {@link SimulationClock#start()}
     */
    public synchronized void startInternal() {
        if (this.isRunning()) throw new TimerAlreadyRunning();
        
        this.task = new TimerTask() {
            
            @Override
            public void run() {
                StandardSimulationClock.this.tick();
            }
        };
        this.timer.schedule(this.task, 0, this.period);
    }
    
    /**
     * This internal stop function actually stops the timer but does not notify the simulation proxy. If you don't know
     * what you do use {@link SimulationClock#stop()}
     */
    public synchronized void stopInternal() {
        if (this.isRunning()) {
            this.task.cancel();
        }
        this.task = null;
    }
    
    @Override
    public synchronized void setPeriod(final int millis) {
        this.period = millis;
        
        if (this.isRunning()) {
            this.stop();
            this.start();
        }
    }
    
    @Override
    public int getRenderTickPeriod() {
        return this.period;
    }
    
    @Override
    public int getGameTickPeriod() {
        return this.period * SimulationClock.RENDER_TICKS_PER_SIMULATION_TICK;
    }
    
    @Override
    public boolean isRunning() {
        return this.task != null;
    }
    
    @Override
    public synchronized void start() {
        if (this.simulationProxy != null) {
            this.simulationProxy.buttonPressed(ButtonType.PLAY);
        } else {
            this.startInternal();
        }
    }
    
    @Override
    public synchronized void stop() {
        if (this.simulationProxy != null) {
            this.simulationProxy.buttonPressed(ButtonType.PAUSE);
        } else {
            this.stopInternal();
        }
    }
    
    @Override
    public synchronized void step() {
        if (this.isRunning()) throw new TimerAlreadyRunning();
        
        new Thread(
                () -> {
                    //FIXME this only executes a graphics tick an not neccessariely
                    // a simulation tick.
                    StandardSimulationClock.this.tick();
                }, "single-step"
        ).start();
    }
    
    /**
     * Process a tick
     */
    private synchronized void tick() {
        synchronized (this.tickListenerLock) {
            this.tickCount++;
            if ((this.tickCount % SimulationClock.RENDER_TICKS_PER_SIMULATION_TICK) == 0) {
                this.tickSimulation(this.tickCount / SimulationClock.RENDER_TICKS_PER_SIMULATION_TICK);
            }
            if (this.drawer != null) {
                this.drawer.draw(this.tickCount);
            }
        }
        
    }
    
    /**
     * Process a simulation tick
     *
     * @param tickNumber
     *     The number of the simulation tick since the start of the clock.
     */
    private void tickSimulation(final long tickNumber) {
        for (final var listener : List.copyOf(this.tickListeners)) {
            if (!listener.apply(tickNumber)) {
                this.tickListeners.remove(listener);
            }
        }
        
        for (final var listener : List.copyOf(this.postTickListeners)) {
            if (!listener.apply(tickNumber)) {
                this.postTickListeners.remove(listener);
            }
        }
    }
    
    @Override
    public void registerTickListener(final Function<Long, Boolean> listener) {
        synchronized (this.tickListenerLock) {
            this.tickListeners.add(listener);
        }
    }
    
    @Override
    public void registerPostTickListener(final Function<Long, Boolean> listener) {
        synchronized (this.tickListenerLock) {
            this.postTickListeners.add(listener);
        }
    }
    
    @Override
    public long getLastTickNumber() {
        //not rounding is intended here as we'd need floor and casting is the same as floor for positive integers
        return this.tickCount / SimulationClock.RENDER_TICKS_PER_SIMULATION_TICK;
    }
    
    @Override
    public long getLastRenderTickNumber() {
        return this.tickCount;
    }
    
    @Override
    public void scheduleOperationAtTick(final long tick, final CompletableFuture<Void> endOfOperation) {
        final CompletableFuture<Void> startOfOperation = new CompletableFuture<>();
        this.registerTickListener(tickNumber -> {
            if (tickNumber >= tick) {
                startOfOperation.complete(null);
                try {
                    endOfOperation.get();
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
                return false;
            }
            return true;
        });
        try {
            startOfOperation.get();
        } catch (final InterruptedException e) {
            throw new UncheckedInterruptedException(e);
        } catch (final ExecutionException e) {
            final Throwable cause = e.getCause().getCause();
            if (cause instanceof InterruptedException) throw new UncheckedInterruptedException(cause);
            throw new IllegalStateException("The end of operation completed exceptionally", cause);
        }
    }
    
    @Override
    public void scheduleOperationInTicks(final long ticks, final CompletableFuture<Void> endOfOperation) {
        this.scheduleOperationAtTick(this.getLastTickNumber() + ticks, endOfOperation);
    }
    
    @Override
    public void scheduleOperationAtNextTick(final CompletableFuture<Void> endOfOperation) {
        this.scheduleOperationInTicks(1, endOfOperation);
    }
}

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
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

import de.unistuttgart.informatik.fius.icge.simulation.Simulation;
import de.unistuttgart.informatik.fius.icge.simulation.SimulationClock;
import de.unistuttgart.informatik.fius.icge.simulation.exception.TimerAlreadyRunning;
import de.unistuttgart.informatik.fius.icge.ui.PlayfieldDrawer;


/**
 * The standard implementation of {@link SimulationClock}
 *
 * @author Tim Neumann
 * @author Tobias Wältken
 * @version 1.0
 */
public class StandardSimulationClock implements SimulationClock {

    private PlayfieldDrawer drawer;

    private List<Function<Long, Boolean>> tickListeners;
    private List<Function<Long, Boolean>> postTickListeners;

    private TimerTask task;
    private Timer timer;

    private long tickCount;

    private int period;

    /**
     * Default constructor
     */
    public StandardSimulationClock() {
        this.tickListeners = Collections.synchronizedList(new ArrayList<>());
        this.postTickListeners = Collections.synchronizedList(new ArrayList<>());
        this.timer = new Timer("STM-TickTimer");
        this.period = DEFAULT_RENDER_TICK_PERIOD;
    }

    /**
     * Initialize this standard tick manager.
     *
     * @param parent
     *     The simulation for this tick manager
     */
    public void initialize(Simulation parent) {
        this.drawer = parent.getUiManager().getPlayfieldDrawer();
    }

    @Override
    public synchronized void setPeriod(int millis) {
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
        return this.period * RENDER_TICKS_PER_SIMULATION_TICK;
    }

    @Override
    public boolean isRunning() {
        return this.task != null;
    }

    @Override
    public synchronized void start() {
        if (this.isRunning()) throw new TimerAlreadyRunning();

        this.task = new TimerTask() {

            @Override
            public void run() {
                tick();
            }
        };
        this.timer.schedule(this.task, 0, this.period);
    }

    @Override
    public synchronized void stop() {
        if (this.isRunning())
            this.task.cancel();
        this.task = null;
    }

    @Override
    public synchronized void step() {
        if (this.isRunning()) throw new TimerAlreadyRunning();

        new Thread(new Runnable(){
            @Override
            public void run() {
                tick();
            }
        }, "single-step").start();
    }

    /**
     * Process a tick
     */
    private synchronized void tick() {
        if ((this.tickCount % RENDER_TICKS_PER_SIMULATION_TICK) == 0) {
            tickSimulation(this.tickCount / RENDER_TICKS_PER_SIMULATION_TICK);
        }
        this.drawer.draw(this.tickCount);
        this.tickCount++;
    }

    /**
     * Process a simulation tick
     *
     * @param tickNumber
     *     The number of the simulation tick since the start of the clock.
     */
    private void tickSimulation(long tickNumber) {
        for (var listener : List.copyOf(this.tickListeners)) {
            if (!listener.apply(tickNumber)) {
                this.tickListeners.remove(listener);
            }
        }

        for (var listener : List.copyOf(this.postTickListeners)) {
            if (!listener.apply(tickNumber)) {
                this.postTickListeners.remove(listener);
            }
        }
    }

    @Override
    public void registerTickListener(Function<Long, Boolean> listener) {
        this.tickListeners.add(listener);
    }

    @Override
    public void registerPostTickListener(Function<Long, Boolean> listener) {
        this.postTickListeners.add(listener);
    }

}

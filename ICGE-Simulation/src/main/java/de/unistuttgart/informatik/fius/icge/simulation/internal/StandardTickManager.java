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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import de.unistuttgart.informatik.fius.icge.simulation.Simulation;
import de.unistuttgart.informatik.fius.icge.simulation.TickManager;
import de.unistuttgart.informatik.fius.icge.ui.PlayfieldDrawer;


/**
 * The standard implementation of {@link TickManager}
 * 
 * @author Tim Neumann
 */
public class StandardTickManager implements TickManager {
    
    private PlayfieldDrawer drawer;
    
    private ExecutorService executor = Executors.newCachedThreadPool(r -> new Thread(r, "StandardTickManagerExecutor"));
    
    private List<Function<Long, Boolean>> tickListeners = Collections.synchronizedList(new ArrayList<>());
    
    private TimerTask task;
    private Timer     timer = new Timer("STM-TickTimer");
    
    private long tickCount;
    
    private int period = DEFAULT_RENDER_TICK_PERIOD;
    
    
    /**
     * Initialize this standard tick manager.
     * 
     * @param parent
     *     The simulation for this tick manager
     */
    public void initialize(Simulation parent) {
        this.drawer = parent.getUiManager().getPlayfieldDrawer();
        this.start();
    }
    
    @Override
    public void setPeriod(int millis) {
        this.period = millis;
        this.stop();
        this.start();
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
    public void start() {
        this.task = new TimerTask() {
            
            @Override
            public void run() {
                doTick();
            }
        };
        this.timer.schedule(this.task, 0, this.period);
    }
    
    @Override
    public void stop() {
        this.task.cancel();
    }
    
    /**
     * Do a low level tick
     */
    private void doTick() {
        doRenderTick(this.tickCount);
        if ((this.tickCount % RENDER_TICKS_PER_SIMULATION_TICK) == 0) {
            doSimulationTick(this.tickCount / RENDER_TICKS_PER_SIMULATION_TICK);
        }
        this.tickCount++;
    }
    
    /**
     * Do / propagate a render tick.
     * 
     * @param tickNumber
     *     The number of the render tick since the start of the tick manager.
     */
    private void doRenderTick(long tickNumber) {
        this.executor.execute(this.drawer::draw);
    }
    
    /**
     * Do / propagate a simulation tick
     * 
     * @param tickNumber
     *     The number of the simulation tick since the start of the tick manager.
     */
    private void doSimulationTick(long tickNumber) {
        for (var listener : List.copyOf(this.tickListeners)) {
            CompletableFuture.supplyAsync(() -> listener.apply(tickNumber), this.executor).thenAcceptAsync(keep -> {
                if (!keep) {
                    this.tickListeners.remove(listener);
                }
            }, this.executor);
        }
    }
    
    @Override
    public void registerTickListener(Function<Long, Boolean> listener) {
        this.tickListeners.add(listener);
    }
    
}

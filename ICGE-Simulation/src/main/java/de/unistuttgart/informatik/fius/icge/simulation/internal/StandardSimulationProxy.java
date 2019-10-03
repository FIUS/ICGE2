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

import java.util.concurrent.CompletableFuture;

import de.unistuttgart.informatik.fius.icge.simulation.SimulationHost;
import de.unistuttgart.informatik.fius.icge.simulation.internal.entity.program.StandardEntityProgramRegistry;
import de.unistuttgart.informatik.fius.icge.simulation.internal.entity.program.StandardEntityProgramRunner;
import de.unistuttgart.informatik.fius.icge.simulation.internal.playfield.StandardPlayfield;
import de.unistuttgart.informatik.fius.icge.simulation.internal.tasks.StandardTaskRegistry;
import de.unistuttgart.informatik.fius.icge.simulation.internal.tasks.StandardTaskRunner;
import de.unistuttgart.informatik.fius.icge.simulation.tasks.Task;
import de.unistuttgart.informatik.fius.icge.simulation.tasks.TaskRegistry;
import de.unistuttgart.informatik.fius.icge.ui.GameWindow;
import de.unistuttgart.informatik.fius.icge.ui.ListenerSetException;
import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy;
import de.unistuttgart.informatik.fius.icge.ui.SimulationTreeNode;
import de.unistuttgart.informatik.fius.icge.ui.TextureRegistry;


/**
 * StandardSimulationProxy
 *
 * @author Tobias Wältken
 * @version 1.0
 */
public class StandardSimulationProxy implements SimulationProxy, SimulationHost {
    
    /** A lookup table for the simulation times */
    public static final int[] SIMULATION_TIMES = {
            // 0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10
            1000, 625, 385, 250, 275, 125, 95, 75, 60, 50, 40
            // This values approximate the folowing function by haslersn.
            // 5000 / (x² + 2x + 5)
    };
    
    // GAME WINDOW
    private GameWindow gameWindow;
    
    // REGISTRIES
    private TextureRegistry            textureRegistry;
    private final StandardTaskRegistry taskRegistry;
    
    // CURRENT SIMULATION
    private StandardSimulationClock simulationClock;
    
    // LISTENERS
    private ButtonStateListener buttonStateListener;
    private SpeedSliderListener speedSliderListener;
    private EntityDrawListener  entityDrawListener;
    
    // CURRENT SIMULATION AND TASKS
    private String                     currentTaskName    = null;
    private StandardSimulation         currentSimulation  = null;
    private CompletableFuture<Boolean> currentRunningTask = null;
    
    private SimulationTreeListener simulationTreeListener;
    
    /**
     * Default Constructor
     */
    public StandardSimulationProxy() {
        this.simulationClock = null;
        this.taskRegistry = new StandardTaskRegistry();
    }
    
    /**
     * Set the game window associated with this simulation proxy.
     * 
     * @param gameWindow
     */
    public void setGameWindow(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        this.textureRegistry = gameWindow.getTextureRegistry();
    }
    
    @Override
    public TaskRegistry getTaskRegistry() {
        return this.taskRegistry;
    }
    
    @Override
    public TextureRegistry getTextureRegistry() {
        return this.textureRegistry;
    }
    
    /**
     * Setter function to set a simulation clock. Use null to remove a clock
     *
     * @param simulationClock
     *     the simulationClock to set
     */
    public void setSimulationClock(final StandardSimulationClock simulationClock) {
        if (this.simulationClock != null) {
            if (this.simulationClock.isRunning()) {
                this.simulationClock.stop();
            }
            this.simulationClock.setSimulationProxy(null);
        }
        
        if (simulationClock == null) {
            this.buttonStateListener.changeButtonState(ClockButtonState.BLOCKED);
        } else {
            //FIXME check for clean simulation and if unclean either clean it or
            // make the button state PAUSED.
            this.buttonStateListener.changeButtonState(ClockButtonState.STOPPED);
        }
        
        this.simulationClock = simulationClock;
        this.simulationClock.setSimulationProxy(this);
    }
    
    @Override
    public void setButtonStateListener(final ButtonStateListener listener) {
        if ((this.buttonStateListener == null) || (listener == null)) {
            this.buttonStateListener = listener;
        } else throw new ListenerSetException();
    }
    
    @Override
    public void buttonPressed(final ButtonType type) {
        switch (type) {
            case PLAY:
                if (this.simulationClock == null) return;
                if (!this.simulationClock.isRunning()) {
                    this.simulationClock.startInternal();
                }
                this.buttonStateListener.changeButtonState(ClockButtonState.PLAYING);
                break;
            
            case STEP:
                if (this.simulationClock == null) return;
                if (!this.simulationClock.isRunning()) {
                    this.simulationClock.step();
                } else {
                    this.simulationClock.stopInternal();
                }
                this.buttonStateListener.changeButtonState(ClockButtonState.PAUSED);
                break;
            
            case PAUSE:
                if (this.simulationClock == null) return;
                if (this.simulationClock.isRunning()) {
                    this.simulationClock.stopInternal();
                }
                this.buttonStateListener.changeButtonState(ClockButtonState.PAUSED);
                break;
            
            case STOP:
                if (this.simulationClock == null) return;
                if (this.simulationClock.isRunning()) {
                    this.simulationClock.stopInternal();
                }
                this.switchTask(this.currentTaskName);
                this.buttonStateListener.changeButtonState(ClockButtonState.STOPPED);
                break;
            
            case VIEW:
                //TODO implement
                break;
            
            case ENTITY:
                //TODO implement
                break;
            
            default:
        }
    }
    
    @Override
    public void setSpeedSliderListener(SpeedSliderListener listener) {
        if ((this.speedSliderListener == null) || (listener == null)) {
            this.speedSliderListener = listener;
        } else throw new ListenerSetException();
    }
    
    @Override
    public void simulationSpeedChange(final int value) {
        if (this.simulationClock == null) return;
        
        this.simulationClock.setPeriod(StandardSimulationProxy.SIMULATION_TIMES[value]);
    }
    
    @Override
    public void setTaskSelectorListener(TaskSelectorListener listener) {
        this.taskRegistry.setTaskSelectorListener(listener);
    }
    
    @Override
    public void selectedTaskChange(String element) {
        if (!element.equals(this.currentTaskName)) {
            this.gameWindow.getPlayfieldDrawer().resetZoomAndPan();
            this.switchTask(element);
        }
    }
    
    private synchronized void switchTask(String newTaskName) {
        
        final Task task = this.taskRegistry.getTask(newTaskName);
        
        // CLEANUP
        if (this.currentSimulation != null) {
            this.currentSimulation.setEntityDrawListener(null);
        }
        if (this.currentRunningTask != null) {
            this.currentRunningTask.cancel(true);
        }
        
        // SETUP NEW
        
        final StandardPlayfield playfield = new StandardPlayfield();
        final StandardSimulationClock newSimulationClock = new StandardSimulationClock();
        
        final StandardEntityProgramRegistry entityProgramRegistry = new StandardEntityProgramRegistry();
        final StandardEntityProgramRunner entityProgramRunner = new StandardEntityProgramRunner(entityProgramRegistry);
        
        final StandardSimulation simulation = new StandardSimulation(
                playfield, newSimulationClock, entityProgramRegistry, entityProgramRunner
        );
        simulation.initialize();
        final StandardTaskRunner taskRunner = new StandardTaskRunner(task, simulation);
        
        simulation.setEntityDrawListener(this.entityDrawListener);
        
        // START TASK
        final CompletableFuture<Boolean> runningTask = taskRunner.runTask();
        
        // REPLACE OLD
        this.setSimulationClock(newSimulationClock);
        this.currentTaskName = newTaskName;
        this.currentSimulation = simulation;
        this.currentRunningTask = runningTask;
        this.gameWindow.setWindowTitle("Task: " + newTaskName);
    }
    
    @Override
    public void setEntityDrawListener(EntityDrawListener listener) {
        if ((this.entityDrawListener == null) || (listener == null)) {
            this.entityDrawListener = listener;
        } else throw new ListenerSetException();
    }
    
    @Override
    public void setSimulationTreeListener(SimulationTreeListener listener) {
        if ((this.simulationTreeListener == null) || (listener == null)) {
            this.simulationTreeListener = listener;
        } else throw new ListenerSetException();
    }
    
    @Override
    public void selectedSimulationEntityChange(SimulationTreeNode node) {
        // Intentionally left blank
    }
}

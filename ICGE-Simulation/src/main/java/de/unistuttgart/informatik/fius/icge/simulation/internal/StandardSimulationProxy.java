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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import de.unistuttgart.informatik.fius.icge.log.Logger;
import de.unistuttgart.informatik.fius.icge.simulation.Playfield;
import de.unistuttgart.informatik.fius.icge.simulation.Position;
import de.unistuttgart.informatik.fius.icge.simulation.Simulation;
import de.unistuttgart.informatik.fius.icge.simulation.SimulationHost;
import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;
import de.unistuttgart.informatik.fius.icge.simulation.entity.EntityTypeRegistry;
import de.unistuttgart.informatik.fius.icge.simulation.exception.CannotRunProgramException;
import de.unistuttgart.informatik.fius.icge.simulation.exception.EntityNotOnFieldException;
import de.unistuttgart.informatik.fius.icge.simulation.internal.actions.StandardActionLog;
import de.unistuttgart.informatik.fius.icge.simulation.internal.entity.StandardEntityTypeRegistry;
import de.unistuttgart.informatik.fius.icge.simulation.internal.entity.program.StandardEntityProgramRegistry;
import de.unistuttgart.informatik.fius.icge.simulation.internal.entity.program.StandardEntityProgramRunner;
import de.unistuttgart.informatik.fius.icge.simulation.internal.playfield.StandardPlayfield;
import de.unistuttgart.informatik.fius.icge.simulation.internal.tasks.StandardTaskRegistry;
import de.unistuttgart.informatik.fius.icge.simulation.internal.tasks.StandardTaskRunner;
import de.unistuttgart.informatik.fius.icge.simulation.tasks.Task;
import de.unistuttgart.informatik.fius.icge.simulation.tasks.TaskRegistry;
import de.unistuttgart.informatik.fius.icge.ui.GameWindow;
import de.unistuttgart.informatik.fius.icge.ui.exception.ListenerSetException;
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
            // 0,   1,   2,   3,  4,  5,  6,  7,  8,  9, 10
            1000, 415, 200, 115, 75, 50, 42, 34, 26, 18, 10
            // These values are aproximated by two functions originally by haslersn which where modified by waeltkts
    };
    
    // GAME WINDOW
    private GameWindow gameWindow;
    
    // REGISTRIES
    private TextureRegistry                  textureRegistry;
    private final StandardTaskRegistry       taskRegistry;
    private final StandardEntityTypeRegistry entityTypeRegistry;
    
    // CURRENT SIMULATION
    private StandardSimulationClock simulationClock;
    
    // LISTENERS
    private ButtonStateListener     buttonStateListener;
    private EntitySelectorListener  entitySelectorListener;
    private SpeedSliderListener     speedSliderListener;
    private EntityDrawListener      entityDrawListener;
    private SimulationTreeListener  simulationTreeListener;
    private EntityInspectorListener entityInspectorListener;
    private ToolStateListener       toolStateListener;
    
    // CURRENT SIMULATION AND TASKS
    private String                     currentTaskName    = null;
    private StandardSimulation         currentSimulation  = null;
    private CompletableFuture<Boolean> currentRunningTask = null;
    
    /**
     * Default Constructor
     */
    public StandardSimulationProxy() {
        this.simulationClock = null;
        this.taskRegistry = new StandardTaskRegistry();
        this.entityTypeRegistry = new StandardEntityTypeRegistry();
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
    
    @Override
    public EntityTypeRegistry getEntityTypeRegistry() {
        return this.entityTypeRegistry;
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
            if (simulationClock.isRunning()) {
                this.buttonStateListener.changeButtonState(ClockButtonState.PLAYING);
            } else {
                this.buttonStateListener.changeButtonState(ClockButtonState.STOPPED);
            }
        }
        
        this.simulationClock = simulationClock;
        this.simulationClock.setSimulationProxy(this);
        this.simulationSpeedChange(this.speedSliderListener.getSpeed());
    }
    
    @Override
    public void setButtonStateListener(final ButtonStateListener listener) {
        if ((this.buttonStateListener == null) || (listener == null)) {
            this.buttonStateListener = listener;
        } else throw new ListenerSetException();
        if (listener != null && this.toolStateListener != null) {
            listener.changeButtonState(ControlButtonState.VIEW);
        }
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
                this.buttonStateListener.changeButtonState(ControlButtonState.VIEW);
                this.toolStateListener.setSelectedTool(ControlButtonState.VIEW);
                break;
            
            case ADD:
                this.buttonStateListener.changeButtonState(ControlButtonState.ADD);
                this.toolStateListener.setSelectedTool(ControlButtonState.ADD);
                break;
            
            case SUB:
                this.buttonStateListener.changeButtonState(ControlButtonState.SUB);
                this.toolStateListener.setSelectedTool(ControlButtonState.SUB);
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
            this.simulationClock.shutdown();
            this.currentSimulation.setEntityDrawListener(null);
        }
        if (this.currentRunningTask != null) {
            this.currentRunningTask.cancel(true);
        }
        
        this.gameWindow.getConsole().clearSimulationConsole();
        
        // SETUP NEW
        
        final StandardPlayfield playfield = new StandardPlayfield();
        final StandardSimulationClock newSimulationClock = new StandardSimulationClock();
        
        final StandardEntityProgramRegistry entityProgramRegistry = new StandardEntityProgramRegistry();
        final StandardEntityProgramRunner entityProgramRunner = new StandardEntityProgramRunner(entityProgramRegistry);
        
        final StandardActionLog actionLog = new StandardActionLog();
        
        final StandardSimulation simulation = new StandardSimulation(
                playfield, newSimulationClock, entityProgramRegistry, entityProgramRunner, actionLog
        );
        simulation.initialize();
        final StandardTaskRunner taskRunner = new StandardTaskRunner(task, simulation);
        
        simulation.setEntityDrawListener(this.entityDrawListener);
        // reset currentTick in playfield drawer
        this.gameWindow.getPlayfieldDrawer().draw(0);
        
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
    public void setEntitySelectorListener(EntitySelectorListener listener) {
        if ((this.entitySelectorListener == null) || (listener == null)) {
            this.entitySelectorListener = listener;
        } else throw new ListenerSetException();
        
        this.entityTypeRegistry.setEntitySelectorListener(listener);
        
        if (listener != null) {
            listener.enable();
            String typeName = listener.getCurrentEntity();
            String textureHandle = null;
            if (typeName != null && !typeName.equals("")) {
                textureHandle = this.entityTypeRegistry.getTextureHandleOfEntityType(typeName);
            }
            try {
                this.toolStateListener.setSelectedEntityType(typeName, textureHandle);
            } catch (@SuppressWarnings("unused") NullPointerException e) {
                // catching exception instead of checking before allows to avoid synchronization here
            }
        }
    }
    
    @Override
    public void selectedEntityChanged(String name) {
        String textureHandle = null;
        if (name != null && !name.equals("")) {
            textureHandle = this.entityTypeRegistry.getTextureHandleOfEntityType(name);
        }
        if (this.toolStateListener != null) {
            this.toolStateListener.setSelectedEntityType(name, textureHandle);
        }
    }
    
    @Override
    public void setEntityDrawListener(EntityDrawListener listener) {
        if ((this.entityDrawListener == null) || (listener == null)) {
            this.entityDrawListener = listener;
        } else throw new ListenerSetException();
    }
    
    @Override
    public void setToolStateListener(ToolStateListener listener) {
        if ((this.toolStateListener == null) || (listener == null)) {
            this.toolStateListener = listener;
        } else throw new ListenerSetException();
        
        if (listener == null) return;
        
        try {
            this.buttonStateListener.changeButtonState(ControlButtonState.VIEW);
        } catch (@SuppressWarnings("unused") NullPointerException e) {
            // catching exception instead of checking before allows to avoid synchronization here
        }
        try {
            String typeName = this.entitySelectorListener.getCurrentEntity();
            String textureHandle = null;
            if (typeName != null && !typeName.equals("")) {
                textureHandle = this.entityTypeRegistry.getTextureHandleOfEntityType(typeName);
            }
            listener.setSelectedEntityType(typeName, textureHandle);
        } catch (@SuppressWarnings("unused") NullPointerException e) {
            // catching exception instead of checking before allows to avoid synchronization here
        }
    }
    
    @Override
    public Set<String> getAvailableProgramsForEntityType(String typeName) {
        final Simulation sim = this.currentSimulation;
        if (sim == null) return new HashSet<>(); // no simulation
        try {
            final Entity entity = this.entityTypeRegistry.getNewEntity(typeName);
            return sim.getEntityProgramRegistry().getProgramsForEntity(entity);
        } catch (Exception e) {
            Logger.simulation.println("Could not load program list for entity type " + typeName + ". (See system log for details.)");
            e.printStackTrace(Logger.error);
        }
        return new HashSet<>();
    }
    
    @Override
    public void spawnEntityAt(String typeName, int x, int y, String program) {
        final Simulation sim = this.currentSimulation;
        if (sim == null) return; // no simulation
        
        final Playfield field = sim.getPlayfield();
        try {
            final Entity ent = this.entityTypeRegistry.getNewEntity(typeName);
            if (ent == null) {
                Logger.simulation.println("Could not create a new entity of type " + typeName + "!");
                return;
            }
            field.addEntity(new Position(x, y), ent);
            if (program != null && !program.equals("")) {
                this.currentSimulation.getEntityProgramRunner().run(program, ent);
            }
        } catch (CannotRunProgramException e) {
            Logger.simulation.println("Could not run program " + program + " for the new entity. (See system log for details.)");
            e.printStackTrace(Logger.error);
        } catch (Exception e) {
            Logger.simulation.println("Something went wrong while creating new entity. (See system log for details.)");
            e.printStackTrace(Logger.error);
        }
    }
    
    @Override
    public void clearCell(int x, int y) {
        final Simulation sim = this.currentSimulation;
        if (sim == null) return; // no simulation
        
        final Playfield field = sim.getPlayfield();
        final List<Entity> toRemove = field.getEntitiesAt(new Position(x, y));
        
        toRemove.forEach(entity -> {
            try {
                field.removeEntity(entity);
            } catch (@SuppressWarnings("unused") IllegalArgumentException | EntityNotOnFieldException e) {
                // nothing to do because entity was either null or already not on the field
            }
        });
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
    
    @Override
    public void setEntityInspectorListener(EntityInspectorListener listener) {
        if ((this.entityInspectorListener == null) || (listener == null)) {
            this.entityInspectorListener = listener;
        } else throw new ListenerSetException();
    }
    
    @Override
    public void entityValueChange(String name, String value) {
        // Intentionally left blank
    }
}

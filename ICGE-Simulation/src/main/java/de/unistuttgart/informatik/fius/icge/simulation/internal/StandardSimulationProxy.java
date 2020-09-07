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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import de.unistuttgart.informatik.fius.icge.simulation.Position;
import de.unistuttgart.informatik.fius.icge.simulation.Simulation;
import de.unistuttgart.informatik.fius.icge.simulation.TaskVerifier;
import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;
import de.unistuttgart.informatik.fius.icge.simulation.exception.EntityNotOnFieldException;
import de.unistuttgart.informatik.fius.icge.simulation.inspection.InspectionManager;
import de.unistuttgart.informatik.fius.icge.simulation.internal.StandardSimulationClock.StateChangeListener;
import de.unistuttgart.informatik.fius.icge.simulation.internal.entity.StandardEntityTypeRegistry;
import de.unistuttgart.informatik.fius.icge.simulation.internal.playfield.StandardPlayfield;
import de.unistuttgart.informatik.fius.icge.ui.Drawable;
import de.unistuttgart.informatik.fius.icge.ui.EntityInspectorEntry;
import de.unistuttgart.informatik.fius.icge.ui.GameWindow;
import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy;
import de.unistuttgart.informatik.fius.icge.ui.SimulationTreeNode;
import de.unistuttgart.informatik.fius.icge.ui.TaskInformation;
import de.unistuttgart.informatik.fius.icge.ui.Toolbar.ClockButtonState;
import de.unistuttgart.informatik.fius.icge.ui.Toolbar.ControlButtonState;


/**
 * StandardSimulationProxy
 *
 * See the comment of the interface for what this class is used for.
 *
 * @author Tobias Wältken, Tim Neumann
 * @version 1.0
 */
public class StandardSimulationProxy implements SimulationProxy {
    
    /** A lookup table for the simulation times */
    public static final int[] SIMULATION_TIMES = {
            // 0,   1,   2,   3,  4,  5,  6,  7,  8,  9, 10
            1000, 415, 200, 115, 75, 50, 42, 34, 26, 18, 10
            // These values are aproximated by two functions originally by haslersn which where modified by waeltkts
    };
    
    // GAME WINDOW
    private GameWindow gameWindow;
    private boolean    stopSimulationWithWindowClose = false;
    
    // MANAGERS
    private final InspectionManager inspectionManager;
    
    // CURRENT SIMULATION
    private final Simulation                      simulation;
    private final StandardEntityTypeRegistry      entityTypeRegistry;
    private final StandardSimulationClock         simulationClock;
    private final StandardPlayfield               playfield;
    private final TaskVerifier                    taskVerifier;
    private final Map<SimulationTreeNode, Entity> simualtionSidebarMap;
    
    private Entity entityToInspect;
    
    /**
     * Create a new standard simulation proxy
     *
     * @param simulation
     *     The Simulation to use
     * @param simulationClock
     *     The simulation clock to use
     * @param inspectionManager
     *     The inspection manager to use
     * @param entityTypeRegistry
     *     The entity type registry to use
     * @param playfield
     *     The playfield to use
     * @param taskVerifier
     *     the task verifier to use to verify the task completion status
     */
    public StandardSimulationProxy(
            final Simulation simulation, final StandardSimulationClock simulationClock, final InspectionManager inspectionManager,
            final StandardEntityTypeRegistry entityTypeRegistry, final StandardPlayfield playfield, final TaskVerifier taskVerifier
    ) {
        this.simulation = simulation;
        this.simulationClock = simulationClock;
        this.inspectionManager = inspectionManager;
        this.entityTypeRegistry = entityTypeRegistry;
        this.playfield = playfield;
        this.taskVerifier = taskVerifier;
        this.simualtionSidebarMap = new ConcurrentHashMap<>();
        
        // attach tick listeners to simulation clock
        // only do this once per SimulationProxy as unsetting these listeners is not possible atm
        this.simulationClock.setAnimationTickListener(new Consumer<Long>() {
            @Override
            public void accept(final Long tickCount) {
                if (StandardSimulationProxy.this.gameWindow != null) {
                    StandardSimulationProxy.this.gameWindow.getPlayfieldDrawer().draw(tickCount);
                }
            }
        });
        
        this.simulationClock.registerPostTickListener(unused -> {
            updateEntityInspector();
            return true; // post tick listener could be removed by returning false here
        });
    }
    
    @Override
    public void attachToGameWindow(final GameWindow window) {
        this.attachToGameWindow(window, false);
    }
    
    @Override
    public void attachToGameWindow(final GameWindow window, final boolean stopWithWindowClose) {
        if (this.gameWindow != null) throw new IllegalStateException("Already attached to a window!");
        this.gameWindow = window;
        this.stopSimulationWithWindowClose = stopWithWindowClose;
        
        //Simulation Clock
        if (this.simulationClock.isRunning()) {
            this.gameWindow.getToolbar().setClockButtonState(ClockButtonState.PLAYING);
        } else {
            this.gameWindow.getToolbar().setClockButtonState(ClockButtonState.PAUSED);
        }
        this.simulationSpeedChange(this.gameWindow.getToolbar().getSpeedSliderPosition());
        
        this.simulationClock.setStateChangeListener(new StateChangeListener() {
            @Override
            public void clockStarted() {
                StandardSimulationProxy.this.gameWindow.getToolbar().setClockButtonState(ClockButtonState.PLAYING);
            }
            
            @Override
            public void clockPaused() {
                StandardSimulationProxy.this.gameWindow.getToolbar().setClockButtonState(ClockButtonState.PAUSED);
            }
        });
        
        //EntityDrawing
        
        this.playfield.setDrawablesChangedListener(new Consumer<List<Drawable>>() {
            @Override
            public void accept(final List<Drawable> drawables) {
                if (StandardSimulationProxy.this.gameWindow != null) {
                    StandardSimulationProxy.this.gameWindow.getPlayfieldDrawer().setDrawables(drawables);
                }
            }
        });
        
        //ControlButtonState
        this.gameWindow.getToolbar().setControlButtonState(ControlButtonState.VIEW);
        
        //EntitySelection
        this.entityTypeRegistry.setEntityRegisteredListener((entityName, textureHandle) -> {
            if (StandardSimulationProxy.this.gameWindow != null) {
                StandardSimulationProxy.this.gameWindow.getToolbar().addEntity(entityName, textureHandle);
            }
        });
        this.gameWindow.getToolbar().enableEntitySelector();
        final String typeName = this.gameWindow.getToolbar().getCurrentlySelectedEntity();
        String textureHandle = null;
        if ((typeName != null) && !typeName.equals("")) {
            textureHandle = this.entityTypeRegistry.getTextureHandleOfEntityType(typeName);
        }
        try {
            this.gameWindow.getPlayfieldDrawer().setSelectedEntityType(typeName, textureHandle);
        } catch (@SuppressWarnings("unused") final NullPointerException e) {
            // catching exception instead of checking before allows to avoid synchronization here
        }
        
        this.gameWindow.getEntitySidebar().setSimulationTreeRootNode(this.playfield.getSimulationTree());
        this.gameWindow.getEntitySidebar().enableSimulationTree();
        this.playfield.setSimulationTreeEntityAddedListener((node, entity) -> {
            this.simualtionSidebarMap.put(node, entity);
            this.gameWindow.getEntitySidebar().updateSimulationTree();
        });
        this.playfield.setSimulationTreeEntityRemovedListener(node -> {
            this.simualtionSidebarMap.remove(node);
            this.gameWindow.getEntitySidebar().updateSimulationTree();
        });
        
        this.gameWindow.getEntitySidebar().disableEntityInspector();
        
        // taskState
        TaskInformation task = null;
        if (this.taskVerifier != null) {
            task = this.taskVerifier.getTaskInformation();
        }
        this.gameWindow.getTaskStatusDisplay().setTaskInformation(task);
        
        this.gameWindow.setSimulationProxy(this);
    }
    
    @Override
    public void windowClosed() {
        // clear listeners first
        // but do not clear tick listeners (see constructor for explanation)
        this.simulationClock.removeStateChangeListener();
        this.playfield.removeDrawablesChangedListener();
        this.playfield.removeSimulationTreeEntityAddedListener();
        this.playfield.removeSimulationTreeEntityRemovedListener();
        this.entityTypeRegistry.removeEntityRegisteredListener();
        
        // remove gameWindow reference
        this.gameWindow = null;
        
        // stop simulation
        if (this.stopSimulationWithWindowClose) {
            this.simulation.stop();
        }
    }
    
    @Override
    public void buttonPressed(final ButtonType type) {
        switch (type) {
            case PLAY:
                if (!this.simulationClock.isRunning()) {
                    this.simulationClock.startInternal();
                }
                this.gameWindow.getToolbar().setClockButtonState(ClockButtonState.PLAYING);
                break;
            
            case STEP:
                if (!this.simulationClock.isRunning()) {
                    this.simulationClock.step();
                } else {
                    this.simulationClock.stopInternal();
                }
                this.gameWindow.getToolbar().setClockButtonState(ClockButtonState.PAUSED);
                break;
            
            case PAUSE:
                if (this.simulationClock.isRunning()) {
                    this.simulationClock.stopInternal();
                }
                this.gameWindow.getToolbar().setClockButtonState(ClockButtonState.PAUSED);
                break;
            
            case VIEW:
                this.gameWindow.getToolbar().setControlButtonState(ControlButtonState.VIEW);
                this.gameWindow.getPlayfieldDrawer().setSelectedTool(ControlButtonState.VIEW);
                break;
            
            case ADD:
                this.gameWindow.getToolbar().setControlButtonState(ControlButtonState.ADD);
                this.gameWindow.getPlayfieldDrawer().setSelectedTool(ControlButtonState.ADD);
                break;
            
            case SUB:
                this.gameWindow.getToolbar().setControlButtonState(ControlButtonState.SUB);
                this.gameWindow.getPlayfieldDrawer().setSelectedTool(ControlButtonState.SUB);
                break;
            
            default:
        }
    }
    
    @Override
    public void simulationSpeedChange(final int value) {
        this.simulationClock.setPeriod(StandardSimulationProxy.SIMULATION_TIMES[value]);
    }
    
    @Override
    public void selectedEntityChanged(final String name) {
        String textureHandle = null;
        if ((name != null) && !name.equals("")) {
            textureHandle = this.entityTypeRegistry.getTextureHandleOfEntityType(name);
        }
        
        this.gameWindow.getPlayfieldDrawer().setSelectedEntityType(name, textureHandle);
    }
    
    @Override
    public void refreshTaskInformation() {
        TaskInformation task = null;
        if (this.taskVerifier != null) {
            this.taskVerifier.verify();
            task = this.taskVerifier.getTaskInformation();
        }
        this.gameWindow.getTaskStatusDisplay().setTaskInformation(task);
    }
    
    @Override
    public void spawnEntityAt(final String typeName, final int x, final int y) {
        try {
            final Entity ent = this.entityTypeRegistry.getNewEntity(typeName);
            if (ent == null) {
                System.out.println("Could not create a new entity of type " + typeName + "!");
                return;
            }
            this.playfield.addEntity(new Position(x, y), ent);
        } catch (final Exception e) {
            System.out.println("Something went wrong while creating new entity.");
            e.printStackTrace();
        }
    }
    
    @Override
    public void clearCell(final int x, final int y) {
        final List<Entity> toRemove = this.playfield.getEntitiesAt(new Position(x, y));
        
        toRemove.forEach(entity -> {
            try {
                this.playfield.removeEntity(entity);
            } catch (@SuppressWarnings("unused") IllegalArgumentException | EntityNotOnFieldException e) {
                // nothing to do because entity was either null or already not on the field
            }
        });
    }
    
    private EntityInspectorEntry[] getEntries(final Entity e) {
        final List<EntityInspectorEntry> result = new ArrayList<>();
        
        for (final String name : this.inspectionManager.getAttributeNamesOfEntity(e)) {
            String type = "string";
            if (!this.inspectionManager.isAttributeEditable(e, name)) {
                type = "readonly_string";
            }
            //TODO: this.inspectionManager.getAttributeType(e, name)
            final String value = this.inspectionManager.getAttributeValue(e, name).toString();
            result.add(new EntityInspectorEntry(name, type, value, newValue -> {
                this.inspectionManager.setAttributeValue(e, name, newValue);
                this.playfield.drawEntities();
                this.gameWindow.getPlayfieldDrawer().draw(this.simulationClock.getLastRenderTickNumber());
                this.updateEntityInspector();
            }));
        }
        
        for (final String name : this.inspectionManager.getMethodNamesOfEntity(e)) {
            final String type = "function";
            result.add(new EntityInspectorEntry(name, type, "", unused -> {
                this.inspectionManager.invokeMethod(e, name);
                this.playfield.drawEntities();
                this.gameWindow.getPlayfieldDrawer().draw(this.simulationClock.getLastRenderTickNumber());
                this.updateEntityInspector();
            }));
        }
        return result.toArray(new EntityInspectorEntry[result.size()]);
    }
    
    private void updateEntityInspector() {
        if (this.gameWindow == null) return;
        if (this.entityToInspect == null) return;
        this.gameWindow.getEntitySidebar().setEntityInspectorEntries(this.getEntries(this.entityToInspect));
    }
    
    @Override
    public void selectedSimulationEntityChange(final SimulationTreeNode node) {
        if (node == null) {
            this.entityToInspect = null;
        } else {
            this.entityToInspect = this.simualtionSidebarMap.get(node);
        }
        
        if (this.entityToInspect != null) {
            this.gameWindow.getEntitySidebar().enableEntityInspector();
            this.gameWindow.getEntitySidebar().setEntityInspectorName(this.entityToInspect.toString());
            this.gameWindow.getEntitySidebar().setEntityInspectorEntries(this.getEntries(this.entityToInspect));
        } else {
            this.gameWindow.getEntitySidebar().setEntityInspectorName("");
            this.gameWindow.getEntitySidebar().setEntityInspectorEntries(new EntityInspectorEntry[0]);
            this.gameWindow.getEntitySidebar().disableEntityInspector();
        }
    }
    
    @Override
    public void entityValueChange(final String name, final String value) {
        // Intentionally left blank
    }
}

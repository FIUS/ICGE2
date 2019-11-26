/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.manualstart;

import java.util.HashSet;
import java.util.Set;

import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy;
import de.unistuttgart.informatik.fius.icge.ui.SimulationTreeNode;


/**
 * A MinimalSimulationProxy implementation. This implementation is used if you want to spwan a ui but don't care about a
 * correct proxy. This is only a class designed for testing and not for productivity use. Only use this class if you
 * know what you are doing.
 * 
 * The class is deprecated to avoid acidental and uninformed use!
 * 
 * @author Tobias Wältken
 * @version 1.0
 */
public class MinimalSimulationProxy implements SimulationProxy {
    
    /** The button state listener */
    public ButtonStateListener buttonStateListener;
    
    /** The speed slider listener */
    public SpeedSliderListener speedSliderListener;
    
    /** The task selector listener */
    public TaskSelectorListener taskSelectorListener;
    
    /** The entity selector listener */
    public EntitySelectorListener entitySelectorListener;
    
    /** The entity draw listener */
    public EntityDrawListener entityDrawListener;
    
    /** The tool state listener */
    public ToolStateListener toolStateListener;
    
    /** The simulation tree listener */
    public SimulationTreeListener simulationTreeListener;
    
    /** The entity inspector listener */
    public EntityInspectorListener entityInspectorListener;
    
    @Override
    public void setButtonStateListener(ButtonStateListener listener) {
        this.buttonStateListener = listener;
    }
    
    @Override
    public void buttonPressed(ButtonType type) {
        // Intentionally left blank
    }
    
    @Override
    public void setSpeedSliderListener(SpeedSliderListener listener) {
        this.speedSliderListener = listener;
    }
    
    @Override
    public void simulationSpeedChange(int value) {
        // Intentionally left blank
    }
    
    @Override
    public void setTaskSelectorListener(TaskSelectorListener listener) {
        this.taskSelectorListener = listener;
    }
    
    @Override
    public void selectedTaskChange(String element) {
        // Intentionally left blank
    }
    
    @Override
    public void setEntitySelectorListener(EntitySelectorListener listener) {
        this.entitySelectorListener = listener;
    }
    
    @Override
    public void selectedEntityChanged(String name) {
        // Intentionally left blank
    }
    
    @Override
    public void setEntityDrawListener(EntityDrawListener listener) {
        this.entityDrawListener = listener;
    }
    
    @Override
    public void setSimulationTreeListener(SimulationTreeListener listener) {
        this.simulationTreeListener = listener;
    }
    
    @Override
    public void selectedSimulationEntityChange(SimulationTreeNode node) {
        // Intentionally left blank
    }
    
    @Override
    public void setEntityInspectorListener(EntityInspectorListener listener) {
        this.entityInspectorListener = listener;
    }
    
    @Override
    public void entityValueChange(String name, String value) {
        // Intentionally left blank
    }
    
    @Override
    public void setToolStateListener(ToolStateListener listener) {
        this.toolStateListener = listener;
    }
    
    @Override
    public Set<String> getAvailableProgramsForEntityType(String typeName) {
        return new HashSet<>();
    }
    
    @Override
    public void spawnEntityAt(String typeName, int x, int y) {
        // Intentionally left blank
    }
    
    @Override
    public void clearCell(int x, int y) {
        // Intentionally left blank
    }
}

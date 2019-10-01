/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.ui;

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
@Deprecated
public class MinimalSimulationProxy implements SimulationProxy {
    
    /** The button state listener */
    public ButtonStateListener buttonStateListener;
    
    /** The speed slider listener */
    public SpeedSliderListener speedSliderListener;
    
    /** The task selector listener */
    public TaskSelectorListener taskSelectorListener;
    
    /** The simulation tree listener */
    public SimulationTreeListener simulationTreeListener;

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
    public void setSimulationTreeListener(SimulationTreeListener listener) {
        this.simulationTreeListener = listener;
    }
    
    @Override
    public void selectedSimulationEntityChange(SimulationTreeNode node) {
        // Intentionally left blank
    }
}

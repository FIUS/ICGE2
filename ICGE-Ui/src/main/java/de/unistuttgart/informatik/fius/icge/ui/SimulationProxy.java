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
 * The SimulationProxy interface
 *
 * @author Tobias Wältken
 * @version 1.0
 */
public interface SimulationProxy {

    //
    // Toolbar
    //

    public enum ClockButtonState {
        PLAYING,
        PAUSED,
        STOPPED,
        BLOCKED
    }

    public enum ControlButtonState {
        VIEW,
        ENTITY,
        BLOCKED
    }

    public interface ButtonStateListener {

        public void changeButtonState(ClockButtonState state);
        public void changeButtonState(ControlButtonState state);
    }

    public void setButtonStateListener(ButtonStateListener listener);

    public enum ButtonType {
        PLAY,
        STEP,
        PAUSE,
        STOP,
        VIEW,
        ENTITY
    }

    public void buttonPressed(ButtonType type);

    public void simulationSpeedChange(int value);
}

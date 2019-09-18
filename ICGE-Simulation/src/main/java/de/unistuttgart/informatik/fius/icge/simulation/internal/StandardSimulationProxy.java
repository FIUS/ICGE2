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

import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy;

/**
 * StandardSimulationProxy
 *
 * @author Tobias Wältken
 * @version 1.0
 */
public class StandardSimulationProxy implements SimulationProxy {

    /** A lookup table for the simulation times */
    public static final int[] SIMULATION_TIMES = {
        // 0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10
        1000, 625, 385, 250, 275, 125,  95,  75,  60,  50,  40
        // This values approximate the folowing function by haslersn.
        // 5000 / (x² + 2x + 5)
    };

    private ButtonStateListener buttonStateListener;

    private StandardSimulationClock simulationClock;

    /**
     * Default Constructor
     */
    public StandardSimulationProxy() {
        this.simulationClock = null;
    }

    /**
     * Setter function to set a simulation clock. Use null to remove a clock
     *
     * @param simulationClock the simulationClock to set
     */
    public void setSimulationClock(StandardSimulationClock simulationClock) {
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
    public void setButtonStateListener(ButtonStateListener listener) {
        if (this.buttonStateListener == null || listener == null)
            this.buttonStateListener = listener;
    }

    @Override
    public void buttonPressed(ButtonType type) {
        switch (type) {
            case PLAY:
                if (this.simulationClock == null) return;
                if (!this.simulationClock.isRunning())
                    this.simulationClock.startInternal();
                this.buttonStateListener.changeButtonState(ClockButtonState.PLAYING);
                break;

            case STEP:
                if (this.simulationClock == null) return;
                if (!this.simulationClock.isRunning())
                    this.simulationClock.step();
                else
                    this.simulationClock.stopInternal();
                this.buttonStateListener.changeButtonState(ClockButtonState.PAUSED);
                break;

            case PAUSE:
                if (this.simulationClock == null) return;
                if (this.simulationClock.isRunning())
                    this.simulationClock.stopInternal();
                this.buttonStateListener.changeButtonState(ClockButtonState.PAUSED);
                break;

            case STOP:
                if (this.simulationClock == null) return;
                if (this.simulationClock.isRunning())
                    this.simulationClock.stopInternal();
                //TODO clean simulation
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
    public void simulationSpeedChange(int value) {
        if (this.simulationClock == null) return;

        this.simulationClock.setPeriod(StandardSimulationProxy.SIMULATION_TIMES[value]);
    }
}

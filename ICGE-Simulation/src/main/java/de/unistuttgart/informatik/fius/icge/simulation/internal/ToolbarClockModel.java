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

import de.unistuttgart.informatik.fius.icge.simulation.SimulationClock;
import de.unistuttgart.informatik.fius.icge.ui.Toolbar;
import de.unistuttgart.informatik.fius.icge.ui.ToolbarListener;

/**
 * This model connects a {@link SimulationClock} to a {@link Toolbar}
 *
 * @author Tobias Wältken
 * @version 1.0
 */
public class ToolbarClockModel implements ToolbarListener {

    /** A lookup table for the simulation times */
    public static final int[] SIMULATION_TIMES = {
        // 0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10
        1000, 625, 385, 250, 275, 125,  95,  75,  60,  50,  40
        // This values approximate the folowing function by haslersn.
        // 5000 / (x² + 2x + 5)
    };

    private SimulationClock clock;

    /**
     * Default constructor
     *
     * @param clock The clock the model reports to
     */
    public ToolbarClockModel(SimulationClock clock) {
        this.clock = clock;
    }

    @Override
    public void simulationStateChanged(SimulationState state) {
        switch (state) {
            case PLAY:
                if (!this.clock.isRunning())
                    this.clock.start();
                break;
            case PAUSE:
                if (this.clock.isRunning())
                    this.clock.stop();
                break;
            case STOP:
                if (this.clock.isRunning())
                    this.clock.stop();
                //TODO Reset Simulation
                break;
            default:
                break;
        }
    }

    @Override
    public void simulationStepRequested() {
        this.clock.step();
    }

    @Override
    public void simulationSpeedChanged(int speed) {
        this.clock.setPeriod(ToolbarClockModel.SIMULATION_TIMES[speed]);
    }

    @Override
    public void inputModeChanged(InputMode mode) {
        // Intentionally left blank
    }
}

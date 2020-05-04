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

import java.util.Collections;
import java.util.List;
import java.util.Set;


/**
 * A MinimalSimulationProxy implementation. This implementation is used as blank proxy.
 *
 * @author Tobias Wältken
 * @version 1.0
 */
public class MinimalSimulationProxy implements SimulationProxy {

    @Override
    public void attachToGameWindow(GameWindow gameWindow) {
        // Intentionally left blank
    }

    @Override
    public void buttonPressed(ButtonType type) {
        // Intentionally left blank
    }

    @Override
    public void simulationSpeedChange(int value) {
        // Intentionally left blank
    }

    @Override
    public void selectedEntityChanged(String name) {
        // Intentionally left blank
    }

    @Override
    public void drawEntities(long tickCount) {
        // Intentionally left blank
    }

    @Override
    public void setDrawables(List<Drawable> drawables) {
        // Intentionally left blank
    }

    @Override
    public Set<String> getAvailableProgramsForEntityType(String typeName) {
        return Collections.emptySet();
    }

    @Override
    public void spawnEntityAt(String typeName, int x, int y, String program) {
        // Intentionally left blank
    }

    @Override
    public void clearCell(int x, int y) {
        // Intentionally left blank
    }

    @Override
    public void selectedSimulationEntityChange(SimulationTreeNode node) {
        // Intentionally left blank
    }

    @Override
    public void entityValueChange(String name, String value) {
        // Intentionally left blank
    }



}

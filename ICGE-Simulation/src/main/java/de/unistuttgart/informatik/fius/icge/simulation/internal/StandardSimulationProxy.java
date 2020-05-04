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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import de.unistuttgart.informatik.fius.icge.log.Logger;
import de.unistuttgart.informatik.fius.icge.simulation.Position;
import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;
import de.unistuttgart.informatik.fius.icge.simulation.exception.CannotRunProgramException;
import de.unistuttgart.informatik.fius.icge.simulation.exception.EntityNotOnFieldException;
import de.unistuttgart.informatik.fius.icge.simulation.inspection.InspectionManager;
import de.unistuttgart.informatik.fius.icge.simulation.internal.entity.StandardEntityTypeRegistry;
import de.unistuttgart.informatik.fius.icge.simulation.internal.entity.StandardEntityTypeRegistry.EntityRegisteredListener;
import de.unistuttgart.informatik.fius.icge.simulation.internal.entity.program.StandardEntityProgramRegistry;
import de.unistuttgart.informatik.fius.icge.simulation.internal.playfield.StandardPlayfield;
import de.unistuttgart.informatik.fius.icge.ui.Drawable;
import de.unistuttgart.informatik.fius.icge.ui.EntityInspectorEntry;
import de.unistuttgart.informatik.fius.icge.ui.GameWindow;
import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy;
import de.unistuttgart.informatik.fius.icge.ui.SimulationTreeNode;
import de.unistuttgart.informatik.fius.icge.ui.Toolbar.ClockButtonState;
import de.unistuttgart.informatik.fius.icge.ui.Toolbar.ControlButtonState;


/**
 * StandardSimulationProxy
 *
 * @author Tobias Wältken
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

    // MANAGERS
    private final InspectionManager inspectionManager;

    // CURRENT SIMULATION
    private final StandardEntityTypeRegistry      entityTypeRegistry;
    private final StandardSimulationClock         simulationClock;
    private final StandardPlayfield               playfield;
    private final StandardEntityProgramRegistry   entityProgramRegistry;
    private final Map<SimulationTreeNode, Entity> simualtionSidebarMap;

    private Entity entityToInspect;

    /**
     * Default Constructor
     */
    public StandardSimulationProxy(StandardSimulationClock simulationClock, InspectionManager inspectionManager, StandardEntityTypeRegistry entityTypeRegistry, StandardPlayfield playfield, StandardEntityProgramRegistry entityProgramRegistry) {
        this.simulationClock = simulationClock;
        this.inspectionManager = inspectionManager;
        this.entityTypeRegistry = entityTypeRegistry;
        this.playfield = playfield;
        this.entityProgramRegistry = entityProgramRegistry;
        this.simualtionSidebarMap = new ConcurrentHashMap<>();

        //Simulation Clock
        this.simulationClock.setSimulationProxy(this);
    }

    /**
     * Set the game window associated with this simulation proxy.
     *
     * @param gameWindow
     */
    @Override
    public void attachToGameWindow(GameWindow gameWindow) {
        if(this.gameWindow != null) {
            throw new IllegalStateException("Already attached to a window!");
        }
        this.gameWindow = gameWindow;

        //Simulation Clock
        if (this.simulationClock.isRunning()) {
            this.gameWindow.getToolbar().setClockButtonState(ClockButtonState.PLAYING);
        } else {
            this.gameWindow.getToolbar().setClockButtonState(ClockButtonState.STOPPED);
        }
        this.simulationSpeedChange(this.gameWindow.getToolbar().getSpeedSliderPosition());

        //ControlButtonState
        this.gameWindow.getToolbar().setControlButtonState(ControlButtonState.VIEW);

        //EntitySelection
        this.entityTypeRegistry.setEntityRegisteredListener(new EntityRegisteredListener() {
            @Override
            public void entityWasRegistered(String entityName, String textureHandle) {
                StandardSimulationProxy.this.gameWindow.getToolbar().addEntity(entityName, textureHandle);
            }
        });
        this.gameWindow.getToolbar().enableEntitySelector();
        String typeName = this.gameWindow.getToolbar().getCurrentEntity();
        String textureHandle = null;
        if (typeName != null && !typeName.equals("")) {
            textureHandle = this.entityTypeRegistry.getTextureHandleOfEntityType(typeName);
        }
        try {
            this.gameWindow.getPlayfieldDrawer().setSelectedEntityType(typeName, textureHandle);
        } catch (@SuppressWarnings("unused") NullPointerException e) {
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

        this.simulationClock.registerPostTickListener(unused -> {
            updateEntityInspector();
            return true;
        });

        this.gameWindow.setSimulationProxy(this);
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

            case STOP:
                if (this.simulationClock.isRunning()) {
                    this.simulationClock.stopInternal();
                }
                this.gameWindow.getToolbar().setClockButtonState(ClockButtonState.STOPPED);
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
    public void selectedEntityChanged(String name) {
        String textureHandle = null;
        if (name != null && !name.equals("")) {
            textureHandle = this.entityTypeRegistry.getTextureHandleOfEntityType(name);
        }

        this.gameWindow.getPlayfieldDrawer().setSelectedEntityType(name, textureHandle);
    }

    @Override
    public void drawEntities(long tickCount) {
        if(this.gameWindow == null) return;
        this.gameWindow.getPlayfieldDrawer().draw(tickCount);
    }

    @Override
    public void setDrawables(List<Drawable> drawables) {
        if(this.gameWindow == null) return;
        this.gameWindow.getPlayfieldDrawer().setDrawables(drawables);
    }

    @Override
    public Set<String> getAvailableProgramsForEntityType(String typeName) {
        try {
            final Entity entity = this.entityTypeRegistry.getNewEntity(typeName);
            return this.entityProgramRegistry.getProgramsForEntity(entity);
        } catch (Exception e) {
            Logger.simout.println("Could not load program list for entity type " + typeName + ". (See system log for details.)");
            e.printStackTrace(Logger.error);
        }
        return new HashSet<>();
    }

    @Override
    public void spawnEntityAt(String typeName, int x, int y, String program) {
        try {
            final Entity ent = this.entityTypeRegistry.getNewEntity(typeName);
            if (ent == null) {
                Logger.simout.println("Could not create a new entity of type " + typeName + "!");
                return;
            }
            this.playfield.addEntity(new Position(x, y), ent);
            //TODO: Run program or remove that feature.
        } catch (CannotRunProgramException e) {
            Logger.simout.println("Could not run program " + program + " for the new entity. (See system log for details.)");
            e.printStackTrace(Logger.error);
        } catch (Exception e) {
            Logger.simout.println("Something went wrong while creating new entity. (See system log for details.)");
            e.printStackTrace(Logger.error);
        }
    }

    @Override
    public void clearCell(int x, int y) {
        final List<Entity> toRemove = this.playfield.getEntitiesAt(new Position(x, y));

        toRemove.forEach(entity -> {
            try {
                this.playfield.removeEntity(entity);
            } catch (@SuppressWarnings("unused") IllegalArgumentException | EntityNotOnFieldException e) {
                // nothing to do because entity was either null or already not on the field
            }
        });
    }

    private EntityInspectorEntry[] getEntries(Entity e) {
        List<EntityInspectorEntry> result = new ArrayList<>();

        for (String name : this.inspectionManager.getAttributeNamesOfEntity(e)) {
            String type = "string";
            if (!this.inspectionManager.isAttributeEditable(e, name)) {
                type = "readonly_string";
            }
            //TODO: this.inspectionManager.getAttributeType(e, name)
            String value = this.inspectionManager.getAttributeValue(e, name).toString();
            result.add(new EntityInspectorEntry(name, type, value, newValue -> {
                this.inspectionManager.setAttributeValue(e, name, newValue);
                this.playfield.drawEntities();
                this.gameWindow.getPlayfieldDrawer().draw(this.simulationClock.getLastRenderTickNumber());
                updateEntityInspector();
            }));
        }

        for (String name : this.inspectionManager.getMethodNamesOfEntity(e)) {
            String type = "function";
            result.add(new EntityInspectorEntry(name, type, "", unused -> {
                this.inspectionManager.invokeMethod(e, name);
                this.playfield.drawEntities();
                this.gameWindow.getPlayfieldDrawer().draw(this.simulationClock.getLastRenderTickNumber());
                updateEntityInspector();
            }));
        }
        return result.toArray(new EntityInspectorEntry[result.size()]);
    }

    private void updateEntityInspector() {
        if (this.entityToInspect != null) {
            this.gameWindow.getEntitySidebar().setEntityInspectorEntries(getEntries(this.entityToInspect));
        }
    }

    @Override
    public void selectedSimulationEntityChange(SimulationTreeNode node) {
        if (node == null) {
            this.entityToInspect = null;
        } else {
            this.entityToInspect = this.simualtionSidebarMap.get(node);
        }

        if (this.entityToInspect != null) {
            this.gameWindow.getEntitySidebar().enableEntityInspector();
            this.gameWindow.getEntitySidebar().setEntityInspectorName(this.entityToInspect.toString());
            this.gameWindow.getEntitySidebar().setEntityInspectorEntries(getEntries(this.entityToInspect));
        } else {
            this.gameWindow.getEntitySidebar().setEntityInspectorName("");
            this.gameWindow.getEntitySidebar().setEntityInspectorEntries(new EntityInspectorEntry[0]);
            this.gameWindow.getEntitySidebar().disableEntityInspector();
        }
    }

    @Override
    public void entityValueChange(String name, String value) {
        // Intentionally left blank
    }
}

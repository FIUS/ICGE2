/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 *
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.ui.internal;

import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

import de.unistuttgart.informatik.fius.icge.ui.ToolbarManager;
import de.unistuttgart.informatik.fius.icge.ui.ToolbarListener;
import de.unistuttgart.informatik.fius.icge.ui.ToolbarListener.InputMode;
import de.unistuttgart.informatik.fius.icge.ui.ToolbarListener.SimulationState;
import de.unistuttgart.informatik.fius.icge.ui.internal.entity_selector.EntitySelector;


/**
 * An implementation of {@link ToolbarManager} using java swing.
 *
 * @see javax.swing.JToolBar
 * @author Tim Neumann
 * @author Tobias Wältken
 * @version 1.0
 */
public class SwingToolbarManager extends JToolBar implements ToolbarManager {
    private static final long serialVersionUID = -2525998620577603876L;

    /** This array list contains all active listeners */
    private ArrayList<ToolbarListener> listeners;

    /** The play button in the toolbar */
    public JButton play;
    /** The pause button in the toolbar */
    public JButton pause;
    /** The stop button in the toolbar */
    public JButton stop;

    /** A slider to set the simulation time */
    public JSlider simulationTime;

    /** The button to change to view mode */
    public JToggleButton view;
    /** The button to change to entity mode */
    public JToggleButton entity;

    /** The selector which selects the entity for the user to place */
    public EntitySelector entitySelect;


    /**
     * The constructor of the toolbar
     */
    public SwingToolbarManager() {
        //
        // class setup
        //
        this.listeners = new ArrayList<>(2);

        //
        // toolbar setup
        //
        this.setFloatable(false);

        //
        // play button setup
        //
        this.play = new JButton("Play"); // FIXME Replace Text with Icon
        this.play.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                updateSimulationState(SimulationState.PLAY);
            }
        });
        this.add(this.play);

        //
        // pause button setup
        //
        this.pause = new JButton("Pause"); // FIXME Replace Text with Icon
        this.pause.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                updateSimulationState(SimulationState.PAUSE);
            }
        });
        this.add(this.pause);

        //
        // stop button setup
        //
        this.stop = new JButton("Stop"); // FIXME Replace Text with Icon
        this.stop.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                updateSimulationState(SimulationState.STOP);
            }
        });
        this.add(this.stop);

        //
        // simulation time slider setup
        //
        this.addSeparator();
        // setup hashtable for the labels
        Hashtable<Integer, JLabel> labels = new Hashtable<>(3);
        labels.put(0, new JLabel("slow"));
        labels.put(5, new JLabel("normal"));
        labels.put(10, new JLabel("fast"));
        // setup actual slider
        this.simulationTime = new JSlider(0, 10);
        this.simulationTime.setSnapToTicks(true);
        this.simulationTime.setPaintTicks(true);
        this.simulationTime.setPaintLabels(true);
        this.simulationTime.setMinorTickSpacing(1);
        this.simulationTime.setMajorTickSpacing(5);
        this.simulationTime.setLabelTable(labels);
        // setup change listener
        this.simulationTime.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent event) {
                JSlider source = (JSlider)event.getSource();
                if (!source.getValueIsAdjusting()) {
                    for (ToolbarListener listener : SwingToolbarManager.this.listeners)
                        listener.simulationSpeedChanged(source.getValue());
                }
            }
        });
        this.add(this.simulationTime);
        this.addSeparator();

        //
        // add visual separator
        //
        this.add(new JSeparator(VERTICAL));

        //
        // view button setup
        //
        this.view = new JToggleButton("View"); // FIXME Replace Text with Icon
        this.view.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                updateInputMode(InputMode.VIEW);
            }
        });
        this.add(this.view);

        //
        // entity button setup
        //
        this.entity = new JToggleButton("Entity"); // FIXME Replace Text with Icon
        this.entity.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                updateInputMode(InputMode.ENTITY);
            }
        });
        this.add(this.entity);

        //
        // entity selector setup
        //
        this.addSeparator();
        this.entitySelect = new EntitySelector();
        this.add(this.entitySelect);

        // setup the initial states
        this.updateSimulationState(SimulationState.STOP);
        this.updateInputMode(InputMode.VIEW);
    }

    //FIXME Initialize with Simulation not JFrame because this is not needed anywhere
    //public void initialize(final JFrame parent) {
    //}

    /**
     * This function updates the simulation state and notifies the listeners
     *
     * @param state The new state of the simulation
     * @see SimulationState
     */
    public void updateSimulationState(SimulationState state) {
        switch (state) {
            case PLAY:
                this.play.setEnabled(false);
                this.pause.setEnabled(true);
                this.stop.setEnabled(true);
                break;

            case PAUSE:
                this.play.setEnabled(true);
                this.pause.setEnabled(false);
                this.stop.setEnabled(true);
                break;

            case STOP:
                this.play.setEnabled(true);
                this.pause.setEnabled(false);
                this.stop.setEnabled(false);
                break;

            default:
        }

        for (ToolbarListener listener : this.listeners)
            listener.simulationStateChanged(state);
    }

    /**
     * This function updates the input mode and notifies the listeners
     *
     * @param mode The new mode of the input
     * @see InputMode
     */
    public void updateInputMode(InputMode mode) {
        switch (mode) {
            case VIEW:
                this.view.setSelected(true);
                this.entity.setSelected(false);
                break;

            case ENTITY:
                this.view.setSelected(false);
                this.entity.setSelected(true);
                break;

            default:
        }

        for (ToolbarListener listener : this.listeners)
            listener.inputModeChanged(mode);
    }

    @Override
    public String getCurrentEntity() {
        return this.entitySelect.getCurrentEntry().displayName;
    }

    @Override
    public void addEntity(String displayName, String textureID) {
        this.entitySelect.addEntry(this.entitySelect.new EntityEntry(displayName, textureID));
    }

    @Override
    public boolean addToolbarListener(ToolbarListener listener) {
        return this.listeners.add(listener);
    }

    @Override
    public boolean removeToolbarListener(ToolbarListener listener) {
        return this.listeners.add(listener);
    }

    @Override
    public void clearAllToolbarListeners() {
        this.listeners.clear();
    }
}

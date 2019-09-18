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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy;
import de.unistuttgart.informatik.fius.icge.ui.Toolbar;
import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy.ButtonState;
import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy.ButtonStateListener;
import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy.ButtonType;
import de.unistuttgart.informatik.fius.icge.ui.internal.entity_selector.EntitySelector;


/**
 * An implementation of {@link Toolbar} using java swing.
 *
 * @see javax.swing.JToolBar
 * @author Tim Neumann
 * @author Tobias Wältken
 * @version 1.0
 */
public class SwingToolbar extends JToolBar implements Toolbar {
    private static final long serialVersionUID = -2525998620577603876L;

    /** The simulation proxy */
    private final SimulationProxy simulationProxy;
    /** The texture registry */
    private final SwingTextureRegistry textureRegistry;

    /** The play button in the toolbar */
    public JButton play;
    /** The step button in the toolbar */
    public JButton step;
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
     *
     * @param simulationProxy The simulation proxy to use
     * @param textureRegistry The texture registry the textures and icons are loaded from
     */
    public SwingToolbar(final SimulationProxy simulationProxy, final SwingTextureRegistry textureRegistry) {
        //
        // class setup
        //
        this.simulationProxy = simulationProxy;
        this.textureRegistry = textureRegistry;

        //
        // toolbar setup
        //
        this.setFloatable(false);

        //
        // play button setup
        //
        this.play = new JButton(new ImageIcon(this.textureRegistry.getTextureForHandle(
                StaticUiTextures.playIcon).getTexture()));
        this.play.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                simulationProxy.buttonPressed(ButtonType.PLAY);
            }
        });
        this.play.setEnabled(false);
        this.add(this.play);

        //
        // step button setup
        //
        this.step = new JButton(new ImageIcon(this.textureRegistry.getTextureForHandle(
                StaticUiTextures.stepIcon).getTexture()));
        this.step.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                simulationProxy.buttonPressed(ButtonType.STEP);
            }
        });
        this.step.setEnabled(false);
        this.add(this.step);

        //
        // pause button setup
        //
        this.pause = new JButton(new ImageIcon(this.textureRegistry.getTextureForHandle(
                StaticUiTextures.pauseIcon).getTexture()));
        this.pause.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                simulationProxy.buttonPressed(ButtonType.PAUSE);
            }
        });
        this.pause.setEnabled(false);
        this.add(this.pause);

        //
        // stop button setup
        //
        this.stop = new JButton(new ImageIcon(this.textureRegistry.getTextureForHandle(
            StaticUiTextures.stopIcon).getTexture()));
        this.stop.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                simulationProxy.buttonPressed(ButtonType.STOP);
            }
        });
        this.stop.setEnabled(false);
        this.add(this.stop);

        //
        // clock button listener setup
        //
        this.simulationProxy.setButtonStateListener(new ButtonStateListener(){
            @Override
            public void changeButtonState(ButtonState state) {
                switch (state) {
                    case PLAYING:
                        SwingToolbar.this.play.setEnabled(false);
                        SwingToolbar.this.step.setEnabled(false);
                        SwingToolbar.this.pause.setEnabled(true);
                        SwingToolbar.this.stop.setEnabled(true);
                        break;

                    case PAUSED:
                        SwingToolbar.this.play.setEnabled(true);
                        SwingToolbar.this.step.setEnabled(true);
                        SwingToolbar.this.pause.setEnabled(false);
                        SwingToolbar.this.stop.setEnabled(true);
                        break;

                    case STOPPED:
                        SwingToolbar.this.play.setEnabled(true);
                        SwingToolbar.this.step.setEnabled(true);
                        SwingToolbar.this.pause.setEnabled(false);
                        SwingToolbar.this.stop.setEnabled(false);
                        break;

                    case BLOCKED:
                        SwingToolbar.this.play.setEnabled(false);
                        SwingToolbar.this.step.setEnabled(false);
                        SwingToolbar.this.pause.setEnabled(false);
                        SwingToolbar.this.stop.setEnabled(false);
                        break;

                    default:
                }
            }
        });

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

                if (!source.getValueIsAdjusting())
                    SwingToolbar.this.simulationProxy.simulationSpeedChange(source.getValue());
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
                SwingToolbar.this.simulationProxy.buttonPressed(ButtonType.VIEW);
            }
        });
        this.view.setEnabled(false);
        this.add(this.view);

        //
        // entity button setup
        //
        this.entity = new JToggleButton("Entity"); // FIXME Replace Text with Icon
        this.entity.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                SwingToolbar.this.simulationProxy.buttonPressed(ButtonType.ENTITY);
            }
        });
        this.entity.setEnabled(false);
        this.add(this.entity);

        //
        // entity selector setup
        //
        this.addSeparator();
        this.entitySelect = new EntitySelector(this.textureRegistry);
        this.add(this.entitySelect);
    }

    @Override
    public String getCurrentEntity() {
        return this.entitySelect.getCurrentEntry().displayName;
    }

    @Override
    public void addEntity(String displayName, String textureID) {
        this.entitySelect.addEntry(this.entitySelect.new EntityEntry(displayName, textureID));
    }
}

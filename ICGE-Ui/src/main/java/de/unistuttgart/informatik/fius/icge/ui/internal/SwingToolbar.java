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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy;
import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy.ButtonStateListener;
import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy.ButtonType;
import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy.ClockButtonState;
import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy.ControlButtonState;
import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy.TaskSelectorListener;
import de.unistuttgart.informatik.fius.icge.ui.internal.dropdown_selector.DropdownSelector;
import de.unistuttgart.informatik.fius.icge.ui.internal.dropdown_selector.DropdownSelector.DropdownEntry;
import de.unistuttgart.informatik.fius.icge.ui.Toolbar;


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
    private final SimulationProxy      simulationProxy;
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
    
    /** The selector which selects the task which is run in the simulation */
    public DropdownSelector taskSelect;
    
    /** The button to change to view mode */
    public JToggleButton view;
    /** The button to change to entity mode */
    public JToggleButton entity;
    
    /** The selector which selects the entity for the user to place */
    public DropdownSelector entitySelect;
    
    /**
     * The constructor of the toolbar
     *
     * @param simulationProxy
     *     The simulation proxy to use
     * @param textureRegistry
     *     The texture registry the textures and icons are loaded from
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
        this.play = new JButton(new ImageIcon(this.textureRegistry.getTextureForHandle(StaticUiTextures.playIcon).getTexture()));
        this.play.addActionListener(ae -> simulationProxy.buttonPressed(ButtonType.PLAY));
        this.play.setEnabled(false);
        this.add(this.play);
        
        //
        // step button setup
        //
        this.step = new JButton(new ImageIcon(this.textureRegistry.getTextureForHandle(StaticUiTextures.stepIcon).getTexture()));
        this.step.addActionListener(ae -> simulationProxy.buttonPressed(ButtonType.STEP));
        this.step.setEnabled(false);
        this.add(this.step);
        
        //
        // pause button setup
        //
        this.pause = new JButton(new ImageIcon(this.textureRegistry.getTextureForHandle(StaticUiTextures.pauseIcon).getTexture()));
        this.pause.addActionListener(ae -> simulationProxy.buttonPressed(ButtonType.PAUSE));
        this.pause.setEnabled(false);
        this.add(this.pause);
        
        //
        // stop button setup
        //
        this.stop = new JButton(new ImageIcon(this.textureRegistry.getTextureForHandle(StaticUiTextures.stopIcon).getTexture()));
        this.stop.addActionListener(ae -> simulationProxy.buttonPressed(ButtonType.STOP));
        this.stop.setEnabled(false);
        this.add(this.stop);
        
        //
        // simulation time slider setup
        //
        this.addSeparator();
        // setup hashtable for the labels
        final Hashtable<Integer, JLabel> labels = new Hashtable<>(3);
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
        this.simulationTime.addChangeListener(event -> {
            final JSlider source = (JSlider) event.getSource();
            
            if (!source.getValueIsAdjusting()) {
                SwingToolbar.this.simulationProxy.simulationSpeedChange(source.getValue());
            }
        });
        this.add(this.simulationTime);
        this.addSeparator();
        
        //
        // add visual separator
        //
        this.add(new JSeparator(SwingConstants.VERTICAL));
        
        //
        // task selector setup
        //
        this.addSeparator();
        this.taskSelect = new DropdownSelector(this.textureRegistry, "Task");
        this.taskSelect.addSelectionListener(new ItemListener() {
            
            @Override
            public void itemStateChanged(ItemEvent arg0) {
                if (arg0.getStateChange() == ItemEvent.SELECTED) {
                    SwingToolbar.this.simulationProxy.selectedTaskChange(((DropdownEntry) arg0.getItem()).displayName);
                }
            }
        });
        this.add(this.taskSelect);
        this.addSeparator();
        
        this.simulationProxy.setTaskSelectorListener(new TaskSelectorListener() {
            
            @Override
            public void setElements(Set<String> elements) {
                SwingToolbar.this.taskSelect.removeAllEntries();
                
                for (String element : elements)
                    SwingToolbar.this.taskSelect.addEntry(SwingToolbar.this.taskSelect.new DropdownEntry(element));
            }
            
            @Override
            public String getSelectedElement() {
                return SwingToolbar.this.taskSelect.getCurrentEntry().displayName;
            }
        });
        
        //
        // add visual separator
        //
        this.add(new JSeparator(SwingConstants.VERTICAL));
        
        //
        // view button setup
        //
        this.view = new JToggleButton("View"); // FIXME Replace Text with Icon
        this.view.addActionListener(ae -> SwingToolbar.this.simulationProxy.buttonPressed(ButtonType.VIEW));
        this.view.setEnabled(false);
        this.add(this.view);
        
        //
        // entity button setup
        //
        this.entity = new JToggleButton("Entity"); // FIXME Replace Text with Icon
        this.entity.addActionListener(ae -> SwingToolbar.this.simulationProxy.buttonPressed(ButtonType.ENTITY));
        this.entity.setEnabled(false);
        this.add(this.entity);
        
        //
        // button listener setup
        //
        this.simulationProxy.setButtonStateListener(new ButtonStateListener() {
            @Override
            public void changeButtonState(final ClockButtonState state) {
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
            
            @Override
            public void changeButtonState(final ControlButtonState state) {
                switch (state) {
                    case VIEW:
                        SwingToolbar.this.view.setEnabled(false);
                        SwingToolbar.this.entity.setEnabled(true);
                        break;
                    
                    case ENTITY:
                        SwingToolbar.this.view.setEnabled(true);
                        SwingToolbar.this.entity.setEnabled(false);
                        break;
                    
                    case BLOCKED:
                        SwingToolbar.this.view.setEnabled(false);
                        SwingToolbar.this.entity.setEnabled(false);
                        break;
                    
                    default:
                }
            }
        });
        
        //
        // entity selector setup
        //
        this.addSeparator();
        this.entitySelect = new DropdownSelector(this.textureRegistry, "Entity");
        this.add(this.entitySelect);
    }
    
    @Override
    public String getCurrentEntity() {
        return this.entitySelect.getCurrentEntry().displayName;
    }
    
    @Override
    public void addEntity(final String displayName, final String textureID) {
        this.entitySelect.addEntry(this.entitySelect.new DropdownEntry(displayName, textureID));
    }
}

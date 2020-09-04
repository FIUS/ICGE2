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
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy;
import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy.ButtonType;
import de.unistuttgart.informatik.fius.icge.ui.Toolbar;
import de.unistuttgart.informatik.fius.icge.ui.internal.dropdown.DropdownSelector;
import de.unistuttgart.informatik.fius.icge.ui.internal.dropdown.DropdownSelector.DropdownEntry;


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
    private SimulationProxy            simulationProxy;
    /** The texture registry */
    private final SwingTextureRegistry textureRegistry;
    
    /** The play button in the toolbar */
    public JButton play;
    /** The step button in the toolbar */
    public JButton step;
    /** The pause button in the toolbar */
    public JButton pause;
    
    /** A slider to set the simulation time */
    public JSlider simulationTime;
    
    /** The button to change to view mode */
    public JToggleButton view;
    /** The button to change to add mode */
    public JToggleButton add;
    /** The button to change to sub mode */
    public JToggleButton sub;
    
    /** The selector which selects the entity for the user to place */
    public DropdownSelector entitySelect;
    
    /**
     * The constructor of the toolbar
     *
     * @param textureRegistry
     *     The texture registry the textures and icons are loaded from
     * @param dpiScale
     *     The scaling value to adjust for highdpi screens
     */
    public SwingToolbar(final SwingTextureRegistry textureRegistry, final double dpiScale) {
        // class setup
        this.textureRegistry = textureRegistry;
        
        // toolbar setup
        this.setFloatable(false);
        
        // play button setup
        this.play = new JButton(new ImageIcon(this.textureRegistry.getTextureForHandle(StaticUiTextures.playIcon).getTexture()));
        this.play.addActionListener(ae -> {
            if (this.simulationProxy != null) {
                this.simulationProxy.buttonPressed(ButtonType.PLAY);
            }
        });
        this.play.setEnabled(false);
        this.add(this.play);
        
        // step button setup
        this.step = new JButton(new ImageIcon(this.textureRegistry.getTextureForHandle(StaticUiTextures.stepIcon).getTexture()));
        this.step.addActionListener(ae -> {
            if (this.simulationProxy != null) {
                this.simulationProxy.buttonPressed(ButtonType.STEP);
            }
        });
        this.step.setEnabled(false);
        this.add(this.step);
        
        // pause button setup
        this.pause = new JButton(new ImageIcon(this.textureRegistry.getTextureForHandle(StaticUiTextures.pauseIcon).getTexture()));
        this.pause.addActionListener(ae -> {
            if (this.simulationProxy != null) {
                this.simulationProxy.buttonPressed(ButtonType.PAUSE);
            }
        });
        this.pause.setEnabled(false);
        this.add(this.pause);
        
        // simulation time slider setup
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
                if (this.simulationProxy != null) {
                    this.simulationProxy.simulationSpeedChange(source.getValue());
                }
            }
        });
        this.simulationTime.setEnabled(false);
        this.add(this.simulationTime);
        this.addSeparator();
        
        // add visual separator
        this.add(new JSeparator(SwingConstants.VERTICAL));
        
        // view button setup
        this.view = new JToggleButton(new ImageIcon(this.textureRegistry.getTextureForHandle(StaticUiTextures.arrowIcon).getTexture()));
        this.view.addActionListener(ae -> {
            if (this.simulationProxy != null) {
                this.simulationProxy.buttonPressed(ButtonType.VIEW);
            }
        });
        this.view.setEnabled(false);
        this.add(this.view);
        
        // add button setup
        this.add = new JToggleButton(new ImageIcon(this.textureRegistry.getTextureForHandle(StaticUiTextures.addIcon).getTexture()));
        this.add.addActionListener(ae -> {
            if (this.simulationProxy != null) {
                this.simulationProxy.buttonPressed(ButtonType.ADD);
            }
        });
        this.add.setEnabled(false);
        this.add(this.add);
        
        // sub button setup
        this.sub = new JToggleButton(new ImageIcon(this.textureRegistry.getTextureForHandle(StaticUiTextures.subIcon).getTexture()));
        this.sub.addActionListener(ae -> {
            if (this.simulationProxy != null) {
                this.simulationProxy.buttonPressed(ButtonType.SUB);
            }
        });
        this.sub.setEnabled(false);
        this.add(this.sub);
        
        // entity selector setup
        this.addSeparator();
        this.entitySelect = new DropdownSelector(this.textureRegistry, "Entity");
        this.entitySelect.addSelectionListener(arg0 -> {
            if (arg0.getStateChange() == ItemEvent.SELECTED) {
                if (this.simulationProxy != null) {
                    this.simulationProxy.selectedEntityChanged(((DropdownEntry) arg0.getItem()).displayName);
                }
            }
        });
        this.entitySelect.setEnabled(false);
        this.add(this.entitySelect);
    }
    
    /**
     *
     * @param simulationProxy
     *     the simulationProxy to set
     */
    public void setSimulationProxy(final SimulationProxy simulationProxy) {
        if (this.simulationProxy != null) throw new IllegalStateException("SimulationProxy is already set and cannot be overwritten!");
        
        this.simulationProxy = simulationProxy;
    }
    
    @Override
    public String getCurrentEntity() {
        final DropdownEntry e = this.entitySelect.getCurrentEntry();
        if (e == null) return null;
        return e.displayName;
    }
    
    @Override
    public void addEntity(final String displayName, final String textureID) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> this.addEntity(displayName, textureID));
            return;
        }
        this.entitySelect.addEntry(this.entitySelect.new DropdownEntry(displayName, textureID));
    }
    
    @Override
    public void setControlButtonState(final ControlButtonState controlButtonState) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> this.setControlButtonState(controlButtonState));
            return;
        }
        switch (controlButtonState) {
            case VIEW:
                this.view.setEnabled(false);
                this.add.setEnabled(true);
                this.sub.setEnabled(true);
                break;
            
            case ADD:
                this.view.setEnabled(true);
                this.add.setEnabled(false);
                this.sub.setEnabled(true);
                break;
            
            case SUB:
                this.view.setEnabled(true);
                this.add.setEnabled(true);
                this.sub.setEnabled(false);
                break;
            
            case BLOCKED:
                this.view.setEnabled(false);
                this.add.setEnabled(false);
                this.sub.setEnabled(false);
                break;
            
            default:
        }
    }
    
    @Override
    public void setClockButtonState(final ClockButtonState clockButtonState) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> this.setClockButtonState(clockButtonState));
            return;
        }
        
        switch (clockButtonState) {
            case PLAYING:
                this.play.setEnabled(false);
                this.step.setEnabled(false);
                this.pause.setEnabled(true);
                this.simulationTime.setEnabled(true);
                break;
            case PAUSED:
                this.play.setEnabled(true);
                this.step.setEnabled(true);
                this.pause.setEnabled(false);
                this.simulationTime.setEnabled(true);
                break;
            case BLOCKED:
                this.play.setEnabled(false);
                this.step.setEnabled(false);
                this.pause.setEnabled(false);
                this.simulationTime.setEnabled(false);
                break;
            default:
        }
    }
    
    @Override
    public int getSpeedSliderPosition() {
        return this.simulationTime.getValue();
    }
    
    @Override
    public void setSpeedSliderPosition(final int position) {
        if (
            (position < 0) || (position > 10)
        ) throw new IllegalArgumentException(
                "Simulation Speed Value is out of bounds. Should be between 0 and 10 (both inclusive) but is: " + position
        );
        SwingUtilities.invokeLater(() -> {
            this.simulationTime.setValue(position);
        });
    }
    
    @Override
    public String getCurrentlySelectedEntity() {
        final DropdownEntry currentEntry = this.entitySelect.getCurrentEntry();
        if (currentEntry == null) return "";
        return currentEntry.displayName;
    }
    
    @Override
    public void setCurrentlySelectedEntity(final String entity) {
        SwingUtilities.invokeLater(() -> {
            this.entitySelect.setCurrentEntry(this.entitySelect.new DropdownEntry(entity));
        });
    }
    
    @Override
    public void enableEntitySelector() {
        SwingUtilities.invokeLater(() -> {
            this.entitySelect.setEnabled(true);
        });
    }
    
    @Override
    public void disableEntitySelector() {
        SwingUtilities.invokeLater(() -> {
            this.entitySelect.removeAllEntries();
            this.entitySelect.setEnabled(false);
        });
    }
    
    @Override
    public void addEntityToEntitySelector(final String name, final String textureId) {
        SwingUtilities.invokeLater(() -> {
            this.entitySelect.addEntry(this.entitySelect.new DropdownEntry(name, textureId));
        });
    }
}

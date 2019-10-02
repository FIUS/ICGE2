/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.example.mario.tasks;

import de.unistuttgart.informatik.fius.icge.example.mario.entity.Coin;
import de.unistuttgart.informatik.fius.icge.example.mario.entity.Mario;
import de.unistuttgart.informatik.fius.icge.example.mario.entity.Wall;
import de.unistuttgart.informatik.fius.icge.simulation.Position;
import de.unistuttgart.informatik.fius.icge.simulation.Simulation;
import de.unistuttgart.informatik.fius.icge.simulation.tasks.Task;


/**
 * An example task
 * 
 * @author Tim Neumann
 */
public abstract class Task1 implements Task {
    
    /**
     * the simulation
     */
    Simulation sim;
    /**
     * The walking mario
     */
    Mario      walkingMario;
    /**
     * The spinning mario
     */
    Mario      spinningMario;
    
    @Override
    public void prepare(final Simulation sim) {
        this.sim = sim;
        sim.initialize();
        sim.getPlayfield().addEntity(new Position(-3, -1), new Wall());
        sim.getPlayfield().addEntity(new Position(-3, 0), new Wall());
        sim.getPlayfield().addEntity(new Position(-3, 1), new Wall());
        sim.getPlayfield().addEntity(new Position(3, -1), new Wall());
        sim.getPlayfield().addEntity(new Position(3, 0), new Wall());
        sim.getPlayfield().addEntity(new Position(3, 1), new Wall());
        
        this.walkingMario = new Mario();
        this.walkingMario.getInventory().add(new Coin());
        
        this.spinningMario = new Mario();
        
        sim.getPlayfield().addEntity(new Position(-1, 0), this.walkingMario);
        sim.getPlayfield().addEntity(new Position(0, 0), this.spinningMario);
        
    }
    
    @Override
    public boolean verify() {
        // TODO
        return true;
    }
    
}

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

import java.util.List;

import de.unistuttgart.informatik.fius.icge.example.mario.entity.Mario;
import de.unistuttgart.informatik.fius.icge.example.mario.entity.Wall;
import de.unistuttgart.informatik.fius.icge.simulation.Position;
import de.unistuttgart.informatik.fius.icge.simulation.Simulation;
import de.unistuttgart.informatik.fius.icge.simulation.actions.ActionLog;
import de.unistuttgart.informatik.fius.icge.simulation.actions.EntityStepAction;
import de.unistuttgart.informatik.fius.icge.simulation.tasks.Task;


/**
 * The Task 2
 *
 * @author Tim Neumann
 */
public abstract class Task2 implements Task {
    
    protected Mario mario;
    protected Simulation sim;
    
    @Override
    public void prepare(Simulation sim) {
        this.sim = sim;
        sim.getPlayfield().addEntity(new Position(-3, -1), new Wall());
        sim.getPlayfield().addEntity(new Position(-3, 0), new Wall());
        sim.getPlayfield().addEntity(new Position(-3, 1), new Wall());
        sim.getPlayfield().addEntity(new Position(3, -1), new Wall());
        sim.getPlayfield().addEntity(new Position(3, 0), new Wall());
        sim.getPlayfield().addEntity(new Position(3, 1), new Wall());
        
        this.mario = new Mario();
        
        sim.getPlayfield().addEntity(new Position(0, 0), this.mario);
    }
    
    /**
     * The task is to have {@link #mario} walk 2 steps forward, turn and walk back to the start.
     */
    @Override
    public abstract void solve();
    
    @Override
    public boolean verify() {
        if (!this.mario.getPosition().equals(new Position(0, 0))) return false;
        ActionLog actionLog = this.sim.getActionLog();
        
        List<EntityStepAction> steps = actionLog.getActionsOfTypeOfEntity(this.mario, EntityStepAction.class, false);
        
        if (steps.size() != 4) return false;
        
        EntityStepAction step = steps.get(0);
        
        if (!(step.from().equals(new Position(0, 0)) && step.to().equals(new Position(1, 0)))) return false;
        
        step = steps.get(1);
        
        if (!(step.from().equals(new Position(1, 0)) && step.to().equals(new Position(2, 0)))) return false;
        
        step = steps.get(2);
        
        if (!(step.from().equals(new Position(2, 0)) && step.to().equals(new Position(1, 0)))) return false;
        
        step = steps.get(3);
        
        if (!(step.from().equals(new Position(1, 0)) && step.to().equals(new Position(0, 0)))) return false;
        
        return true;
    }
    
}

/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.manualstart;

import java.util.List;

import de.unistuttgart.informatik.fius.icge.simulation.Simulation;
import de.unistuttgart.informatik.fius.icge.simulation.TaskVerifier;
import de.unistuttgart.informatik.fius.icge.simulation.actions.ActionLog;
import de.unistuttgart.informatik.fius.icge.simulation.actions.EntityMoveAction;
import de.unistuttgart.informatik.fius.icge.ui.TaskInformation;
import de.unistuttgart.informatik.fius.icge.ui.TaskVerificationStatus;


/**
 * Example task verifier.
 * 
 * @author Fabian Bühler
 */
public class TestTaskVerifier implements TaskVerifier, TaskInformation {
    
    private ActionLog              log;
    private TaskVerificationStatus taskIsValid = TaskVerificationStatus.UNDECIDED;
    private int                    stepsToWalk = 4;
    
    @Override
    public void attachToSimulation(Simulation sim) {
        // get the log to verify if the required events did happen later
        this.log = sim.getActionLog();
    }
    
    @Override
    public void verify() {
        if (this.log == null) {
            return;
        }
        // check the number of steps/moves all entity have taken together
        int steps = this.log.getActionsOfType(EntityMoveAction.class, true).size();
        if (steps > 3) {
            this.stepsToWalk = 0;
            this.taskIsValid = TaskVerificationStatus.SUCCESSFUL;
        } else {
            this.stepsToWalk = 4 - steps;
            this.taskIsValid = TaskVerificationStatus.FAILED;
        }
    }
    
    @Override
    public TaskInformation getTaskInformation() {
        return this;
    }
    
    @Override
    public String getTaskTitle() {
        return "Test Task";
    }
    
    @Override
    public String getTaskDescription() {
        String description = "Just a demo task to test the UI and the Backend.";
        if (this.stepsToWalk > 1) {
            description += " (" + this.stepsToWalk + " steps to walk)";
        }
        if (this.stepsToWalk == 1) {
            description += " (1 step to walk)";
        }
        return description;
    }
    
    @Override
    public TaskVerificationStatus getTaskStatus() {
        return this.taskIsValid;
    }
    
    @Override
    public List<TaskInformation> getChildTasks() {
        return null;
    }
    
}

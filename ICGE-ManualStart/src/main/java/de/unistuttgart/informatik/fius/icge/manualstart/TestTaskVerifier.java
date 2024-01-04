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

import java.util.ArrayList;
import java.util.Collections;
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
 * Verifies that between {@code minStepsToWalk} and {@code maxStepsToWalk} {@code EntityMoveAction} events are in the
 * log of the simulation. Below the task is UNDECIDED and above the task is FAILED.
 *
 * @author Fabian Bühler
 */
public class TestTaskVerifier implements TaskVerifier, TaskInformation {

    private ActionLog              log;
    private TaskVerificationStatus taskIsValid    = TaskVerificationStatus.UNDECIDED;
    private int                    minStepsToWalk = 4;
    private int                    maxStepsToWalk = 14;
    private int                    stepsWalked    = 0;

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
        this.stepsWalked = this.log.getActionsOfType(EntityMoveAction.class, true).size();

        // as long as task is still achievable use UNDECIDED status
        if (this.stepsWalked < this.minStepsToWalk) {
            this.taskIsValid = TaskVerificationStatus.UNDECIDED;
            return;
        }

        // if task is failed irreversibly use FAILED status
        if (this.stepsWalked > this.maxStepsToWalk) {
            this.taskIsValid = TaskVerificationStatus.FAILED;
            return;
        }

        // use SUCCESSFUL status if task is solved correctly
        this.taskIsValid = TaskVerificationStatus.SUCCESSFUL;
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
        String description = "Just a demo task to test the UI and the Backend.\n";
        description += "Walk between " + this.minStepsToWalk + " and " + this.maxStepsToWalk + " steps to solve this task.";
        if (this.stepsWalked != 1) { // computing hints is fine if they only change when verify is called
            description += " (" + this.stepsWalked + " steps walked)";
        } else {
            description += " (1 step walked)";
        }
        return description;
    }

    @Override
    public TaskVerificationStatus getTaskStatus() {
        return this.taskIsValid;
    }

    @Override
    public List<TaskInformation> getChildTasks() {
        ArrayList<TaskInformation> subTasks = new ArrayList<>();

        subTasks.add(new TaskInformation() {

            @Override
            public String getTaskTitle() {
                return "a) Successful sub task";
            }

            @Override
            public String getTaskDescription() {
                return "Super basic sample description";
            }

            @Override
            public TaskVerificationStatus getTaskStatus() {
                return TaskVerificationStatus.SUCCESSFUL;
            }

            @Override
            public List<TaskInformation> getChildTasks() {
                return Collections.emptyList();
            }
        });

        subTasks.add(new TaskInformation() {

            @Override
            public String getTaskTitle() {
                return "b) undecided sub task";
            }

            @Override
            public String getTaskDescription() {
                return "Super basic sample description";
            }

            @Override
            public TaskVerificationStatus getTaskStatus() {
                return TaskVerificationStatus.UNDECIDED;
            }

            @Override
            public List<TaskInformation> getChildTasks() {
                ArrayList<TaskInformation> subTasks = new ArrayList<>();

                subTasks.add(new TaskInformation() {

                    @Override
                    public String getTaskTitle() {
                        return "b.1) Successful sub sub task";
                    }

                    @Override
                    public String getTaskDescription() {
                        return "Super basic sample description";
                    }

                    @Override
                    public TaskVerificationStatus getTaskStatus() {
                        return TaskVerificationStatus.SUCCESSFUL;
                    }

                    @Override
                    public List<TaskInformation> getChildTasks() {
                        return Collections.emptyList();
                    }
                });

                subTasks.add(new TaskInformation() {

                    @Override
                    public String getTaskTitle() {
                        return "b.2) undecided sub sub task";
                    }

                    @Override
                    public String getTaskDescription() {
                        return "Super basic sample description";
                    }

                    @Override
                    public TaskVerificationStatus getTaskStatus() {
                        return TaskVerificationStatus.UNDECIDED;
                    }

                    @Override
                    public List<TaskInformation> getChildTasks() {
                        return Collections.emptyList();
                    }
                });

                subTasks.add(new TaskInformation() {

                    @Override
                    public String getTaskTitle() {
                        return "b.3) failed sub sub task";
                    }

                    @Override
                    public String getTaskDescription() {
                        return "Super basic sample description";
                    }

                    @Override
                    public TaskVerificationStatus getTaskStatus() {
                        return TaskVerificationStatus.FAILED;
                    }

                    @Override
                    public List<TaskInformation> getChildTasks() {
                        return Collections.emptyList();
                    }
                });

                return subTasks;
            }
        });

        subTasks.add(new TaskInformation() {

            @Override
            public String getTaskTitle() {
                return "c) failed sub task";
            }

            @Override
            public String getTaskDescription() {
                return "Super basic sample description";
            }

            @Override
            public TaskVerificationStatus getTaskStatus() {
                return TaskVerificationStatus.FAILED;
            }

            @Override
            public List<TaskInformation> getChildTasks() {
                return Collections.emptyList();
            }
        });

        return subTasks;
    }

}

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

import java.util.List;


/**
 * A interface providing the title, description and status of a task.
 *
 * @author Fabian Bühler
 * @version 1.0
 */
public interface TaskInformation {

    /**
     * Get the title of the task.
     *
     * @return the task title (must not be {@code null}, without trailing newline)
     */
    String getTaskTitle();

    /**
     * Get the description of the task.
     *
     * @return the task description (can be {@code null}, can contain newlines, without trailing newline)
     */
    String getTaskDescription();

    /**
     * Get the verification status of the task.
     *
     * @return the task status (must not be {@code null})
     */
    TaskVerificationStatus getTaskStatus();

    /**
     * Get a list of child/sub tasks of this task.
     *
     * @return a list of sub-tasks (must not be {@code null}, use {@code Collections.emptyList()} instead)
     */
    List<TaskInformation> getChildTasks();
}

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

/**
 * The task status used by a {@link GameWindow} to show the current task status.
 *
 * @author Fabian Bühler
 * @version 1.0
 */
public interface TaskStatusDisplay {

    /**
     * Set the task information to be displayed.
     *
     * @param task
     *     the information of the current task (and subtasks)
     */
    void setTaskInformation(TaskInformation task);
}

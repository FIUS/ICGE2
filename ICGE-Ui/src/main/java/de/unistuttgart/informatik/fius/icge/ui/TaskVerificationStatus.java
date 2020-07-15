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
 * Enum describing the verification status of a task.
 */
public enum TaskVerificationStatus {
    /** Undecided is for tasks that have not been verified (or have not failed yet). */
    UNDECIDED,
    /** Successful is for tasks that have ben solved correctly. */
    SUCCESSFUL,
    /** Failed is for tasks that have not been solved correctly. */
    FAILED,
}

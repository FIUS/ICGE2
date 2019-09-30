/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
module de.unistuttgart.informatik.fius.icge.simulation {
    requires transitive de.unistuttgart.informatik.fius.icge.ui;
    
    exports de.unistuttgart.informatik.fius.icge.simulation;
    exports de.unistuttgart.informatik.fius.icge.simulation.entity;
    exports de.unistuttgart.informatik.fius.icge.simulation.entity.program;
    exports de.unistuttgart.informatik.fius.icge.simulation.exception;
    exports de.unistuttgart.informatik.fius.icge.simulation.tasks;
}

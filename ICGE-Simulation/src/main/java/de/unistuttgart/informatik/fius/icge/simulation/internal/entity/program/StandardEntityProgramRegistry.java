/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.internal.entity.program;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;
import de.unistuttgart.informatik.fius.icge.simulation.entity.program.EntityProgramInfo;
import de.unistuttgart.informatik.fius.icge.simulation.entity.program.EntityProgramRegistry;


/**
 * The standard implementation of {@link EntityProgramRegistry}
 * 
 * @author Tim Neumann
 */
public class StandardEntityProgramRegistry implements EntityProgramRegistry {
    
    private final Set<EntityProgramInfo> programs = new HashSet<>();
    
    @Override
    public void registerEntityProgram(final EntityProgramInfo program) {
        this.programs.add(program);
    }
    
    @Override
    public Set<EntityProgramInfo> getEntityPrograms() {
        return Set.copyOf(this.programs);
    }
    
    @Override
    public Set<EntityProgramInfo> getProgramsForEntity(final Entity entity) {
        return this.programs.stream().filter(p -> p.couldRunOn(entity)).collect(Collectors.toSet());
    }
    
}

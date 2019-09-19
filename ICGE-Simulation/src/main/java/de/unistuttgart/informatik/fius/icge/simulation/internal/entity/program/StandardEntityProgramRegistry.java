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

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;
import de.unistuttgart.informatik.fius.icge.simulation.entity.program.EntityProgram;
import de.unistuttgart.informatik.fius.icge.simulation.entity.program.EntityProgramRegistry;
import de.unistuttgart.informatik.fius.icge.simulation.exception.ElementExistsException;


/**
 * The standard implementation of {@link EntityProgramRegistry}
 * 
 * @author Tim Neumann
 */
public class StandardEntityProgramRegistry implements EntityProgramRegistry {
    
    private final Map<String, EntityProgramRegistryEntry> programs = new HashMap<>();
    
    @Override
    public void registerEntityProgram(final String name, final EntityProgram program) {
        if ((name == null) || (program == null)) throw new IllegalArgumentException("An argument is null.");
        
        if (this.programs.containsKey(name)) throw new ElementExistsException("Name is already in use.");
        
        this.programs.put(name, new EntityProgramRegistryEntry(name, program));
    }
    
    @Override
    public void registerManyEntityProgram(final String name, final Supplier<EntityProgram> programGenerator) {
        if ((name == null) || (programGenerator == null)) throw new IllegalArgumentException("An argument is null.");
        
        if (this.programs.containsKey(name)) throw new ElementExistsException("Name is already in use.");
        
        this.programs.put(name, new EntityProgramRegistryEntry(name, programGenerator));
    }
    
    @Override
    public Set<String> getPrograms() {
        return Set.copyOf(this.programs.keySet());
    }
    
    @Override
    public Set<String> getProgramsForEntity(final Entity entity) {
        return this.programs.entrySet().stream().filter(entry -> entry.getValue().getProgram().canRunOn(entity))
                .map(entry -> entry.getKey()).collect(Collectors.toSet());
    }
    
    @Override
    public EntityProgram getEntityProgram(final String name) {
        if (name == null) throw new IllegalArgumentException("An argument is null.");
        if (!this.programs.containsKey(name)) throw new NoSuchElementException();
        return this.programs.get(name).getProgram();
    }
    
}

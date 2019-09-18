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

import java.util.function.Supplier;

import de.unistuttgart.informatik.fius.icge.simulation.entity.program.EntityProgram;
import de.unistuttgart.informatik.fius.icge.simulation.entity.program.EntityProgramRegistry;


/**
 * Objects of this class store information about an {@link EntityProgram} needed by the {@link EntityProgramRegistry}.
 * 
 * @author Tim Neumann
 */
public class EntityProgramRegistryEntry {
    private final String                  name;
    private final boolean                 single;
    private final EntityProgram           program;
    private final Supplier<EntityProgram> programGenerator;
    
    /**
     * Create a new info for a single entity program
     * 
     * @param name
     *     The name for the program; cannot be null
     * @param program
     *     The program; cannot be null
     * @throws IllegalArgumentException
     *     if an argument is null
     */
    public EntityProgramRegistryEntry(final String name, final EntityProgram program) {
        if ((name == null) || (program == null)) throw new IllegalArgumentException("Argument cannot be null.");
        this.name = name;
        this.single = true;
        this.program = program;
        this.programGenerator = null;
    }
    
    /**
     * Create a new info for many entity programs provided by the given generator
     * 
     * @param name
     *     The name for this type of program; cannot be null
     * @param programGenerator
     *     A generator for this type of program; cannot be null; must provide non null programs
     * @throws IllegalArgumentException
     *     if an argument is null
     */
    public EntityProgramRegistryEntry(final String name, final Supplier<EntityProgram> programGenerator) {
        if ((name == null) || (programGenerator == null)) throw new IllegalArgumentException("Argument cannot be null.");
        this.name = name;
        this.single = false;
        this.program = null;
        this.programGenerator = programGenerator;
    }
    
    /**
     * @return the name of this program
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Get the program instance of this info.
     * <p>
     * If this is a info about many programs, calls the get method of the generator.
     * </p>
     * @return the program instance
     */
    public EntityProgram getProgram() {
        if(this.single) return this.program;
        EntityProgram prog = this.programGenerator.get();
        if (prog == null) throw new IllegalStateException("Program Generator returned null.");
        return prog;
    }
    
    @Override
    public int hashCode() {
        if (this.single) return this.name.hashCode() + this.program.hashCode();
        return this.name.hashCode() + this.programGenerator.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof EntityProgramRegistryEntry)) return false;
        final EntityProgramRegistryEntry other = (EntityProgramRegistryEntry) obj;
        if (this.single != other.single) return false;
        if (!this.name.equals(other.name)) return false;
        if (this.single) return this.program.equals(other.program);
        return this.programGenerator.equals(other.programGenerator);
    }
    
}

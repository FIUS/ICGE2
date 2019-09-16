/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.entity.program;

import java.util.function.Supplier;

import de.unistuttgart.informatik.fius.icge.simulation.entity.Entity;


/**
 * Objects of this class store information about an {@link EntityProgram} needed by the {@link EntityProgramRegistry}.
 * 
 * @author Tim Neumann
 */
public class EntityProgramInfo {
    private static final String THREAD_NAME_PREFIX = "EntityProgram_";
    
    private final String                  name;
    private final boolean                 single;
    private final EntityProgram           program;
    private final Supplier<EntityProgram> programGenerator;
    
    private boolean running = false;
    
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
    public EntityProgramInfo(final String name, final EntityProgram program) {
        if ((name == null) || (program == null)) throw new IllegalArgumentException("Argument cannot be null");
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
    public EntityProgramInfo(final String name, final Supplier<EntityProgram> programGenerator) {
        if ((name == null) || (programGenerator == null)) throw new IllegalArgumentException("Argument cannot be null");
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
     * Check whether this program could run on the given entity.
     * <p>
     * Will return false if it cannot run at all.
     * </p>
     * 
     * @param entity
     *     The entity to check
     * @return true if this program could run on the given entity
     */
    public boolean couldRunOn(final Entity entity) {
        if (!this.canRun()) return false;
        
        EntityProgram toRun;
        if (this.single) {
            toRun = this.program;
        } else {
            toRun = this.programGenerator.get();
        }
        
        if (toRun == null) throw new IllegalStateException("Program to run is null.");
        
        return toRun.canRunOn(entity);
    }
    
    /**
     * Check whether the program could be run now.
     * 
     * @return true if the program could be run now
     */
    public boolean canRun() {
        if (!this.single) return true;
        return !this.running;
    }
    
    /**
     * Run the program of this object on a given entity in a new thread.
     * 
     * @param entity
     *     The entity to run the program on
     * @throws IllegalStateException
     *     if the program cannot run
     */
    public void run(final Entity entity) {
        if (!this.canRun()) throw new IllegalStateException("Cannot run.");
        
        this.running = true;
        EntityProgram toRun;
        if (this.single) {
            toRun = this.program;
        } else {
            toRun = this.programGenerator.get();
        }
        
        if (toRun == null) throw new IllegalStateException("Program to run is null.");
        
        final String threadName = EntityProgramInfo.THREAD_NAME_PREFIX + this.getName();
        new Thread(() -> {
            toRun.run(entity);
        }, threadName).start();
    }
    
    @Override
    public int hashCode() {
        if (this.single) return this.name.hashCode() + this.program.hashCode();
        return this.name.hashCode() + this.programGenerator.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof EntityProgramInfo)) return false;
        final EntityProgramInfo other = (EntityProgramInfo) obj;
        if (this.single != other.single) return false;
        if (!this.name.equals(other.name)) return false;
        if (this.single) return this.program.equals(other.program);
        return this.programGenerator.equals(other.programGenerator);
    }
    
}

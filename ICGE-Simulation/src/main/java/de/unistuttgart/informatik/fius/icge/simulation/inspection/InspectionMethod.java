/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/neumantm/ICGE
 * 
 * Copyright (c) 2018 the ICGE project authors.
 */

package de.unistuttgart.informatik.fius.icge.simulation.inspection;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * A annotation specifying that the annotated method should be displayed as a method in the inspector.
 * 
 * @author Tim Neumann
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface InspectionMethod {
    /** @return The optional name of the method, to be displayed in the front end. */
    String name() default "";
}

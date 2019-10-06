/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/neumantm/ICGE
 * 
 * Copyright (c) 2018 the ICGE project authors.
 */

package de.unistuttgart.informatik.fius.icge.simulation.inspection;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * A annotation specifying that the annotated element should be displayed as a attribute in the inspector.
 * <p>
 * This can be used for attributes or for getters and setters.
 * 
 * @author Tim Neumann
 */
@Documented
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface InspectionAttribute {
    /** @return The optional name of the attribute, to be displayed in the front end. */
    String name() default "";

    /** @return Optional argument setting this attribute to read only. */
    boolean readOnly() default false;
}

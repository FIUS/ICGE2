/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 *
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.ui.exception;

*
/**
 * The LeafNodeException is thrown when someone tries to add a child node to a leaf node or interact with the child
 * nodes of a leaf node.
 *
 * @author Tobias Wältken
 * @version 1.0
 */
public class LeafNodeException extends UnsupportedOperationException {
    private static final long serialVersionUID = 2227365905217962083L;
 *
    /**
     * Default constructor
     */
    public LeafNodeException() {
        super("A leaf node can't have children.");
    }
}

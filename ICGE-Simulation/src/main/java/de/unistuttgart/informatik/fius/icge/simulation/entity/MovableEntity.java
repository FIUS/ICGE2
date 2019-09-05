/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.entity;

import java.util.concurrent.CompletableFuture;

import de.unistuttgart.informatik.fius.icge.simulation.Position;
import de.unistuttgart.informatik.fius.icge.simulation.exception.EntityNotOnFieldException;
import de.unistuttgart.informatik.fius.icge.simulation.exception.IllegalMoveException;


/**
 * A movable entity
 * 
 * @author Tim Neumann
 */
public abstract class MovableEntity extends BasicEntity {
    
    private Direction lookingDirection = Direction.EAST;
    
    /**
     * Turn this entity for 90 degrees in clock wise direction.
     */
    public void turnClockWise() {
        final CompletableFuture<Void> endOfOperation = new CompletableFuture<>();
        this.getSimulation().getSimulationClock().scheduleTickOperationAtNextTick(endOfOperation);
        this.lookingDirection = this.lookingDirection.clockWiseNext();
        endOfOperation.complete(null);
    }
    
    /**
     * @return the current looking direction of this entity
     */
    public Direction getLookingDirection() {
        return this.lookingDirection;
    }
    
    private boolean isSolidEntityAt(final Position pos) {
        return !this.getPlayfield().getEntitiesOfTypeAt(pos, SolidEntity.class, true).isEmpty();
    }
    
    /**
     * Move this entity forward one field.
     * 
     * @throws EntityNotOnFieldException
     *     if this entity is not on a playfield
     * @throws IllegalMoveException
     *     if a solid entity is in the way
     */
    public void move() {
        final CompletableFuture<Void> endOfOperation = new CompletableFuture<>();
        this.getSimulation().getSimulationClock().scheduleTickOperationInTicks(4, endOfOperation);
        final Position nextPos = this.getPosition().adjacentPosition(this.lookingDirection);
        if (this.isSolidEntityAt(nextPos)) throw new IllegalMoveException("Solid Entity in the way");
        this.getPlayfield().moveEntity(this, nextPos);
        endOfOperation.complete(null);
    }
    
    /**
     * @return whether this entity can move forward one field.
     */
    public boolean canMove() {
        final Position nextPos = this.getPosition().adjacentPosition(this.lookingDirection);
        return this.isOnPlayfield() && !this.isSolidEntityAt(nextPos);
    }
    
    /**
     * Move this entity forward one field if that is possible.
     */
    public void moveIfPossible() {
        if (this.canMove()) {
            this.move();
        }
    }
}

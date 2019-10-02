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

import de.unistuttgart.informatik.fius.icge.simulation.Direction;
import de.unistuttgart.informatik.fius.icge.simulation.Position;
import de.unistuttgart.informatik.fius.icge.simulation.SimulationClock;
import de.unistuttgart.informatik.fius.icge.simulation.exception.EntityNotOnFieldException;
import de.unistuttgart.informatik.fius.icge.simulation.exception.IllegalMoveException;
import de.unistuttgart.informatik.fius.icge.ui.AnimatedDrawable;
import de.unistuttgart.informatik.fius.icge.ui.Drawable;


/**
 * A movable entity
 * 
 * @author Tim Neumann
 */
public abstract class MovableEntity extends BasicEntity {
    
    private Direction lookingDirection = Direction.EAST;
    
    private Drawable movingDrawable = null;
    
    @Override
    public Drawable getDrawInformation() {
        if (this.movingDrawable != null) {
            return this.movingDrawable;
        }
        final Position pos = this.getPosition();
        return new UntilableDrawable(pos.getX(), pos.getY(), this.getZPosition(), this.getTextureHandle());
    }
    
    /**
     * Turn this entity for 90 degrees in clock wise direction.
     */
    public void turnClockWise() {
        final CompletableFuture<Void> endOfOperation = new CompletableFuture<>();
        this.getSimulation().getSimulationClock().scheduleOperationAtNextTick(endOfOperation);
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
        return this.getPlayfield().isSolidEntityAt(pos);
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
        final int duration = 4;
        final int renderTickDuration = duration * SimulationClock.RENDER_TICKS_PER_SIMULATION_TICK;
        final CompletableFuture<Void> endOfOperation = new CompletableFuture<>();
        final SimulationClock clock = this.getSimulation().getSimulationClock();
        final long currentTick = clock.getLastRenderTickNumber();
        clock.scheduleOperationInTicks(duration, endOfOperation);
        final Position currentPos = this.getPosition();
        final Position nextPos = currentPos.adjacentPosition(this.lookingDirection);
        if (this.isSolidEntityAt(nextPos)) throw new IllegalMoveException("Solid Entity in the way");
        this.getPlayfield().moveEntity(this, nextPos);
        this.movingDrawable = new AnimatedDrawable(
                currentTick, currentPos.getX(), currentPos.getY(), renderTickDuration, nextPos.getX(), nextPos.getY(), this.getZPosition(),
                this.getTextureHandle()
        );
        endOfOperation.complete(null);
        this.movingDrawable = null;
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

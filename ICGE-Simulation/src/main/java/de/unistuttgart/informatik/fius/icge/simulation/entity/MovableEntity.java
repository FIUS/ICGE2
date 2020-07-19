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
import de.unistuttgart.informatik.fius.icge.simulation.actions.EntityMoveAction;
import de.unistuttgart.informatik.fius.icge.simulation.actions.EntityStepAction;
import de.unistuttgart.informatik.fius.icge.simulation.actions.EntityTurnAction;
import de.unistuttgart.informatik.fius.icge.simulation.exception.EntityNotOnFieldException;
import de.unistuttgart.informatik.fius.icge.simulation.exception.IllegalMoveException;
import de.unistuttgart.informatik.fius.icge.simulation.inspection.InspectionAttribute;
import de.unistuttgart.informatik.fius.icge.simulation.inspection.InspectionMethod;
import de.unistuttgart.informatik.fius.icge.ui.AnimatedDrawable;
import de.unistuttgart.informatik.fius.icge.ui.Drawable;


/**
 * A movable entity
 * 
 * @author Tim Neumann
 */
public abstract class MovableEntity extends BasicEntity {
    
    private Direction lookingDirection = Direction.EAST;
    
    private AnimatedDrawable movingDrawable = null;
    
    private Direction directionOfAlmostArrivedMove;
    
    @Override
    public Drawable getDrawInformation() {
        if (this.movingDrawable != null) return this.movingDrawable;
        final Position pos = this.getPosition();
        return new UntilableDrawable(pos.getX(), pos.getY(), this.getZPosition(), this.getTextureHandle());
    }
    
    /**
     * Turn this entity for 90 degrees in clock wise direction.
     */
    public void turnClockWise() {
        final CompletableFuture<Void> endOfOperation = new CompletableFuture<>();
        this.scheduleActionAfterCurrentAction(endOfOperation);
        try {
            this.getSimulation().getSimulationClock().scheduleOperationAtNextTick(endOfOperation);
            this.turnClockWiseInternal();
        } finally {
            endOfOperation.complete(null);
        }
    }
    
    private void turnClockWiseInternal() {
        final Direction oldLookingDirection = this.lookingDirection;
        this.lookingDirection = this.lookingDirection.clockWiseNext();
        final long tick = this.getSimulation().getSimulationClock().getLastTickNumber();
        this.getSimulation().getActionLog().logAction(new EntityTurnAction(tick, this, oldLookingDirection, this.lookingDirection));
    }
    
    @InspectionMethod(name = "turnClockwise")
    private void turnClockWiseInspector() {
        this.turnClockWiseInternal();
        this.recalculateAnimationAfterInspector();
    }
    
    private void recalculateAnimationAfterInspector() {
        if (this.movingDrawable != null) {
            
            final long tickStart = this.movingDrawable.getTickStart();
            final long duration = this.movingDrawable.getDuration();
            
            Direction movingDir;
            if (this.directionOfAlmostArrivedMove == null) {
                movingDir = this.lookingDirection;
            } else {
                movingDir = this.directionOfAlmostArrivedMove;
            }
            
            final Position currentPos = this.getPosition();
            final Position nextPos = currentPos.adjacentPosition(movingDir);
            
            this.movingDrawable = new AnimatedDrawable(
                    tickStart, currentPos.getX(), currentPos.getY(), duration, nextPos.getX(), nextPos.getY(), this.getZPosition(),
                    this.getTextureHandle()
            );
        }
    }
    
    /**
     * @return the current looking direction of this entity
     */
    public Direction getLookingDirection() {
        return this.lookingDirection;
    }
    
    /**
     * Set the looking direction
     * 
     * @param direction
     *     the name of the new direction
     */
    @InspectionAttribute(name = "LookingDirection")
    private void setLookingDirectionByString(final String direction) {
        this.lookingDirection = Direction.valueOf(direction.toUpperCase());
        this.recalculateAnimationAfterInspector();
    }
    
    /**
     * @return the looking direction as a string
     */
    @InspectionAttribute(name = "LookingDirection")
    public String getLookingDirectionString() {
        return this.getLookingDirection().toString();
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
        // use extra future for whole operation as it is split amongst two futures
        final CompletableFuture<Void> endOfOperation = new CompletableFuture<>();
        this.scheduleActionAfterCurrentAction(endOfOperation);
        
        // setup move
        final int duration = 4;
        final int renderTickDuration = duration * SimulationClock.RENDER_TICKS_PER_SIMULATION_TICK;
        final SimulationClock clock = this.getSimulation().getSimulationClock();
        final long currentTick = clock.getLastRenderTickNumber();
        this.directionOfAlmostArrivedMove = null;
        Position currentPos = this.getPosition();
        Position nextPos = currentPos.adjacentPosition(this.lookingDirection);
        this.movingDrawable = new AnimatedDrawable(
                currentTick, currentPos.getX(), currentPos.getY(), renderTickDuration, nextPos.getX(), nextPos.getY(), this.getZPosition(),
                this.getTextureHandle()
        );
        
        final CompletableFuture<Void> endOfOperation1 = new CompletableFuture<>();
        try {
            clock.scheduleOperationInTicks(duration / 2, endOfOperation1);
            //Check if really still going this direction. Maybe we were turned by inspector in the mean time.
            //Later turns are not changing destination.
            this.directionOfAlmostArrivedMove = this.lookingDirection;
        } finally {
            endOfOperation1.complete(null);
        }
        
        final CompletableFuture<Void> endOfOperation2 = new CompletableFuture<>();
        try {
            clock.scheduleOperationInTicks(duration / 2, endOfOperation2);
            currentPos = this.getPosition();
            nextPos = currentPos.adjacentPosition(this.directionOfAlmostArrivedMove);
            this.internalMove(currentPos, nextPos);
        } finally {
            endOfOperation2.complete(null);
            endOfOperation.complete(null); // complete future for whole operation
            this.movingDrawable = null;
        }
    }
    
    @InspectionMethod(name = "move")
    private void moveInspector() {
        final Position currentPos = this.getPosition();
        final Position nextPos = currentPos.adjacentPosition(this.lookingDirection);
        
        this.internalMove(currentPos, nextPos);
        this.recalculateAnimationAfterInspector();
    }
    
    private void internalMove(final Position currentPos, final Position nextPos) {
        if (this.isSolidEntityAt(nextPos)) throw new IllegalMoveException("Solid Entity in the way");
        final EntityMoveAction action = new EntityStepAction(
                this.getSimulation().getSimulationClock().getLastTickNumber(), this, currentPos, nextPos
        );
        this.getPlayfield().moveEntity(this, nextPos, action);
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

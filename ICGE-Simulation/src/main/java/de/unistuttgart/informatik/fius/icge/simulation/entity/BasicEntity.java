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

import java.lang.ref.WeakReference;
import java.util.concurrent.CompletableFuture;

import de.unistuttgart.informatik.fius.icge.simulation.Playfield;
import de.unistuttgart.informatik.fius.icge.simulation.Position;
import de.unistuttgart.informatik.fius.icge.simulation.Simulation;
import de.unistuttgart.informatik.fius.icge.simulation.exception.EntityNotOnFieldException;
import de.unistuttgart.informatik.fius.icge.simulation.exception.EntityOnAnotherFieldException;
import de.unistuttgart.informatik.fius.icge.simulation.inspection.InspectionAttribute;
import de.unistuttgart.informatik.fius.icge.ui.BasicDrawable;
import de.unistuttgart.informatik.fius.icge.ui.Drawable;


/**
 * A basic implementation of {@link Entity}
 *
 * @author Tim Neumann
 */
public abstract class BasicEntity implements Entity {

    /** Lock object to make setting the Playfield threadsafe. */
    private Object                   fieldLock = new Object();
    /** The current (weak) reference to the playfield. */
    private WeakReference<Playfield> field;

    /**
     * Lock object to ensure no two long running operations ({@code >= 1} clock tick) are scheduled at the same time.
     */
    private Object                    operationQueueLock = new Object();
    /** The completable future representing the completion of the last enqueued operation. */
    protected CompletableFuture<Void> endOfLastEnqueuedOperation;

    /**
     * @throws EntityNotOnFieldException
     *     if this entity is not on a playfield
     */
    @InspectionAttribute(readOnly = true)
    @Override
    public Position getPosition() {
        // never synchronize on fieldLock in this method to avoid deadlocks!
        // local variable to avoid race conditions in if later
        WeakReference<Playfield> field = this.field;
        if (field == null) throw new EntityNotOnFieldException("This entity is not on a playfield");
        Playfield playfield = field.get();
        if (playfield == null) throw new EntityNotOnFieldException("This entity is not on a playfield");
        return playfield.getEntityPosition(this);
    }

    /**
     * Get the texture handle, with which to get the texture for this entity.
     *
     * @return the texture handle for the texture of this entity
     */
    protected abstract String getTextureHandle();

    /**
     * Get the z position of this entity.
     * <p>
     * The z position is used to order entities on the same field.
     * </p>
     *
     * @return the z position of this entity.
     */
    protected abstract int getZPosition();

    /**
     * @throws EntityNotOnFieldException
     *     if this entity is not on a playfield
     */
    @Override
    public Drawable getDrawInformation() {
        final Position pos = this.getPosition();
        return new BasicDrawable(pos.getX(), pos.getY(), this.getZPosition(), this.getTextureHandle());
    }

    @Override
    public void initOnPlayfield(final Playfield playfield) {
        if (playfield == null) throw new IllegalArgumentException("The given playfield is null.");
        synchronized (this.fieldLock) {
            if (this.isOnPlayfield()) throw new EntityOnAnotherFieldException("This entity can only be on a single playfield!");
            this.field = new WeakReference<>(playfield);
        }
    }

    /**
     * Check whether this entity is on a playfield
     *
     * @return true if and only if this entity is on a playfield
     */
    public boolean isOnPlayfield() {
        synchronized (this.fieldLock) {
            if (this.field == null) return false;
            final Playfield playfield = this.field.get();
            if (playfield == null) {
                // Was on playfield, but no reference to it left, so it does not have a reference to this either.
                this.field = null;
                return false;
            }
            if (!playfield.containsEntity(this)) {
                // Was on playfield, but no more or just being added on playfield
                return false;
            }
            return true;
        }
    }

    /**
     * Get the playfield of this entity.
     *
     * @return the playfield
     * @throws EntityNotOnFieldException
     *     if this entity is not on a playfield
     */
    protected Playfield getPlayfield() {
        synchronized (this.fieldLock) {
            if (!this.isOnPlayfield()) throw new EntityNotOnFieldException("This entity is not on a playfield");
            return this.field.get();
        }
    }

    /**
     * Get the simulation of this entity.
     *
     * @return the simulation
     * @throws EntityNotOnFieldException
     *     if this entity is not on a playfield
     * @throws IllegalStateException
     *     if the playfield of this entity is not part of any simulation
     */
    protected Simulation getSimulation() {
        synchronized (this.fieldLock) {
            return this.getPlayfield().getSimulation();
        }
    }

    /**
     * Prevent this entity from performing any long running operation for {@code ticks} simulation ticks.
     * <p>
     * Only operations that take {@code >= 1} clock ticks to execute will be affected by this sleep.
     *
     * @param ticks
     *     numberof simulation ticks to pause; must be {@code > 0}
     * @throws IllegalArgumentException
     *     if ticks is {@code <= 0}
     */
    public void sleep(final int ticks) {
        if (ticks <= 0) throw new IllegalArgumentException("The number of ticks must be > 0 !");
        final CompletableFuture<Void> endOfOperation = new CompletableFuture<>();
        this.enqueueToPerformNewOperation(endOfOperation);
        try {
            this.getSimulation().getSimulationClock().scheduleOperationInTicks(ticks, endOfOperation);
        } finally {
            endOfOperation.complete(null);
        }
    }

    /**
     * Wait for the last enqueued long running operation to finish before allowing the new Operation to perform.
     * <p>
     * Use this method only if you know what you are doing!
     * <p>
     * Due to the possibility of using an entity from multiple threads, it would be possible for an entity to have
     * multiple long running operations (e.g. turning, walking) at once. To make sure that does not happen it is
     * necessary to only allow one thread per entity to schedule an operation via the simulation clock. This method
     * helps with that.
     * <p>
     * Call this method before scheduling an operation with the simulation clock.
     * <p>
     * This method synchronizes on the {@link #operationQueueLock} to make sure that only one thread can pass.
     * <p>
     * The field {@link #endOfLastEnqueuedOperation} keeps track of the operation that is currently performed by this
     * entity.
     *
     * @param endOfNewOperation
     *     The completable future representing the operation to be performed (must be completed when the operation is
     *     completed)
     */
    protected void enqueueToPerformNewOperation(CompletableFuture<Void> endOfNewOperation) {
        /* Help to understand what happens in this method:
         * Think of a (single) linked list. The list head is this.endOfLastEnqueuedOperation.
         * In the synchronized block the current list head is stored in the local temp variable
         * and replaced by the new list head.
         * The list links are the calls to the local endOfLastEnqueuedOperation.join()
         * to wait for the current list head to finish before exiting this method.
         */
        final CompletableFuture<Void> endOfLastEnqueuedOperation;
        synchronized (this.operationQueueLock) {
            // switch out last enqueued operation with the new operation end
            endOfLastEnqueuedOperation = this.endOfLastEnqueuedOperation;
            this.endOfLastEnqueuedOperation = endOfNewOperation;
        }
        if (endOfLastEnqueuedOperation != null) {
            endOfLastEnqueuedOperation.join();
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(this.hashCode());
    }

}

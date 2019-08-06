/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 *
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.ui.internal;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import de.unistuttgart.informatik.fius.icge.ui.Drawable;
import de.unistuttgart.informatik.fius.icge.ui.PlayfieldDrawer;
import de.unistuttgart.informatik.fius.icge.ui.TextureRegistry;


/**
 * An implementation of {@link PlayfieldDrawer} using java swing.
 *
 * @author Tim Neumann
 */
public class SwingPlayfieldDrawer extends JPanel implements PlayfieldDrawer {

    /** Stretch factor for mapping row/column coordinates to screen coordinates. */
    private final double CELL_SIZE = 32;

    // Colors
    private final Color GRID_COLOR    = new Color(46, 52, 54);
    private final Color OVERLAY_COLOR = new Color(0, 255, 40, 50);

    private TextureRegistry textureRegistry;

    // current display offset and zoom
    private double offsetX = 0;
    private double offsetY = 0;
    private double scale   = 1.0;

    private int minX = 0;
    private int minY = 0;
    private int maxX = 10;
    private int maxY = 10;

    private Rectangle viewport;

    private List<Drawable> drawables;

    /**
     * Initialize the PlayfieldDrawer.
     *
     * @param parent
     *     the SwingUIManager that contains this PlayfieldDrawer.
     */
    public void initialize(SwingUIManager parent) {
        this.textureRegistry = parent.getTextureRegistry();
    }

    public void setDrawables(List<Drawable> drawables) {
        drawables.sort((a, b) -> a.compareTo(b));
        this.drawables = drawables;
    }

    @Override
    public void paint(Graphics g) {
        this.updateViewport(g);
        this.paintGrid(g);
        this.paintDrawableList(g, this.drawables);
    }

    public void updateViewport(Graphics g) {
        this.viewport = g.getClipBounds();
        double centerColumn = 0.5 * (this.minX + this.maxX);
        double centerRow = 0.5 * (this.minY + this.maxY);
        this.offsetX = (0.5 * this.viewport.width) - (centerColumn * this.scale);
        this.offsetY = (0.5 * this.viewport.height) - (centerRow * this.scale);
    }

    private void paintGrid(Graphics g) {
        // cell size on screen (with zoom)
        double cellSize = this.CELL_SIZE * this.scale;
        // first visible cell (row/column coordinates without fractions correspond
        // to the top left corner of e cell on screen)
        double firstX = Math.IEEEremainder(this.offsetX, cellSize);
        double firstY = Math.IEEEremainder(this.offsetY, cellSize);

        g.setColor(this.GRID_COLOR);
        for (double x = firstX; x <= this.viewport.width; x += cellSize) {
            int ix = (int) x;
            g.drawLine(ix, 0, ix, this.viewport.height);
        }
        for (double y = firstY; y <= this.viewport.height; y += cellSize) {
            int iy = (int) y;
            g.drawLine(0, iy, this.viewport.width, iy);
        }

        // mark (0,0) for debugging
        g.fillRect(
                Math.toIntExact(Math.round(this.offsetX)), Math.toIntExact(Math.round(this.offsetY)), Math.toIntExact(Math.round(cellSize)), Math.toIntExact(Math.round(cellSize))
        );
    }

    /**
     * Compare two drawables and checks if they can be grouped together.
     *
     * Drawables have to be in the same cell and have the same texture to be considered equal.
     * The cell coordinates of both drawables are rounded for this comparison.
     *
     * @param a Drawable a
     * @param b Drawable b
     * @return Drawables can be grouped
     */
    private boolean canGroupDrawables(Drawable a, Drawable b) {
        if (a == null || b == null) {
            return false;
        }
        if (!a.textureHandle.equals(b.textureHandle)) {
            return false;
        }
        if (Math.round(a.x) != Math.round(b.x)) {
            return false;
        }
        if (Math.round(a.y) != Math.round(b.y)) {
            return false;
        }
        return true;
    }

    private void paintDrawableList(Graphics g, List<Drawable> drawables) {
        if (drawables.size() <= 0) {
            return;
        }
        Iterator<Drawable> iter = drawables.iterator();
        Drawable last = null;
        int currentCount = 0;

        // group and count drawables
        while (iter.hasNext()) {
            Drawable next = iter.next();
            currentCount += 1;
            if (this.canGroupDrawables(last, next)) {
                continue;
            }
            if (last != null) {
                this.paintDrawable(g, last, currentCount);
            }
            last = next;
            if (iter.hasNext()) {
                // do not reset count for the last drawable
                currentCount = 0;
            }
        }
        if (last != null) {
            this.paintDrawable(g, last, currentCount);
        }
    }

    private void paintDrawable(Graphics g, Drawable drawable, int count) {
        if (count <= 1) {
            Image texture = this.textureRegistry.getTextureForHandle(drawable.textureHandle);
            double cellSize = this.CELL_SIZE * this.scale;
            int x = Math.toIntExact(Math.round(drawable.x * cellSize + this.offsetX));
            int y = Math.toIntExact(Math.round(drawable.y * cellSize + this.offsetY));
            int textureSize = Math.toIntExact(Math.round(cellSize));
            g.drawImage(texture, x, y, textureSize, textureSize, null);
            return;
        }
        if (count <= 4) {
            Double[] xOffsets = {0.0, 0.5, 0.0, 0.5};
            Double[] yOffsets = {0.0, 0.0, 0.5, 0.5};
            Double scaleAdjust = 0.5;
            this.paintMultiCountDrawable(g, drawable, count, xOffsets, yOffsets, scaleAdjust);
            return;
        }
        Double third = 1.0/3;
        Double twoThird = 2.0/3;
        Double[] xOffsets = {0.0, third, twoThird, 0.0, third, twoThird, 0.0, third, twoThird};
        Double[] yOffsets = {0.0, 0.0, 0.0, third, third, third, twoThird, twoThird, twoThird};
        Double scaleAdjust = third;
        this.paintMultiCountDrawable(g, drawable, count, xOffsets, yOffsets, scaleAdjust);
    }

    private void paintMultiCountDrawable(Graphics g, Drawable drawable, int count, Double[] xOffsets, Double[] yOffsets, Double scaleAdjust) {
        double cellSize = this.CELL_SIZE * this.scale;
        int textureSize = Math.toIntExact(Math.round(cellSize * scaleAdjust));
        Image texture = this.textureRegistry.getTextureForHandle(drawable.textureHandle);
        // limit count to available offsets
        count = Math.min(Math.min(xOffsets.length, yOffsets.length), count);
        for (int i=0; i < count; i++) {
            // intra cell offsets
            double offsetX = cellSize * xOffsets[i];
            double offsetY = cellSize * yOffsets[i];
            int x = Math.toIntExact(Math.round(drawable.x * cellSize + this.offsetX + offsetX));
            int y = Math.toIntExact(Math.round(drawable.y * cellSize + this.offsetY + offsetY));
            g.drawImage(texture, x, y, textureSize, textureSize, null);
        }
    }

    private void paintOverlays(Graphics g) {

    }
}

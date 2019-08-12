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
    
    /**
     * generated
     */
    private static final long serialVersionUID = 1800137555269066525L;
    
    /** Stretch factor for mapping row/column coordinates to screen coordinates. */
    private final double CELL_SIZE = 32;
    
    // Colors
    private final Color GRID_COLOR    = new Color(46, 52, 54);
    private final Color OVERLAY_COLOR = new Color(0, 255, 40, 50);
    
    private TextureRegistry textureRegistry;
    
    // current display offset and zoom
    private double       offsetX = 0;
    private double       offsetY = 0;
    private final double scale   = 1.0;
    
    private final int minX = 0;
    private final int minY = 0;
    private final int maxX = 10;
    private final int maxY = 10;
    
    private Rectangle viewport;
    
    private List<Drawable> drawables = List.of();
    
    /**
     * Initialize the PlayfieldDrawer.
     *
     * @param parent
     *     the SwingUIManager that contains this PlayfieldDrawer.
     */
    public void initialize(final SwingUIManager parent) {
        this.textureRegistry = parent.getTextureRegistry();
    }
    
    @Override
    public void setDrawables(final List<Drawable> drawables) {
        drawables.sort((a, b) -> a.compareTo(b));
        this.drawables = drawables;
    }
    
    @Override
    public void draw() {
        repaint();
    }
    
    @Override
    public void paint(final Graphics g) {
        this.updateViewport(g);
        this.paintGrid(g);
        this.paintDrawableList(g, this.drawables);
    }
    
    public void updateViewport(final Graphics g) {
        this.viewport = g.getClipBounds();
        final double centerColumn = 0.5 * (this.minX + this.maxX);
        final double centerRow = 0.5 * (this.minY + this.maxY);
        this.offsetX = (0.5 * this.viewport.width) - (centerColumn * this.scale);
        this.offsetY = (0.5 * this.viewport.height) - (centerRow * this.scale);
    }
    
    private void paintGrid(final Graphics g) {
        // cell size on screen (with zoom)
        final double cellSize = this.CELL_SIZE * this.scale;
        // first visible cell (row/column coordinates without fractions correspond
        // to the top left corner of e cell on screen)
        final double firstX = Math.IEEEremainder(this.offsetX, cellSize);
        final double firstY = Math.IEEEremainder(this.offsetY, cellSize);
        
        g.setColor(this.GRID_COLOR);
        for (double x = firstX; x <= this.viewport.width; x += cellSize) {
            final int ix = (int) x;
            g.drawLine(ix, 0, ix, this.viewport.height);
        }
        for (double y = firstY; y <= this.viewport.height; y += cellSize) {
            final int iy = (int) y;
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
     * Drawables have to be in the same cell and have the same texture to be considered equal. The cell coordinates of
     * both drawables are rounded for this comparison.
     *
     * @param a
     *     Drawable a
     * @param b
     *     Drawable b
     * @return Drawables can be grouped
     */
    private boolean canGroupDrawables(final Drawable a, final Drawable b) {
        if ((a == null) || (b == null)) return false;
        if (!a.getTextureHandle().equals(b.getTextureHandle())) return false;
        if (Math.round(a.getX()) != Math.round(b.getX()))return false;
        if (Math.round(a.getY()) != Math.round(b.getY())) return false;
        return true;
    }
    
    private void paintDrawableList(final Graphics g, final List<Drawable> drawables) {
        if (drawables.size() <= 0) return;
        final Iterator<Drawable> iter = drawables.iterator();
        Drawable last = null;
        int currentCount = 0;
        boolean isTilable = true;
        
        // group and count drawables
        while (iter.hasNext()) {
            final Drawable next = iter.next();
            currentCount += 1;
            isTilable = isTilable && next.isTilable();
            boolean groupable = this.canGroupDrawables(last, next);
            if (! groupable && last != null) {
                this.paintDrawable(g, last, currentCount, isTilable);
            }
            last = next;
            if (! groupable) {
                currentCount = 0;
                isTilable = true;
            }
        }
        if (last != null) {
            this.paintDrawable(g, last, currentCount + 1, isTilable);
        }
    }
    
    private void paintDrawable(final Graphics g, final Drawable drawable, final int count, final boolean isTilable) {
        if (!isTilable || count <= 1) {
            final Image texture = this.textureRegistry.getTextureForHandle(drawable.getTextureHandle());
            final double cellSize = this.CELL_SIZE * this.scale;
            final int x = Math.toIntExact(Math.round((drawable.getX() * cellSize) + this.offsetX));
            final int y = Math.toIntExact(Math.round((drawable.getY() * cellSize) + this.offsetY));
            final int textureSize = Math.toIntExact(Math.round(cellSize));
            g.drawImage(texture, x, y, textureSize, textureSize, null);
            return;
        }
        if (count <= 4) {
            final Double[] xOffsets = { 0.0, 0.5, 0.0, 0.5 };
            final Double[] yOffsets = { 0.0, 0.0, 0.5, 0.5 };
            final Double scaleAdjust = 0.5;
            this.paintMultiCountDrawable(g, drawable, count, xOffsets, yOffsets, scaleAdjust);
            return;
        }
        final Double third = 1.0 / 3;
        final Double twoThird = 2.0 / 3;
        final Double[] xOffsets = { 0.0, third, twoThird, 0.0, third, twoThird, 0.0, third, twoThird };
        final Double[] yOffsets = { 0.0, 0.0, 0.0, third, third, third, twoThird, twoThird, twoThird };
        final Double scaleAdjust = third;
        this.paintMultiCountDrawable(g, drawable, count, xOffsets, yOffsets, scaleAdjust);
    }
    
    private void paintMultiCountDrawable(
            final Graphics g, final Drawable drawable, int count, final Double[] xOffsets, final Double[] yOffsets, final Double scaleAdjust
    ) {
        final double cellSize = this.CELL_SIZE * this.scale;
        final int textureSize = Math.toIntExact(Math.round(cellSize * scaleAdjust));
        final Image texture = this.textureRegistry.getTextureForHandle(drawable.getTextureHandle());
        // limit count to available offsets
        count = Math.min(Math.min(xOffsets.length, yOffsets.length), count);
        for (int i = 0; i < count; i++) {
            // intra cell offsets
            final double offsetX = cellSize * xOffsets[i];
            final double offsetY = cellSize * yOffsets[i];
            final int x = Math.toIntExact(Math.round((drawable.getX() * cellSize) + this.offsetX + offsetX));
            final int y = Math.toIntExact(Math.round((drawable.getY() * cellSize) + this.offsetY + offsetY));
            g.drawImage(texture, x, y, textureSize, textureSize, null);
        }
    }
}

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

import java.awt.Graphics;
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

    private TextureRegistry textureRegistry;

    private double       offsetX  = 0;
    private double       offsetY  = 0;
    private double       scale    = 1.0;
    private final double cellSize = 16;

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
        // TODO sort drawables!
        this.drawables = drawables;
    }

    @Override
    public void paint(Graphics g) {
        this.updateViewport(g);
        this.paintGrid(g);
    }

    public void updateViewport(Graphics g) {
        this.viewport = g.getClipBounds();
        double centerColumn = 0.5 * (this.minX + this.maxX);
        double centerRow = 0.5 * (this.minY + this.maxY);
        this.offsetX = (0.5 * this.viewport.width) - (centerColumn * this.scale);
        this.offsetY = (0.5 * this.viewport.height) - (centerRow * this.scale);
    }

    private static double remainder(double a, double b) {
        double q = a / b;
        return b * (q - Math.floor(q));
    }

    private void paintGrid(Graphics g) {
        double cellSize = this.cellSize * this.scale;
        double firstX = remainder(this.offsetX - (0.5 * cellSize), cellSize);
        double firstY = remainder(this.offsetY - (0.5 * cellSize), cellSize);
        for (double x = firstX; x <= this.viewport.width; x += cellSize) {
            int ix = (int) x;
            g.drawLine(ix, 0, ix, this.viewport.height);
        }
        for (double y = firstY; y <= this.viewport.height; y += cellSize) {
            int iy = (int) y;
            g.drawLine(0, iy, this.viewport.width, iy);
        }
    }

    private void paintDrawableList(Graphics g, List<Drawable> drawables) {
        if (drawables.size() <= 0) {
            return;
        }
        Iterator<Drawable> iter = drawables.iterator();
        Drawable last = null;
        int currentCount = 0;

        while (iter.hasNext()) {
            Drawable next = iter.next();

            last = next;
        }
    }

    private void paintDrawable(Graphics g, Drawable drawable) {

    }

    private void paintOverlays(Graphics g) {

    }
}

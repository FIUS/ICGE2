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
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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
    private final Color BACKGROUND_COLOR = new Color(255, 255, 255);
    private final Color GRID_COLOR       = new Color(46, 52, 54);
    private final Color OVERLAY_COLOR    = new Color(0, 255, 40, 50);
    
    private SwingTextureRegistry textureRegistry;
    
    // current display offset and zoom
    private double       offsetX = CELL_SIZE;
    private double       offsetY = CELL_SIZE;
    private double scale         = 1.0;
    
    private final int minX = 0;
    private final int minY = 0;
    private final int maxX = 10;
    private final int maxY = 10;
    
    // mouse events
    private int mouseStartX = 0;
    private int mouseStartY = 0;
    private boolean isDrag = false;
    
    private Rectangle viewport;
    
    private List<Drawable> drawables = List.of();
    
    /**
     * Initialize the PlayfieldDrawer.
     *
     * @param parent
     *     the SwingUIManager that contains this PlayfieldDrawer.
     */
    public void initialize(final SwingUIManager parent) {
        TextureRegistry tr = parent.getTextureRegistry();
        if (!(tr instanceof SwingTextureRegistry)) {
            throw new IllegalArgumentException("Only SwingTextureRegistry is supported!");
        }
        this.textureRegistry = (SwingTextureRegistry) tr;
        
        this.addMouseListener(new MouseListener(){
        
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    int x = e.getX();
                    int y = e.getY();
                    SwingPlayfieldDrawer.this.mouseReleased(x, y);
                }
            }
        
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    int x = e.getX();
                    int y = e.getY();
                    SwingPlayfieldDrawer.this.mousePressed(x, y);
                }
            }
        
            @Override
            public void mouseExited(MouseEvent e) {
                // ignore this for now
            }
        
            @Override
            public void mouseEntered(MouseEvent e) {
                // ignore this for now
            }
        
            @Override
            public void mouseClicked(MouseEvent e) {
                // ignore this for now
                
            }
        });
        
        this.addMouseMotionListener(new MouseMotionListener(){
        
            @Override
            public void mouseMoved(MouseEvent e) {
                // ignore this for now
            }
        
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                SwingPlayfieldDrawer.this.updateDrag(x, y);
            }
        });
        
        this.addMouseWheelListener(new MouseWheelListener() {
            
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int rot = e.getWheelRotation();
                int x = e.getX();
                int y = e.getY();
                SwingPlayfieldDrawer.this.updateZoom(rot, x, y);
            }
        });
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
        g.setColor(this.BACKGROUND_COLOR);
        g.fillRect(0, 0, this.viewport.width, this.viewport.height);
        this.paintGrid(g);
        this.paintDrawableList(g, this.drawables);
    }
    
    public void updateViewport(final Graphics g) {
        this.viewport = g.getClipBounds();
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
            final Texture texture = this.textureRegistry.getTextureForHandle(drawable.getTextureHandle());
            final double cellSize = this.CELL_SIZE * this.scale;
            final int x = Math.toIntExact(Math.round((drawable.getX() * cellSize) + this.offsetX));
            final int y = Math.toIntExact(Math.round((drawable.getY() * cellSize) + this.offsetY));
            final int textureSize = Math.toIntExact(Math.round(cellSize));
            texture.drawTexture(g, x, y, textureSize, textureSize);
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
        final Texture texture = this.textureRegistry.getTextureForHandle(drawable.getTextureHandle());
        // limit count to available offsets
        count = Math.min(Math.min(xOffsets.length, yOffsets.length), count);
        for (int i = 0; i < count; i++) {
            // intra cell offsets
            final double offsetX = cellSize * xOffsets[i];
            final double offsetY = cellSize * yOffsets[i];
            final int x = Math.toIntExact(Math.round((drawable.getX() * cellSize) + this.offsetX + offsetX));
            final int y = Math.toIntExact(Math.round((drawable.getY() * cellSize) + this.offsetY + offsetY));
            texture.drawTexture(g, x, y, textureSize, textureSize);
        }
    }
    
    private void mousePressed(int x, int y) {
        this.mouseStartX = x;
        this.mouseStartY = y;
        this.isDrag = false;
    }
    
    private void mouseReleased(int x, int y) {
        if (! this.isDrag) {
            this.mouseClick(this.mouseStartX, this.mouseStartY);
        }
    }
    
    private void mouseClick(int x, int y) {
        // TODO
        this.repaint();
    }
    
    private void updateDrag(int x, int y) {
        this.isDrag = true;
        // TODO change this later to support different tools...
        this.offsetX += x - this.mouseStartX;
        this.offsetY += y - this.mouseStartY;
        this.mouseStartX = x;
        this.mouseStartY = y;
        this.repaint();
    }
    
    private void updateZoom(int amount, int x, int y) {
        double zoomScaling = 0.1 * Math.ceil(this.scale);
        double newScale = this.scale + (zoomScaling * -amount);
        if (newScale < 0.4) {
            newScale = 0.4;
        }
        if (newScale > 10) {
            newScale = 10;
        }
        double dxOld = x - this.offsetX;
        double dyOld = y - this.offsetY;
        double stretchFactor = newScale / this.scale;
        double dx = dxOld - (dxOld * stretchFactor);
        double dy = dyOld - (dyOld * stretchFactor);
        this.scale = newScale;
        this.offsetX += dx;
        this.offsetY += dy;
        this.repaint();
    }
}

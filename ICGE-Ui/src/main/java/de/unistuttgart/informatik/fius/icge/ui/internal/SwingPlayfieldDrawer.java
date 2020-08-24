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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.JPanel;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

import de.unistuttgart.informatik.fius.icge.ui.Drawable;
import de.unistuttgart.informatik.fius.icge.ui.PlayfieldDrawer;
import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy;
import de.unistuttgart.informatik.fius.icge.ui.Toolbar.ControlButtonState;


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
    private final double CELL_SIZE;
    
    private final int INFO_BAR_HEIGHT;
    
    // Colors
    private static final Color BACKGROUND_COLOR             = new Color(255, 255, 255);
    private static final Color BACKGROUND_COLOR_TRANSPARENT = new Color(255, 255, 255, 230);
    private static final Color GRID_COLOR                   = new Color(46, 52, 54);
    private static final Color OVERLAY_COLOR                = new Color(0, 40, 255, 50);
    
    private static final RenderingHints RENDERING_HINTS = new RenderingHints(
            RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON
    );
    
    private final SwingTextureRegistry textureRegistry;
    
    // current display offset and zoom
    private double offsetX;
    private double offsetY;
    private double scale = 1.0;
    
    // mouse events
    private boolean mouseInWindow = false;
    private int     currentMouseX = 0;
    private int     currentMouseY = 0;
    private int     mouseStartX   = 0;
    private int     mouseStartY   = 0;
    private boolean isDrag        = false;
    
    // current tool state
    private ControlButtonState activeTool            = ControlButtonState.BLOCKED;
    private String             selectedEntityType    = null;
    private String             selectedEntityTexture = null;
    private SimulationProxy    simulationProxy;
    
    private List<Drawable> drawables         = List.of();
    private List<Drawable> animatedDrawables = List.of();
    private boolean        fullRepaintNeeded = true;
    private Rectangle      lastRedrawArea    = null;
    private long           currentFrame      = 0;
    
    // current graphic settings
    private final RepaintManager repaintManager;
    private boolean              useDoubleBuffer = true;
    private boolean              syncToscreen    = true;
    
    /**
     * Create a new SwingPlayfieldDrawer.
     *
     * @param textureRegistry
     *     The texture registry
     * @param dpiScale
     *     The scaling value to adjust for highdpi screens
     */
    public SwingPlayfieldDrawer(final SwingTextureRegistry textureRegistry, final double dpiScale) {
        this.textureRegistry = textureRegistry;
        
        this.setOpaque(true);
        this.repaintManager = RepaintManager.currentManager(this);
        
        this.INFO_BAR_HEIGHT = (int) Math.floor(25 * dpiScale);
        this.CELL_SIZE = (int) Math.floor(32 * dpiScale);
        this.offsetX = this.CELL_SIZE;
        this.offsetY = this.CELL_SIZE;
        
        Font font = this.getFont();
        System.out.println(font.getFontName() + " " + font.getSize());
        final int newFontSize = (int) Math.floor(12 * dpiScale);
        this.setFont(new Font(font.getFontName(), font.getStyle(), newFontSize));
    }
    
    /**
     * Set the simulation proxy. TODO better doc
     *
     * @param simulationProxy
     *     The simulation proxy this SwingPlayfieldDrawer should subscribe to
     */
    public void setSimulationProxy(final SimulationProxy simulationProxy) {
        if (this.simulationProxy != null) throw new IllegalStateException("SimulationProxy is already set and cannot be overwritten!");
        
        this.simulationProxy = simulationProxy;
    }
    
    /**
     * Initialize the PlayfieldDrawer.
     */
    public void initialize() {
        
        this.addMouseListener(new MouseListener() {
            
            @Override
            public void mouseReleased(final MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    SwingPlayfieldDrawer.this.mouseReleased(e.getX(), e.getY());
                }
            }
            
            @Override
            public void mousePressed(final MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    SwingPlayfieldDrawer.this.mousePressed(e.getX(), e.getY());
                }
            }
            
            @Override
            public void mouseExited(final MouseEvent e) {
                SwingPlayfieldDrawer.this.updateMouseInWindow(false);
            }
            
            @Override
            public void mouseEntered(final MouseEvent e) {
                SwingPlayfieldDrawer.this.updateMouseInWindow(true);
            }
            
            @Override
            public void mouseClicked(final MouseEvent e) {
                // ignore this for now
                
            }
        });
        
        this.addMouseMotionListener(new MouseMotionListener() {
            
            @Override
            public void mouseMoved(final MouseEvent e) {
                SwingPlayfieldDrawer.this.updateMousePosition(e.getX(), e.getY());
            }
            
            @Override
            public void mouseDragged(final MouseEvent e) {
                SwingPlayfieldDrawer.this.updateDrag(e.getX(), e.getY());
                SwingPlayfieldDrawer.this.updateMousePosition(e.getX(), e.getY());
            }
        });
        
        this.addMouseWheelListener(e -> {
            final int rot = e.getWheelRotation();
            final int x = e.getX();
            final int y = e.getY();
            SwingPlayfieldDrawer.this.updateZoom(rot, x, y);
        });
    }
    
    @Override
    public void setDrawables(final List<Drawable> drawables) {
        this.drawables = drawables.stream().sorted((a, b) -> a.compareTo(b)).collect(Collectors.toUnmodifiableList());
        this.animatedDrawables = drawables.stream().filter(
                d -> d.isAnimated() || this.textureRegistry.isTextureAnimated(d.getTextureHandle())
        ).collect(Collectors.toUnmodifiableList());
        this.fullRepaintNeeded = true;
        this.draw(this.currentFrame);
    }
    
    @Override
    public void draw(final long tickCount) {
        this.currentFrame = tickCount;
        this.animatedDrawables.forEach(d -> d.setCurrentTick(tickCount));
        if (this.animatedDrawables.size() > 0) {
            this.drawables = this.drawables.stream().sorted((a, b) -> a.compareTo(b)).collect(Collectors.toUnmodifiableList());
        }
        
        SwingUtilities.invokeLater(() -> {
            // synchronize this to fix possible null pointer when setting double buffer setting
            
            final boolean bufferEnabled = this.repaintManager.isDoubleBufferingEnabled();
            if (!this.useDoubleBuffer) {
                // only  change strategy to false since true should already be default
                this.repaintManager.setDoubleBufferingEnabled(this.useDoubleBuffer);
            }
            
            if (this.fullRepaintNeeded) {
                Rectangle visible = this.getVisibleRect();
                if (visible == null) {
                    visible = new Rectangle(0, 0, this.getWidth(), this.getHeight());
                }
                this.paintImmediately(visible);
                this.fullRepaintNeeded = false;
            } else {
                if (this.animatedDrawables.size() > 0) {
                    final Rectangle visible = this.getVisibleRect();
                    final double cellSize = this.CELL_SIZE * this.scale;
                    final int textureSize = Math.toIntExact(Math.round(cellSize));
                    final Optional<Rectangle> rectToDraw = this.animatedDrawables.stream().map(
                            d -> this.getScreenPointFromCellCoordinates(d.getX(), d.getY(), cellSize)
                    ).map(p -> SwingPlayfieldDrawer.getPaintRectFromPoint(p, textureSize)).filter(r -> r.intersects(visible))
                            .reduce((final Rectangle r1, final Rectangle r2) -> {
                                r1.add(r2);
                                return r1;
                            }).map(rect -> {
                                return new Rectangle(rect.x - 5, rect.y - 5, rect.width + 10, rect.height + 10);
                            });
                    if (rectToDraw.isPresent()) {
                        final Rectangle lastRedraw = this.lastRedrawArea;
                        if (lastRedraw != null) {
                            this.paintImmediately(lastRedraw);
                        }
                        final Rectangle toDraw = rectToDraw.get();
                        this.lastRedrawArea = toDraw;
                        if (toDraw != null) {
                            this.paintImmediately(toDraw);
                        }
                    } else {
                        this.lastRedrawArea = null;
                    }
                }
            }
            if (this.syncToscreen) {
                // flush drawing changes to screen (improves render latency when mouse is not in window)
                Toolkit.getDefaultToolkit().sync();
            }
            // reset setting after draw
            if (!this.useDoubleBuffer) {
                // only  change strategy to false since true should already be default
                this.repaintManager.setDoubleBufferingEnabled(bufferEnabled);
            }
        });
        // filter out finished animations
        this.animatedDrawables = this.drawables.stream().filter(
                d -> d.isAnimated() || this.textureRegistry.isTextureAnimated(d.getTextureHandle())
        ).collect(Collectors.toUnmodifiableList());
    }
    
    @Override
    public void resetZoomAndPan() {
        this.scale = 1.0;
        this.offsetX = this.CELL_SIZE;
        this.offsetY = this.CELL_SIZE;
    }
    
    @Override
    public void setDoubleBuffering(final boolean useDoubleBuffering) {
        this.useDoubleBuffer = useDoubleBuffering;
    }
    
    @Override
    public void setSyncToScreen(final boolean syncToScreen) {
        this.syncToscreen = syncToScreen;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }
    
    private int getColumnCoordinateFromScreenCoordinate(final int screenX) {
        final double cellSize = this.CELL_SIZE * this.scale;
        return (int) Math.floor((screenX - this.offsetX) / cellSize);
    }
    
    private int getRowCoordinateFromScreenCoordinate(final int screenY) {
        final double cellSize = this.CELL_SIZE * this.scale;
        return (int) Math.floor((screenY - this.offsetY) / cellSize);
    }
    
    private Point getScreenPointFromCellCoordinates(final double x, final double y, final double cellSize) {
        final int screenX = Math.toIntExact(Math.round((x * cellSize) + this.offsetX));
        final int screenY = Math.toIntExact(Math.round((y * cellSize) + this.offsetY));
        return new Point(screenX, screenY);
    }
    
    private static Rectangle getPaintRectFromPoint(final Point upperLeftCorner, final int cellSize) {
        return new Rectangle(upperLeftCorner.x, upperLeftCorner.y, cellSize, cellSize);
    }
    
    @Override
    public void paintComponent(final Graphics g) {
        if (g instanceof Graphics2D) {
            ((Graphics2D) g).setRenderingHints(SwingPlayfieldDrawer.RENDERING_HINTS);
        }
        g.setColor(SwingPlayfieldDrawer.BACKGROUND_COLOR);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        this.paintGrid(g);
        this.paintDrawableList(g, this.drawables);
        this.paintOverlay(g);
    }
    
    private void paintGrid(final Graphics g) {
        final Rectangle clipBounds = g.getClipBounds();
        // cell size on screen (with zoom)
        final double cellSize = this.CELL_SIZE * this.scale;
        // first visible cell (row/column coordinates without fractions correspond
        // to the top left corner of e cell on screen)
        final double firstX = (Math.IEEEremainder(this.offsetX - clipBounds.x, cellSize) + clipBounds.x) - cellSize;
        final double firstY = (Math.IEEEremainder(this.offsetY - clipBounds.y, cellSize) + clipBounds.y) - cellSize;
        
        final int width = this.getWidth();
        final int height = this.getHeight();
        g.setColor(SwingPlayfieldDrawer.GRID_COLOR);
        final int upperX = clipBounds.x + clipBounds.width;
        for (double x = firstX; x <= upperX; x += cellSize) {
            final int ix = (int) x;
            g.drawLine(ix, 0, ix, height);
        }
        final int upperY = clipBounds.y + clipBounds.height;
        for (double y = firstY; y <= upperY; y += cellSize) {
            final int iy = (int) y;
            g.drawLine(0, iy, width, iy);
        }
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
    private static boolean canGroupDrawables(final Drawable a, final Drawable b) {
        if ((a == null) || (b == null)) return false;
        if (!a.getTextureHandle().equals(b.getTextureHandle())) return false;
        // consider drawables more than 0.1% of the cell width apart from another as different
        if (Math.abs(a.getX() - b.getX()) > 0.001) return false;
        if (Math.abs(a.getY() - b.getY()) > 0.001) return false;
        return true;
    }
    
    private void paintDrawableList(final Graphics g, final List<Drawable> drawablesList) {
        if (drawablesList.size() <= 0) return;
        final Iterator<Drawable> iter = drawablesList.iterator();
        Drawable last = null;
        int currentCount = 0;
        boolean isTilable = true;
        
        // group and count drawables
        while (iter.hasNext()) {
            final Drawable next = iter.next();
            currentCount += 1;
            final boolean groupable = SwingPlayfieldDrawer.canGroupDrawables(last, next);
            if (!groupable && (last != null)) {
                this.paintDrawable(g, last, currentCount, isTilable);
            }
            isTilable = isTilable && next.isTilable();
            last = next;
            if (!groupable) {
                currentCount = 0;
                isTilable = next.isTilable();
            }
        }
        if (last != null) {
            this.paintDrawable(g, last, currentCount + 1, isTilable);
        }
    }
    
    private void paintDrawable(final Graphics g, final Drawable drawable, final int count, final boolean isTilable) {
        final double cellSize = this.CELL_SIZE * this.scale;
        final int x = Math.toIntExact(Math.round((drawable.getX() * cellSize) + this.offsetX));
        final int y = Math.toIntExact(Math.round((drawable.getY() * cellSize) + this.offsetY));
        final int textureSize = Math.toIntExact(Math.round(cellSize));
        if (!g.hitClip(x, y, textureSize, textureSize)) // drawable is not in the area that gets painted
            return;
        if (!isTilable || (count <= 1)) {
            final Texture texture = this.textureRegistry.getTextureForHandle(drawable.getTextureHandle());
            texture.drawTexture(this.currentFrame, g, x, y, textureSize, textureSize);
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
            final Graphics g, final Drawable drawable, final int count, final Double[] xOffsets, final Double[] yOffsets,
            final Double scaleAdjust
    ) {
        final double cellSize = this.CELL_SIZE * this.scale;
        final int textureSize = Math.toIntExact(Math.round(cellSize * scaleAdjust));
        final Texture texture = this.textureRegistry.getTextureForHandle(drawable.getTextureHandle());
        // limit count to available offsets
        final int limitedCount = Math.min(Math.min(xOffsets.length, yOffsets.length), count);
        for (int i = 0; i < limitedCount; i++) {
            // intra cell offsets
            final double tmpOffsetX = cellSize * xOffsets[i];
            final double tmpOffsetY = cellSize * yOffsets[i];
            final int x = Math.toIntExact(Math.round((drawable.getX() * cellSize) + this.offsetX + tmpOffsetX));
            final int y = Math.toIntExact(Math.round((drawable.getY() * cellSize) + this.offsetY + tmpOffsetY));
            texture.drawTexture(this.currentFrame, g, x, y, textureSize, textureSize);
        }
    }
    
    private void paintOverlay(final Graphics g) {
        final int width = this.getWidth();
        final int height = this.getHeight();
        
        final int currentCellX = this.getColumnCoordinateFromScreenCoordinate(this.currentMouseX);
        final int currentCellY = this.getRowCoordinateFromScreenCoordinate(this.currentMouseY);
        
        final double cellSize = this.CELL_SIZE * this.scale;
        final int roundedCellSize = Math.toIntExact(Math.round(cellSize));
        final int screenX = Math.toIntExact(Math.round(this.offsetX + (currentCellX * cellSize)));
        final int screenY = Math.toIntExact(Math.round(this.offsetY + (currentCellY * cellSize)));
        
        // draw cell highlight
        if (this.mouseInWindow && g.hitClip(screenX, screenY, roundedCellSize, roundedCellSize)) {
            // draw current tool texture
            if ((this.activeTool == ControlButtonState.ADD) && (this.selectedEntityTexture != null)) {
                try {
                    final Texture texture = this.textureRegistry.getTextureForHandle(this.selectedEntityTexture);
                    texture.drawTexture(this.currentFrame, g, screenX, screenY, roundedCellSize);
                } catch (@SuppressWarnings("unused") final NoSuchElementException e) {
                    // cannot draw texture
                }
            }
            if (this.activeTool == ControlButtonState.SUB) {
                // redraw background to simulate empty field
                g.setColor(SwingPlayfieldDrawer.BACKGROUND_COLOR_TRANSPARENT);
                g.fillRect(screenX, screenY, roundedCellSize, roundedCellSize);
            }
            // draw cell highlight
            g.setColor(SwingPlayfieldDrawer.OVERLAY_COLOR);
            g.fillRect(screenX, screenY, roundedCellSize, roundedCellSize);
        }
        
        // draw info bar
        if (this.mouseInWindow && g.hitClip(0, height - this.INFO_BAR_HEIGHT, width, this.INFO_BAR_HEIGHT)) {
            
            g.setColor(SwingPlayfieldDrawer.BACKGROUND_COLOR);
            g.fillRect(0, height - this.INFO_BAR_HEIGHT, width, this.INFO_BAR_HEIGHT);
            g.setColor(SwingPlayfieldDrawer.GRID_COLOR);
            
            // calculate baseline
            final FontMetrics font = g.getFontMetrics();
            final int heightAboveBaseline = font.getAscent();
            final int heightBelowBaseline = font.getMaxDescent();
            final int baselineCentered = Math.toIntExact(Math.round((this.INFO_BAR_HEIGHT / 2.0) - (heightAboveBaseline / 2.0)));
            final int baseline = height - Math.max(baselineCentered, heightBelowBaseline);
            
            // build string
            final String infoText = "Cell (x=" + currentCellX + ", y=" + currentCellY + ")";
            g.drawString(infoText, 5, baseline);
            
        }
    }
    
    private void mousePressed(final int screenX, final int screenY) {
        this.mouseStartX = screenX;
        this.mouseStartY = screenY;
        this.isDrag = false;
    }
    
    private void mouseReleased(final int screenX, final int screenY) {
        if (!this.isDrag) {
            this.mouseClick(this.mouseStartX, this.mouseStartY);
        }
    }
    
    private void mouseClick(final int screenX, final int screenY) {
        final int x = this.getColumnCoordinateFromScreenCoordinate(screenX);
        final int y = this.getRowCoordinateFromScreenCoordinate(screenY);
        
        if (this.activeTool == ControlButtonState.ADD) {
            final String type = this.selectedEntityType;
            if ((type == null) || type.equals("")) {
                System.out.println("Could not spawn entity, no type selected!");
                return;
            }
            try {
                this.simulationProxy.spawnEntityAt(type, x, y);
            } catch (final Exception e) {
                System.out.println("Error while spawning entity of type " + type + " at (x=" + x + ", y=" + y + ").");
                e.printStackTrace();
            }
        }
        if (this.activeTool == ControlButtonState.SUB) {
            try {
                this.simulationProxy.clearCell(x, y);
            } catch (final Exception e) {
                System.out.println("Error while clearing the cell (x=" + x + ", y=" + y + ").");
                e.printStackTrace();
            }
        }
        this.repaint();
    }
    
    private void updateMouseInWindow(final boolean mouseInWindow) {
        this.mouseInWindow = mouseInWindow;
        this.repaintMouseOverlay();
    }
    
    private void updateMousePosition(final int x, final int y) {
        final int oldX = this.currentMouseX;
        final int oldY = this.currentMouseY;
        this.currentMouseX = x;
        this.currentMouseY = y;
        this.repaintCellHighlight(oldX, oldY);
        this.repaintMouseOverlay();
    }
    
    private void repaintMouseOverlay() {
        this.repaintCellHighlight(this.currentMouseX, this.currentMouseY);
        this.repaint(0, this.getHeight() - this.INFO_BAR_HEIGHT, this.getWidth(), this.INFO_BAR_HEIGHT);
    }
    
    private void repaintCellHighlight(final int x, final int y) {
        final double cellSize = this.CELL_SIZE * this.scale;
        final int roundedCellSize = Math.toIntExact(Math.round(cellSize));
        this.repaint(x - roundedCellSize, y - roundedCellSize, 2 * roundedCellSize, 2 * roundedCellSize);
    }
    
    private void updateDrag(final int x, final int y) {
        this.isDrag = true;
        // TODO change this later to support different tools...
        this.offsetX += x - this.mouseStartX;
        this.offsetY += y - this.mouseStartY;
        this.mouseStartX = x;
        this.mouseStartY = y;
        this.repaint();
    }
    
    private void updateZoom(final int amount, final int x, final int y) {
        final double zoomScaling = 0.1 * Math.ceil(this.scale);
        double newScale = this.scale + (zoomScaling * -amount);
        if (newScale < 0.4) {
            newScale = 0.4;
        }
        if (newScale > 10) {
            newScale = 10;
        }
        final double dxOld = x - this.offsetX;
        final double dyOld = y - this.offsetY;
        final double stretchFactor = newScale / this.scale;
        final double dx = dxOld - (dxOld * stretchFactor);
        final double dy = dyOld - (dyOld * stretchFactor);
        this.scale = newScale;
        this.offsetX += dx;
        this.offsetY += dy;
        this.repaint();
    }
    
    @Override
    public void setSelectedTool(final ControlButtonState selectedTool) {
        this.activeTool = selectedTool;
    }
    
    @Override
    public void setSelectedEntityType(final String typeName, final String textureHandle) {
        this.selectedEntityType = typeName;
        this.selectedEntityTexture = textureHandle;
    }
}

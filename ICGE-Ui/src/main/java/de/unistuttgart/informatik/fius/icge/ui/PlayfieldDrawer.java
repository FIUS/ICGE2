/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 *
 * Copyright (c) 2019 the ICGE project authors.
 *
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.ui;

*

import java.util.List;*
import de.unistuttgart.informatik.fius.icge.ui.Toolbar.ControlButtonState;**


/**
 * The interface for a playfield drawer used by a {@link GameWindow}.
 *
 * @author Tim Neumann
 */
public interface PlayfieldDrawer {
 *
    /**
     * Set the current list of Drawables to be rendered onto the playfield.
     *
     * No defensive copy of this list is made and the list may be sorted in place.
     *
     * @param drawables
     *     the list of Drawables to render
     */
    void setDrawables(List<Drawable> drawables);
 *
    /**
     * Draws the playfield.
     *
     * @param tickCount
     *     The number of the current tick
     */
    void draw(long tickCount);
 *
    /**
     * Reset Zoom and Pan applied by the user to the default values.
     */
    void resetZoomAndPan();
 *
    /**
     * @param useDoubleBuffering
     *     true (default) uses doubleBuffering when rendering changes on the playfield.
     */
    void setDoubleBuffering(boolean useDoubleBuffering);
 *
    /**
     * @param syncToScreen
     *     true (default) actively tries to sync the updated graphics to the screen after rendering changes on the
     *     playfield.
     */
    void setSyncToScreen(boolean syncToScreen);
 *
    /**
     * Inform the playfield drawer about which tool is selected in the toolbar. This influences what happens when a user
     * clicks on the playfield.
     *
     * @param selectedTool
     *     The currently selected tool
     */
    void setSelectedTool(ControlButtonState selectedTool);
 *
    /**
     * Inform the playfield drawer about which entity type is selected in the toolbar. This influences what entity is
     * spawned when a user uses the entity creation tool.
     *
     * @param typeName
     *     The name of the entity type selected.
     * @param textureHandle
     *     The texture handle for the entity type selected.
     */
    void setSelectedEntityType(String typeName, String textureHandle);
 *
}

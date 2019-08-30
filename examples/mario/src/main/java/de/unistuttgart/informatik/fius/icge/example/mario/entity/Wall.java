/*
 * ICGE-Example-Mario
 *
 * TODO: Project Beschreibung
 *
 * @author Tim Neumann
 * @version 1.0.0
 *
 */
package de.unistauttgart.informatik.fius.icge.example.mario.entity;

import de.unistauttgart.informatik.fius.icge.example.mario.Texture;
import de.unistuttgart.informatik.fius.icge.simulation.entity.BasicEntity;

/**
 * The wall entity
 * 
 * @author Tim Neumann
 */
public class Wall extends BasicEntity {

    @Override
    protected String getTextureHandle() {
        return Texture.WALL.getHandle();
    }

    @Override
    protected int getZPosition() {
        return 0;
    }

}

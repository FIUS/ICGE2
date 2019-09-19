/*
 * ICGE-Example-Mario
 *
 * TODO: Project Beschreibung
 *
 * @author Tim Neumann
 * @version 1.0.0
 *
 */
package de.unistuttgart.informatik.fius.icge.example.mario.entity;

import de.unistuttgart.informatik.fius.icge.example.mario.Texture;
import de.unistuttgart.informatik.fius.icge.simulation.entity.BasicEntity;
import de.unistuttgart.informatik.fius.icge.simulation.entity.SolidEntity;


/**
 * The wall entity
 * 
 * @author Tim Neumann
 */
public class Wall extends BasicEntity implements SolidEntity {
    
    @Override
    protected String getTextureHandle() {
        return Texture.WALL.getHandle();
    }
    
    @Override
    protected int getZPosition() {
        return 0;
    }
    
}

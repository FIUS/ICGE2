package de.unistuttgart.informatik.fius.icge.manualstart;

import de.unistuttgart.informatik.fius.icge.simulation.entity.BasicEntity;
import de.unistuttgart.informatik.fius.icge.simulation.entity.CollectableEntity;


/**
 * Basic coin class.
 *
 * @author Fabian Bühler
 */
public class Coin extends BasicEntity implements CollectableEntity {
    
    /**
     * The texture handle used for the test entity.
     */
    public static String TEXTURE_HANDLE;
    
    @Override
    protected String getTextureHandle() {
        return TEXTURE_HANDLE;
    }
    
    @Override
    protected int getZPosition() {
        return 0;
    }
}

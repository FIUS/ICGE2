/*
 * ICGE-Example-Mario
 *
 * TODO: Project Beschreibung
 *
 * @author Tim Neumann
 * @version 1.0.0
 *
 */
package de.unistuttgart.informatik.fius.icge.example.mario;

import de.unistuttgart.informatik.fius.icge.example.mario.entity.Mario;
import de.unistuttgart.informatik.fius.icge.example.mario.entity.Wall;
import de.unistuttgart.informatik.fius.icge.simulation.Position;
import de.unistuttgart.informatik.fius.icge.simulation.Simulation;
import de.unistuttgart.informatik.fius.icge.simulation.SimulationFactory;
import de.unistuttgart.informatik.fius.icge.ui.TextureRegistry;
import de.unistuttgart.informatik.fius.icge.ui.UiManager;

/**
 * Main class of the example
 * 
 * @author Tim Neumann
 */
public class Main {

    /**
     * The main entry point of the example
     * 
     * @param args
     *            the command line args; not used
     */
    public static void main(String[] args) {
        Simulation sim = SimulationFactory.createSimulation();
        prepareUiManager(sim.getUiManager());

        sim.initialize();
        sim.getPlayfield().addEntity(new Position(1, 4), new Wall());
        sim.getPlayfield().addEntity(new Position(2, 4), new Wall());
        sim.getPlayfield().addEntity(new Position(3, 4), new Wall());
        sim.getPlayfield().addEntity(new Position(4, 4), new Wall());
        
        Mario mario = new Mario();
        
        sim.getPlayfield().addEntity(new Position(0, 0), mario);
        
        sim.getSimulationClock().start();
        
        mario.turnClockWise();
        mario.turnClockWise();
        mario.turnClockWise();
        mario.turnClockWise();
        mario.move();
        mario.turnClockWise();
        
        mario.move();
        mario.move();
        mario.move();
        mario.moveIfPossible();
        System.out.println(mario.canMove());
        mario.move();
    }

    private static void prepareUiManager(UiManager manager) {
        // load textures
        final TextureRegistry tr = manager.getTextureRegistry();
        for (Texture texture : Texture.values()) {
            texture.load(tr);
        }
        manager.setWindowTitle("Manual simulation start");
    }
}

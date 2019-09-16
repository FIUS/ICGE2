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
import de.unistuttgart.informatik.fius.icge.simulation.entity.program.EntityProgramInfo;
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
        sim.getPlayfield().addEntity(new Position(-3, -1), new Wall());
        sim.getPlayfield().addEntity(new Position(-3, 0), new Wall());
        sim.getPlayfield().addEntity(new Position(-3, 1), new Wall());
        sim.getPlayfield().addEntity(new Position(3, -1), new Wall());
        sim.getPlayfield().addEntity(new Position(3, 0), new Wall());
        sim.getPlayfield().addEntity(new Position(3, 1), new Wall());
        
        Mario walkingMario = new Mario();
        
        Mario spinningMario = new Mario();
        
        sim.getPlayfield().addEntity(new Position(-1, 0), walkingMario);
        sim.getPlayfield().addEntity(new Position(0, 0), spinningMario);
        
        EntityProgramInfo walking = new EntityProgramInfo("Walking", new WalkingProgram());
        
        sim.getEntityProgramRegistry().registerEntityProgram(walking);
        
        sim.getSimulationClock().start();
        
        walking.run(walkingMario);
        
        while (true) {
            spinningMario.turnClockWise();
        }
        
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

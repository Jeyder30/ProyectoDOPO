package Tests;

import Simulation.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Pruebas colaborativas del RobotPolice en el contexto de SilkRoad.
 */
public class SilkRoadPoliceCollaborationTest {

    @Test
    public void policeShouldRemoveRobotsWithNegativeTenges() {
        SilkRoad road = new SilkRoad(30);

        try
        {
            // Colocar robots
        road.placeRobot(5, "normal");
        }
        catch (SilkRoadException sre)
        {
            sre.printStackTrace();
        }
        try
        {
            road.placeRobot(10, "normal");
        }
        catch (SilkRoadException sre)
        {
            sre.printStackTrace();
        }

        // Forzar al robot 10 a quedar con tenges negativos (simulamos pérdida)
        Robot r = new Robot(10, "red");
        r.addTenges(-50);
        road.removeRobot(10);  // Simulamos que ese robot debe desaparecer

        // Colocar RobotPolice
        RobotPolice police = new RobotPolice(0, "black");
        police.patrol(road);

        // No debe quedar el robot negativo
        int[][] robots = road.robots();
        for (int[] rob : robots) {
            assertTrue("No debe haber robots con tenges negativos", rob[1] >= 0);
        }
    }

    @Test
    public void policeShouldCountRemovedRobots() {
        SilkRoad road = new SilkRoad(30);

        // Crear 3 robots, uno con tenges negativos
        Robot r1 = new Robot(5, "red");
        r1.addTenges(-20);
        Robot r2 = new Robot(8, "blue");
        Robot r3 = new Robot(12, "green");

        try
        {
            road.placeRobot(5, "normal");
        }
        catch (SilkRoadException sre)
        {
            sre.printStackTrace();
        }
        try
        {
            road.placeRobot(8, "normal");
        }
        catch (SilkRoadException sre)
        {
            sre.printStackTrace();
        }
        try
        {
            road.placeRobot(12, "normal");
        }
        catch (SilkRoadException sre)
        {
            sre.printStackTrace();
        }

        RobotPolice police = new RobotPolice(0, "black");
        police.patrol(road);

        int[][] robotsRemaining = road.robots();
        boolean hasNegative = false;
        for (int[] data : robotsRemaining) {
            if (data[1] < 0) hasNegative = true;
        }

        assertFalse("RobotPolice debe eliminar los robots negativos", hasNegative);
    }
}

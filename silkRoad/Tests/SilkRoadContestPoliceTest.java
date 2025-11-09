package Tests;

import Simulation.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Prueba de aceptación general del Ciclo 4.
 */
public class SilkRoadContestPoliceTest {

    @Test
    public void contestShouldSimulateWithPoliceWithoutCrashing() {
        int[][] days = {
            {1, 0},         // robot normal
            {2, 5, 30},     // tienda
            {1, 10, 3},     // robot tipo tender
            {1, 15, 2},     // robot tipo neverback
            {1, 20, 99}     // código inventado (ignorado)
        };

        SilkRoadContest contest = new SilkRoadContest(days);
        try {
            contest.simulate(days, false);
        } catch (Exception ex) {
            fail("La simulación con police no debe lanzar excepciones: " + ex.getMessage());
        }
    }

    @Test
    public void policeIntegrationShouldKeepAllVisible() {
        SilkRoad road = new SilkRoad(20);
        road.placeStore(5, 40, "fighter");
        try
        {
            road.placeRobot(3, "tender");
        }
        catch (SilkRoadException sre)
        {
            sre.printStackTrace();
        }
        try
        {
            road.placeRobot(7, "neverback");
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

        RobotPolice police = new RobotPolice(0, "black");
        police.makeVisible();

        road.makeVisible();
        road.makeInvisible();

        assertTrue("Debe existir al menos un robot en la simulación", road.robots().length > 0);
    }
}

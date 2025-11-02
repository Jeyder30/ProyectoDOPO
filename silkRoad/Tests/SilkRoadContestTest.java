package Tests; 

import Simulation.*;
import Shapes.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas de unidad para SilkRoadContest (versión compatible con tu implementación).
 */
public class SilkRoadContestTest {

    @Test
    public void accordingSSShouldSolveSimpleCaseCorrectly() {
        SilkRoadContest contest = new SilkRoadContest();
        int[][] days = {
            {1, 2},       // Día 1: robot en posición 2
            {2, 5, 20}    // Día 2: tienda en 5 con 20 tenges
        };
        int[] result = contest.solve(days);

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals(0, result[0]);
        assertEquals(17, result[1]); // 20 - 3 distancia
    }

    @Test
    public void accordingSSShouldHandleMultipleRobotsAndStores() {
        SilkRoadContest contest = new SilkRoadContest();
        int[][] days = {
            {1, 1},
            {1, 10},
            {2, 2, 15},
            {2, 9, 25}
        };
        int[] result = contest.solve(days);

        assertNotNull(result);
        assertEquals(4, result.length);
        assertTrue(result[2] > 0);
        assertTrue(result[3] >= result[2]);
    }

    @Test
    public void accordingSSShouldSimulateWithoutErrors() {
        int[][] days = {
            {1, 0},
            {2, 5, 10},
            {1, 3},
            {2, 8, 15}
        };

        SilkRoadContest contest = new SilkRoadContest(days);
        try {
            contest.simulate(days, false);
        } catch (Exception ex) {
            fail("simulate() lanzó una excepción: " + ex.getMessage());
        }
        assertNotNull(contest);
    }

    @Test
    public void accordingSSShouldReturnZeroWhenNoEvents() {
        SilkRoadContest contest = new SilkRoadContest();
        int[][] days = new int[0][];
        int[] result = contest.solve(days);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void accordingSSShouldIgnoreInvalidEvents() {
        SilkRoadContest contest = new SilkRoadContest(); 
        int[][] days = {
            {1, -5},
            {2, -3, 10},
            {2, 9999999, 20}
        };
        int[] result = contest.solve(days);
        assertNotNull(result);
        for (int val : result) {
            assertTrue("Todas las ganancias deben ser >= 0", val >= 0);
        }
    }

}

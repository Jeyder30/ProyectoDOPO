import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas de unidad para SilkRoadContest (versión que usa tu implementación).
 */
public class SilkRoadContestTest {

    /**
     * Requisito 14: caso simple — un robot y una tienda.
     */
    @Test
    public void accordingSSShouldSolveSimpleCaseCorrectly() {
        SilkRoadContest contest = new SilkRoadContest();
        int[][] days = {
            {1, 2},       // Día 1: robot en posición 2
            {2, 5, 20}    // Día 2: tienda en 5 con 20 tenges
        };
        int[] result = contest.solve(days);

        assertNotNull("El resultado no debe ser null", result);
        assertEquals("Debe haber 2 resultados (dos días)", 2, result.length);
        assertEquals("Día 1 sin tiendas → ganancia 0", 0, result[0]);

        // Según la lógica del DP implementado, ganancia esperada = 20 - distancia(3) = 17
        assertEquals("Día 2 ganancia esperada (20 - 3)", 17, result[1]);
    }

    /**
     * Requisito 14: manejar múltiples robots y tiendas.
     */
    @Test
    public void accordingSSShouldHandleMultipleRobotsAndStores() {
        SilkRoadContest contest = new SilkRoadContest();
        int[][] days = {
            {1, 1},      // robot at 1
            {1, 10},     // robot at 10
            {2, 2, 15},  // store at 2 with 15
            {2, 9, 25}   // store at 9 with 25
        };
        int[] result = contest.solve(days);

        assertNotNull("Resultado no debe ser null", result);
        assertEquals("Deben existir 4 entradas (4 días)", 4, result.length);

        // día 3 (index 2) debería tener ganancia positiva
        assertTrue("Día 3 debe tener ganancia positiva", result[2] > 0);

        // día 4 (index 3) con más opciones no debería ser menor que el anterior
        assertTrue("Día 4 debe ser >= día 3", result[3] >= result[2]);
    }

    /**
     * Requisito 15: simulate() se debe ejecutar sin lanzar excepciones.
     */
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

        assertNotNull("Objeto SilkRoadContest debe existir tras la simulación", contest);
    }

    /**
     * Requisito 14: entrada vacía debe retornar arreglo vacío.
     */
    @Test
    public void accordingSSShouldReturnZeroWhenNoEvents() {
        SilkRoadContest contest = new SilkRoadContest();
        int[][] days = new int[0][];
        int[] result = contest.solve(days);
        assertNotNull("Resultado no debe ser null", result);
        assertEquals("No debe haber resultados para entrada vacía", 0, result.length);
    }

    /**
     * Requisito 14/15: robustez ante eventos inválidos (posiciones negativas, etc.).
     */
    @Test
    public void accordingSSShouldIgnoreInvalidEvents() {
        SilkRoadContest contest = new SilkRoadContest();
        int[][] days = {
            {1, -5},           // robot con posición inválida
            {2, -3, 10},       // tienda posición inválida
            {2, 9999999, 20}   // tienda con posición fuera de rango razonable
        };
        int[] result = contest.solve(days);
        assertNotNull("Resultado no debe ser null", result);

        for (int val : result) {
            assertTrue("Todas las ganancias deben ser >= 0", val >= 0);
        }
    }
}

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

/**
 * SilkRoadC2Test
 *
 * Pruebas de unidad en modo invisible para la clase SilkRoad.
 */
public class SilkRoadC2Test {

    private SilkRoad road;

    @Before
    public void setUp() {
        road = new SilkRoad(50);
        road.makeInvisible();
    }

    // REQ 10: CREACIÓN DE RUTA
    @Test
    public void accordingSSShouldCreateSilkRoadWithInputDays() {
        int[][] days = {
            {1, 5}, {2, 10, 30}, {2, 15, 40}
        };
        SilkRoad route = new SilkRoad(days);
        assertNotNull("Debe crear correctamente la ruta de seda", route);
        assertTrue("getCellSize debe devolver tamaño positivo", route.getCellSize() > 0);
    }

    // ✅ REQ 11: GANANCIA POSITIVA USANDO profitPerMove()
    @Test
    public void accordingSSShouldAllowRobotToGainProfit() {
        road.placeRobot(5);
        road.placeStore(10, 30);
        road.moveRobot(5, 5);

        int[][] profits = road.profitPerMove();
        assertEquals("Debe haber un robot", 1, profits.length);
        assertTrue("El robot debe haber ganado algún tenges", profits[0][1] > 0);
    }

    @Test
    public void accordingSSShouldNotGainProfitWhenDistanceTooHigh() {
        road.placeRobot(0);
        road.placeStore(20, 10);
        road.moveRobot(0, 20);

        int[][] profits = road.profitPerMove();
        assertEquals("Debe haber un robot en el registro", 1, profits.length);
        assertTrue("No debe haber ganancia si la distancia supera el valor de la tienda", profits[0][1] <= 0);
    }

    // REQ 12: TIENDAS DESOCUPADAS
    @Test
    public void accordingSSShouldTrackStoreEmptyingCount() {
        road.placeRobot(0);
        road.placeStore(5, 20);
        road.moveRobot(0, 5);

        int[][] empties = road.emptiedStores();
        assertEquals("Debe existir una tienda registrada", 1, empties.length);
        assertTrue("Debe haberse vaciado una vez", empties[0][1] == 1);
    }

    @Test
    public void accordingSSShouldNotCountUnvisitedStoresAsEmptied() {
        road.placeStore(10, 50);
        int[][] empties = road.emptiedStores();
        assertEquals("Debe existir una tienda registrada", 1, empties.length);
        assertEquals("La tienda no visitada debe tener conteo 0", 0, empties[0][1]);
    }

    // REQ 13: GANANCIA POR MOVIMIENTO
    @Test
    public void accordingSSShouldRecordProfitPerMove() {
        road.placeRobot(0);
        road.placeStore(5, 20);
        road.moveRobot(0, 5);

        int[][] profits = road.profitPerMove();
        assertEquals("Debe haber un robot en el registro", 1, profits.length);
        assertTrue("Debe haberse registrado al menos un movimiento con ganancia", profits[0].length > 1);
    }

    @Test
    public void accordingSSShouldReturnEmptyProfitIfNoMoves() {
        road.placeRobot(0);
        int[][] profits = road.profitPerMove();
        assertEquals("Debe existir registro para el robot sin movimientos", 1, profits.length);
        assertEquals("Sin movimientos, solo debe contener posición y ganancia 0", 2, profits[0].length);
        assertEquals("Ganancia inicial debe ser 0", 0, profits[0][1]);
    }

    // REQ VISUAL: TIENDA VACÍA
    @Test
    public void accordingSSShouldMarkStoreAsEmptyAfterVisit() {
        road.placeRobot(0);
        road.placeStore(5, 10);
        road.moveRobot(0, 5);

        int[][] stores = road.stores();
        assertEquals("Debe haber una tienda", 1, stores.length);
        assertEquals("Tienda vacía debe tener tenges = 0", 0, stores[0][1]);
    }

    // ✅ REQ VISUAL: MEJOR ROBOT (usa profitPerMove en lugar de robots)
    @Test
    public void accordingSSShouldIdentifyTopEarningRobot() {
        road.placeRobot(0);
        road.placeRobot(10);
        road.placeStore(5, 50);
        road.moveRobot(0, 5);

        int[][] profits = road.profitPerMove();
        int max = Integer.MIN_VALUE;
        for (int[] p : profits) max = Math.max(max, p[1]);

        assertTrue("Debe haber al menos un robot con la ganancia máxima detectada", max > 0);
    }
}

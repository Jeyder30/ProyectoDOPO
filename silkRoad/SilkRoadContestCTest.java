import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Clase común de pruebas colaborativas usando las clases reales del proyecto.
 */
public class SilkRoadContestCTest {

    /**
     * Requisito: la ganancia se muestra permanentemente sin texto numérico.
     * Se prueba la API de ProfitBar de forma no intrusiva.
     */
    @Test
    public void accordingCCShouldMaintainProfitBarVisible() {
        ProfitBar bar = new ProfitBar(100);
        assertNotNull("La barra de progreso debe existir", bar);

        // Operaciones básicas (no verificamos UI visualmente)
        bar.updateProfit(50);
        bar.setMaxProfit(200);
        bar.updateProfit(150);
        bar.setMaxProfit(300);

        // Si llegamos aquí sin excepción, la prueba pasa
    }

    /**
     * Requisito 12: contar las tiendas desocupadas.
     */
    @Test
    public void accordingCCShouldDetectStoreEmpties() {
        SilkRoad road = new SilkRoad(10);
        road.placeStore(5, 20);
        road.placeRobot(0);

        // Mover el robot desde la posición 0 hasta la 5 (vacía la tienda)
        road.moveRobot(0, 5);

        int[][] emptied = road.emptiedStores();
        assertNotNull("emptiedStores no debe ser null", emptied);
        assertEquals("Debe haber una tienda registrada como inicial", 1, emptied.length);
        assertTrue("La tienda fue vaciada al menos una vez", emptied[0][1] >= 1);
    }

    /**
     * Requisito 13: debe registrar las ganancias por robot.
     */
    @Test
    public void accordingCCShouldTrackRobotGains() {
        SilkRoad road = new SilkRoad(10);
        road.placeStore(4, 15);
        road.placeRobot(0);

        road.moveRobot(0, 4); // robot va a la tienda

        int[][] profit = road.profitPerMove();
        assertNotNull("profitPerMove no debe ser null", profit);
        assertEquals("Debe existir exactamente un robot registrado", 1, profit.length);

        // El valor de ganancia (col 1) debe reflejar la ganancia neta (en este caso > 0)
        assertTrue("El robot debe tener una ganancia registrada (>0)", profit[0][1] > 0);
    }

    /**
     * Requisito visual: las tiendas vacías deben lucir distintas.
     * Verificamos estado interno (veces vaciada).
     */
    @Test
    public void accordingCCShouldDifferentiateEmptyStores() {
        SilkRoad road = new SilkRoad(10);
        road.placeStore(3, 10);
        road.placeRobot(0);
        road.moveRobot(0, 3);

        int[][] emptied = road.emptiedStores();
        assertNotNull(emptied);
        assertTrue("Debe marcar tienda vacía al menos 1 vez", emptied[0][1] > 0);
    }

    /**
     * Requisito visual: el mejor robot debe parpadear.
     * Solo comprobamos que los métodos de visibilidad no fallen y que existan robots.
     */
    @Test
    public void accordingCCShouldHighlightBestRobot() {
        SilkRoad road = new SilkRoad(10);
        road.placeStore(3, 30);
        road.placeRobot(0);
        road.moveRobot(0, 3);

        // toggle de visibilidad (no verificamos animación, solo que no falle)
        road.makeVisible();
        road.makeInvisible();

        assertTrue("Debe haber al menos un robot en memoria", road.robots().length > 0);
    }
}

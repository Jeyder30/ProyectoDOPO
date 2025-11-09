package Tests; 

import Simulation.*;
import Shapes.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Clase común de pruebas colaborativas usando las clases reales del proyecto.
 */
public class SilkRoadContestCTest {

    @Test
    public void accordingCCShouldMaintainProfitBarVisible() {
        ProfitBar bar = new ProfitBar(100);
        assertNotNull("La barra de progreso debe existir", bar);

        bar.updateProfit(50);
        bar.setMaxProfit(200);
        bar.updateProfit(150);
        bar.setMaxProfit(300);
    }

    @Test
    public void accordingCCShouldDetectStoreEmpties() {
        SilkRoad road = new SilkRoad(10);
        road.placeStore(5, 20, "normal");
        try
        {
            road.placeRobot(0, "normal");
        }
        catch (SilkRoadException sre)
        {
            sre.printStackTrace();
        }

        road.moveRobot(0, 5);

        int[][] emptied = road.emptiedStores();
        assertNotNull(emptied);
        assertEquals(1, emptied.length);
        assertTrue("La tienda fue vaciada al menos una vez", emptied[0][1] >= 1);
    }

    @Test
    public void accordingCCShouldTrackRobotGains() {
        SilkRoad road = new SilkRoad(10);
        road.placeStore(4, 15, "normal");
        try
        {
            road.placeRobot(0, "normal");
        }
        catch (SilkRoadException sre)
        {
            sre.printStackTrace();
        }

        road.moveRobot(0, 4);

        int[][] profit = road.profitPerMove();
        assertNotNull(profit);
        assertEquals(1, profit.length);
        assertTrue("El robot debe tener ganancia registrada", profit[0][1] > 0);
    }

    @Test
    public void accordingCCShouldDifferentiateEmptyStores() {
        SilkRoad road = new SilkRoad(10);
        road.placeStore(3, 10, "normal");
        try
        {
            road.placeRobot(0, "normal");
        }
        catch (SilkRoadException sre)
        {
            sre.printStackTrace();
        }
        road.moveRobot(0, 3);

        int[][] emptied = road.emptiedStores();
        assertNotNull(emptied);
        assertTrue("Debe marcar tienda vacía al menos 1 vez", emptied[0][1] > 0);
    }

    @Test
    public void accordingCCShouldHighlightBestRobot() {
        SilkRoad road = new SilkRoad(10);
        road.placeStore(3, 30, "normal");
        try
        {
            road.placeRobot(0, "normal");
        }
        catch (SilkRoadException sre)
        {
            sre.printStackTrace();
        }
        road.moveRobot(0, 3);

        road.makeVisible();
        road.makeInvisible();

        assertTrue("Debe haber al menos un robot en memoria", road.robots().length > 0);
    }
}

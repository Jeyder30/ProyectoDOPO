package Tests;

import Simulation.*;
import Shapes.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Pruebas unitarias para RobotPolice (Ciclo 4)
 */
public class RobotPoliceTest {

    @Test
    public void policeShouldStartWith1000Tenges() {
        RobotPolice police = new RobotPolice(0, "black");
        assertEquals("RobotPolice debe iniciar con 1000 tenges", 1000, police.getTenges());
    }

    @Test
    public void policeShouldBeVisibleWhenPlacedInCell() {
        SilkRoad road = new SilkRoad(20);
        RobotPolice police = new RobotPolice(5, "gray");
        Cell[] cells = road.getCells();
        police.placeInCell(cells[5]);
        police.makeVisible();
        assertNotNull("Debe poder colocarse y hacerse visible sin errores", police);
    }
}

package Simulation;

import Shapes.*;
import java.util.*;

/**
 * RobotPolice — recorre el tablero en busca de robots con tenges negativos.
 * Si encuentra alguno, lo elimina del tablero.
 * Tiene 1000 tenges iniciales para moverse libremente.
 */
public class RobotPolice extends Robot {

    private int patrolRange = 10;  // rango de patrullaje por iteración

    public RobotPolice(int location, String color) {
        super(location, color);
        addTenges(1000); // inicia con 1000 tenges
    }

    /**
     * Patrulla el tablero buscando robots con tenges negativos.
     * Si los encuentra, los elimina visualmente.
     */
    public void patrol(SilkRoad road) {
        if (road == null) return;

        int[][] robotsSnapshot = road.robots();

        for (int[] r : robotsSnapshot) {
            int pos = r[0];
            int tenges = r[1];

            if (tenges < 0) {
                // robot con tenges negativos encontrado
                road.removeRobot(pos);

                // efecto visual de "captura"
                Circle flash = new Circle();
                flash.changeSize(25);
                flash.changeColor("red");
                flash.setAbsolutePosition(pos * road.getCellSize(), 50);
                flash.makeVisible();
                flash.discoTime(50);
                flash.makeInvisible();
            }
        }
    }

    /**
     * Mueve el robot police por el tablero (puede cruzar libremente).
     */
    public void patrolMove(SilkRoad road) {
        if (road == null) return;

        int[][] robotsSnapshot = road.robots();
        if (robotsSnapshot.length == 0) return;

        int move = new Random().nextInt(patrolRange * 2) - patrolRange;
        int newLoc = getCurrentLocation() + move;
        if (newLoc < 0) newLoc = 0;
        if (newLoc >= road.robots().length) newLoc = road.robots().length - 1;

        setCurrentLocation(newLoc);

        // actualizar posición visual
        Cell[] cells = road.getCells();
        if (newLoc < cells.length && cells[newLoc] != null) {
            placeInCell(cells[newLoc]);
            makeVisible();
        }
    }

    @Override
    public String toString() {
        return "RobotPolice{loc=" + getCurrentLocation() + ", tenges=" + getTenges() + "}";
    }
}

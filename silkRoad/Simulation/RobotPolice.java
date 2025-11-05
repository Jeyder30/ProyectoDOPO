package Simulation;

import Shapes.*;
import java.util.*;

/**
 * RobotPolice — patrulla la ruta de seda buscando robots con tenges negativos.
 * No se mueve automáticamente por hilos; puede moverse mediante moveRobot()
 * o mediante policePatrol() que llama a patrolMove/patrol manualmente.
 */
public class RobotPolice extends Robot {

    private int robotsRemoved = 0;
    private int patrolRange = 10;

    public RobotPolice(int location, String color) {
        super(location, color);
        addTenges(1000);
    }

    /**
     * Patrulla (elimina robots en la misma posición si tienen tenges negativos).
     * Usa la API pública de SilkRoad para obtener snapshot y limpiar celdas.
     */
    public void patrol(SilkRoad road) {
        Cell[] cells = road.getCells();
        int currentPos = getCurrentLocation();
    
        // Buscar si hay un robot con tenges negativos en esta posición
        Map<Integer, Robot> robots = road.getRobotsMap();  // vamos a agregar este getter en SilkRoad
    
        for (Robot r : new ArrayList<>(robots.values())) {
            if (r != null && r != this && r.getCurrentLocation() == currentPos && r.getTenges() < 0) {
                // Eliminar visualmente y del mapa
                road.removeRobotAtPosition(currentPos);
                robotsRemoved++; // contador local del police
                break; // solo elimina uno por turno
            }
        }
    }

    /** Incrementa el contador de eliminaciones (usado cuando se elimina por moveRobot). */
    public void addElimination() {
        robotsRemoved++;
    }

    public int getRobotsRemoved() {
        return robotsRemoved;
    }

    @Override
    public void placeInCell(Cell cell) {
        super.placeInCell(cell);
        if (getEyeLeftShape() != null) getEyeLeftShape().changeColor("red");
        if (getEyeRightShape() != null) getEyeRightShape().changeColor("red");
    }

    @Override
    public String toString() {
        return "RobotPolice{loc=" + getCurrentLocation() +
               ", tenges=" + getTenges() +
               ", eliminados=" + robotsRemoved + "}";
    }
}

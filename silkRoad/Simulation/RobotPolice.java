package Simulation;

import Shapes.*;
import java.util.*;

/**
 * RobotPolice — patrulla la ruta de seda buscando robots con tenges negativos.
 * Si encuentra alguno en su misma posición, lo elimina.
 * 
 * Tiene 1000 tenges iniciales y puede moverse libremente por el mapa.
 */
public class RobotPolice extends Robot {

    private int patrolRange = 10;     // rango aleatorio de movimiento
    private int robotsRemoved = 0;    // contador de robots eliminados

    public RobotPolice(int location, String color) {
        super(location, color);
        addTenges(1000); // inicia con 1000 tenges para moverse libremente
    }

    /**
     * Patrulla el tablero buscando robots con tenges negativos en su misma celda.
     * Si los encuentra, los elimina del tablero y actualiza su contador.
     */
    public void patrol(SilkRoad road) {
        if (road == null) return;

        int myLocation = getCurrentLocation();
        int[][] robotsSnapshot = road.robots(); // [pos, tenges]

        for (int[] r : robotsSnapshot) {
            int pos = r[0];
            int tenges = r[1];

            // Si hay un robot en la misma posición y tiene tenges negativos
            if (pos == myLocation && tenges < 0) {
                road.removeRobot(pos);  // lo elimina visualmente
                robotsRemoved++;        // aumenta el contador

                Circle flash = new Circle();
                flash.changeSize(25);
                flash.changeColor("red");
                flash.setAbsolutePosition(myLocation * road.getCellSize(), 50);
                flash.makeVisible();
                flash.discoTime(40);
                flash.makeInvisible();
            }
        }
    }

    /**
     * Mueve el robot policial a una nueva posición aleatoria dentro del rango definido.
     * El movimiento es libre: no se penaliza ni restringe.
     */
    public void patrolMove(SilkRoad road) {
        if (road == null) return;

        Cell[] cells = road.getCells();
        if (cells == null || cells.length == 0) return;

        Random rand = new Random();
        int move = rand.nextInt(patrolRange * 2 + 1) - patrolRange; 
        int newLoc = getCurrentLocation() + move;

        if (newLoc < 0) newLoc = 0;
        if (newLoc >= cells.length) newLoc = cells.length - 1;

        setCurrentLocation(newLoc);

        // Actualizar posición visual
        Cell c = cells[newLoc];
        if (c != null) {
            placeInCell(c);
            makeVisible();
        }
    }

    /**
     * Devuelve cuántos robots ha eliminado el RobotPolice.
     */
    public int getRobotsRemoved() {
        return robotsRemoved;
    }

    @Override
    public String toString() {
        return "RobotPolice{loc=" + getCurrentLocation() +
               ", tenges=" + getTenges() +
               ", eliminados=" + robotsRemoved + "}";
    }
    
    @Override
    public void placeInCell(Cell cell) {
        super.placeInCell(cell);
    
        if (getEyeLeftShape() != null) getEyeLeftShape().changeColor("red");
        if (getEyeRightShape() != null) getEyeRightShape().changeColor("red");
    }

}

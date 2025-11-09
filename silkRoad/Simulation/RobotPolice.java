package Simulation;

import Shapes.*;
import java.util.*;

/**
 * Representa al robot de policía encargado de patrullar la Ruta de la Seda.
 * Su función principal es eliminar robots que tengan una cantidad negativa de tenges.
 * El RobotPolice no se mueve automáticamente mediante hilos, sino que puede moverse
 * de forma controlada mediante los métodos moveRobot() o policePatrol() 
 * (que a su vez invocan el método patrol).
 */
public class RobotPolice extends Robot {

    private int robotsRemoved = 0;
    private int patrolRange = 10;
    
    /**
     * Crea un RobotPolice en una posición inicial dentro del recorrido
     * y le asigna un color visual. 
     * Este robot comienza con 1000 tenges para permitirle moverse libremente.
     */
    public RobotPolice(int location, String color) {
        super(location, color);
        addTenges(1000);
    }

    /**
     * Patrulla la celda actual del robot, buscando otros robots con tenges negativos.
     * Si encuentra un robot con saldo negativo en la misma posición, lo elimina
     * tanto visualmente como de la estructura de datos del recorrido (SilkRoad).
     * Este método no mueve al policía; solo actúa sobre su posición actual.
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

    /**
     * Incrementa manualmente el contador de eliminaciones.
     * Este método se puede usar si un robot es eliminado como consecuencia de
     * otro tipo de acción 
     */
    public void addElimination() {
        robotsRemoved++;
    }
    
    /**
     * Devuelve la cantidad total de robots eliminados por este RobotPolice.
     */
    public int getRobotsRemoved() {
        return robotsRemoved;
    }
    
    /**
     * Coloca visualmente al robot en una celda.
     * Este método sobrescribe la versión de la clase padre (Robot) para 
     * personalizar la apariencia del RobotPolice, cambiando el color
     * de sus ojos a rojo para distinguirlo de los demás.
     */
    @Override
    public void placeInCell(Cell cell) {
        super.placeInCell(cell);
        if (getEyeLeftShape() != null) getEyeLeftShape().changeColor("red");
        if (getEyeRightShape() != null) getEyeRightShape().changeColor("red");
    }
    
    /**
     * Retorna una descripción textual del RobotPolice,
     * incluyendo su posición actual, cantidad de tenges y robots eliminados.
     */
    @Override
    public String toString() {
        return "RobotPolice{loc=" + getCurrentLocation() +
               ", tenges=" + getTenges() +
               ", eliminados=" + robotsRemoved + "}";
    }
}

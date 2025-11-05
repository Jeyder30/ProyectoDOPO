package Simulation;

import Shapes.*;  
import java.util.*;

/**
 * Clase SilkRoad requisitos DOPO (Ciclo 4 refactorizado).
 *
 * Esta clase se encarga EXCLUSIVAMENTE de la simulación visual:
 * - Maneja la ruta, los robots y las tiendas.
 * - Ejecuta los movimientos que YA fueron calculados por SilkRoadContest.
 * - No resuelve la maratón ni realiza optimización dinámica (DP).
 */
public class SilkRoad {

    private final int length;
    private final Map<Integer, Store> stores = new HashMap<>();
    private final Map<Integer, Robot> robots = new HashMap<>();

    // snapshots inmutables para reboot
    private final Map<Integer, Integer> initialStoreTenges = new HashMap<>();
    private final Map<Integer, String> initialStoreColors = new HashMap<>();
    private final Map<Integer, String> initialRobotColors = new HashMap<>();

    private boolean visible = true;
    private boolean lastOk = true;
    private int profitToday = 0;
    private int arrivalCounter = 0;

    private ColorManager robotColors = new ColorManager();
    private ColorManager storeColors = new ColorManager();
    private static final int defaultLength = 1000;
    private static final int MATRIX_SIZE = 15;

    private ProfitBar profitBar;
    private Cell[] cells;
    private int cellSize = 30;
    
    private RobotPolice police;  // Robot policía único
    private boolean policeActive = false;


    /**
     * Crea una nueva ruta de seda con una longitud fija.
     */
    public SilkRoad(int length) {
        this.length = length;
        this.cells = new Cell[length];
        profitBar = new ProfitBar(1000);

        int matrixSize = MATRIX_SIZE;
        while ((long) matrixSize * (long) matrixSize < (long) length) matrixSize++;

        List<Cell> recorrido = RouteBuilder.buildSpiralInGrid(matrixSize, cellSize, 0, 0);
        for (int i = 0; i < length; i++) {
            Cell c = recorrido.get(i);
            cells[i] = c;
            if (visible && c != null) c.makeVisible();
        }
    }

    /** 
     * Crea la ruta de seda a partir de la entrada del problema. 
     */
    public SilkRoad(int[][] days) {
        this(computeLengthFromDays(days));
    }

    private static int computeLengthFromDays(int[][] days) {
        if (days == null) return defaultLength;
        long maxLoc = 0;
        for (int[] ev : days) {
            if (ev == null || ev.length < 2) continue;
            maxLoc = Math.max(maxLoc, (long) ev[1]);
            for (int k = 2; k < ev.length; k++) maxLoc = Math.max(maxLoc, (long) ev[k]);
        }
        long needed = maxLoc + 1;
        if (needed < defaultLength) needed = defaultLength;
        if (needed > Integer.MAX_VALUE - 5) return Integer.MAX_VALUE - 5;
        return (int) needed;
    }

    /** Coloca una tienda en la ubicación indicada. */
    public void placeStore(int position, int tenges, String type) {
        // Si la posición está fuera del rango, se ignora
        if (position < 0 || position >= cells.length) return;
    
        // Si la tienda es autónoma, elige una posición aleatoria válida
        if (type.equalsIgnoreCase("autonomous")) {
            Random rand = new Random();
            position = rand.nextInt(cells.length); // cambia la posición de forma aleatoria
        }
    
        // Si ya hay algo en esa celda, se limpia
        if (cells[position] != null) {
            cells[position].clear();
            cells[position].makeVisible();
        }
    
        Store store;
        String color;
    
        switch (type.toLowerCase()) {
            case "fighter":
                color = "red";
                store = new StoreFighter(position, tenges, color);
                break;
    
            case "autonomous":
                color = "yellow";
                store = new StoreAutonomous(position, tenges, color);
                break;
    
            default:
                color = "magenta";
                store = new Store(position, tenges, color);
                break;
        }
    
        store.placeInCell(cells[position]);
        store.makeVisible();
        stores.put(position, store);
    }

    /** Coloca un robot en la ubicación indicada. */
    public void placeRobot(int position, String type) {
        if (position < 0 || position >= cells.length) return;
    
        Robot robot;
        String color;
    
        switch (type.toLowerCase()) {
            case "neverback":
                color = "orange";
                robot = new RobotNeverBack(position, color);
                break;
    
            case "tender":
                color = "green";
                robot = new RobotTender(position, color);
                break;
    
            case "police":
                color = "cyan";
                robot = new RobotPolice(position, color);
                // registrar la referencia al policía para usarla sin instanceof
                police = (RobotPolice) robot;
                policeActive = true;
                break;
    
            default:
                color = "blue";
                robot = new Robot(position, color);
                break;
        }
    
        robot.placeInCell(cells[position]);
        robot.makeVisible();
        robots.put(position, robot);
    }

    /** Elimina una tienda existente. */
    public void removeStore(int location) {
        Store s = stores.remove(location);
        if (s != null) {
            s.makeInvisible();
            if (cells[location] != null) cells[location].clear();
        }
        actualizarMaxProfit();
    }

    /** Elimina un robot existente. */
    public void removeRobot(int location) {
        Robot r = robots.remove(location);
        if (r != null) {
            // Si se está eliminando al policía, limpiar su referencia
            if (r == police) {
                police = null;
                policeActive = false;
            }
            r.makeInvisible();
            if (cells[location] != null) {
                cells[location].clear();
                cells[location].makeVisible();
            }
        }
    }

    /** Restablece el contenido de las tiendas. */
    public void resupplyStores() {
        for (Store s : stores.values()) s.resupply();
        actualizarMaxProfit();
    }

    /** Devuelve los robots a sus posiciones iniciales. */
    public void returnRobots() {
        for (Robot r : robots.values()) r.resetPosition();
    }

    /** Reinicia toda la simulación a su estado inicial. */
    public void reboot() {
        stores.clear();
        // limpiar visualmente y estructura de robots
        for (Cell c : cells) if (c != null) c.clear();
        robots.clear();

        profitToday = 0;
        if (profitBar != null) profitBar.updateProfit(0);

        // Restaurar tiendas
        for (Map.Entry<Integer, Integer> e : initialStoreTenges.entrySet()) {
            int loc = e.getKey();
            int tenges = e.getValue();
            String color = initialStoreColors.getOrDefault(loc, storeColors.nextColor());
            Store s = new Store(loc, tenges, color);
            stores.put(loc, s);
            if (cells[loc] != null) {
                s.placeInCell(cells[loc]);
                s.makeVisible();
            }
        }

        // Restaurar robots
        police = null;
        policeActive = false;
        for (Map.Entry<Integer, String> e : initialRobotColors.entrySet()) {
            int loc = e.getKey();
            String color = e.getValue();
            Robot r = new Robot(loc, color);
            robots.put(loc, r);
            if (cells[loc] != null) {
                r.placeInCell(cells[loc]);
                r.makeVisible();
            }
        }

        arrivalCounter = 0;
        actualizarMaxProfit();
    }

    /** Mueve un robot una cantidad de metros, actualizando su ganancia. */
    public void moveRobot(int location, int meters) {
        Robot robot = robots.get(location);

        if (robot == null) {
            for (Map.Entry<Integer, Robot> e : robots.entrySet()) {
                Robot r = e.getValue();
                if (r != null && r.getCurrentLocation() == location) {
                    robot = r;
                    break;
                }
            }
        }
        if (robot == null) return;

        // Si el robot es RobotNeverBack y el movimiento es hacia atrás, no hacer nada.
        String classNameCheck = robot.getClass().getSimpleName();
        if (classNameCheck.equals("RobotNeverBack") && meters < 0) {
            // No se permite retroceder para este tipo de robot.
            return;
        }

        int oldPos = robot.getCurrentLocation();
        int newPos = oldPos + meters;
        if (newPos < 0 || newPos >= length) return;

        // Si quien se mueve es el policía, verificar eliminación en destino antes de mover
        if (robot == police) {
            Robot target = robots.get(newPos);
            if (target == null) {
                // también buscar por currentLocation si no está bajo la key newPos
                for (Map.Entry<Integer, Robot> e : robots.entrySet()) {
                    Robot r = e.getValue();
                    if (r != null && r.getCurrentLocation() == newPos) {
                        target = r;
                        break;
                    }
                }
            }
            if (target != null && target.getTenges() < 0) {
                // eliminar objetivo y contar eliminación
                removeRobotAtPosition(newPos);
                if (police != null) police.addElimination();
            }
        }

        // Ocultar origen y limpiar visualmente
        robot.makeInvisible();
        if (cells[oldPos] != null) cells[oldPos].clear();

        // Actualizar ubicación interna
        robot.move(meters);
        robot.setCurrentLocation(newPos);

        // Si en destino hay aún un robot (no eliminado), eliminarlo antes de colocar al que se mueve
        Robot destRobot = robots.get(newPos);
        if (destRobot == null) {
            for (Map.Entry<Integer, Robot> e : robots.entrySet()) {
                Robot r = e.getValue();
                if (r != null && r.getCurrentLocation() == newPos && r != robot) {
                    destRobot = r;
                    break;
                }
            }
        }
        if (destRobot != null && destRobot != robot) {
            removeRobotAtPosition(newPos);
        }

        // Colocar visualmente en nueva celda
        if (cells[newPos] != null) {
            robot.placeInCell(cells[newPos]);
            robot.makeVisible();
        }

        // Asegurar que el mapa `robots` tenga la entrada correcta (key = newPos)
        Iterator<Map.Entry<Integer, Robot>> it = robots.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Robot> e = it.next();
            if (e.getValue() == robot) {
                it.remove();
                break;
            }
        }
        robots.put(newPos, robot);

        // Interacción con tienda y ganancia (igual que antes, respetando RobotTender)
        int distancia = Math.abs(newPos - oldPos);
        int ganancia;
        Store store = stores.get(newPos);
        if (store != null && !store.isEmptiedToday()) {
            String className = robot.getClass().getSimpleName();
            if (className.equals("RobotTender")) {
                RobotTender tender = (RobotTender) robot;
                ganancia = tender.interactWithStore(store, distancia);
            } else {
                ganancia = store.getCurrentTenges() - distancia;
                store.empty();
            }
        } else {
            ganancia = -distancia;
        }

        profitToday += ganancia;
        if (profitBar != null) profitBar.updateProfit(Math.max(0, profitToday));
        
        // Evita doble suma de ganancia 
        if (!robot.getClass().getSimpleName().equals("RobotTender")) {
            robot.addProfit(ganancia, newPos, distancia);
        }

    }

    /** Ejecuta una lista de movimientos. */
    public void moveRobots(List<int[]> movimientos) {
        if (movimientos == null) return;
        for (int[] mov : movimientos) {
            if (mov == null || mov.length < 2) continue;
            moveRobot(mov[0], mov[1]);
        }
        highlightBestRobot();
    }

    public long profit() {
        return profitToday;
    }

    private void actualizarMaxProfit() {
        int max = 0;
        for (Store s : stores.values()) max += s.getCurrentTenges();
        if (profitBar != null) profitBar.setMaxProfit(max);
    }

    public int[][] emptiedStores() {
        List<Integer> locs = new ArrayList<>(initialStoreTenges.keySet());
        Collections.sort(locs);
        int[][] result = new int[locs.size()][2];
        for (int i = 0; i < locs.size(); i++) {
            int loc = locs.get(i);
            Store s = stores.get(loc);
            result[i][0] = loc;
            result[i][1] = (s != null) ? s.getTimesEmptied() : 0;
        }
        return result;
    }

    public int[][] profitPerMove() {
        List<Integer> locs = new ArrayList<>(robots.keySet());
        Collections.sort(locs);
        List<int[]> resultList = new ArrayList<>();
        for (int loc : locs) {
            Robot r = robots.get(loc);
            if (r != null)
                resultList.add(new int[]{r.getCurrentLocation(), r.getProfit()});
        }
        return resultList.toArray(new int[resultList.size()][]);
    }

    public void makeVisible() {
        // Primero las celdas (fondo)
        for (Cell cell : cells) {
            if (cell != null) cell.makeVisible();
        }
    
        // Luego las tiendas
        for (Store store : stores.values()) {
            if (store != null) {
                store.makeVisible();
            }
        }
    
        // Finalmente los robots
        for (Robot robot : robots.values()) {
            if (robot != null) {
                robot.makeVisible();
            }
        }
    }


    public void makeInvisible() {
        // Primero los robots (para que desaparezcan encima)
        for (Robot robot : robots.values()) {
            if (robot != null) {
                robot.makeInvisible();
            }
        }
    
        // Luego las tiendas
        for (Store store : stores.values()) {
            if (store != null) {
                store.makeInvisible();
            }
        }
    
        // Finalmente las celdas
        for (Cell cell : cells) {
            if (cell != null) cell.makeInvisible();
        }
    }


    public int[][] stores() {
        List<Integer> locs = new ArrayList<>(stores.keySet());
        Collections.sort(locs);
        int[][] result = new int[locs.size()][2];
        for (int i = 0; i < locs.size(); i++) {
            int loc = locs.get(i);
            Store s = stores.get(loc);
            result[i][0] = loc;
            result[i][1] = (s != null) ? s.getCurrentTenges() : 0;
        }
        return result;
    }

    public int[][] robots() {
        List<Integer> locs = new ArrayList<>(robots.keySet());
        Collections.sort(locs);
        int[][] result = new int[locs.size()][2];
        for (int i = 0; i < locs.size(); i++) {
            int loc = locs.get(i);
            Robot r = robots.get(loc);
            result[i][0] = loc;
            result[i][1] = (r != null) ? r.getTenges() : 0;
        }
        return result;
    }

    public void highlightBestRobot() {
        if (robots.isEmpty()) return;
    
        Robot best = null;
        int maxProfit = Integer.MIN_VALUE;
    
        for (Robot r : robots.values()) {
            if (r == null) continue;
            // Ignorar al robot police (color cyan)
            if (r.getColor().equalsIgnoreCase("cyan")) continue;
    
            int p = r.getProfit();
            if (p > maxProfit) {
                maxProfit = p;
                best = r;
            }
        }
    
        if (best != null) best.blink();
    }


    public void finish() {
        // Primero eliminar todos los robots visual y lógicamente
        for (Robot r : robots.values()) {
            if (r != null) {
                r.makeInvisible();
            }
        }
        robots.clear();
    
        // Luego eliminar todas las tiendas visual y lógicamente
        for (Store s : stores.values()) {
            if (s != null) {
                s.makeInvisible();
            }
        }
        stores.clear();
    
        // Limpiar visualmente todas las celdas del tablero
        for (Cell c : cells) {
            if (c != null) {
                c.clear();
                c.makeInvisible();
            }
        }
    
        // Reiniciar contadores y ganancias
        profitToday = 0;
        profitBar.updateProfit(0);
        visible = false;
    }


    public boolean ok() {
        return lastOk;
    }

    public int getCellSize() {
        return cellSize;
    }
    
    /**
     * Retorna todas las celdas del tablero (para uso de robots especiales).
     */
    public Cell[] getCells() {
        return cells;
    }
    
    /**
     * Activa un RobotPolice en la ruta.
     * Por defecto inicia en la celda 0 y se hace visible.
     */
    public void activatePolice() {
        if (policeActive) return;
        police = new RobotPolice(0, "cyan");
        police.placeInCell(cells[0]);
        police.makeVisible();
        policeActive = true;
    }
    
    /**
     * Devuelve cuántos robots ha eliminado el robot policial.
     */
    public int getRobotsRemovedByPolice() {
        if (police == null) return 0;
        return police.getRobotsRemoved();
    }
    
    /**
     * Elimina un robot en la posición indicada, si existe.
     * Limpia la celda visualmente y la deja en blanco.
     */
    public void removeRobotAtPosition(int position) {
        if (position < 0 || position >= length) return;
    
        Iterator<Map.Entry<Integer, Robot>> it = robots.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Robot> e = it.next();
            Robot r = e.getValue();
            if (r != null && r.getCurrentLocation() == position) {
                r.makeInvisible();
                it.remove();
                break;
            }
        }
    
        if (cells[position] != null) {
            cells[position].clear();
            cells[position].makeVisible();
        }
    }

    public Map<Integer, Robot> getRobotsMap() {
    return robots;
    }

}


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
    public void placeStore(int location, int tenges, String type) {
        if (location < 0 || location >= length) return;
        if (stores.containsKey(location)) return;
    
        String color = storeColors.nextColor();
        Store s;
    
        switch (type.toLowerCase()) {
            case "autonomous":
                s = new StoreAutonomous(location, tenges, color);
                break;
            case "fighter":
                s = new StoreFighter(location, tenges, color);
                break;
            default:
                s = new Store(location, tenges, color);
                break;
        }
    
        stores.put(location, s);
    
        if (!initialStoreTenges.containsKey(location)) {
            initialStoreTenges.put(location, tenges);
            initialStoreColors.put(location, color);
        }
    
        if (cells[location] != null) {
            s.placeInCell(cells[location]);
            s.makeVisible();
        }
        actualizarMaxProfit();
    }

    /** Coloca un robot en la ubicación indicada. */
    public void placeRobot(int location, String type) {
        if (location < 0 || location >= length) return;
        if (robots.containsKey(location)) return;
    
        String color = robotColors.nextColor();
        Robot r;
    
        switch (type.toLowerCase()) {
            case "neverback":
                r = new RobotNeverBack(location, color);
                break;
            case "tender":
                r = new RobotTender(location, color);
                break;
            default:
                r = new Robot(location, color);
                break;
        }
    
        robots.put(location, r);
    
        if (!initialRobotColors.containsKey(location)) {
            initialRobotColors.put(location, color);
        }
    
        if (cells[location] != null) {
            r.placeInCell(cells[location]);
            r.makeVisible();
        }
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
            r.makeInvisible();
            if (cells[location] != null) {
                cells[location].clear();
                cells[location].placeStore("white");
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
        robots.clear();
        for (Cell c : cells) if (c != null) c.clear();
        profitToday = 0;
        profitBar.updateProfit(0);

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

        int oldPos = robot.getCurrentLocation();
        int newPos = oldPos + meters;
        if (newPos < 0 || newPos >= length) return;

        robot.makeInvisible();
        if (cells[oldPos] != null) cells[oldPos].clear();

        robot.move(meters);
        if (cells[newPos] != null) {
            robot.placeInCell(cells[newPos]);
            robot.makeVisible();
        }

        robots.remove(oldPos);
        robots.put(newPos, robot);

        int distancia = Math.abs(newPos - oldPos);
        int ganancia;

        Store store = stores.get(newPos);
        if (store != null && !store.isEmptiedToday()) {
            ganancia = store.getCurrentTenges() - distancia;
            store.empty();
        } else {
            ganancia = -distancia;
        }

        profitToday += ganancia;
        profitBar.updateProfit(Math.max(0, profitToday));

        robot.addProfit(ganancia, newPos, distancia);
        robot.setOrderOfArrival(arrivalCounter++);
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
        profitBar.setMaxProfit(max);
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
        if (!visible) visible = true;
        for (Cell c : cells) if (c != null) c.makeVisible();
    }

    public void makeInvisible() {
        if (visible) visible = false;
        for (Cell c : cells) if (c != null) c.makeInvisible();
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
            int p = r.getProfit();
            if (p > maxProfit) {
                maxProfit = p;
                best = r;
            }
        }
        if (best != null) best.blink();
    }

    public void finish() {
        for (Cell c : cells) if (c != null) c.makeInvisible();
        stores.clear();
        robots.clear();
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

}

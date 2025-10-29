

package Simulation;

import Shapes.*;
import java.util.*;

/**
 * Clase SilkRoadContest — Controlador principal de la simulación y resolución.
 *
 * Esta clase coordina el flujo de la maratón:
 * - Usa SilkRoad solo para simular visualmente (nunca para calcular el resultado).
 *
 * REQUISITOS:
 * (14) Resolver el problema de la maratón calculando la ganancia óptima diaria.
 * (15) Simular paso a paso los movimientos de los robots sobre la ruta de seda.
 */
public class SilkRoadContest {

    /** Instancia de SilkRoad usada únicamente para simular visualmente. */
    private SilkRoad road;

    /** Crea una simulación con una ruta de longitud por defecto (1000). */
    public SilkRoadContest() {
        this.road = new SilkRoad(1000);
    }

    /** Crea una simulación con una ruta adaptada a los datos de entrada. */
    public SilkRoadContest(int[][] days) {
        this.road = new SilkRoad(days);
    }

    /**
     * Calcula la ganancia óptima acumulada día por día sin usar la simulación.
     */
    public int[] solve(int[][] days) {
        if (days == null) return new int[0];

        int n = days.length;
        int[] result = new int[n];

        int[] robots = new int[1000];
        int robotCount = 0;

        TreeMap<Integer, Integer> stores = new TreeMap<Integer, Integer>();

        for (int day = 0; day < n; day++) {
            int[] event = days[day];
            if (event == null || event.length < 2) {
                result[day] = 0;
                continue;
            }

            if (event[0] == 1) {
                if (event.length >= 2 && event[1] >= 0) {
                    robots[robotCount] = event[1];
                    robotCount++;
                }
            } else if (event[0] == 2 && event.length >= 3) {
                if (event[1] >= 0 && event[2] >= 0)
                    stores.put(event[1], event[2]);
            }

            if (robotCount == 0 || stores.isEmpty()) {
                result[day] = 0;
                continue;
            }

            // ordenar robots (simple sort)
            Arrays.sort(robots, 0, robotCount);

            int m = stores.size();
            int[] storePos = new int[m];
            long[] pref = new long[m + 1];

            int idx = 0;
            for (Map.Entry<Integer, Integer> entry : stores.entrySet()) {
                storePos[idx] = entry.getKey();
                pref[idx + 1] = pref[idx] + entry.getValue();
                idx++;
            }

            int[] usedRobots = new int[robotCount];
            for (int i = 0; i < robotCount; i++) usedRobots[i] = robots[i];

            int bestProfit = computeMaxProfitDP(usedRobots, storePos, pref);
            result[day] = bestProfit;
        }

        return result;
    }

     /**
     * Simula día a día los movimientos de robots y tiendas usando SilkRoad.
     * No calcula resultados, solo visualiza el estado progresivo.
     */
    public void simulate(int[][] days, boolean slow) {
        road.reboot();
    
        if (days == null) return;
    
        for (int i = 0; i < days.length; i++) {
            int[] event = days[i];
            if (event == null || event.length < 2) continue;
    
            if (event[0] == 1) {
                // Formato posible: [1, posicion] o [1, posicion, tipo]
                if (event.length >= 3) {
                    String type = decodeRobotType(event[2]);
                    road.placeRobot(event[1], type);
                } else {
                    road.placeRobot(event[1], "normal");
                }
            } 
            else if (event[0] == 2) {
                // Formato posible: [2, posicion, tenges] o [2, posicion, tenges, tipo]
                if (event.length >= 4) {
                    String type = decodeStoreType(event[3]);
                    road.placeStore(event[1], event[2], type);
                } else if (event.length >= 3) {
                    road.placeStore(event[1], event[2], "normal");
                }
            }
    
            // --- movimientos automáticos (igual que antes) ---
            List<int[]> moves = new ArrayList<>();
            int[][] robotsSnapshot = road.robots(); // [pos, tenges]
            int[][] storesSnapshot = road.stores(); // [pos, tenges]
            for (int[] r : robotsSnapshot) {
                int rpos = r[0];
                int bestStore = -1;
                int bestGain = Integer.MIN_VALUE;
                for (int[] s : storesSnapshot) {
                    int spos = s[0];
                    int stenges = s[1];
                    if (stenges <= 0) continue;
                    int dist = Math.abs(spos - rpos);
                    int gain = stenges - dist;
                    if (gain > bestGain && gain > 0) {
                        bestGain = gain;
                        bestStore = spos;
                    }
                }
                if (bestStore != -1) {
                    moves.add(new int[]{ rpos, bestStore - rpos });
                }
            }
    
            road.moveRobots(moves);
    
            if (slow) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * Convierte un entero o código corto en tipo de robot.
     * Puedes ajustar esto según tu formato de entrada.
     */
    private String decodeRobotType(int code) {
        return switch (code) {
            case 2 -> "neverback";
            case 3 -> "tender";
            default -> "normal";
        };
    }

    /**
     * Convierte un entero o código corto en tipo de tienda.
     */
    private String decodeStoreType(int code) {
        return switch (code) {
            case 2 -> "autonomous";
            case 3 -> "fighter";
            default -> "normal";
        };
    }

    /**
     * Cálculo interno del máximo beneficio.
     */
    private int computeMaxProfitDP(int[] robots, int[] storePos, long[] pref) {

        final long NEG = Long.MIN_VALUE / 4;
        int R = robots.length;
        int M = storePos.length;

        long[][] dp = new long[R + 1][M + 1];
        for (int i = 0; i <= R; i++) {
            for (int j = 0; j <= M; j++) {
                dp[i][j] = NEG;
            }
        }

        dp[0][0] = 0;

        for (int r = 1; r <= R; r++) {
            int robotPos = robots[r - 1];
            for (int i = 0; i <= M; i++) {
                long best = dp[r - 1][i];
                for (int j = 0; j < i; j++) {
                    if (dp[r - 1][j] == NEG) continue;

                    long sum = pref[i] - pref[j];
                    int L = storePos[j];
                    int Rpos = storePos[i - 1];
                    long span = Rpos - L;
                    long dist = Math.min(Math.abs(robotPos - L), Math.abs(robotPos - Rpos));
                    long gain = sum - (span + dist);

                    if (gain > 0 && dp[r - 1][j] + gain > best) {
                        best = dp[r - 1][j] + gain;
                    }
                }
                dp[r][i] = best;
            }
        }

        long ans = 0;
        for (int i = 0; i <= M; i++) {
            if (dp[R][i] > ans) ans = dp[R][i];
        }

        return (int) ans;
    }
}

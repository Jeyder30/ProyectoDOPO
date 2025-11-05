package Simulation;

import Shapes.*;
import java.util.*;

/**
 * Clase Main: punto de entrada de la simulación Silk Road.
 * 
 * Lee los datos desde entrada estándar:
 *  - n: número de días
 *  - luego, para cada día, una secuencia de enteros (tipo y posiciones)
 * 
 * Ejecuta la simulación usando SilkRoadContest y muestra los resultados.
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        // Verificar si hay datos de entrada
        if (!sc.hasNextInt()) {
            System.out.println("No hay datos de entrada.");
            return;
        }

        int n = sc.nextInt(); // Número de días
        int[][] days = new int[n][];

        // Leer todos los días con validación básica
        for (int i = 0; i < n; i++) {
            if (!sc.hasNextInt()) {
                days[i] = new int[0];
                continue;
            }

            int t = sc.nextInt(); // tipo de evento

            // Caso: tipo 1 (solo necesita una posición)
            if (t == 1) {
                if (sc.hasNextInt()) {
                    int x = sc.nextInt();
                    days[i] = new int[]{t, x};
                } else {
                    days[i] = new int[]{t};
                }
            } 
            // Caso: otros tipos (necesitan al menos dos valores más)
            else {
                List<Integer> valores = new ArrayList<>();
                valores.add(t);

                while (sc.hasNextInt()) {
                    int v = sc.nextInt();
                    valores.add(v);

                    // Limita a 5 enteros por evento para evitar lecturas infinitas
                    if (valores.size() > 5) break;
                }

                days[i] = valores.stream().mapToInt(Integer::intValue).toArray();
            }
        }

        // Crear el concurso Silk Road
        SilkRoadContest contest = new SilkRoadContest(days);

        // Resolver la simulación
        int[] result = contest.solve(days);

        // Mostrar resultados
        System.out.println("=== Resultados diarios ===");
        for (int i = 0; i < result.length; i++) {
            System.out.println("Día " + (i + 1) + ": " + result[i]);
        }
    }
}

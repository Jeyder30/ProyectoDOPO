

package Simulation;

import Shapes.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n;
        if (!sc.hasNextInt()) return;
        n = sc.nextInt();  // número de días

        int[][] days = new int[n][];
        for (int i = 0; i < n; i++) {
            if (!sc.hasNextInt()) { days[i] = new int[0]; continue; }
            int t = sc.nextInt();
            if (t == 1) {
                if (!sc.hasNextInt()) { days[i] = new int[]{t}; continue; }
                int x = sc.nextInt();
                days[i] = new int[]{t, x};
            } else {
                if (!sc.hasNextInt()) { days[i] = new int[]{t}; continue; }
                int x = sc.nextInt();
                if (!sc.hasNextInt()) { days[i] = new int[]{t, x}; continue; }
                int c = sc.nextInt();
                days[i] = new int[]{t, x, c};
            }
        }

        SilkRoadContest contest = new SilkRoadContest(days);
        int[] result = contest.solve(days);

        System.out.println("Resultados diarios:");
        for (int val : result) {
            System.out.println(val);
        }
    }
}

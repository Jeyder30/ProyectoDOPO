
package Simulation;

import Shapes.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Construye un recorrido en espiral dentro de una cuadrícula.
 */
public class RouteBuilder {
    /**
     * Construye un recorrido en espiral dentro de una cuadrícula.
     * @param gridSize tamaño de la cuadrícula (ej: 15 → 15x15)
     * @param cellSize tamaño de cada celda en pantalla
     * @param startX coordenada X inicial en pantalla
     * @param startY coordenada Y inicial en pantalla
     * @return lista de celdas en orden espiral
     */
    public static List<Cell> buildSpiralInGrid(int gridSize, int cellSize, int startX, int startY) {
        List<Cell> cells = new ArrayList<>();

        int x = 0, y = 0;
        int[][] dirs = {{0,1},{1,0},{0,-1},{-1,0}};
        boolean[][] used = new boolean[gridSize][gridSize];

        int dir = 0;
        for (int i = 0; i < gridSize * gridSize; i++) {
            int screenX = startX + y * cellSize;
            int screenY = startY + x * cellSize;
            cells.add(new Cell(screenX, screenY, cellSize));
            used[x][y] = true;

            int nx = x + dirs[dir][0];
            int ny = y + dirs[dir][1];
            if (nx < 0 || nx >= gridSize || ny < 0 || ny >= gridSize || used[nx][ny]) {
                dir = (dir + 1) % 4;
                nx = x + dirs[dir][0];
                ny = y + dirs[dir][1];
            }
            x = nx;
            y = ny;
        }

        return cells;
    }
}

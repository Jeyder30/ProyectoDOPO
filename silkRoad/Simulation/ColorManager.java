
package Simulation;

import Shapes.*;
/**
 * Clase que gestiona colores de forma cíclica.
 */
public class ColorManager {

    private final String[] colors;
    private int index;

    public ColorManager() {
        colors = new String[]{"red", "blue", "green", "yellow", "magenta", "black"};
        index = 0;
    }

    /**
     * Retorna el siguiente color de manera cíclica.
     */
    public String nextColor() {
        String color = colors[index];
        index++;
        if (index >= colors.length) {
            index = 0;
        }
        return color;
    }

    /**
     * Permite reiniciar el ciclo de colores.
     */
    public void reset() {
        index = 0;
    }

    public int getIndex() {
        return index;
    }
}

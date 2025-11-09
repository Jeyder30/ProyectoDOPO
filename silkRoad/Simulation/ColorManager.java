
package Simulation;

import Shapes.*;
/**
 * Clase que gestiona una lista de colores y los entrega de forma cíclica.
 * Cada vez que se solicita un color, el índice avanza al siguiente.
 * Cuando se llega al final de la lista, el ciclo se reinicia automáticamente.
 */
public class ColorManager {

    private final String[] colors;
    private int index;

    public ColorManager() {
        colors = new String[]{"red", "blue", "green", "yellow", "magenta", "black"};
        index = 0;
    }

     /**
     * Devuelve el siguiente color del arreglo.
     * Si se llega al final de la lista de colores,
     * el ciclo vuelve a empezar desde el primer color.
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
    
    /**
     * Retorna el índice actual dentro del ciclo de colores.
     */
    public int getIndex() {
        return index;
    }
}

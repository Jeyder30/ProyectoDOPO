
package Simulation;

import Shapes.*;
/**
 * Representa una celda dentro del recorrido de SilkRoad.
 * Cada celda puede estar vacía, contener una tienda o un robot.
 * Visualmente se dibuja como un rectángulo con borde negro marco
 * y un fondo blanco interno. Si tiene contenido, se superpone un
 * círculo robot o un rectángulo más pequeño tienda.
 */
public class Cell {
    private Rectangle marco;       // rectángulo negro grande
    private Rectangle fondo;       // rectángulo blanco encima (fondo real)
    private Rectangle storeRect;   // rectángulo de la tienda
    private Circle robotCircle;    // círculo del robot
    private int x, y, size;
    private boolean visible = false;

    // Posiciones por defecto de BlueJ shapes
    private static final int DEFAULT_RECT_X = 70;
    private static final int DEFAULT_RECT_Y = 15;
    private static final int DEFAULT_CIRCLE_X = 20;
    private static final int DEFAULT_CIRCLE_Y = 15;
    
    /**
     * Construye una celda con posición y tamaño definidos.
     * Se crean dos rectángulos: uno negro para el borde marco
     * y otro blanco ligeramente más pequeño para el fondo.
     */
    public Cell(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;

        // Marco negro (celda externa)
        marco = new Rectangle();
        marco.changeSize(size, size);
        marco.changeColor("black");
        marco.moveHorizontal(x - DEFAULT_RECT_X);
        marco.moveVertical(y - DEFAULT_RECT_Y);

        // Fondo blanco (interior)
        fondo = new Rectangle();
        fondo.changeSize(size - 4, size - 4); // margen de 2 px por lado
        fondo.changeColor("white");
        fondo.moveHorizontal((x + 2) - DEFAULT_RECT_X);
        fondo.moveVertical((y + 2) - DEFAULT_RECT_Y);
    }

    /**
     * Coloca un robot en el centro de la celda.
     * Si la celda ya tenía contenido, primero se limpia.
     * El robot se representa como un círculo de color definido.
     */
    public void placeRobot(String color) {
        clear();
        int bodySize = (int)(size * 0.7);
        int offset = (size - bodySize) / 2;

        robotCircle = new Circle();
        robotCircle.changeSize(bodySize);
        robotCircle.changeColor(color);

        int targetX = x + offset;
        int targetY = y + offset;

        robotCircle.moveHorizontal(targetX - DEFAULT_CIRCLE_X);
        robotCircle.moveVertical(targetY - DEFAULT_CIRCLE_Y);

        if (visible) robotCircle.makeVisible();
    }

    /**
     * Coloca una tienda en el centro de la celda.
     * Si la celda ya tenía contenido, se limpia antes.
     * La tienda se representa como un rectángulo de color definido.
     */
    public void placeStore(String color) {
        clear();
        int storeSize = (int)(size * 0.6);
        int offset = (size - storeSize) / 2;

        storeRect = new Rectangle();
        storeRect.changeSize(storeSize, storeSize);
        storeRect.changeColor(color);

        int targetX = x + offset;
        int targetY = y + offset;

        storeRect.moveHorizontal(targetX - DEFAULT_RECT_X);
        storeRect.moveVertical(targetY - DEFAULT_RECT_Y);

        if (visible) storeRect.makeVisible();
    }

    /**
     * Limpia el contenido de la celda.
     * Si hay un robot o una tienda, los elimina visualmente
     * y libera sus referencias.
     */
    public void clear() {
        if (robotCircle != null) {
            robotCircle.makeInvisible();
            robotCircle = null;
        }
        if (storeRect != null) {
            storeRect.makeInvisible();
            storeRect = null;
        }
    }

    /**
     * Muestra la celda en pantalla junto con su contenido actual.
     * Primero se dibuja el marco negro, luego el fondo blanco,
     * y finalmente el contenido si lo hay.
     */
    public void makeVisible() {
        marco.makeVisible();         // primero marco negro
        fondo.makeVisible();         // luego fondo blanco encima
        if (storeRect != null) storeRect.makeVisible();
        if (robotCircle != null) robotCircle.makeVisible();
        visible = true;
    }

    /**
     * Oculta completamente la celda de la pantalla.
     * Se desactivan todos los elementos gráficos asociados.
     */
    public void makeInvisible() {
        if (robotCircle != null) robotCircle.makeInvisible();
        if (storeRect != null) storeRect.makeInvisible();
        fondo.makeInvisible();
        marco.makeInvisible();
        visible = false;
    }

    // Métodos de acceso a las coordenadas y tamaño
    public int getX() { return x; }
    public int getY() { return y; }
    public int getSize() { return size; }
}

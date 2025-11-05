
package Simulation;

import Shapes.*;
/**
 * Representa una tienda en una linea horizontal con una cantidad de recursos llamada "tenges".
 * Cada tienda tiene una posicion fija, puede reabastecerse, vaciarse y ser mostrada graficamente.
 *
 * Conserva la API original; añade placeInCell(Cell) para pintar centrada en una celda.
 */
public class Store {
    private int location;
    private final int initialTenges;
    private int currentTenges;
    private Rectangle base;
    private Triangle roof;
    private Rectangle door;
    private final String color;
    private boolean isVisible;
    private static final int SCALE = 10;
    private int timesEmptied;

    public Store(int location, int tenges, String color) {
        this.location = location;
        this.initialTenges = tenges;
        this.currentTenges = tenges;
        this.color = color;
        this.base = new Rectangle();
        this.roof = new Triangle();
        this.door = new Rectangle();
        this.isVisible = false;
        this.timesEmptied = 0;
        updatePosition();
    }

    private void updatePosition() {
        if (base != null) {
            base.moveHorizontal(location * SCALE);
            base.moveVertical(100);
        }
    }

    /**
     * Coloca/centra la tienda dentro de la celda recibida.
     */
    public void placeInCell(Cell cell) {
        int cx = cell.getX();
        int cy = cell.getY();
        int cellSize = cell.getSize();

        int baseSize = (int)(cellSize * 0.6);
        int centerX = cx + cellSize / 2;
        int centerY = cy + cellSize / 2;

        base = new Rectangle();
        base.changeSize(baseSize, baseSize);
        base.changeColor(color);

        int baseX = centerX - baseSize / 2;
        int baseY = centerY - baseSize / 2;
        base.setAbsolutePosition(baseX, baseY);

        int roofWidth = baseSize + 4;
        int roofHeight = Math.max(6, baseSize / 2);

        roof = new Triangle();
        roof.changeSize(roofHeight, roofWidth);
        roof.changeColor("red");

        int roofX = baseX + baseSize / 2;
        int roofY = baseY - roofHeight;
        roof.setAbsolutePosition(roofX, roofY);

        int doorWidth = baseSize / 3;
        int doorHeight = baseSize / 2;

        door = new Rectangle();
        door.changeSize(doorHeight, doorWidth);
        door.changeColor("black");

        int doorX = baseX + (baseSize - doorWidth) / 2;
        int doorY = baseY + baseSize - doorHeight;
        door.setAbsolutePosition(doorX, doorY);

        if (isVisible) {
            if (base != null) base.makeVisible();
            if (roof != null) roof.makeVisible();
            if (door != null) door.makeVisible();
        }
    }

    public int getLocation() {
        return location;
    }

    public int getCurrentTenges() {
        return currentTenges;
    }

    public int getInitialTenges() {
        return initialTenges;
    }

    public void resupply() {
        currentTenges = initialTenges;
        if (isVisible && base != null) base.changeColor(color);
    }

    /**
     * Vacía la tienda (deja de tener tenges disponibles) y
     * aumenta en 1 el contador de veces que ha sido vaciada.
     * Cambia el color de la base a gris para indicar visualmente
     * que la tienda ya no tiene recursos disponibles.
     */
    public void empty() {
        // Solo se vacía si aún tiene tenges
        if (currentTenges > 0) {
            currentTenges = 0;
            timesEmptied++;
    
            // Cambio visual de color: tienda vacía = gris
            if (base != null) base.changeColor("gray");
            if (roof != null) roof.changeColor("lightGray");
            if (door != null) door.changeColor("darkGray");
        }
    }


    public boolean isEmptiedToday() {
        return currentTenges == 0;
    }

    public void makeVisible() {
        if (!isVisible) {
            if (base != null) base.changeColor(color);
            if (base != null) base.makeVisible();
            if (roof != null) roof.makeVisible();
            if (door != null) door.makeVisible();
            isVisible = true;
        }
    }

    public void makeInvisible() {
        if (isVisible) {
            if (roof != null) roof.makeInvisible();
            if (door != null) door.makeInvisible();
            if (base != null) base.makeInvisible();
            isVisible = false;
        }
    }

    /**
     * Retorna cuántas veces esta tienda ha sido vaciada.
     * @return número de veces vaciada
     */
    public int getTimesEmptied() {
        return timesEmptied;
    }

    @Override
    public String toString() {
        return "Store{loc=" + location + ", current=" + currentTenges + ", timesEmptied=" + timesEmptied + "}";
    }
    
    public void reduceTengesByHalf() {
    currentTenges = currentTenges / 2;
    }
}
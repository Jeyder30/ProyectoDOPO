

package Simulation;

import Shapes.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un robot en un espacio horizontal basado en celdas.
 * La representación visual usa Circle para cuerpo y ojos, Rectangle para la boca.
 *
 * Las shapes se crean en placeInCell(Cell) y se controlan con makeVisible/makeInvisible.
 */
public class Robot {

    private final int initialLocation;
    private int currentLocation;
    private int orderOfArrival;
    private Circle shape;     // cuerpo
    private Circle eyeLeft;
    private Circle eyeRight;
    private Rectangle mouth;
    private final String color;
    private boolean isVisible;
    private int tenges;
    private int profit;
    private final List<ProfitRecord> profitRecords;

    public Robot(int location, String color) {
        this.initialLocation = location;
        this.currentLocation = location;
        this.color = color;
        this.shape = null;
        this.eyeLeft = null;
        this.eyeRight = null;
        this.mouth = null;
        this.isVisible = false;
        this.tenges = 0;
        this.orderOfArrival = 0;
        this.profit = 0;
        this.profitRecords = new ArrayList<>();
    }

    /**
     * Registro estructurado de ganancias.
     */
    public static class ProfitRecord {
        public final int amount;
        public final int cell;
        public final int distance;
        public ProfitRecord(int amount, int cell, int distance) {
            this.amount = amount;
            this.cell = cell;
            this.distance = distance;
        }
    }

    /**
     * Coloca el robot dentro de una celda, centrado, y crea las shapes (cuerpo, ojos, boca).
     * Si las shapes ya existen, las actualiza y reposiciona.
     */
    public void placeInCell(Cell cell) {
        if (cell == null) return;

        int cx = cell.getX();
        int cy = cell.getY();
        int cellSize = cell.getSize();

        int bodySize = (int) (cellSize * 0.7);
        int offset = (cellSize - bodySize) / 2;
        int targetX = cx + offset;
        int targetY = cy + offset;

        // cuerpo
        if (shape == null) shape = new Circle();
        shape.changeSize(bodySize);
        shape.changeColor(color);
        shape.setAbsolutePosition(targetX, targetY);

        // ojos
        int eyeSize = Math.max(4, bodySize / 5);
        if (eyeLeft == null) eyeLeft = new Circle();
        eyeLeft.changeSize(eyeSize);
        eyeLeft.changeColor("white");
        eyeLeft.setAbsolutePosition(targetX + bodySize / 4, targetY + bodySize / 4);

        if (eyeRight == null) eyeRight = new Circle();
        eyeRight.changeSize(eyeSize);
        eyeRight.changeColor("white");
        eyeRight.setAbsolutePosition(targetX + (bodySize / 2), targetY + bodySize / 4);

        // boca
        if (mouth == null) mouth = new Rectangle();
        mouth.changeSize(Math.max(3, bodySize / 10), bodySize / 2);
        mouth.changeColor("black");
        mouth.setAbsolutePosition(targetX + (bodySize / 4), targetY + (2 * bodySize / 3));

        // visibilidad coherente
        if (isVisible) {
            if (shape != null) shape.makeVisible();
            if (eyeLeft != null) eyeLeft.makeVisible();
            if (eyeRight != null) eyeRight.makeVisible();
            if (mouth != null) mouth.makeVisible();
        } else {
            if (shape != null) shape.makeInvisible();
            if (eyeLeft != null) eyeLeft.makeInvisible();
            if (eyeRight != null) eyeRight.makeInvisible();
            if (mouth != null) mouth.makeInvisible();
        }
    }

    public int getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(int loc) {
        this.currentLocation = loc;
    }

    /**
     * Mueve el robot (unidad: celdas).
     */
    public void move(int meters) {
        this.currentLocation += meters;
    }

    public void resetPosition() {
        this.currentLocation = initialLocation;
    }

    public void setOrderOfArrival(int order) {
        this.orderOfArrival = order;
    }

    public int getOrderOfArrival() {
        return orderOfArrival;
    }

    public int getTenges() {
        return tenges;
    }

    public String getColor() {
        return color;
    }

    public void makeVisible() {
        if (!isVisible) {
            isVisible = true;
            if (shape != null) shape.makeVisible();
            if (eyeLeft != null) eyeLeft.makeVisible();
            if (eyeRight != null) eyeRight.makeVisible();
            if (mouth != null) mouth.makeVisible();
        }
    }

    public void makeInvisible() {
        if (isVisible) {
            isVisible = false;
            if (shape != null) shape.makeInvisible();
            if (eyeLeft != null) eyeLeft.makeInvisible();
            if (eyeRight != null) eyeRight.makeInvisible();
            if (mouth != null) mouth.makeInvisible();
        }
    }

    /**
     * Registra una ganancia obtenida por el robot en una tienda.
     * Guarda registro estructurado.
     */
    public void addProfit(int amount, int cell, int distance) {
    int net = amount;
    this.profit += net;
    this.profitRecords.add(new ProfitRecord(net, cell, distance));
    }

    public int getProfit() {
        return profit;
    }

    /**
     * Historial en texto (compatibilidad con código existente).
     */
    public List<String> getProfitHistory() {
        List<String> out = new ArrayList<>();
        for (ProfitRecord r : profitRecords) {
            out.add("Ganó " + r.amount + " en celda " + r.cell + " (distancia " + r.distance + ")");
        }
        return out;
    }

    /**
     * Historial estructurado (recomendado para parsing/logic).
     */
    public List<ProfitRecord> getProfitRecords() {
        return new ArrayList<>(profitRecords);
    }

    public void addTenges(int amount) {
        this.tenges += amount;
    }

    /**
     * Hace parpadear al robot (visual). Solo si la forma existe.
     */
    public void blink() {
        if (shape != null) shape.discoTime(100);
    }

    public Circle getBodyShape() { return shape; }
    public Circle getEyeLeftShape() { return eyeLeft; }
    public Circle getEyeRightShape() { return eyeRight; }
    public Rectangle getMouthShape() { return mouth; }

    @Override
    public String toString() {
        return "Robot{loc=" + currentLocation + ", profit=" + profit + "}";
    }
}

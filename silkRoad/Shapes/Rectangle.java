package Shapes;
import java.awt.*;

/**
 * Un rectángulo que se puede manipular y dibuja en el Canvas.
 */
public class Rectangle extends Shape {

    public static final int EDGES = 4;
    private int height;
    private int width;

    public Rectangle() {
        height = 30;
        width = 40;
        xPosition = 70;
        yPosition = 15;
        color = "magenta";
        isVisible = false;
    }

    @Override
    protected void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color,
                new java.awt.Rectangle(xPosition, yPosition, width, height));
            canvas.wait(10);
        }
    }

    @Override
    protected void erase() {
        if (isVisible) {
            Canvas.getCanvas().erase(this);
        }
    }

    public void changeSize(int newHeight, int newWidth) {
        erase();
        height = newHeight;
        width = newWidth;
        draw();
    }

    public void changeColor(String newColor) {
        color = newColor;
        draw();
    }
}

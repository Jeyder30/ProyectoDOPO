package Shapes;
import java.awt.*;
import java.awt.geom.*;

/**
 * Un círculo que se puede manipular y dibuja en el Canvas.
 */
public class Circle extends Shape {

    public static final double PI = 3.1416;
    private int diameter;
    private double area;
    private int disco = 0;

    public Circle() {
        diameter = 30;
        xPosition = 20;
        yPosition = 15;
        color = "blue";
        isVisible = false;
    }

    public Circle(double area) {
        this.area = area;
        double radio = Math.sqrt(area / PI);
        diameter = (int) (2 * radio);
        xPosition = 20;
        yPosition = 15;
        color = "green";
        isVisible = false;
    }

    @Override
    protected void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color,
                new Ellipse2D.Double(xPosition, yPosition, diameter, diameter));
            canvas.wait(10);
        }
    }

    @Override
    protected void erase() {
        if (isVisible) {
            Canvas.getCanvas().erase(this);
        }
    }

    public void changeSize(int newDiameter) {
        erase();
        diameter = newDiameter;
        draw();
    }

    public void changeColor(String newColor) {
        color = newColor;
        draw();
    }

    public double area() {
        double radio = diameter / 2.0;
        return PI * radio * radio;
    }

    public void bigger(int percentage) {
        if (percentage < 0 || percentage > 100) {
            System.out.println("Error: porcentaje fuera de rango");
            return;
        }
        double area = area();
        double incremento = area * (percentage / 100.0);
        double newArea = area + incremento;
        double newRadio = Math.sqrt(newArea / PI);
        diameter = (int) (2 * newRadio);
        erase();
        draw();
    }

    public void shrink(int times, int areaF) {
        for (int i = 0; i < times; i++) {
            if (area() <= areaF) break;
            double newArea = area() / 2.0;
            double newRadius = Math.sqrt(newArea / PI);
            diameter = (int) (2 * newRadius);
            erase();
            draw();
        }
    }

    public void discoTime(int time) {
        String[] colors = {"red", "green", "blue", "yellow", "magenta", "black"};
        for (int i = 0; i < time; i++) {
            changeColor(colors[disco]);
            disco++;
            if (disco >= colors.length) disco = 0;
            draw();
        }
    }
}

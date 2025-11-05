package Shapes;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

/**
 * Canvas is a class to allow for simple graphical drawing on a canvas.
 * Modified for BlueJ shapes demo.
 */
public class Canvas {
    private static Canvas canvasSingleton;

    /**
     * Factory method to get the canvas singleton object.
     */
    public static Canvas getCanvas() {
        if (canvasSingleton == null) {
            canvasSingleton = new Canvas("BlueJ Shapes Demo", 8000, 8000, Color.white);
        }
        canvasSingleton.setVisible(true);
        return canvasSingleton;
    }

    private JFrame frame;
    private CanvasPane canvas;
    private Graphics2D graphic;
    private Color backgroundColour;
    private Image canvasImage;
    private List<Object> objects;
    private HashMap<Object, ShapeDescription> shapes;

    /**
     * Create a Canvas.
     */
    private Canvas(String title, int width, int height, Color bgColour) {
        frame = new JFrame();
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        canvas.setPreferredSize(new Dimension(width, height));
        backgroundColour = bgColour;
        frame.pack();
        objects = new ArrayList<Object>();
        shapes = new HashMap<Object, ShapeDescription>();
    }

    /**
     * Set the canvas visibility and initialize the graphic context.
     */
    public void setVisible(boolean visible) {
        if (graphic == null) {
            Dimension size = canvas.getSize();
            canvasImage = canvas.createImage(size.width, size.height);
            graphic = (Graphics2D) canvasImage.getGraphics();
            graphic.setColor(backgroundColour);
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(Color.black);
        }
        frame.setVisible(visible);
    }

    /**
     * Draw a given shape onto the canvas.
     */
    public void draw(Object referenceObject, String color, java.awt.Shape shape) {
        objects.remove(referenceObject); // remove if already present
        objects.add(referenceObject);    // add to end
        shapes.put(referenceObject, new ShapeDescription(shape, color));
        redraw();
    }

    /**
     * Erase a given object’s shape from the canvas.
     */
    public void erase(Object referenceObject) {
        objects.remove(referenceObject);
        shapes.remove(referenceObject);
        redraw();
    }

    /**
     * Set the current foreground color for future drawing.
     */
    public void setForegroundColor(String colorString) {
        switch (colorString) {
            case "red" -> graphic.setColor(Color.red);
            case "black" -> graphic.setColor(Color.black);
            case "blue" -> graphic.setColor(Color.blue);
            case "yellow" -> graphic.setColor(Color.yellow);
            case "green" -> graphic.setColor(Color.green);
            case "magenta" -> graphic.setColor(Color.magenta);
            case "white" -> graphic.setColor(Color.white);
            case "orange" -> graphic.setColor(Color.orange);
            case "cyan" -> graphic.setColor(Color.cyan);
            case "gray" -> graphic.setColor(Color.gray);
            case "lightGray" -> graphic.setColor(Color.lightGray);
            case "darkGray" -> graphic.setColor(Color.darkGray);
            default -> graphic.setColor(Color.black);
        }
    }

    /**
     * Wait for a specified number of milliseconds before continuing.
     */
    public void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {
            // ignore
        }
    }

    /**
     * Redraw all shapes currently on the Canvas.
     */
    private void redraw() {
        erase();
        for (Iterator<Object> i = objects.iterator(); i.hasNext();) {
            Object key = i.next();
            ShapeDescription sd = shapes.get(key);
            if (sd != null) sd.draw(graphic);
        }
        canvas.repaint();
    }

    /**
     * Erase the whole canvas (background only).
     */
    private void erase() {
        Color original = graphic.getColor();
        graphic.setColor(backgroundColour);
        Dimension size = canvas.getSize();
        graphic.fill(new java.awt.Rectangle(0, 0, size.width, size.height));
        graphic.setColor(original);
    }

    /**
     * CanvasPane - internal class for drawing on the canvas.
     */
    private class CanvasPane extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(canvasImage, 0, 0, null);
        }
    }

    /**
     * ShapeDescription - stores information about a drawn shape.
     */
    private class ShapeDescription {
        private java.awt.Shape shape;
        private String colorString;

        public ShapeDescription(java.awt.Shape shape, String color) {
            this.shape = shape;
            this.colorString = color;
        }

        public void draw(Graphics2D graphic) {
            setForegroundColor(colorString);
            graphic.draw(shape);
            graphic.fill(shape);
        }
    }
}

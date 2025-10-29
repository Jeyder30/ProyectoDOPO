package Shapes;

public abstract class Shape {
    protected int xPosition;
    protected int yPosition;
    protected String color;
    protected boolean isVisible;

    public void makeVisible() {
        isVisible = true;
        draw();
    }

    public void makeInvisible() {
        erase();
        isVisible = false;
    }

    protected abstract void draw();
    protected abstract void erase();

    public void moveHorizontal(int distance) {
        erase();
        xPosition += distance;
        draw();
    }

    public void moveVertical(int distance) {
        erase();
        yPosition += distance;
        draw();
    }

    public void setAbsolutePosition(int newX, int newY) {
        erase();
        xPosition = newX;
        yPosition = newY;
        draw();
    }
}

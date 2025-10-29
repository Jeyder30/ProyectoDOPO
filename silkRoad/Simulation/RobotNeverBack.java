package Simulation;

public class RobotNeverBack extends Robot {

    public RobotNeverBack(int location, String color) {
        super(location, color);
    }

    @Override
    public void move(int meters) {
        if (meters < 0) {
            // No se mueve hacia atrás
            return;
        }
        super.move(meters);
    }

    @Override
    public String toString() {
        return "RobotNeverBack{loc=" + getCurrentLocation() + ", profit=" + getProfit() + "}";
    }
}

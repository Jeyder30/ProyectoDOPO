package Simulation;

public class StoreFighter extends Store {

    public StoreFighter(int location, int tenges, String color) {
        super(location, tenges, color);
    }

    /**
     * Entrega el dinero solo si el robot tiene más que la tienda.
     * Si no, la tienda se defiende y el robot pierde su movimiento.
     */
    public int interactWithRobot(Robot robot, int distance) {
        if (robot.getTenges() > getCurrentTenges()) {
            int gain = getCurrentTenges() - distance;
            empty();
            robot.addProfit(gain, getLocation(), distance);
            robot.addTenges(gain);
            return gain;
        } else {
            // robot no gana nada
            return -distance;
        }
    }

    @Override
    public String toString() {
        return "StoreFighter{loc=" + getLocation() + ", tenges=" + getCurrentTenges() + "}";
    }
}

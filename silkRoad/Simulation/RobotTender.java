package Simulation;

public class RobotTender extends Robot {

    public RobotTender(int location, String color) {
        super(location, color);
    }

    public int interactWithStore(Store store, int distance) {
        if (store.getCurrentTenges() <= 0) return -distance;
        int half = store.getCurrentTenges() / 2;
        int gain = half - distance;

        store.empty();
        addProfit(gain, store.getLocation(), distance);
        addTenges(half);
        return gain;
    }

    @Override
    public String toString() {
        return "RobotTender{loc=" + getCurrentLocation() + ", profit=" + getProfit() + "}";
    }
}

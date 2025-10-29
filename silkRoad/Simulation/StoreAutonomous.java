package Simulation;

public class StoreAutonomous extends Store {

    public StoreAutonomous(int location, int tenges, String color) {
        // Sobrescribe la ubicación de forma autónoma
        super((int)(Math.random() * 50), tenges, color); 
    }

    @Override
    public String toString() {
        return "StoreAutonomous{loc=" + getLocation() + ", tenges=" + getCurrentTenges() + "}";
    }
}

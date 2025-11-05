package Simulation;

public class StoreAutonomous extends Store {

    /**
     * Crea una tienda autónoma que se ubica aleatoriamente en el mapa.
     * @param ignoredLocation se ignora (para compatibilidad)
     * @param tenges cantidad inicial de dinero
     * @param color color de la tienda
     */
    public StoreAutonomous(int ignoredLocation, int tenges, String color) {
        super(generateRandomLocation(), tenges, color);
    }

    /**
     * Genera una ubicación aleatoria dentro de la longitud total del mapa.
     * Usa SilkRoad.defaultLength para evitar desbordes si no hay un mapa aún.
     */
    private static int generateRandomLocation() {
        int max = 1000; // valor por defecto
        try {
            // Si el mapa ya existe, usamos su longitud real
            java.lang.reflect.Field field = SilkRoad.class.getDeclaredField("defaultLength");
            field.setAccessible(true);
            max = (int) field.get(null);
        } catch (Exception ignored) {}

        return (int) (Math.random() * max);
    }

    @Override
    public String toString() {
        return "StoreAutonomous{loc=" + getLocation() +
               ", tenges=" + getCurrentTenges() + "}";
    }
}

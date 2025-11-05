package Simulation;

import Shapes.*;

public class RobotTender extends Robot {

    public RobotTender(int position, String color) {
        super(position, color);
    }

    /**
     * El RobotTender toma solo la mitad de los tenges disponibles en la tienda.
     * La tienda conserva la otra mitad 
     * 
     * @param store     La tienda con la que interactúa.
     * @param distance  La distancia recorrida para llegar a la tienda.
     * @return          La ganancia obtenida.
     */
    public int interactWithStore(Store store, int distance) {
        if (store == null) return -distance;

        int currentTenges = store.getCurrentTenges();
        if (currentTenges <= 0) return -distance;

        // Toma la mitad de las monedas de la tienda
        int halfTenges = currentTenges / 2;

        // La tienda se queda con la otra mitad
        store.reduceTengesByHalf();

        // Calcula la ganancia (toma de la tienda - costo por distancia)
        int gain = halfTenges - distance;

        // Actualiza los tenges y la ganancia del robot
        this.addTenges(halfTenges);
        this.addProfit(gain, store.getLocation(), distance);

        return gain;
    }
    
    
}

package pandemic.agents;

import pandemic.shops.Product;
import pandemic.main.Util;

import java.util.ArrayList;

/**
 * Class Vehicle - simulates a Vehicle
 */
public abstract class Vehicle {
    private final String brand;
    private final double tankCapacity;
    private final double gasBurnRate;
    private double gasLevel;

    /**
     * Constructor for Vehicle.
     */
    public Vehicle() {
        this.brand = Util.generateRandomString(5);
        this.tankCapacity = 35;
        this.gasBurnRate = 1;
        this.gasLevel = 20;
    }

    /**
     * fills tank of the Vehicle
     * @param howMuchToFill - how much to fill
     */
    public abstract void fillTank(double howMuchToFill);

    /**
     * Adds a product to Trunk.
     * @param product - what product to add to trunk
     * @return true if successful, false otherwise
     */
    public abstract boolean addProductToTrunk(Product product);

    /**
     * Removes all of the products form Vehicle's trunk.
     * @return list of removed products
     */
    public abstract ArrayList<Product> removeProductsFromTrunk();

    /**
     * Burns gas, that is burned to travel one tile.
     */
    public void burnGas(){
        this.gasLevel -= this.gasBurnRate;
    }

    public double getTankCapacity() {
        return tankCapacity;
    }

    public double getGasLevel() {
        return gasLevel;
    }

    public void setGasLevel(double gasLevel) {
        this.gasLevel = gasLevel;
    }
}

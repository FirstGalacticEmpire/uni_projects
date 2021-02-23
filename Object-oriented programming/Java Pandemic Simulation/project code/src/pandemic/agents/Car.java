package pandemic.agents;

import pandemic.shops.Product;
import pandemic.shops.Shop;

import java.util.ArrayList;

/**
 * Class car simulates a car.
 */
public class Car extends Vehicle {
    private final int trunkCapacity = 2;
    private final ArrayList<Product> trunk = new ArrayList<>();

    /**
     * Constructor for car.
     */
    public Car() { }

    /**
     * fills tank of the Car
     * @param howMuchToFill - how much to fill
     */
    @Override
    public void fillTank(double howMuchToFill) {
        this.setGasLevel(Math.min(this.getGasLevel() + howMuchToFill, this.getTankCapacity()));
    }

    /**
     * Loads provided products to the trunk
     * When using, the user should artificially prevent overloading the trunk.
     * @param products to be loaded
     */
    public void loadProductsToTrunk(ArrayList<Product> products) {
        if (trunk.size() + products.size() <= trunkCapacity) {
            trunk.addAll(products);
        } else {
            //too many products
        }
    }

    /**
     * Unloads products to selected shop.
     * @param shop to which it should unload products
     * @return true if successful, false otherwise
     */
    public boolean unloadProductsToShop(Shop shop) {
        int size = trunk.size();
        for (int x = 0; x < size; x++) {
            if (!shop.addProductToListOfProducts(this.trunk.remove(0))) {
                break;
            }
        }
        return trunk.isEmpty();
    }


    /**
     * @param product - what product to add to trunk
     * @return true if successful, false if trunk is full
     */
    @Override
    public boolean addProductToTrunk(Product product) {
        if (trunk.size() + 1 <= trunkCapacity) {
            trunk.add(product);
            return true;
        } else {
            //trunk full
            return false;
        }
    }

    /**
     * Removes products from trunk.
     * @return list of removed products from trunk
     */
    @Override
    public ArrayList<Product> removeProductsFromTrunk() {
        ArrayList<Product> products = new ArrayList<>(trunk);
        trunk.clear();
        return products;
    }

    /**
     * @return available space (int) in trunk
     */
    public int availableSpaceInTrunk(){
        return trunkCapacity - trunk.size();
    }

    public ArrayList<Product> getTrunk() {
        return trunk;
    }

}

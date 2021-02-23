package pandemic.shops;

import pandemic.agents.Agent;
import pandemic.map.Position;
import pandemic.main.Util;

import java.util.ArrayList;
import java.util.Date;

/**
 * Class simulating shop.
 */
public class Shop {
    private final String nameOfShop;
    private final String address;
    private int maxAgentCapacity;
    private final int maxProductCapacity;
    private final ArrayList<Product> listOfProducts = new ArrayList<>();
    private final ArrayList<Agent> listOfAgentsInside = new ArrayList<>();
    private boolean isOnLockdown;
    private final Object shopLock = new Object();


    /**
     * Shop constructor.
     * @param posOfShop
     */
    public Shop(Position posOfShop) {
        this.nameOfShop = Util.generateRandomString(5);
        this.address = posOfShop.toString();
        this.maxAgentCapacity = 3;
        this.maxProductCapacity = 15;
    }

    /**
     * Removes expired products.
     */
    public synchronized void removeExpiredProducts() {
        this.listOfProducts.removeIf(product -> product.getExpirationDate() < new Date().getTime() + 1000);
    }

    /**
     * Adds product to the storage.
     * @param product product to be added to the list
     * @return true if successful, false if not successful.
     */
    public synchronized boolean addProductToListOfProducts(Product product) {
        if (this.listOfProducts.size() <= this.maxProductCapacity - 1) {
            this.listOfProducts.add(product);
            return true;
        } else {
//            System.out.println("Storage is full");
            return false;

        }
    }

    /**
     * Unloads products from the storage.
     * @param howMany products to unload from storage
     * @return list of unloaded products
     */
    public synchronized ArrayList<Product> takeProductsFromStorage(int howMany) {
        ArrayList<Product> productsToBeTaken = new ArrayList<>();
        for (int x = 0; x < howMany; x++) {
            try {
                productsToBeTaken.add(listOfProducts.remove(0));
            } catch (IndexOutOfBoundsException ignored) {
                //trying to take more products than there are in Storage
            }
        }
        return productsToBeTaken;
    }

    /**
     * @param agent trying to enter the shop
     * @return true if agent entered the shop, false if shop is full.
     */
    public boolean enterShop(Agent agent) {
        synchronized (shopLock) {
            if (this.listOfAgentsInside.size() <= this.maxAgentCapacity) {
                this.listOfAgentsInside.add(agent);
                return true;
            } else {
//                System.out.println("Shop is full");
                return false;
            }
        }
    }

    /**
     * @param agent trying to leave the shop
     * @return true if agent leaves the shop.
     */
    public boolean leaveShop(Agent agent) {
        synchronized (shopLock) {
            this.listOfAgentsInside.remove(agent);
            return true;
        }
    }

    /**
     * Updates state regarding lockdown. Connected to class Observable.
     * @param isLockDown true is should begin lockdown, false if otherwise
     */
    public void update(boolean isLockDown) {
        if (isLockDown) {
            this.isOnLockdown = true;
            this.maxAgentCapacity = (int) (0.75 * this.maxAgentCapacity);
        } else {
            this.isOnLockdown = false;
            this.maxAgentCapacity = 5;
        }
    }

    /**
     * @return Shop description
     */
    @Override
    public String toString() {
        return "Shop{" +
                "nameOfShop='" + nameOfShop + '\'' +
                ", listOfProducts=" + listOfProducts +
                ", listOfAgentsInside=" + listOfAgentsInside +
                '}';
    }

    public ArrayList<Agent> getListOfAgentsInside() {
        return listOfAgentsInside;
    }

    public double getGas() {
        return 50;
    }

    public boolean isOnLockdown() {
        return isOnLockdown;
    }
}

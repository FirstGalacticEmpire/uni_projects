package pandemic.threadControllers;

import pandemic.map.Position;
import pandemic.shops.RetailShop;
import pandemic.shops.Shop;
import pandemic.main.Util;
import pandemic.shops.WholesaleShop;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ShopThreadController {
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
    private final ArrayList<WholesaleShop> listOfWholeSaleShops = new ArrayList<>();
    private final ArrayList<RetailShop> listOfRetailShops = new ArrayList<>();

    /**
     * Creates wholeSaleShop (factory).
     * @param shopPosition what position is the shop to be created in
     * @return created shop
     */
    public Shop createWholesaleShop(Position shopPosition) {
        WholesaleShop shop = new WholesaleShop(shopPosition);
        this.listOfWholeSaleShops.add(shop);
        return shop;
    }

    /**
     * Creates RetailShop (factory).
     * @param shopPosition what position is the shop to be created in
     * @return created shop
     */
    public Shop createRetailShop(Position shopPosition) {
        RetailShop shop = new RetailShop(shopPosition);
        this.listOfRetailShops.add(shop);
        return shop;
    }

    /**
     * Schedules the shop to remove expired products every 15 seconds, after 10 seconds of initial delays.
     * Schedules production of products every 1-3 second.
     */
    public void engageProduction() {
        for (WholesaleShop shop : this.listOfWholeSaleShops) {
            scheduledExecutorService.scheduleAtFixedRate(shop::CreateProduct, 0,
                    Util.generateRandomNum(1, 3), TimeUnit.SECONDS);
            scheduledExecutorService.scheduleAtFixedRate(shop::removeExpiredProducts, 10, 15,
                    TimeUnit.SECONDS);
        }
        for (RetailShop shop : this.listOfRetailShops) {
            scheduledExecutorService.scheduleAtFixedRate(shop::removeExpiredProducts, 10, 15,
                    TimeUnit.SECONDS);
        }
    }
}

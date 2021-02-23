package pandemic.agents;

import pandemic.map.Map;
import pandemic.map.Position;
import pandemic.shops.Product;
import pandemic.shops.RetailShop;
import pandemic.shops.Shop;
import pandemic.main.Util;
import pandemic.pathFinding.PathFinder;
import pandemic.threadControllers.ThreadController;

import java.util.ArrayList;


/**
 * Client class - entity client. Simulates an Client.
 */
public class Client extends Agent {
    private final int maxCartCapacity = 3;
    private final ArrayList<Product> cartOfProducts = new ArrayList<>(maxCartCapacity);
    private boolean hasBoughtProducts = false;


    /**
     * Constructor for client.
     * @param xCord
     * @param yCord
     * @param clientLock
     * @param map
     * @param threadController
     */
    public Client(int xCord, int yCord, Object clientLock, Map map, ThreadController threadController)  {
        super(0, xCord, yCord, clientLock, map, threadController);
        this.setPathFinder(new PathFinder(map.listOfTeleports, 0));
    }

    @Override
    protected void findRoute() {
        this.setCurrentRoute(this.getPathFinder().getRoute(map.MapArray, new Position(this.getxCord(), this.getyCord()),
                this.getGoalPosition(), 0, 0));
        if (this.getCurrentRoute().isEmpty()) {
            //route is empty
        }
    }


    /**
     * Selects next goal for a client (selects random retail shop)
     */
    @Override
    public void selectNextGoal() {
        this.setGoalPosition(map.getRandomRetailShopPosition());
        this.setWasGoalChosen(true);
        this.setGoalShop(map.MapArray.get(this.getGoalPosition().getX()).get(this.getGoalPosition().getY()).getShop());
    }

    private void buyProducts() {
        Shop shop = this.getGoalShop();
        ArrayList<Product> productsToBeBought = shop.takeProductsFromStorage(
                Util.generateRandomNum(1, this.maxCartCapacity));
        for (Product product : productsToBeBought) {
            if (this.cartOfProducts.size() + 1 > this.maxCartCapacity) {
                this.cartOfProducts.remove(0);
            }
            this.cartOfProducts.add(product);
        }
    }

    @Override
    protected void insideShopShenanigans() {
        //sanity check
        if (this.getGoalShop() instanceof RetailShop) {
            this.buyProducts();
        }
    }

    /**
     * @return Client's description (string).
     */
    @Override
    public String toString() {
        String string  = super.toString();

        return string +
                "cartOfProducts=" + cartOfProducts +
                '}';
    }
}

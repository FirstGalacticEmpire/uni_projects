package pandemic.agents;

import pandemic.map.Map;
import pandemic.map.Position;
import pandemic.shops.RetailShop;
import pandemic.map.Tile;
import pandemic.main.Util;
import pandemic.pathFinding.PathFinder;
import pandemic.threadControllers.ThreadController;

import java.util.ArrayList;

/**
 * Entity supplier, inherits from agent, simulates an supplier.
 */
public class Supplier extends Agent {
    private boolean unloadedAllProducts = false;
    private final ArrayList<Position> routePlan = new ArrayList<>();
    private int routeCounter = 0;
    private final Car car;

    /**
     * Constructor for supplier.
     * @param xCord
     * @param yCord
     * @param supplierLock
     * @param map
     * @param threadController
     */
    public Supplier(int xCord, int yCord, Object supplierLock, Map map, ThreadController threadController) {
        super(1, xCord, yCord, supplierLock, map, threadController);
        this.generateRandomRoutePlan();
        this.car = new Car();
        this.setPathFinder(new PathFinder(map.listOfTeleports, 1));
    }

    /**
     * Selects next goal for a supplier. (Takes shop from generated route).
     */
    @Override
    public void selectNextGoal() {
        if (routeCounter == routePlan.size() - 1) {
            routeCounter = 0;
        }
        this.setGoalPosition(routePlan.get(routeCounter));
        routeCounter += 1;

        this.setGoalShop(map.MapArray.get(this.getGoalPosition().getX()).get(this.getGoalPosition().getY()).getShop());
    }

    @Override
    protected  void insideShopShenanigans() {
        this.car.fillTank(getGoalShop().getGas());

        if (this.getGoalShop() instanceof RetailShop) {
            if (car.unloadProductsToShop(this.getGoalShop())) {
                this.unloadedAllProducts = true;
            }

        } else {
            car.loadProductsToTrunk(this.getGoalShop().takeProductsFromStorage(car.availableSpaceInTrunk()));
            this.unloadedAllProducts = true;
        }
    }

    /**
     * Burns gas in car.
     */
    @Override
    public void additionalFunctionsWhileMoving() {
        this.car.burnGas();
    }

    @Override
    protected void findRoute(){
        if (this.getGoalShop() instanceof RetailShop) {
            this.setCurrentRoute(this.getPathFinder().getRoute(map.MapArray, new Position(this.getxCord(), this.getyCord()),
                    this.getGoalPosition(), 1, 0));
        } else {
            this.setCurrentRoute(this.getPathFinder().getRoute(map.MapArray, new Position(this.getxCord(), this.getyCord()),
                    this.getGoalPosition(), 1, 1));
        }
        if (this.getCurrentRoute().isEmpty()) {
            System.out.println("Empty route");
        }
        //System.out.println(this.getCurrentRoute());
    }


    /**
     * Generates random route plan. Alternately pick a wholeSaleShop (factory) and retail shop.
     */
    @Override
    public void generateRandomRoutePlan() {
//        System.out.println("Generating new route!");
        this.routeCounter = 0;
        ArrayList<Tile> listOfShopsPositions = new ArrayList<>(map.getListOfRetailShopPositions());
        ArrayList<Tile> listOfFactoriesPositions = new ArrayList<>(map.getListOfWholeSaleShopPositions());
        do {
            try {
                this.routePlan.add(listOfFactoriesPositions.remove(Util.generateRandomNum(0,
                        listOfFactoriesPositions.size())).convertToPosition());
            } catch (IndexOutOfBoundsException ignored) {
            }
            try {
                this.routePlan.add(listOfShopsPositions.remove(Util.generateRandomNum(0,
                        listOfShopsPositions.size())).convertToPosition());
            } catch (IndexOutOfBoundsException ignored) {
            }

        } while (!listOfFactoriesPositions.isEmpty() && !listOfShopsPositions.isEmpty());

    }

    /**
     * @return Client's description (string).
     */
    @Override
    public String toString() {
        String string  = super.toString();

        return string +
                "Car's trunk=" + this.car.getTrunk() +
                '}';
    }
}

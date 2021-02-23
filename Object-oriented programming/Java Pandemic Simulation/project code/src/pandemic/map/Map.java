package pandemic.map;

import pandemic.agents.Agent;
import pandemic.shops.Shop;
import pandemic.controls.Observable;
import pandemic.threadControllers.ShopThreadController;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Creates map from loaded json file. Handles the map.
 */
public class Map {
    public final Object lock;
    private final int xDimension;
    private final int yDimension;
    public final ArrayList<ArrayList<Tile>> MapArray;
    public final ArrayList<Tile> listOfTeleports = new ArrayList<>();
    private final ArrayList<Tile> listOfRetailShopPositions = new ArrayList<>();
    private final ArrayList<Tile> listOfWholeSaleShopPositions = new ArrayList<>();

    /**
     * Map constructor. Loads map from the file, then creates it. Handles shop creation.
     * @param lock
     * @param xDimension
     * @param yDimension
     * @param shopThreadController
     */
    public Map(Object lock, int xDimension, int yDimension, ShopThreadController shopThreadController, String pathToMapFile) {
        ArrayList<Long> mapSchema = new JsonLoader().loadMap(pathToMapFile);

        this.xDimension = xDimension;
        this.yDimension = yDimension;
        MapArray = new ArrayList<>(xDimension * yDimension);
        for (int x = 0; x < this.xDimension; x++) {
            MapArray.add(new ArrayList<>());
            for (int y = 0; y < this.yDimension; y++) {

                int mapTileValue = Math.toIntExact(mapSchema.get(y * this.yDimension + x));
                Observable observable = Observable.getInstance();
                Shop shop;
                Tile tile;
                switch (mapTileValue) {
                    case 1 -> MapArray.get(x).add(new Tile(x, y, 0));
                    case 2 -> MapArray.get(x).add(new Tile(x, y, 1));
                    case 3 -> MapArray.get(x).add(new Tile(x, y, 2));
                    case 4 -> {
                        shop = shopThreadController.createRetailShop(new Position(x, y));
                        tile = new Tile(x, y, 3, shop);
                        MapArray.get(x).add(tile);
                        this.listOfRetailShopPositions.add(tile);
                        observable.addObserver(shop);
                    }
                    case 5 -> {
                        shop = shopThreadController.createWholesaleShop(new Position(x, y));
                        tile = new Tile(x, y, 4, shop);
                        MapArray.get(x).add(tile);
                        this.listOfWholeSaleShopPositions.add(tile);
                        observable.addObserver(shop);
                    }
                    case 6 -> MapArray.get(x).add(new Tile(x, y, 5));
                    default -> MapArray.get(x).add(new Tile(x, y, 99));
                }
            }
        }

        this.lock = lock;
    }

    /**
     * @return position of random retail shop.
     */
    public Position getRandomRetailShopPosition() {
        int randomNum = ThreadLocalRandom.current().nextInt(0, listOfRetailShopPositions.size());
        return listOfRetailShopPositions.get(randomNum).convertToPosition();
    }

    /**
     * Makes a tile and it's goal tile into a teleport.
     * @param x cord of first teleport
     * @param y cord of first teleport
     * @param x1 cord of goto teleport
     * @param y1 cord of goto teleport
     */
    public void setTileTeleport(int x, int y, int x1, int y1) {
        this.MapArray.get(x).get(y).setTeleport(true);
        this.MapArray.get(x).get(y).setGoalTeleportTile(this.MapArray.get(x1).get(y1));
        listOfTeleports.add(this.MapArray.get(x).get(y));

        this.MapArray.get(x1).get(y1).setTeleport(true);
        this.MapArray.get(x1).get(y1).setGoalTeleportTile(this.MapArray.get(x).get(y));
        listOfTeleports.add(this.MapArray.get(x1).get(y1));
    }

    /**
     * Looks for agents on adjacent tiles.
     * @param tileType type of tile, on which to look
     * @param adjacentTo position of tile for which adjacent tiles will be found
     * @return list of agents on adjacent occupied tiles.
     */
    public synchronized ArrayList<Agent> getListOfAgentsOnAdjacentOccupiedTiles(int tileType, Position adjacentTo) {
        ArrayList<Tile> tileList = adjacentTiles(tileType, adjacentTo);
        ArrayList<Agent> agentList = new ArrayList<>(4);
        for (Tile tile : tileList) {
            if (tile.isOccupied() && !tile.isTeleport()) {
                if (tile.getAgent() != null) {
                    agentList.add(tile.getAgent());
                }
            }
        }
        return agentList;
    }

    /**
     * Looks for adjacent unoccupied positions.
     * @param tileType type of tile, on which to look
     * @param adjacentTo position of tile for which adjacent tiles will be found
     * @return list of adjacent unoccupied positions.
     */
    public synchronized ArrayList<Position> getListOfAdjacentUnOccupiedPositions(int tileType, Position adjacentTo) {
        ArrayList<Tile> tileList = adjacentTiles(tileType, adjacentTo);
        ArrayList<Position> positionList = new ArrayList<>(4);
        for (Tile tile : tileList) {
            if (!tile.isOccupied() && !tile.isTeleport()) {
                positionList.add(tile.convertToPosition());
            }
        }
        return positionList;
    }


    private synchronized ArrayList<Tile> adjacentTiles(int tileType, Position adjacentTo) {
        ArrayList<Tile> tileList = new ArrayList<>(4);
        int x = adjacentTo.getX();
        int y = adjacentTo.getY();
        Tile currentTile = this.MapArray.get(x).get(y);
        synchronized (lock) {
            if (!currentTile.isTeleport()) {
                try {
                    Tile tile = this.MapArray.get(x).get(y - 1);
                    if ((tile.getTileType() == tileType) || tile.isTeleport()) {
                        //System.out.println(x + " " + (y - 1));
                        tileList.add(tile);
                    }

                } catch (IndexOutOfBoundsException ignored) {
                }
                try {
                    Tile tile = this.MapArray.get(x).get(y + 1);
                    if ((tile.getTileType() == tileType) || tile.isTeleport()) {
                        //System.out.println((x - 1) + " " + y);
                        tileList.add(tile);
                    }
                } catch (IndexOutOfBoundsException ignored) {
                }
                try {
                    Tile tile = this.MapArray.get(x - 1).get(y);
                    if ((tile.getTileType() == tileType) || tile.isTeleport()) {
                        //System.out.println(x + " " + (y + 1));
                        tileList.add(tile);
                    }
                } catch (IndexOutOfBoundsException ignored) {
                }
                try {
                    Tile tile = this.MapArray.get(x + 1).get(y);
                    if ((tile.getTileType() == tileType) || tile.isTeleport()) {
                        //System.out.println((x + 1) + " " + y);
                        tileList.add(tile);
                    }
                } catch (IndexOutOfBoundsException ignored) {
                }
            } else {
                tileList.add(currentTile.getGoalTeleportTile());
            }
        }
        return tileList;
    }


    /**
     * Return random empty position on map that is of a certain type.
     * @param tileType type of tile
     * @param isShop should include shops in search
     * @return random empty position
     */
    public Position getRandomEmptyPosition(int tileType, boolean isShop) {
        ArrayList<Position> emptyPositionList = new ArrayList<>();
        synchronized (lock) {
            for (ArrayList<Tile> tileArrayList : this.MapArray) {
                for (Tile tile : tileArrayList) {
                    if (isShop) {
                        if (tile.getTileType() == tileType) {
                            emptyPositionList.add(new Position(tile.getX(), tile.getY()));
                        }
                    } else {
                        if (tile.getTileType() == tileType && !tile.isOccupied()) {
                            emptyPositionList.add(new Position(tile.getX(), tile.getY()));
                        }
                    }
                }
            }
        }
        if (!emptyPositionList.isEmpty()) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, emptyPositionList.size());
            return emptyPositionList.get(randomNum);
        } else {
            return null;
        }
    }


//    public static void main(String[] args) {
//        ShopThreadController shopThreadController = new ShopThreadController();
//        Map map = new Map(new Object(), 20, 20, shopThreadController);

//        map.setTileTeleport(2, 5, 5, 5);
//        map.setTileTeleport(2, 6, 5, 6);
//        map.setTileTeleport(2, 8, 5, 8);
//        map.setTileTeleport(2, 9, 5, 9);
//        map.setTileTeleport(12, 8, 15, 8);
//        map.setTileTeleport(12, 9, 15, 9);
//        map.setTileTeleport(8, 12, 8, 15);
//        map.setTileTeleport(9, 12, 9, 15);
//        map.setTileTeleport(16, 12, 16, 15);
//        map.setTileTeleport(17, 12, 17, 15);
//        map.setTileTeleport(17, 7, 10, 19);

//        PathFinder pathFinder = new PathFinder(map.listOfTeleports, 0);
//        System.out.println(map.getRandomAdjacentOfType(0, new Position(2, 16)));
//        ArrayList<Position> currentRoute = pathFinder.getRoute(map.MapArray, new Position(17, 9),
//                new Position(16, 16), 0, 0);
//        System.out.println(currentRoute);
//        Position xd = map.getClosestUnOccupiedPositionToTile(new Position(17, 9), new Position(2, 16), 0);
//        System.out.println(xd);
//    }


    public boolean isTeleport(int x, int y) {
        return MapArray.get(x).get(y).isTeleport();
    }

    public Tile getTeleportTile(int x, int y) {
        return MapArray.get(x).get(y).getGoalTeleportTile();
    }

    public boolean isOccupied(int x, int y) {
        return MapArray.get(x).get(y).isOccupied();
    }

    public void setOccupied(int x, int y, boolean val) {
        MapArray.get(x).get(y).setOccupied(val);
    }

    public void setAgent(int x, int y, Agent val) {
        MapArray.get(x).get(y).setAgent(val);
    }

    public int getxDimension() {
        return xDimension;
    }

    public int getyDimension() {
        return yDimension;
    }

    public ArrayList<Tile> getListOfRetailShopPositions() {
        return listOfRetailShopPositions;
    }

    public ArrayList<Tile> getListOfWholeSaleShopPositions() {
        return listOfWholeSaleShopPositions;
    }
}

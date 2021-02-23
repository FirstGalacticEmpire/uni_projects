package pandemic.map;

import pandemic.agents.Agent;
import pandemic.shops.Shop;

/**
 * Basic object building a map.
 */
public class Tile {
    private final int x;
    private final int y;
    // 0 - road for people, 1 - road for cars, 2 - blockage, 3 - shop,
    // 4 - factory, 5 - teleport.
    private final int tileType;
    private Agent agent;
    private final Shop shop;

    // only pathfinding
    private int score;
    private boolean isOpen;
    private Tile parent;
    // only pathfinding


    private boolean isOccupied;
    private final boolean isShop;
    private final boolean isFactory;

    //Teleporters
    private boolean isTeleport;
    private Tile goalTeleportTile;


    /**
     * Tile constructor
     * @param x
     * @param y
     */
    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.tileType = 0;

        this.score = 0;
        this.isOpen = true;
        this.parent = null;

        this.isOccupied = false;
        this.isTeleport = false;
        this.isShop = false;
        this.isFactory = false;
        this.shop = null;
        this.agent = null;
    }

    /**
     * Tile constructor
     * @param x
     * @param y
     * @param tileType
     */
    public Tile(int x, int y, int tileType) {
        this.x = x;
        this.y = y;
        this.tileType = tileType;

        this.score = 0;
        this.isOpen = true;
        this.parent = null;

        this.isOccupied = false;

        this.isShop = tileType == 3;
        this.isFactory = tileType == 4;
        this.shop = null;
        this.agent = null;
    }

    /**
     * Tile constructor including shop.
     * @param x
     * @param y
     * @param tileType
     * @param shop
     */
    public Tile(int x, int y, int tileType, Shop shop) {
        this.x = x;
        this.y = y;
        this.tileType = tileType;

        this.score = 0;
        this.isOpen = true;
        this.parent = null;

        this.isOccupied = false;

        this.isShop = tileType == 3;
        this.isFactory = tileType == 4;
        this.shop = shop;
        this.agent = null;
    }

    /**
     * @return tile description
     */
    @Override
    public String toString() {
        return "Tile{" +
                "x=" + x +
                ", y=" + y +
                ", tileType=" + tileType +
                ", score=" + score +
                ", isOccupied=" + isOccupied +
                '}';
    }

    public Position convertToPosition() {
        return new Position(this.x, this.y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getTileType() {
        return tileType;
    }

    public int getScore() {
        return score;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public Tile getParent() {
        return parent;
    }

    public void setParent(Tile parent) {
        this.parent = parent;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public boolean isTeleport() {
        return isTeleport;
    }

    public void setTeleport(boolean teleport) {
        isTeleport = teleport;
    }

    public Tile getGoalTeleportTile() {
        return goalTeleportTile;
    }

    public void setGoalTeleportTile(Tile goalTeleportTile) {
        this.goalTeleportTile = goalTeleportTile;
    }

    public boolean isShop() {
        return isShop;
    }

    public boolean isFactory() {
        return isFactory;
    }

    public Shop getShop() {
        return shop;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        if (!isShop || !isTeleport) {
            this.agent = agent;
        } else {
            this.agent = null;
        }
    }
}

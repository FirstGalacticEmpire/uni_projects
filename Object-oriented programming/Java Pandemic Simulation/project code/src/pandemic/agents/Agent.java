package pandemic.agents;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import pandemic.GUI.Graphics;
import pandemic.main.Util;
import pandemic.map.Map;
import pandemic.map.Position;
import pandemic.shops.RetailShop;
import pandemic.shops.Shop;
import pandemic.controls.EnumControlVariables;
import pandemic.controls.Observable;
import pandemic.map.Tile;
import pandemic.pathFinding.PathFinder;
import pandemic.threadControllers.ThreadController;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Agent class - template of an agent. Has an ability to move from shop to shop.
 */
public abstract class Agent extends Thread {
    private final Object lock;
    protected static Map map;
    private static ThreadController agentThreadController;
    private PathFinder pathFinder;
    private final int tileType;
    private int xCord;
    private int yCord;
    private int previousX;
    private int previousY;
    private boolean wasGoalChosen;
    private boolean wasGoalReached;
    private Position goalPosition;
    private boolean inShop;
    private Shop goalShop;
    private Shape shape;
    private final Graphics graphics = Graphics.getInstance();

    private boolean isSleeping;
    private ArrayList<Position> currentRoute;

    private boolean isVaccinated;
    private boolean isWearingMask;
    private boolean isInfected;
    private Paint domainColor;
    private int numOfVisitsInShopAfterInfection;
    private Tile goalTeleportTile;
    private boolean inTeleport;
    private int counter;
    private int howManyNonMoves;


    /**
     * Constructor for agent.
     * Set occupation on map on its tile to occupied.
     * @param tileType on what tile it travels
     * @param xCord
     * @param yCord
     * @param lock
     * @param map
     * @param threadController
     */
    public Agent(int tileType, int xCord, int yCord, Object lock, Map map, ThreadController threadController) {
        this.tileType = tileType;
        this.xCord = xCord;
        this.yCord = yCord;
        this.lock = lock;
        this.wasGoalChosen = false;
        Agent.map = map;
        this.wasGoalReached = false;
        this.inShop = false;
        Agent.agentThreadController = threadController;
        synchronized (this.lock) {
            map.MapArray.get(xCord).get(yCord).setOccupied(true);
            map.MapArray.get(xCord).get(yCord).setAgent(this);
        }

        this.isVaccinated = false;
        this.isWearingMask = false;

    }

    private void infectAgentsOnAdjacentTiles() {
        ArrayList<Agent> agentList;
        synchronized (lock) {
            agentList = map.getListOfAgentsOnAdjacentOccupiedTiles(this.tileType,
                    map.MapArray.get(this.xCord).get(this.yCord).convertToPosition());
        }
        for (Agent agent : agentList) {
            agent.tryBecomingInfected(this);
        }
    }

    private void infectAgentsInShop() {
        for (Agent agent : this.goalShop.getListOfAgentsInside()) {
            agent.tryBecomingInfected(this);
        }
    }

    private void tryBecomingInfected(Agent injector) {
        if (!isInfected) {
            EnumControlVariables enumControlVariables = EnumControlVariables.getInstance();
            double chanceToGetInfected;
            if (injector.isWearingMask()) {
                chanceToGetInfected = enumControlVariables.getDiseaseTransmissionRateWithMask() / 100;
            } else {
                //todo make map bigger, javadoc
                //todo beautify text window
                chanceToGetInfected = enumControlVariables.getDiseaseTransmissionRateWithOutMask() / 100;
            }
            if (isVaccinated) {
                chanceToGetInfected = Math.min(0,
                        chanceToGetInfected - enumControlVariables.getVaccineEffectiveness() / 100);
            }
            Random random = new Random();
            if (chanceToGetInfected <= random.nextFloat()) {
                becomeInfected();
            }

        }
    }


    /**
     * Makes selected agent infected. Changes it's color to red.
     */
    public void becomeInfected() {
        this.isInfected = true;
        this.numOfVisitsInShopAfterInfection = 0;
        this.shape.setFill(Color.RED);
        Observable.getInstance().addSickPerson();
    }

    private void tryStopBeingInfected() {
        if (isInfected) {
            this.numOfVisitsInShopAfterInfection += 1;
            if (this.numOfVisitsInShopAfterInfection >=
                    EnumControlVariables.getInstance().getNumOfVisitsBeforeRecovering()) {
                this.isInfected = false;
                this.shape.setFill(domainColor);
                Observable.getInstance().removeSickPerson();
            }
        }
    }


    private void leaveShop() {

        this.tryStopBeingInfected();

        //leaving the shop
        goalShop.leaveShop(this);
        this.setInShop(false);

        //so agent will try moving to another shop
        this.setWasGoalChosen(false);
        this.setWasGoalReached(false);
        if (isInfected) {
            this.shape.setFill(Color.RED);
        } else {
            this.shape.setFill(this.domainColor);
        }

    }

    private void enterShop() {
        if (this.getGoalShop() instanceof RetailShop) {
            this.shape.setFill(Color.YELLOW);
        } else {
            this.shape.setFill(Color.ORANGE);
        }

//        System.out.println("Entering shop");
        //infecting agents in shop
        if (isInfected) {
            this.infectAgentsInShop();
        }
        this.setInShop(true);
        setOccupationOnCurrentTile(false);
    }


    private synchronized void sleep(int howLong) throws InterruptedException {
        this.wait(howLong * 1000L);
    }

    private synchronized void sleep() throws InterruptedException {
        int randomNum = ThreadLocalRandom.current().nextInt(2, 4);
        sleep(1);
    }

    private void Sleep() throws InterruptedException {
        try {
            sleep();
        } catch (InterruptedException e) {
            setOccupationOnCurrentTile(false);
            throw e;
        }
    }


    private void setOccupationOnCurrentTile(boolean val) {
        synchronized (this.lock) {
            map.setOccupied(this.xCord, this.yCord, val);
        }
    }

    private void setAgentOnCurrentTile(Agent agent) {
        synchronized (this.lock) {
            map.setAgent(this.previousX, this.previousY, agent);
        }
    }

    private boolean checkIfCurrentTileIsATeleport() {
        synchronized (this.lock) {
            return map.isTeleport(this.getxCord(), this.getyCord());
        }
    }

    private void simpleStep(Position goToPosition, boolean teleportAnimation) {
        this.previousX = this.xCord;
        this.previousY = this.yCord;
        this.xCord = goToPosition.getX();
        this.yCord = goToPosition.getY();
        this.getGraphics().createPathTransitionForShape(this, teleportAnimation);
    }

    private void step(Position goToPosition, boolean teleportAnimation) {
        setOccupationOnCurrentTile(false);
        setAgentOnCurrentTile(null);
        simpleStep(goToPosition, teleportAnimation);
        setOccupationOnCurrentTile(true);
        setAgentOnCurrentTile(this);
    }


    private void wasGoalReachedCheck(Position goToPosition) {
        this.setWasGoalReached(goToPosition.getX() == this.goalPosition.getX() && goToPosition.getY() == this.goalPosition.getY());
    }


    private void moveOneTile(Position goToPosition, boolean teleportAnimation) {
        if (!inTeleport) {

            step(goToPosition, teleportAnimation);
            if (isInfected) {
                infectAgentsOnAdjacentTiles();
            }
            wasGoalReachedCheck(goToPosition);

            if (checkIfCurrentTileIsATeleport()) {
                this.goalTeleportTile = map.getTeleportTile(this.getxCord(), this.getyCord());
                this.goalTeleportTile.setOccupied(true);
                this.inTeleport = true;
                this.counter = 0;
            }


        } else {

            if (counter == 0) {
                goToPosition = this.getCurrentRoute().get(2);
            }
            if (counter == 1) {
                goToPosition = this.getCurrentRoute().get(3);
                if (map.isOccupied(goToPosition.getX(), goToPosition.getY())) {
                    return;
                }
            }

            simpleStep(goToPosition, teleportAnimation);

            this.counter += 1;
            if (this.counter == 2) {
                setOccupationOnCurrentTile(true);
                this.inTeleport = false;
                this.goalTeleportTile.setOccupied(false);
                this.goalTeleportTile.getGoalTeleportTile().setOccupied(false);
            }


        }


    }

    private synchronized void tryMoving() {
        if (!this.inTeleport) {
            findRoute();
        }

        if (this.getCurrentRoute().size() > 1) {
            Position goToPosition = this.getCurrentRoute().get(1);
            synchronized (this.lock) {
                if (!this.inTeleport) {
                    if (!map.isOccupied(goToPosition.getX(), goToPosition.getY())) {
                        moveOneTile(goToPosition, false);

                        //else: tile occupied, solving deadlock:
                    } else {
                        this.howManyNonMoves += 1;
                        ArrayList<Position> positionList;
                        if (this.howManyNonMoves == 3) {
                            positionList = map.getListOfAdjacentUnOccupiedPositions(tileType,
                                    map.MapArray.get(this.xCord).get(this.yCord).convertToPosition());
                            if (!positionList.isEmpty()) {
                                this.howManyNonMoves = 0;
                                if (positionList.size() == 1) {
                                    if (!map.isOccupied(positionList.get(0).getX(),
                                            positionList.get(0).getY())) {
                                        moveOneTile(positionList.get(0), false);
                                    }
                                } else {
                                    Position aPosition = positionList.get(Util.generateRandomNum(0,
                                            positionList.size()));
                                    if (!map.isOccupied(aPosition.getX(),
                                            goToPosition.getY())) {
                                        moveOneTile(aPosition, true);
                                    }
                                }

                            }
                        }
                        if (this.howManyNonMoves == 5) {
                            //teleports to random location
//                            Position randomEmptyPosition = map.getRandomEmptyPosition(0, false);
//                            if (randomEmptyPosition != null) {
//                                moveOneTile(randomEmptyPosition, true);
//                            }

                        }
                        if (this.howManyNonMoves == 8) {
                            if (this.tileType == 0) {
                                this.shape.setFill(Color.LIGHTGREEN);
                            } else if (this.tileType == 1) {
                                this.shape.setFill(Color.LIGHTBLUE);
                            }

                            this.interrupt();
                            //Zelda magic? This is very interesting
                            //prevents  java.lang.IllegalStateException: Not on FX application thread;
                            try{
                            Platform.runLater(()->Agent.agentThreadController.removeAgent(this));}
                            catch (IndexOutOfBoundsException ignored){ }
                            return;
                        }
                    }
                } else {
                    //inside teleport
                    moveOneTile(goToPosition, false);
                }
            }
            additionalFunctionsWhileMoving();
        } else {
            //most likely we have chosen the same shop to visit
            this.setWasGoalChosen(false);
            this.setWasGoalReached(false);

        }

    }

    /**
     * Generates random route plan.
     */
    public void generateRandomRoutePlan() {
    }

    /**
     * Overwrite with additional functionality to be performed while moving.
     */
    public void additionalFunctionsWhileMoving() {
    }

    /**
     * Finds a route with pathfinder.
     */
    abstract void findRoute();

    /**
     * Action in shop.
     */
    abstract void insideShopShenanigans();

    /**
     * Selects next goal for an agent.
     */
    abstract void selectNextGoal();

    private void dealWithTeleportsAfterDying() {
        synchronized (lock) {
            if (checkIfCurrentTileIsATeleport()) {
                this.goalTeleportTile = map.getTeleportTile(this.getxCord(), this.getyCord());
                this.goalTeleportTile.setOccupied(false);
                this.goalTeleportTile.getGoalTeleportTile().setOccupied(false);
            }
        }
    }

    /**
     * Main loop of an agent. (Moving on selected route, from shop to shop)
     * Breaks from the loop if interrupted.
     */
    @Override
    public void run() {
        while (true) {

            //sleeping
            try {
                Sleep();
            } catch (InterruptedException e) {
                //can't stop while being in shop
                dealWithTeleportsAfterDying();

                if (!this.inShop) {
                    break;
                }
            }

            //Checking if agent was deleted, can self-destruct only outside a shop
            if (this.isInterrupted() && !this.inShop) {
                dealWithTeleportsAfterDying();
                setOccupationOnCurrentTile(false);
                break;
            }

            //Checking if agent has a select goal
            if (!this.wasGoalChosen) {
                selectNextGoal();
                this.wasGoalChosen = true;
            }

            //moves only if simulation isn't paused
            if (!this.isSleeping) {
                //if not in shop, agents tries to move
                if (!this.inShop) {
                    if (!this.isWasGoalReached()) {
                        tryMoving();
                    } else {
                        //checking if the shop isn't full
                        if (this.goalShop.enterShop(this)) {
                            enterShop();
                        }
                    }
                    //if in shop we perform insideShopShenanigans
                } else {
                    try {
                        Sleep();
                    } catch (InterruptedException ignored) {
                        //we can ignore this, agent will stop working after he leaves the shop
                    }

                    //personalized for children
                    insideShopShenanigans();
//                    System.out.println("Leaving shop");
                    leaveShop();
                }
            }
        }

        if (this.tileType == 0) {
            this.shape.setFill(Color.LIGHTGREEN);
        } else if (this.tileType == 1) {
            this.shape.setFill(Color.LIGHTBLUE);
        }
    }

    /**
     * @return Agent's description (string).
     */
    @Override
    public String toString() {
        return "Agent{" +
                "xCord=" + xCord +
                ", yCord=" + yCord +
                ", wasGoalChosen=" + wasGoalChosen +
                ", wasGoalReached=" + wasGoalReached +
                ", goalPosition=" + goalPosition +
                ", isVaccinated=" + isVaccinated +
                ", isWearingMask=" + isWearingMask +
                ", isInfected=" + isInfected +
                ", howManyNonMoves=" + howManyNonMoves +
                ", ";
    }

//    @Override
//    public String toString() {
//        return "Agent{" +
//                "xCord=" + xCord +
//                ", yCord=" + yCord +
//                ", wasGoalReached=" + wasGoalReached +
//                ", goalPosition=" + goalPosition +
//                '}';
//    }

    public int getxCord() {
        return xCord;
    }

    public int getyCord() {
        return yCord;
    }

    public void setInfected(boolean infected) {
        isInfected = infected;
    }

    public int getPreviousX() {
        return previousX;
    }

    public int getPreviousY() {
        return previousY;
    }

    public void setSleeping(boolean sleeping) {
        isSleeping = sleeping;
    }

    public ArrayList<Position> getCurrentRoute() {
        return currentRoute;
    }

    public void setPathFinder(PathFinder pathFinder) {
        this.pathFinder = pathFinder;
    }

    public PathFinder getPathFinder() {
        return pathFinder;
    }

    public Object getLock() {
        return lock;
    }

    public static Map getMap() {
        return map;
    }

    public static void setMap(Map map) {
        Agent.map = map;
    }

    public boolean isWasGoalReached() {
        return wasGoalReached;
    }

    public void setWasGoalReached(boolean wasGoalReached) {
        this.wasGoalReached = wasGoalReached;
    }

    public Position getGoalPosition() {
        return goalPosition;
    }

    public void setCurrentRoute(ArrayList<Position> currentRoute) {
        this.currentRoute = currentRoute;
    }

    public void setWasGoalChosen(boolean wasGoalChosen) {
        this.wasGoalChosen = wasGoalChosen;
    }

    public void setGoalPosition(Position goalPosition) {
        this.goalPosition = goalPosition;
    }

    public void setInShop(boolean inShop) {
        this.inShop = inShop;
    }

    public Shop getGoalShop() {
        return goalShop;
    }

    public void setGoalShop(Shop goalShop) {
        this.goalShop = goalShop;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public void setVaccinated(boolean vaccinated) {
        isVaccinated = vaccinated;
    }

    public boolean isWearingMask() {
        return isWearingMask;
    }

    public void setWearingMask(boolean wearingMask) {
        isWearingMask = wearingMask;
    }

    public void setDomainColor(Paint domainColor) {
        this.domainColor = domainColor;
    }
}

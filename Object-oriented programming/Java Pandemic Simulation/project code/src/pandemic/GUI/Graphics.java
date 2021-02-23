package pandemic.GUI;

import javafx.animation.PathTransition;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import pandemic.agents.Agent;
import pandemic.map.Map;
import pandemic.map.Position;
import pandemic.shops.Shop;
import pandemic.map.Tile;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Class responsible for painting objects and animating them
 */
public class Graphics {
    private static Graphics instance;
    private final int tileSize;
    private final int xNumOfTiles;
    private final int yNumOfTiles;
    private final GraphicsContext graphicsContext;
    private final Canvas canvas;

    private final ArrayList<Circle> listOfCircles = new ArrayList<>();
    private final ArrayList<Rectangle> listOfRectangles = new ArrayList<>();
    private final Map map;


    /**
     * Graphics constructor.
     * @param map
     * @param tileSize
     */
    public Graphics(Map map, int tileSize) {
        this.map = map;
        this.tileSize = tileSize;
        this.xNumOfTiles = map.getxDimension();
        this.yNumOfTiles = map.getyDimension();
        this.canvas = new Canvas(this.xNumOfTiles * tileSize, this.yNumOfTiles * tileSize);
        this.graphicsContext = this.canvas.getGraphicsContext2D();
    }

    public static void setInstance(Graphics instance) {
        Graphics.instance = instance;
    }

    public static synchronized Graphics getInstance() {
        return instance;
    }

    private void setFillOfShape(Shape shape) {
        shape.setFill(Color.rgb(ThreadLocalRandom.current().nextInt(0, 255),
                ThreadLocalRandom.current().nextInt(0, 255),
                ThreadLocalRandom.current().nextInt(0, 255)));
    }

    /**
     * Creates rectangle for an agent.
     * @param agent - for which the rectangle is to be created
     * @return created rectangle
     */
    public Rectangle createRectangle(Agent agent) {
        Rectangle rectangle = new Rectangle();
        rectangle.setX((agent.getxCord() * (tileSize)) + ((tileSize) / 2.0));
        rectangle.setY((agent.getyCord() * (tileSize)) + ((tileSize) / 2.0));
        rectangle.setHeight(15);
        rectangle.setWidth(15);

        setFillOfShape(rectangle);
        InterfaceCreator.addAgentClickedEvent(agent, rectangle);
        SceneCreator.getMapRoot().getChildren().add(rectangle);
        listOfRectangles.add(rectangle);
        return rectangle;
    }

    /**
     * Creates circle for an agent.
     * @param agent - for which the circle is to be created
     * @return created circle
     */
    public Circle createCircle(Agent agent) {
        Circle circle = new Circle();
        circle.setCenterX((agent.getxCord() * tileSize) + (tileSize / 2.0));
        circle.setCenterY((agent.getyCord() * tileSize) + (tileSize / 2.0));
        circle.setRadius((tileSize / 2.0) - 2.0);

        setFillOfShape(circle);
        InterfaceCreator.addAgentClickedEvent(agent, circle);
        SceneCreator.getMapRoot().getChildren().add(circle);
        listOfCircles.add(circle);
        return circle;
    }

    private PathTransition createPathTransition(Agent agent, boolean animationTeleport) {
        Path path = new Path();
        MoveTo moveTo = new MoveTo((agent.getPreviousX() * tileSize) + (tileSize / 2.0), (agent.getPreviousY() * tileSize) + (tileSize / 2.0));
        path.getElements().add(moveTo);
        LineTo lineTo = new LineTo((agent.getxCord() * tileSize) + (tileSize / 2.0), (agent.getyCord() * tileSize) + (tileSize / 2.0));
        path.getElements().add(lineTo);
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(500));
        pathTransition.setPath(path);
        return pathTransition;
    }


    /**
     * @param agent Agent for which path-transition is created
     * @param animationTeleport added for special effects, currently not used.
     */
    public void createPathTransitionForShape(Agent agent, boolean animationTeleport) {
        PathTransition pathTransition = createPathTransition(agent, animationTeleport);
        pathTransition.setNode(agent.getShape());
        pathTransition.play();
    }


    /**
     * Paints the map for the first time.
     */
    public void paintMapFirstTime() {
        graphicsContext.setFill(Color.GRAY);
        graphicsContext.fillRect(0, 0, xNumOfTiles * tileSize, yNumOfTiles * tileSize);
        this.paintWholeMap();
    }

    /**
     * @param text label to be stylized
     */
    public static void styleText(Label text) {
        text.setTextAlignment(TextAlignment.CENTER);
        text.setTextAlignment(TextAlignment.JUSTIFY);
        text.setWrapText(true);
    }

    /**
     * @param text label to be stylized
     * @param startValue start value of label
     */
    public static void styleText(Label text, String startValue) {
        styleText(text);
        text.setText(startValue);
    }

    private Rectangle createRectangle(Paint paint, Tile tile) {
        Rectangle rectangle = new Rectangle();
        rectangle.setX((tile.getX() * tileSize));
        rectangle.setY((tile.getY() * tileSize));
        rectangle.setHeight(tileSize - 1);
        rectangle.setWidth(tileSize - 1);
        rectangle.setFill(paint);
        return rectangle;
    }

    private void createShopRectangle(Paint paint, Tile tile) {
        Rectangle rectangle = createRectangle(paint, tile);
        SceneCreator.getMapRoot().getChildren().add(rectangle);
        Shop shop = map.MapArray.get(tile.getX()).get(tile.getY()).getShop();
        InterfaceCreator.addShopEvent(rectangle, shop);
    }

    /**
     * Removes circle from the scene.
     * @param indexOf circle to be removed
     */
    public void removeCircle(int indexOf) {
        SceneCreator.getMapRoot().getChildren().remove(this.listOfCircles.remove(indexOf));
    }

    /**
     * Removes rectangle from the scene.
     * @param indexOf rectangle to be removed
     */
    public void removeRectangle(int indexOf) {
        SceneCreator.getMapRoot().getChildren().remove(this.listOfRectangles.remove(indexOf));
    }


    /**
     * Refreshes map.
     */
    public void refreshMap() {
        for (ArrayList<Tile> tiles : this.map.MapArray) {
            for (Tile tile : tiles) {
                int tileType = tile.getTileType();
                switch (tileType) {
                    case 0:
                        graphicsContext.setFill(Color.LIGHTGREEN);
                        graphicsContext.fillRect(tile.getX() * tileSize, tile.getY() * tileSize,
                                tileSize - 1, tileSize - 1);
                        break;
                    case 1:
                        graphicsContext.setFill(Color.LIGHTBLUE);
                        graphicsContext.fillRect(tile.getX() * tileSize, tile.getY() * tileSize,
                                tileSize - 1, tileSize - 1);
                        break;
                    case 5:
                        graphicsContext.setFill(Color.DARKBLUE);
                        graphicsContext.fillRect(tile.getX() * tileSize, tile.getY() * tileSize,
                                tileSize - 1, tileSize - 1);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * Paints whole map, including shops.
     */
    public void paintWholeMap() {
        for (ArrayList<Tile> tiles : this.map.MapArray) {
            for (Tile tile : tiles) {
                int tileType = tile.getTileType();
                switch (tileType) {
                    case 0:
                        graphicsContext.setFill(Color.LIGHTGREEN);
                        graphicsContext.fillRect(tile.getX() * tileSize, tile.getY() * tileSize,
                                tileSize - 1, tileSize - 1);
                        break;
                    case 1:
                        graphicsContext.setFill(Color.LIGHTBLUE);
                        graphicsContext.fillRect(tile.getX() * tileSize, tile.getY() * tileSize,
                                tileSize - 1, tileSize - 1);
                        break;
                    case 2:
                        graphicsContext.setFill(Color.BLACK);
                        graphicsContext.fillRect(tile.getX() * tileSize, tile.getY() * tileSize,
                                tileSize - 1, tileSize - 1);
                        break;
                    case 3:
                        createShopRectangle(Color.YELLOW, tile);
                        break;
                    case 4:
                        createShopRectangle(Color.ORANGE, tile);
                        break;
                    case 5:
                        graphicsContext.setFill(Color.DARKBLUE);
                        graphicsContext.fillRect(tile.getX() * tileSize, tile.getY() * tileSize,
                                tileSize - 1, tileSize - 1);
                        break;
                    default:
                        break;
                }
            }
        }
    }


    /**
     * Marks path for current agent.
     * Sets a map refresher 5 second later
     * @param agent for which effect is to be created
     */
    public void markPathForAgent(Agent agent) {
        try {
            this.paintWholeMap();
            graphicsContext.setFill(Color.LIGHTPINK);
            int counter = 0;
            for (Position position : agent.getCurrentRoute()) {
                if (counter == agent.getCurrentRoute().size() - 1) {
                    break;
                }
                if (counter > 1) {
                    graphicsContext.fillRect(position.getX() * tileSize,
                            position.getY() * tileSize, tileSize - 1, tileSize - 1);
                }

                counter += 1;
            }
            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.schedule(this::refreshMap, 5, TimeUnit.SECONDS);

        } catch (NullPointerException ignore) {
            //no agent is selected
        }
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getxNumOfTiles() {
        return xNumOfTiles;
    }

    public int getyNumOfTiles() {
        return yNumOfTiles;
    }

    public Canvas getCanvas() {
        return canvas;
    }

}

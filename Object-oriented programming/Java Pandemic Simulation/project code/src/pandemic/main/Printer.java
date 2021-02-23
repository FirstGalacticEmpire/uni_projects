package pandemic.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import pandemic.GUI.Graphics;
import pandemic.GUI.SceneCreator;
import pandemic.map.Map;
import pandemic.threadControllers.ClientThreadController;
import pandemic.threadControllers.ShopThreadController;
import pandemic.threadControllers.SupplierThreadController;

import java.time.LocalDateTime;


public class Printer extends Application {
    private final static Object lock = new Object();
    private static Map map;
    private static ClientThreadController clientThreadController;
    private static SupplierThreadController supplierThreadController;
    private static ShopThreadController shopThreadController;
    private static Graphics graphics;
    private static MediaPlayer mediaPlayer;

    /**
     * Creates client and supplier thread controllers.
     * Creates Graphics
     * Creates GUI
     * Plays animation
     * Plays music
     * Creates and shows scene.
     * @param stage created stage
     */
    public void start(Stage stage) {
        shopThreadController.engageProduction();
        graphics = new Graphics(map, 35);
        Graphics.setInstance(graphics);
        clientThreadController = new ClientThreadController(lock, map);
        supplierThreadController = new SupplierThreadController(lock, map);

        HBox mainRoot = SceneCreator.CreateMainGroup(clientThreadController, supplierThreadController);
        graphics.paintMapFirstTime();

        AnimationTimerExtended animationTimerExtended = createAnimationTimer(1);
        //animationTimerExtended.start();

        //mediaPlayer = Media.playMusic();

        int tileSize = graphics.getTileSize();
        Scene scene = new Scene(mainRoot, graphics.getxNumOfTiles() * tileSize * 1.35, graphics.getyNumOfTiles() * tileSize);
        stage.setScene(scene);
        stage.setTitle("Pandemic Simulation");
        stage.show();
    }

    /**
     * Instates map, creates shopThreadController.
     * Sets up teleports on map
     * Launches JavaFx.
     * @param args pathToMapFile
     */
    public static void main(String[] args) {
        shopThreadController = new ShopThreadController();
        map = new Map(lock, 25, 25, shopThreadController, args[0]);

        map.setTileTeleport(2, 3, 2, 6);
        map.setTileTeleport(3, 3, 3, 6);
        map.setTileTeleport(3, 10, 3, 13);
        map.setTileTeleport(4, 10, 4, 13);
        map.setTileTeleport(5, 8, 8, 8);
        map.setTileTeleport(5, 9, 8, 9);
        map.setTileTeleport(5, 17, 8, 17);
        map.setTileTeleport(5, 18, 8, 18);
        map.setTileTeleport(9, 3, 9, 6);
        map.setTileTeleport(10, 3, 10, 6);
        map.setTileTeleport(14, 8, 17, 8);
        map.setTileTeleport(14, 9, 17, 9);
        map.setTileTeleport(14, 18, 17, 18);
        map.setTileTeleport(14, 19, 17, 19);
        map.setTileTeleport(19, 3, 19, 6);
        map.setTileTeleport(20, 3, 20, 6);
        map.setTileTeleport(20, 10, 20, 13);
        map.setTileTeleport(24, 21, 21, 0);
        map.setTileTeleport(0, 15, 7, 0);

        launch(args);
    }


    /**
     * Animation tick, for later use. Currently not needed.
     */
    private static void tick() {
    }

    //Animation timer, for later use. Currently not needed.
    private static AnimationTimerExtended createAnimationTimer(int sleepS) {
        return new AnimationTimerExtended(sleepS) {
            @Override
            public void handle() {
                LocalDateTime currentTime = java.time.LocalDateTime.now();
                System.out.println(currentTime);
                tick();
            }

        };
    }
}

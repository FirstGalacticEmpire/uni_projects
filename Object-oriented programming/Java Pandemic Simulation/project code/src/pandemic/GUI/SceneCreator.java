package pandemic.GUI;

import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pandemic.threadControllers.ClientThreadController;
import pandemic.threadControllers.SupplierThreadController;


/**
 * Creates the main group for the main scene.
 */
public class SceneCreator {
    private static final HBox mainRoot = new HBox();
    private static final Group mapRoot = new Group();
    private static VBox leftMenu;

    /**
     * Creats mainRoot that contains elements of the gui
     * @param clientThreadController - controlling clients
     * @param supplierThreadController controlling suppliers
     * @return mainRoot - main root that contains every element of GUI
     */
    public static HBox CreateMainGroup(ClientThreadController clientThreadController, SupplierThreadController supplierThreadController) {
        Graphics graphics = Graphics.getInstance();
        mapRoot.getChildren().add(graphics.getCanvas());
        mainRoot.getChildren().add(mapRoot);
        leftMenu = InterfaceCreator.CreateLeftMenu(clientThreadController, supplierThreadController);
        mainRoot.getChildren().add(leftMenu);
        return mainRoot;
    }


    public static Group getMapRoot() {
        return mapRoot;
    }

}

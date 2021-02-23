package pandemic.GUI;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import pandemic.agents.Agent;
import pandemic.controls.EnumControlVariables;
import pandemic.shops.Shop;
import pandemic.threadControllers.ClientThreadController;
import pandemic.threadControllers.SupplierThreadController;

import java.util.function.Consumer;


/**
 * Creates an interface.
 */
public class InterfaceCreator {
    private static final Label agentInformationText = new Label();
    private static final Label shopInformationText = new Label();
    private static Agent selectedAgent = null;


    /**
     * Creates left menu of GUI.
     * @param clientThreadController
     * @param supplierThreadController
     * @return VBox containing left menu of GUI
     */
    public static VBox CreateLeftMenu(ClientThreadController clientThreadController, SupplierThreadController supplierThreadController) {
        VBox leftMenu = new VBox();
        Graphics graphics = Graphics.getInstance();
        InterfaceCreator.addButtons(leftMenu, graphics.getxNumOfTiles(), graphics.getTileSize(),
                clientThreadController, supplierThreadController);
        InterfaceCreator.addSliders(leftMenu);
        leftMenu.getChildren().add(agentInformationText);
        leftMenu.getChildren().add(new Label("---------------------"));
        leftMenu.getChildren().add(shopInformationText);
        Graphics.styleText(agentInformationText, "Agent");
        Graphics.styleText(shopInformationText, "Shop");
        return leftMenu;
    }


    /**
     * Creates a on click event, that prints information about the shop.
     * @param rectangle map rectangle containing shop
     * @param shop for which event is to be created
     */
    public static void addShopEvent(Rectangle rectangle, Shop shop) {
        EventHandler<MouseEvent> eventHandler = e -> shopInformationText.setText(shop.toString());
        rectangle.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    }

    /**
     * Creates on click event that marks path of selected agent and
     * prints information about selected agent.
     * @param agent for which event is to be created
     * @param shape for which effect is to be added.
     */
    public static void addAgentClickedEvent(Agent agent, Shape shape) {
        Graphics graphics = Graphics.getInstance();
        EventHandler<MouseEvent> eventHandler = e -> {
            agentInformationText.setText(agent.toString());
            selectedAgent = agent;
            graphics.markPathForAgent(agent);
        };
        shape.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    }


    private static Button createButton(String text, Consumer<Void> someFunction, double minWidth) {
        Button button = new Button(text);
        button.setMinWidth(minWidth);
        EventHandler<MouseEvent> eventHandler = e -> someFunction.accept(null);
        button.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        return button;
    }

    private static Button createButton(String text, EventHandler<MouseEvent> eventHandler, double minWidth) {
        Button button = new Button(text);
        button.setMinWidth(minWidth);
        button.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        return button;
    }

    private static Button createButton(String text, Consumer<Void> firstFunction, Consumer<Void> secondFunction, double minWidth) {
        Button button = new Button(text);
        button.setMinWidth(minWidth);
        EventHandler<MouseEvent> eventHandler = e -> {
            firstFunction.accept(null);
            secondFunction.accept(null);
        };
        button.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        return button;
    }

    private static Slider createSlider(double startValue, Consumer<Double> someFunction, int min,
                                       int max) {
        Slider slider = new Slider();
        slider.setMin(min);
        slider.setMax(max);
        slider.setValue(startValue);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(50);
        slider.setMinorTickCount(5);
        slider.setBlockIncrement(10);

        slider.valueProperty().addListener((ov, old_val, new_val) -> {
            someFunction.accept(new_val.doubleValue());
        });
        return slider;
    }

    private static void createSliderWithDescription(VBox vBox, String description, double startValue,
                                                    Consumer<Double> someFunction, int min, int max) {
        Label label = new Label(description);
        Graphics.styleText(label);
        Slider slider = createSlider(startValue,
                someFunction, min, max);
        vBox.getChildren().add(label);
        vBox.getChildren().add(slider);
    }

    private static void createSliderWithDescription(VBox vBox, String description, double startValue,
                                                    Consumer<Double> someFunction) {
        createSliderWithDescription(vBox, description, startValue, someFunction, 1, 100);
    }




    private static void createRowOfButtons(VBox vBox, String firstString, String secondString,
                                           Consumer<Void> firstFunction, Consumer<Void> secondFunction,
                                           double minWidth) {
        HBox rowOfButtons = new HBox();
        Button addClient = createButton(firstString, firstFunction,
                minWidth);
        Button removeClient = createButton(secondString, secondFunction,
                minWidth);
        rowOfButtons.getChildren().add(addClient);
        rowOfButtons.getChildren().add(removeClient);
        vBox.getChildren().add(rowOfButtons);

    }


    private static void addButtons(VBox vBox, int xNumOfTiles, int tileSize,
                                   ClientThreadController clientThreadController,
                                   SupplierThreadController supplierThreadController) {

        double minWidth = xNumOfTiles * tileSize * 0.35 / 2;

        HBox differentRowOfButtons = new HBox();
        Button startButton = createButton("Start", clientThreadController::resumeAgents,
                supplierThreadController::resumeAgents,
                minWidth);
        Button stopButton = createButton("Stop", clientThreadController::pauseAgents,
                supplierThreadController::pauseAgents,
                minWidth);
        differentRowOfButtons.getChildren().add(startButton);
        differentRowOfButtons.getChildren().add(stopButton);
        vBox.getChildren().add(differentRowOfButtons);


        createRowOfButtons(vBox, "Add Client", "Remove Client", clientThreadController::addAgent,
                clientThreadController::removeSelectedAgent, minWidth);

        createRowOfButtons(vBox, "Add Supplier", "Remove Supplier", supplierThreadController::addAgent,
                supplierThreadController::removeSelectedAgent, minWidth);

        EventHandler<MouseEvent> eventHandler = e -> {
            try {
                selectedAgent.generateRandomRoutePlan();
            } catch (NullPointerException ignored) {
            }
        };
        Button changeRoute = createButton("Change route", eventHandler, minWidth * 2);
        vBox.getChildren().add(changeRoute);


    }

    private static void addSliders(VBox vBox) {
        EnumControlVariables controlVariables = EnumControlVariables.getInstance();
        createSliderWithDescription(vBox, "DiseaseTransmissionRate without mask",
                controlVariables.getDiseaseTransmissionRateWithOutMask(),
                controlVariables::setDiseaseTransmissionRateWithOutMask);

        createSliderWithDescription(vBox, "DiseaseTransmissionRate with mask",
                controlVariables.getDiseaseTransmissionRateWithMask(),
                controlVariables::setDiseaseTransmissionRateWithMask);

        createSliderWithDescription(vBox, "Lockdown threshold",
                controlVariables.getLockdownThreshold(),
                controlVariables::setLockdownThreshold);

        createSliderWithDescription(vBox, "Num of visits before recovery",
                controlVariables.getNumOfVisitsBeforeRecovering(),
                controlVariables::setNumOfVisitsBeforeRecovering, 1, 10);

        createSliderWithDescription(vBox, "Effectiveness of vaccine",
                controlVariables.getVaccineEffectiveness(),
                controlVariables::setVaccineEffectiveness);

        createSliderWithDescription(vBox, "% of people wearing mask",
                controlVariables.getPercentageOfAgentsWithMask(),
                controlVariables::setPercentageOfAgentsWithMask);

        createSliderWithDescription(vBox, "% of people vaccinated",
                controlVariables.getPercentageOfAgentsVaccinated(),
                controlVariables::setPercentageOfAgentsVaccinated);

        createSliderWithDescription(vBox, "Chance to be sick on creation",
                controlVariables.getChanceOnCreationToBeInfected(),
                controlVariables::setChanceOnCreationToBeInfected);

    }

    /**
     * @param selectedAgent is set as new selected agent.
     */
    public static void setSelectedAgent(Agent selectedAgent) {
        InterfaceCreator.selectedAgent = selectedAgent;
    }

    public static Agent getSelectedAgent() {
        return selectedAgent;
    }
}

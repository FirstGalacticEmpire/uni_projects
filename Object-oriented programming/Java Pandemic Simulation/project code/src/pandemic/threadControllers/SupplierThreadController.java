package pandemic.threadControllers;

import javafx.scene.shape.Rectangle;
import pandemic.agents.Agent;
import pandemic.agents.Supplier;
import pandemic.controls.EnumControlVariables;
import pandemic.GUI.Graphics;
import pandemic.GUI.InterfaceCreator;
import pandemic.map.Map;
import pandemic.map.Position;
import pandemic.controls.Observable;

public class SupplierThreadController extends ThreadController {
    /**
     * Supplier thread controller constructor.
     * @param lock
     * @param map
     */
    public SupplierThreadController(Object lock, Map map) {
        super(lock, map);
    }

    /**
     * Adds supplier to the simulation
     * @param unused needed to pass the function as an argument for method.
     */
    @Override
    public void addAgent(Void unused) {
        Supplier supplier;
        EnumControlVariables enumControlVariables = EnumControlVariables.getInstance();
        synchronized (this.getLock()) {
            Position randomEmptyPosition = map.getRandomEmptyPosition(1, false);
            supplier = new Supplier(randomEmptyPosition.getX(),
                    randomEmptyPosition.getY(), this.getLock(), map, this);
            if(enumControlVariables.isPaused()){
                supplier.setSleeping(true);
            }
            this.getListOfAgents().add(supplier);
            this.getThreadPool().execute(supplier);
            Graphics graphics = Graphics.getInstance();
            Rectangle rectangle = graphics.createRectangle(supplier);
            supplier.setShape(rectangle);
            supplier.setDomainColor(rectangle.getFill());
        }

        enumControlVariables.incrementNumOfAgents();
        setMasksAndVaccine(supplier, enumControlVariables);
        startInfection(supplier);

    }

    /**
     * Removes selected supplier from the simulation.
     * @param unused needed to pass the function as an argument for method.
     */
    @Override
    public void removeSelectedAgent(Void unused) {
        Agent selectedAgent = InterfaceCreator.getSelectedAgent();
        if(selectedAgent == null){
            return;
        }
        selectedAgent.interrupt();
        removeAgent(selectedAgent);
        InterfaceCreator.setSelectedAgent(null);
    }

    /**
     * Removes supplier from the simulation.
     * @param agent to be removed
     */
    @Override
    public void removeAgent(Agent agent) {
        Observable.getInstance().removeSickPerson();
        int indexOf = this.getListOfAgents().indexOf(agent);
        synchronized (this.getLock()) {
            this.getListOfAgents().remove(indexOf);
            Graphics graphics = Graphics.getInstance();
            graphics.removeRectangle(indexOf);
        }
        EnumControlVariables enumControlVariables = EnumControlVariables.getInstance();
        enumControlVariables.decrementNumOfAgents();
    }


}

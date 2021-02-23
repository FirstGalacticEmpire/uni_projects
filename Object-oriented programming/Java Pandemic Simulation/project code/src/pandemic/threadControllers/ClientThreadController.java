package pandemic.threadControllers;

import javafx.scene.shape.Circle;
import pandemic.agents.Agent;
import pandemic.agents.Client;
import pandemic.controls.EnumControlVariables;
import pandemic.GUI.Graphics;
import pandemic.GUI.InterfaceCreator;
import pandemic.map.Map;
import pandemic.map.Position;
import pandemic.controls.Observable;


public class ClientThreadController extends ThreadController {
    /**
     * Client thread controller constructor.
     * @param lock
     * @param map
     */
    public ClientThreadController(Object lock, Map map) {
        super(lock, map);
    }

    /**
     * Adds client to the simulation
     * @param unused needed to pass the function as an argument for method.
     */
    @Override
    public void addAgent(Void unused) {
        Client client;
        EnumControlVariables enumControlVariables = EnumControlVariables.getInstance();
        synchronized (this.getLock()) {
            Position randomEmptyPosition = map.getRandomEmptyPosition(0, false);
            client = new Client(randomEmptyPosition.getX(), randomEmptyPosition.getY(), this.getLock(), map, this);
            this.getListOfAgents().add(client);
            if(enumControlVariables.isPaused()){
                client.setSleeping(true);
            }
            this.getThreadPool().execute(client);
            Graphics graphics = Graphics.getInstance();
            Circle circle = graphics.createCircle(client);
            client.setShape(circle);
            client.setDomainColor(circle.getFill());
        }

        enumControlVariables.incrementNumOfAgents();
        setMasksAndVaccine(client, enumControlVariables);
        startInfection(client);

    }

    /**
     * Removes selected client from the simulation
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
     * Removes client from the simulation.
     * @param agent to be removed
     */
    @Override
    public void removeAgent(Agent agent) {
        Observable.getInstance().removeSickPerson();
        int indexOf = this.getListOfAgents().indexOf(agent);
        synchronized (this.getLock()) {
            this.getListOfAgents().remove(indexOf);
            Graphics graphics = Graphics.getInstance();
            graphics.removeCircle(indexOf);
        }
        EnumControlVariables enumControlVariables = EnumControlVariables.getInstance();
        enumControlVariables.decrementNumOfAgents();
    }


}

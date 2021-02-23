package pandemic.threadControllers;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pandemic.agents.Agent;
import pandemic.map.Map;
import pandemic.controls.EnumControlVariables;


public abstract class ThreadController {
    private final ExecutorService threadPool = Executors.newFixedThreadPool(100);
    private final Object lock;
    public static Map map;
    private final ArrayList<Agent> listOfAgents = new ArrayList<>();

    /**
     * ThreadController constructor.
     * @param lock
     * @param map
     */
    public ThreadController(Object lock, Map map) {
        this.lock = lock;
        ThreadController.map = map;
    }

    /**
     * Creates an agent.
     * @param unused needed to pass the function as an argument for method.
     */
    public abstract void addAgent(Void unused);

    /**
     * Removes currently selected agent
     * @param unused needed to pass the function as an argument for method.
     */
    public abstract void removeSelectedAgent(Void unused);

    /**
     * Removes agent from simulation.
     * @param agent to be removed
     */
    public abstract void removeAgent(Agent agent);

    /**
     * Resumes agents.
     * @param unused needed to pass the function as an argument for method.
     */
    public void resumeAgents(Void unused) {
        EnumControlVariables.getInstance().setPaused(false);
        for (Agent agent : listOfAgents) {
            agent.setSleeping(false);
        }
    }

    /**
     * Pauses agents.
     * @param unused needed to pass the function as an argument for method.
     */
    public void pauseAgents(Void unused) {
        EnumControlVariables.getInstance().setPaused(true);
        for (Agent agent : listOfAgents) {
            agent.setSleeping(true);
        }
    }

    protected static void startInfection(Agent agent) {
        Random random = new Random();
        if (EnumControlVariables.getInstance().getChanceOnCreationToBeInfected()/100 >= random.nextFloat()) {
            agent.becomeInfected();
        } else {
            agent.setInfected(false);
        }
    }

    protected void setMasksAndVaccine(Agent agent, EnumControlVariables enumControlVariables) {
        Random random = new Random();
        if (enumControlVariables.getPercentageOfAgentsVaccinated()/100 >= random.nextFloat()) {
            agent.setVaccinated(true);
        }
        if (enumControlVariables.getPercentageOfAgentsWithMask()/100 >= random.nextFloat()) {
            agent.setWearingMask(true);
        }
    }

    public Object getLock() {
        return this.lock;
    }

    public static Map getMap() {
        return map;
    }

    public ArrayList<Agent> getListOfAgents() {
        return listOfAgents;
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }
}

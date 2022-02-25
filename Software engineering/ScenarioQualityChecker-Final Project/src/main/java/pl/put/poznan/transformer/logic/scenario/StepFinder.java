package pl.put.poznan.transformer.logic.scenario;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to creating a list of Steps that satisfy a given condition defined in shouldAdd function.
 * By default all Steps are added to a list.
 */
public class StepFinder implements ScenarioElementVisitor{

    private final List<Step> matchedSteps;


    /**
     * Default constructor - initializes list of steps.
     */
    public StepFinder() {
        this.matchedSteps = new ArrayList<>();
    }

    /**
     * Normally used to inspect scenario. Here does nothing.
     * @param scenario currently visited scenario
     */
    @Override
    public void visit(Scenario scenario) {

    }

    /**
     * Called when Step is being visited. Checks if step should be added to a list.
     * @param step currently visited Step
     */
    @Override
    public void visit(Step step) {
        if(shouldAdd(step)) {
            matchedSteps.add(step);
        }
    }

    /**
     * Function that tells whether a given step should be added. By default all Steps are added.
     * If you want to impose a different rule override this method.
     * @param step currently visited step
     * @return whether a given step should be added to list
     */
    protected boolean shouldAdd(Step step){
        return true;
    }

    /**
     * @return List of matched steps/
     */
    public List<Step> getSteps(){
        return this.matchedSteps;
    }
}

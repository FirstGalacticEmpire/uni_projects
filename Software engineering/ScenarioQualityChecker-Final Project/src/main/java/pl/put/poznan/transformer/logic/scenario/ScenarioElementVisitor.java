package pl.put.poznan.transformer.logic.scenario;

/**
 * Interface for visitor that will visit ScenarioEl
 */

public interface ScenarioElementVisitor {
    /**
     * Method used to perform action on Scenario.
     * @param scenario currently visited scenario
     */
    void visit(Scenario scenario);

    /**
     * Method used to perform action in Step.
     * @param step currently visited step
     */
    void visit(Step step);
}

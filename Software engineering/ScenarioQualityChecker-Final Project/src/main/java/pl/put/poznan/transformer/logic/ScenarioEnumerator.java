package pl.put.poznan.transformer.logic;

import pl.put.poznan.transformer.logic.scenario.Scenario;
import pl.put.poznan.transformer.logic.scenario.Step;

import java.util.ArrayList;

/**
 * Enumerates steps in a Scenario
 */
public class ScenarioEnumerator {
    private final Scenario scenario;
    private final ArrayList<StepEnumerator> scenarioSteps = new ArrayList<>();

    /**
     * Constructor for ScenarioEnumerator
     * @param scenario to enumerate the steps
     */
    public ScenarioEnumerator(Scenario scenario) {
        this.scenario = scenario;
    }

    /**
     * Method that enumerates the scenario steps and creates ArrayList scenarioSteps containing StepEnumerator for each step
     */
    public void enumerateSteps() {
        int i = 1;
        for (Step step : scenario.getSteps()) {
            scenarioSteps.add(new StepEnumerator(step, i, null));
            i = i + 1;
        }
    }

    /**
     * Creates string representation of steps by concatenating strings generated for each step in ArrayList scenarioSteps
     */
    public String generateStepsAsAText() {
        StringBuilder stringBuilder = new StringBuilder();
        for (StepEnumerator stepEnumerator : scenarioSteps) {
            stringBuilder.append(stepEnumerator.toString());

        }
        return stringBuilder.toString();
    }

    /**
     * Creates string representation with JSON syntax of a steps
     * @param howDeep - integer number that specifies the depth of a steps.
     */
    public String generateJSONString(int howDeep) {
        if (howDeep > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            for (StepEnumerator stepEnumerator : scenarioSteps) {
                stringBuilder.append(stepEnumerator.toJsonString(howDeep - 1));
            }
            stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "");
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
        else {
            return "[]";
        }

    }

}

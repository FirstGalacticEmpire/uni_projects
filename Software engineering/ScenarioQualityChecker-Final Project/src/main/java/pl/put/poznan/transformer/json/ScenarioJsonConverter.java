package pl.put.poznan.transformer.json;

import pl.put.poznan.transformer.logic.scenario.Actor;
import pl.put.poznan.transformer.logic.scenario.ActorType;
import pl.put.poznan.transformer.logic.scenario.Scenario;
import pl.put.poznan.transformer.logic.scenario.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts an Scenario into a ScenarioJsonConverter
 */
public class ScenarioJsonConverter {
    private final Scenario scenario;

    /**
     * Constructor for ScenarioJsonConverter
     * @param scenario to convert into a json object
     */
    public ScenarioJsonConverter(Scenario scenario) {
        this.scenario = scenario;
    }

    /**
     * @return title of the scenario
     */
    public String getTitle() {
        return this.scenario.getTitle();
    }

    /**
     * @param actorType actor type of which actors should be provided
     * @return list of actors of type of actor type (converted into ActorJsonConverter)
     */
    public List<ActorJsonConverter> getActors(ActorType actorType) {
        List<ActorJsonConverter> actorJsonConverters = new ArrayList<>();
        for (Actor actor : this.scenario.getActors(actorType)) {
            actorJsonConverters.add(new ActorJsonConverter(actor));
        }
        return actorJsonConverters;
    }

    /**
     * @return all steps converted into StepJsonConverter
     */
    public ArrayList<StepJsonConverter> getSteps() {
        ArrayList<StepJsonConverter> stepJsonConverters = new ArrayList<>();
        for (Step step : this.scenario.getSteps()) {
            stepJsonConverters.add(new StepJsonConverter(step));
        }
        return stepJsonConverters;
    }

}

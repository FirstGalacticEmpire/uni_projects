package pl.put.poznan.transformer.logic;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import pl.put.poznan.transformer.json.JsonConverter;
import pl.put.poznan.transformer.logic.scenario.Scenario;

/**
 * Class responsible for cutting the Scenario to match a certain depth
 */
public class ScenarioDepthSelector {
    /**
     * Cuts the Scenario to match a certain depth
     * @param scenario
     * @param howDeep - integer number that specifies the depth of a cut.
     * @return JSONObject containing cut scenario
     */
    public JSONObject cutScenario(Scenario scenario, int howDeep) throws ParseException {
        ScenarioEnumerator scenarioEnumerator = new ScenarioEnumerator(scenario);
        scenarioEnumerator.enumerateSteps();
        String cutSteps = scenarioEnumerator.generateJSONString(howDeep);
        JsonConverter jsonConverter = new JsonConverter();
        return jsonConverter.exportJson(scenario, cutSteps);
    }

}

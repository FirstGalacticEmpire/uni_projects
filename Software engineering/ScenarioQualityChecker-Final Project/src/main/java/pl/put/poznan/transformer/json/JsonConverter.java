package pl.put.poznan.transformer.json;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pl.put.poznan.transformer.logic.scenario.ActorType;
import pl.put.poznan.transformer.logic.scenario.Scenario;

import java.util.Arrays;

/**
 * Converts an Scenario into a JSONObject
 */
public class JsonConverter {
    private ScenarioJsonConverter scenarioJsonConverter;
    /**
     * Creates JSONObject from a Scenario
     * @param scenario to convert into a json object
     * @param steps string with JSON syntax containing steps in the scenario
     * @return JSONObject
     */
    public JSONObject exportJson(Scenario scenario, String steps) throws ParseException {
        this.scenarioJsonConverter = new ScenarioJsonConverter(scenario);

        JSONObject jsonScenario = new JSONObject();
        JSONObject header = createHeader();
        jsonScenario.put("header", header);
        jsonScenario.put("steps", new JSONParser().parse(steps));
        return jsonScenario;
    }
    /**
     * Creates header for JSONObject containing title,actors and system actors
     * @return header
     */
    private JSONObject createHeader() throws ParseException {
        JSONObject header = new JSONObject();
        header.put("title", this.scenarioJsonConverter.getTitle());
        header.put("actors", new JSONParser().parse(Arrays.toString(this.scenarioJsonConverter.getActors(ActorType.EXTERNAL).toArray())));
        header.put("system_actors", new JSONParser().parse(Arrays.toString(this.scenarioJsonConverter.getActors(ActorType.SYSTEM).toArray())));
        return header;
    }

}

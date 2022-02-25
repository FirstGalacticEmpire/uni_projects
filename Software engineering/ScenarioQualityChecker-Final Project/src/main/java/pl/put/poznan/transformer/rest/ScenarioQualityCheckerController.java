package pl.put.poznan.transformer.rest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.transformer.json.JsonScenarioLoader;
import pl.put.poznan.transformer.json.StepJsonConverter;
import pl.put.poznan.transformer.logic.ScenarioDepthSelector;
import pl.put.poznan.transformer.logic.ScenarioEnumerator;
import pl.put.poznan.transformer.logic.scenario.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ScenarioQualityCheckerController {

    private static final Logger logger = LoggerFactory.getLogger(ScenarioQualityCheckerController.class);

    /**
     * @param jsonScenario the scenario that is sent inside the body of request to the endpoint.
     * @return ResponseEntity  with the number of keywords in the scenario inside response body.
     */
    @RequestMapping(value = "/keywords", method = RequestMethod.POST, produces = "application/json")
    ResponseEntity<JSONObject> getKeywordsCount(@RequestBody JSONObject jsonScenario) throws ParseException {
        logger.info("POST request /keywords");
        logger.debug("Processing JSON");
        String value = jsonScenario.toJSONString();
        Scenario scenario = new JsonScenarioLoader().loadJson((JSONObject) new JSONParser().parse(value));
        logger.debug("Got Scenario with title: "+scenario.getTitle());
        ScenarioElementKeywordCounter scenarioElementKeywordCounter = new ScenarioElementKeywordCounter();
        logger.debug("Running ScenarioElementKeywordCounter");
        scenario.accept(scenarioElementKeywordCounter);
        int keywordCount = scenarioElementKeywordCounter.getCount();
        logger.debug("Found: "+keywordCount+" steps");
        logger.debug("Creating JSON response.");
        JSONObject response = new JSONObject();
        response.put("keywordCount", keywordCount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * @param jsonScenario the scenario that is sent inside the body of request to the endpoint.
     * @return ResponseEntity with the number of steps in the scenario inside response body.
     */
    @RequestMapping(value = "/numberOfSteps", method = RequestMethod.POST, produces = "application/json")
    ResponseEntity<JSONObject> getNumberOfSteps(@RequestBody JSONObject jsonScenario) throws ParseException {
        logger.info("POST request /numberOfSteps");
        logger.debug("Processing JSON");
        String value = jsonScenario.toJSONString();
        Scenario scenario = new JsonScenarioLoader().loadJson((JSONObject) new JSONParser().parse(value));
        logger.debug("Got Scenario with title: "+scenario.getTitle());
        ScenarioElementCounter scenarioElementCounter = new ScenarioElementCounter();
        logger.debug("Running ScenarioElementCounter");
        scenario.accept(scenarioElementCounter);
        int size = scenarioElementCounter.getCount();
        logger.debug("Found: "+size+" steps");
        logger.debug("Creating JSON response.");
        JSONObject response = new JSONObject();
        response.put("numberOfSteps",size);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    /**
     * @param jsonScenario the scenario that is sent inside the body of request to the endpoint.
     * @return ResponseEntity with the array containing the steps with out actors.
     */
    @RequestMapping(value = "/stepsWithoutActors", method = RequestMethod.POST, produces = "application/json")
    ResponseEntity<JSONArray> returnStepsWithoutActors(@RequestBody JSONObject jsonScenario) throws ParseException {
        logger.info("POST request /numberOfSteps");
        logger.debug("Processing JSON");
        String value = jsonScenario.toJSONString();
        Scenario scenario = new JsonScenarioLoader().loadJson((JSONObject) new JSONParser().parse(value));
        logger.debug("Got Scenario with title: "+scenario.getTitle());
        StepsWithoutActorsFinder stepsWithoutActorsFinder = new StepsWithoutActorsFinder(scenario.getActorsWithOutTypes());
        logger.debug("Running StepsWithoutActorsFinder");
        scenario.accept(stepsWithoutActorsFinder);
        List<Step> stepsWithOutActors = stepsWithoutActorsFinder.getSteps();
        logger.debug("Found: "+stepsWithOutActors.size()+" steps");
        logger.debug("Creating JSON response.");
        List<StepJsonConverter> stepJsonConverters = new ArrayList<>();
        for (Step step : stepsWithOutActors) {
            stepJsonConverters.add(new StepJsonConverter(step));
        }
        JSONArray response = (JSONArray) new JSONParser().parse(stepJsonConverters.toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * @param jsonScenario the scenario that is sent inside the body of request to the endpoint.
     * @return ResponseEntity with the array containing the steps with out actors.
     */
    @RequestMapping(value = "/stepsWithGivenWord/{word}", method = RequestMethod.POST, produces = "application/json")
    ResponseEntity<JSONArray> returnStepsWithGivenWord(@RequestBody JSONObject jsonScenario, @PathVariable String word) throws ParseException {
        String value = jsonScenario.toJSONString();
        Scenario scenario = new JsonScenarioLoader().loadJson((JSONObject) new JSONParser().parse(value));
        StepsWithGivenWordFinder stepsWithGivenWordFinder = new StepsWithGivenWordFinder(word);
        scenario.accept(stepsWithGivenWordFinder);
        List<Step> stepsWithOutActors = stepsWithGivenWordFinder.getSteps();
        List<StepJsonConverter> stepJsonConverters = new ArrayList<>();
        for (Step step : stepsWithOutActors) {
            stepJsonConverters.add(new StepJsonConverter(step));
        }
        JSONArray response = (JSONArray) new JSONParser().parse(stepJsonConverters.toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * @param jsonScenario the scenario that is sent inside the body of request to the endpoint.
     * @return ResponseEntity with the enumerated steps in the form of txt.
     */
    @RequestMapping(value = "/enumeratedSteps", method = RequestMethod.POST, produces = "application/txt")
    ResponseEntity<String> getEnumeratedSteps(@RequestBody JSONObject jsonScenario) throws ParseException {
        logger.info("POST request /enumeratedSteps");
        logger.debug("Processing JSON");
        String value = jsonScenario.toJSONString();
        Scenario scenario = new JsonScenarioLoader().loadJson((JSONObject) new JSONParser().parse(value));
        logger.debug("Got Scenario with title: "+scenario.getTitle());
        String response;
        ScenarioEnumerator scenarioEnumerator = new ScenarioEnumerator(scenario);
        logger.debug("Running ScenarioEnumerator");
        scenarioEnumerator.enumerateSteps();
        logger.debug("Creating response");
        response = scenarioEnumerator.generateStepsAsAText();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * @param postedRequest the scenario that is sent inside the body of request to the endpoint.
     * @return ResponseEntity with the scenario containing steps up to selected level.
     */
    @RequestMapping(value = "/getStepsUpToDepth", method = RequestMethod.POST, produces = "application/json")
    ResponseEntity<JSONObject> getStepsUptoLevel(@RequestBody JSONObject postedRequest) throws ParseException {
        logger.info("POST request /enumeratedSteps");
        logger.debug("Processing JSON");
        String value = postedRequest.toJSONString();
        JSONObject parsedRequest = (JSONObject) new JSONParser().parse(value);
        Scenario scenario = new JsonScenarioLoader().loadJson((JSONObject) parsedRequest.get("scenario"));
        logger.debug("Got Scenario with title: "+scenario.getTitle());
        JSONObject response;
        ScenarioDepthSelector scenarioDepthSelector = new ScenarioDepthSelector();
        logger.debug("Running ScenarioEnumerator");
        response = scenarioDepthSelector.cutScenario(scenario, Integer.parseInt(parsedRequest.get("how_deep").toString()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Endpoint to check whether the API is working
     *
     * @return string "Alive"
     */
    @RequestMapping(method = RequestMethod.GET, produces = "text/plain", path = "/isAlive")
    public String isAlive() {
        return "Alive";
    }

}

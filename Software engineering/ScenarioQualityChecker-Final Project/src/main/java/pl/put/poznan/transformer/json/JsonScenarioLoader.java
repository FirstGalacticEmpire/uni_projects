package pl.put.poznan.transformer.json;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pl.put.poznan.transformer.logic.scenario.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


/**
 * Loads an json file and converts it into java scenario object.
 */
public class JsonScenarioLoader {
    private Scenario scenario;

    private static ArrayList<Actor> getArrayListOfActorsFromJson(JSONArray jsonArray, ActorType actorType) {
        ArrayList<Actor> arrayList = new ArrayList<>();
        if (jsonArray != null) {
            for (Object o : jsonArray) {
                arrayList.add(new Actor(o.toString(), actorType));
            }
        }
        return arrayList;
    }


    private void parseHeader(JSONObject header) {
        JSONArray actorsArray = (JSONArray) header.get("actors");
        ArrayList<Actor> externalActorsList = getArrayListOfActorsFromJson(actorsArray, ActorType.EXTERNAL);

        actorsArray = (JSONArray) header.get("system_actors");
        ArrayList<Actor> systemActorsList = getArrayListOfActorsFromJson(actorsArray, ActorType.SYSTEM);

        this.scenario = new Scenario((String) header.get("title"));
        scenario.addAllActors(externalActorsList);
        scenario.addAllActors(systemActorsList);
    }


    private static Step parseStep(JSONObject jsonStep) {
        String textString = (String) jsonStep.get("text");
        return new Step(textString);
    }

    private static ArrayList<Step> parseSubSteps(JSONArray jsonSubSteps) {
        ArrayList<Step> subSteps = new ArrayList<>();
        for (Object o : jsonSubSteps) {
            JSONObject jsonStep = (JSONObject) o;
            Step step = parseStep(jsonStep);
            JSONArray jsonSubSteps2 = (JSONArray) jsonStep.get("sub_steps");

            if (jsonSubSteps2 != null) {
                ArrayList<Step> subSteps1 = parseSubSteps(jsonSubSteps2);
                step.addAllSubSteps(subSteps1);
            }
            subSteps.add(step);
        }

        return subSteps;
    }

    private void parseSteps(JSONArray steps) {
        if (steps != null) {
            for (Object o : steps) {
                JSONObject jsonStep = (JSONObject) o;
                Step step = parseStep(jsonStep);

                JSONArray jsonSubSteps = (JSONArray) jsonStep.get("sub_steps");
                if (jsonSubSteps != null) {
                    ArrayList<Step> subSteps = parseSubSteps(jsonSubSteps);
                    step.addAllSubSteps(subSteps);
                }
                this.scenario.addStep(step);
            }

        }
    }

    public Scenario loadJson(JSONObject scenario) {
        parseHeader((JSONObject) scenario.get("header"));
        parseSteps((JSONArray) scenario.get("steps"));
        return this.scenario;
    }
}

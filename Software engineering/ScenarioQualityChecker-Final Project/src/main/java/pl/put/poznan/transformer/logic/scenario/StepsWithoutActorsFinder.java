package pl.put.poznan.transformer.logic.scenario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * StepsWithoutActorsFinder - visitor, which visits all Steps of a given Scenario.
 * If a given step does not start with any of actors names it is added to the list.
 * List can be then obtained using getSteps() method.
 */
public class StepsWithoutActorsFinder extends StepFinder{

    private final Pattern searchPattern;

    /**
     * Default constructor
     * @param actors list of Scenarios' actors
     */
    public StepsWithoutActorsFinder(List<Actor> actors) {
        super();
        this.searchPattern = getSearchPattern(actors);
    }

    /**
     * Return whether a Step starts with an actor name. It marks all Steps which do not have an actor.
     * @param step currently visited Step
     * @return true if does not start, false otherwise
     */
    @Override
    protected boolean shouldAdd(Step step){
        String text = step.getText();
        Matcher m = searchPattern.matcher(text);
        boolean containsActor = m.find();
        return !containsActor;
    }

    private Pattern getSearchPattern(List<Actor> actors){
        List<String> keywordStrings = Arrays.stream(Keywords.values()).map(Keywords::getKeyword).collect(Collectors.toList());
        String keywordPart = String.join(" |", keywordStrings)+" ";
        List<String> actorNames = actors.stream().map(Actor::getName).collect(Collectors.toList());
        String actorsPart = String.join("|", actorNames);
        return Pattern.compile("^("+keywordPart+")?("+actorsPart+")");
    }
}

package pl.put.poznan.transformer.logic.scenario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * StepsWithGivenWordFinder - visitor, which visits all Steps of a given Scenario.
 * If a given step contains a given keyword it is added to the list.
 */
public class StepsWithGivenWordFinder extends StepFinder{

    private final String givenWord;


    /**
     * Default constructor.
     * @param givenWord a word to match
     */
    public StepsWithGivenWordFinder(String givenWord) {
        super();
        this.givenWord = givenWord;
    }

    /**
     * Checks whether Step contains a given word. If it doeas it will be added to list.
     * @param step currently checked step
     * @return true if Step contains word
     */
    @Override
    protected boolean shouldAdd(Step step){
        String text = step.getText();
        String[] words = text.split("\\s+");
        return Arrays.asList(words).contains(givenWord);
    }
}

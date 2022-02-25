package pl.put.poznan.transformer.logic.scenario;

import java.util.Arrays;
import pl.put.poznan.transformer.logic.scenario.Step;
/**
 * Counts how many {@link Step} in {@link Scenario}nario begin with one of {@link Keywords}.
 */
public class ScenarioElementKeywordCounter extends ScenarioElementCounter{

    /**
     * Increases counter if {@link Step} starts with a {@link Keywords}
     * @param step currently visited Step
     */
    @Override
    public void visit(Step step) {
        String text = step.getText();
        boolean containsKeyword = Arrays.stream(Keywords.values()).map(Keywords::getKeyword)
                .anyMatch(keyword -> text.startsWith(keyword+" "));
        if(containsKeyword) {
            incrementCounter();
        }
    }
}

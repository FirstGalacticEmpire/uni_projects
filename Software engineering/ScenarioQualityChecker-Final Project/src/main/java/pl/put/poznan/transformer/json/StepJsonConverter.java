package pl.put.poznan.transformer.json;

import pl.put.poznan.transformer.logic.scenario.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts an Step into a StepJsonConverter
 */
public class StepJsonConverter {
    private final Step step;

    /**
     * @param step from which StepJsonConverter should be constructed
     */
    public StepJsonConverter(Step step) {
        this.step = step;
    }

    /**
     * @return string adjusted for json
     */
    @Override
    public String toString() {
        List<StepJsonConverter> subSteps;
        if (!this.step.getSubSteps().isEmpty()) {
            List<StepJsonConverter> stepJsonConverters = new ArrayList<>();
            for (Step step: this.step.getSubSteps()){
                stepJsonConverters.add(new StepJsonConverter(step));
            }

            subSteps = stepJsonConverters;
        } else {
            subSteps = null;
        }
        return "{" +
                '\"'+ "text\": " + '\"'+ this.step.getText() + '\"' +
                ", \"sub_steps\": " + subSteps +
                '}';

    }
}

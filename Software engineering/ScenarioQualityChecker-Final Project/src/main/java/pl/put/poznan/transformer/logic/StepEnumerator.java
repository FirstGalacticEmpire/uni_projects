package pl.put.poznan.transformer.logic;

import pl.put.poznan.transformer.logic.scenario.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Recursive class that enumerates steps in a scenario.
 */
public class StepEnumerator {
    private final Step step;
    private final int level;
    private final StepEnumerator parent;
    private final ArrayList<StepEnumerator> subEnumeratedSteps;

    /**
     * Function that add substeps to the parent step
     */
    private void parseSubSteps(List<Step> subSteps, StepEnumerator parent) {
        int i = 1;
        for (Step step : subSteps) {
            subEnumeratedSteps.add(new StepEnumerator(step, i, parent));
            i += 1;
        }
    }

    /**
     * Constructor with a recursive call, if number of substeps if different than zero it add substeps recursively
     */
    public StepEnumerator(Step step, int level, StepEnumerator parent) {
        this.level = level;
        this.step = step;
        this.subEnumeratedSteps = new ArrayList<>();
        if (!step.getSubSteps().isEmpty()) {
            parseSubSteps(step.getSubSteps(), this);
        }
        this.parent = parent;
    }

    /**
     * Recursively adds iteration to each substeps
     */
    private String getSubLevels(String stringLevel, StepEnumerator stepEnumerator) {
        if (stepEnumerator.parent != null) {
            stringLevel = stepEnumerator.parent.getLevel() + "." + stringLevel;
            return getSubLevels(stringLevel, stepEnumerator.parent);
        } else {
            return stringLevel;
        }
    }

    /**
     * Adds iteration to steps.
     */
    private String getLevels() {
        String stringLevel = getSubLevels(Integer.toString(level), this);
        stringLevel = stringLevel + ". ";
        return stringLevel;
    }

    /**
     * Generates string-like representation of scenario for substeps.
     */
    private void getSubSteps(StringBuilder subSteps, StepEnumerator parent, int howDeep) {
        if (parent.getSubEnumeratedSteps() != null) {
            for (StepEnumerator stepEnumerator : parent.getSubEnumeratedSteps()) {
                subSteps.append("\t".repeat(Math.max(0, howDeep)));
                subSteps.append(stepEnumerator.getLevels()).append(stepEnumerator.getStep().getText()).append("\n");
                if (!stepEnumerator.getSubEnumeratedSteps().isEmpty()) {
                    getSubSteps(subSteps, stepEnumerator, howDeep + 1);
                }
            }
        }
    }

    private void getSteps(StringBuilder subSteps, StepEnumerator stepEnumerator) {
        getSubSteps(subSteps, stepEnumerator, 1);
    }

    /**
     * Aggregates substeps strings
     */
    @Override
    public String toString() {
        StringBuilder subSteps = new StringBuilder();
        subSteps.append(getLevels()).append(this.step.getText()).append("\n");
        getSteps(subSteps, this);
        return subSteps.toString();
    }


    /**
     * Creates JSON-like string represenation of the data
     */
    private void getSubStepsJSON(StringBuilder subSteps, StepEnumerator parent, int howDeep) {
        if (howDeep != 0) {
            if (parent.getSubEnumeratedSteps() != null) {
                for (StepEnumerator stepEnumerator : parent.getSubEnumeratedSteps()) {
                    subSteps.append("{" + "\"text\":" + "\"" + stepEnumerator.getStep().getText() + "\"");
                    subSteps.append("," + "\n" + "\"sub_steps\": [");
                    if (!stepEnumerator.getSubEnumeratedSteps().isEmpty()) {
                        getSubStepsJSON(subSteps, stepEnumerator, howDeep - 1);
                        subSteps.replace(subSteps.length() - 2, subSteps.length(), "");
                    }
                    subSteps.append("]");
                    subSteps.append("}," + "\n");
                }
            }
        } else {
            subSteps.append("],");
        }


    }

    /**
     * Cuts string represenation of a scenario to a given depth.
     */
    private void getStepsJSON(StringBuilder subSteps, StepEnumerator stepEnumerator, int howDeep) {
        if (stepEnumerator.getSubEnumeratedSteps().isEmpty()) {
            subSteps.append("");
        } else {
            getSubStepsJSON(subSteps, stepEnumerator, howDeep);
            subSteps.replace(subSteps.length() - 2, subSteps.length(), "");
        }
    }

    /**
     * Returns JSON-like string scenario.
     */
    public String toJsonString(int howDeep) {
        StringBuilder subSteps = new StringBuilder();
        subSteps.append("{" + "\"text\":" + "\"" + this.step.getText() + "\"");
        subSteps.append("," + "\n" + "\"sub_steps\": [");
        if (howDeep != 0) {
            getStepsJSON(subSteps, this, howDeep);
        }
        subSteps.append("]}," + "\n");
        return subSteps.toString();
    }

    public int getLevel() {
        return level;
    }


    public Step getStep() {
        return step;
    }

    public ArrayList<StepEnumerator> getSubEnumeratedSteps() {
        return subEnumeratedSteps;
    }
}

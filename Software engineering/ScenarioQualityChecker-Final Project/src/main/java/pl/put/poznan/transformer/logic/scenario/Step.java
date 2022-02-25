package pl.put.poznan.transformer.logic.scenario;

import java.util.ArrayList;
import java.util.List;

/**
 * Step - a basic operation contained inside a {@link pl.put.poznan.transformer.logic.scenario.Scenario}.
 * Can consist of substeps that are also instances of the Step class.
 */
public class Step implements ScenarioElement{
    private String text;
    private final List<Step> substeps;

    /**
     * Default constructor.
     * @param text is the description of the step.
     */
    public Step(String text){
        this.text = text;
        this.substeps = new ArrayList<>();
    }

    /**
     * Used to add a single substep.
     * @param subStep is an instance of the Step class.
     */
    public void addSubStep(Step subStep){
        this.substeps.add(subStep);
    }

    public void addAllSubSteps(ArrayList<Step> subSteps){
        this.substeps.addAll(subSteps);
    }

    /**
     * Used to get the description of the step.
     * @return text.
     */
    public String getText(){
        return text;
    }

    public List<Step> getSubSteps() {
        return substeps;
    }


    /**
     * Method that exposes Step and its Substeps to visitor.
     * @param visitor Visitor that will extract information from {@link pl.put.poznan.transformer.logic.scenario.Step}
     */
    @Override
    public void accept(ScenarioElementVisitor visitor) {
        for(Step s : substeps) {
            s.accept(visitor);

        }
        visitor.visit(this);
    }
}

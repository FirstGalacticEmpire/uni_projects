package pl.put.poznan.transformer.logic.scenario;

import java.util.ArrayList;
import java.util.List;

/**
 * Scenario - describes one way that a system is or is envisaged to be used in the context of an activity in a defined time-frame.
 * It has title and the list of {@link pl.put.poznan.transformer.logic.scenario.Actor} instances that take part in it.
 * It consists of the list of {@link pl.put.poznan.transformer.logic.scenario.Step} instances that can contain substeps.
 */
public class Scenario implements ScenarioElement{
    private String title;
    private List<Actor> actors;
    private List<Step> steps;

    /**
     * Default constructor.
     * @param title is the title of the scenario.
     */
    public Scenario(String title){
        this.title = title;
        this.actors = new ArrayList<>();
        this.steps = new ArrayList<>();
    }

    /**
     * addActor - used to add one {@link pl.put.poznan.transformer.logic.scenario.Actor} to the scenario.
     * @param actor is a single actor taking part in the scenario.
     */
    public void addActor(Actor actor){
        this.actors.add(actor);
    }

    /**
     * addStep - used to add one {@link pl.put.poznan.transformer.logic.scenario.Step} to the scenario.
     * @param step is a single step that the scenario contains.
     */
    public void addStep(Step step){
        this.steps.add(step);
    }

    public void addAllActors(List<Actor> actors) {
        this.actors.addAll(actors);
    }


    public String getTitle() {
        return title;
    }

    public List<Actor> getActors(ActorType actorType) {
        List<Actor> listOfActors = new ArrayList<>();
        for (Actor actor: actors){
            if (actor.getActorType() == actorType){
                listOfActors.add(actor);
            }
        }
        return listOfActors;
    }

    public List<Actor> getActorsWithOutTypes(){
        return this.actors;
    }

    public List<Step> getSteps() {
        return steps;
    }

    /**
     * Method that exposes Scenario and its Steps to visitor.
     * @param visitor Visitor that will extract information from {@link pl.put.poznan.transformer.logic.scenario.Scenario}
     */
    @Override
    public void accept(ScenarioElementVisitor visitor) {
        for(Step s : steps) {
            s.accept(visitor);
        }
        visitor.visit(this);
    }
}

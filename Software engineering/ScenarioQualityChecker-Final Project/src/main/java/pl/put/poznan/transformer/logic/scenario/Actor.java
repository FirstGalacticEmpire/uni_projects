package pl.put.poznan.transformer.logic.scenario;

/**
 * Actor - the entity that initiates or takes part in {@link pl.put.poznan.transformer.logic.scenario.Scenario}.
 * it has a name and an {@link pl.put.poznan.transformer.logic.scenario.ActorType}.
 */
public class Actor {
    private String name;
    private ActorType actorType;

    /**
     * Default constructor
     * @param name the name that will be given to the actor.
     * @param actorType the type of the actor, can be one of the following: external, system.
     */
    public Actor(String name, ActorType actorType) {
        this.name = name;
        this.actorType = actorType;
    }

    /**
     * @return name of the actor.
     */
    public String getName() {
        return name;
    }

    /**
     * @return type of the actor.
     */
    public ActorType getActorType() {
        return actorType;
    }

}

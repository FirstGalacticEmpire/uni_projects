package pl.put.poznan.transformer.json;

import pl.put.poznan.transformer.logic.scenario.Actor;

/**
 * Converts an Actor into a ActorJsonConverter
 */
public class ActorJsonConverter {

    private final Actor actor;

    /**
     * @param actor from which ActorJsonConverter should be constructed
     */
    public ActorJsonConverter(Actor actor) {
        this.actor = actor;
    }

    /**
     * @return string adjusted for json
     */
    @Override
    public String toString() {
        return '\"' + actor.getName() + '\"';
    }
}

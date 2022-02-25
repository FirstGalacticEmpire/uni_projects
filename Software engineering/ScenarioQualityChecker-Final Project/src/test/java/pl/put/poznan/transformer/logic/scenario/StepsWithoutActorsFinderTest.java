package pl.put.poznan.transformer.logic.scenario;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StepsWithoutActorsFinderTest {

    static List<Actor> actors;

    @BeforeAll
    public static void setup(){
        actors = new ArrayList<>();
        actors.add(new Actor("Dog", ActorType.EXTERNAL));
        actors.add(new Actor("Cat", ActorType.EXTERNAL));
        actors.add(new Actor("House", ActorType.SYSTEM));
    }

    @Test
    public void TestEmpty(){
        Scenario scenario = new Scenario("scenario");

        StepsWithoutActorsFinder finder = new StepsWithoutActorsFinder(actors);
        scenario.accept(finder);

        assertEquals(0, finder.getSteps().size());
    }

    @Test
    public void TestOneWithActor(){
        Scenario scenario = new Scenario("scenario");
        scenario.addStep(new Step("Dog is good"));

        StepsWithoutActorsFinder finder = new StepsWithoutActorsFinder(actors);
        scenario.accept(finder);

        assertEquals(0, finder.getSteps().size());
    }

    @Test
    public void TestOneWithActorWithKeyword(){
        Scenario scenario = new Scenario("scenario");
        scenario.addStep(new Step("FOR EACH Dog is good"));

        StepsWithoutActorsFinder finder = new StepsWithoutActorsFinder(actors);
        scenario.accept(finder);

        assertEquals(0, finder.getSteps().size());
    }

    @Test
    public void TestOneWithoutActorWithKeyword(){
        Scenario scenario = new Scenario("scenario");
        scenario.addStep(new Step("FOR EACH is good"));

        StepsWithoutActorsFinder finder = new StepsWithoutActorsFinder(actors);
        scenario.accept(finder);

        assertEquals(1, finder.getSteps().size());
    }

    @Test
    public void TestOneWithoutActorWithoutKeyword(){
        Scenario scenario = new Scenario("scenario");
        scenario.addStep(new Step("is good"));

        StepsWithoutActorsFinder finder = new StepsWithoutActorsFinder(actors);
        scenario.accept(finder);

        assertEquals(1, finder.getSteps().size());
    }
}
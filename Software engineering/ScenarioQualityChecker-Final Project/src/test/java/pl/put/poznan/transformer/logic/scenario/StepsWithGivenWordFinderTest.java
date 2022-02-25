package pl.put.poznan.transformer.logic.scenario;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StepsWithGivenWordFinderTest {

    @Test
    public void TestEmpty(){
        Scenario scenario = new Scenario("scenario");

        StepsWithGivenWordFinder finder = new StepsWithGivenWordFinder("word");
        scenario.accept(finder);

        assertEquals(0, finder.getSteps().size());
    }

    @Test
    public void TestOneWithWord(){
        Scenario scenario = new Scenario("scenario");
        scenario.addStep(new Step("this is word is good"));

        StepsWithGivenWordFinder finder = new StepsWithGivenWordFinder("word");
        scenario.accept(finder);

        assertEquals(1, finder.getSteps().size());
    }

    @Test
    public void TestTwoWithWord(){
        Scenario scenario = new Scenario("scenario");
        scenario.addStep(new Step("this is word is good"));
        scenario.addStep(new Step("word is good"));

        StepsWithGivenWordFinder finder = new StepsWithGivenWordFinder("word");
        scenario.accept(finder);

        assertEquals(2, finder.getSteps().size());
    }

    @Test
    public void TestOneWithAndOneWithout(){
        String searchedText = "word is good";
        Scenario scenario = new Scenario("scenario");
        scenario.addStep(new Step("this is is good"));
        scenario.addStep(new Step(searchedText));

        StepsWithGivenWordFinder finder = new StepsWithGivenWordFinder("word");
        scenario.accept(finder);

        assertEquals(1, finder.getSteps().size());
        assertEquals(searchedText, finder.getSteps().get(0).getText());
    }

    @Test
    public void TestMultiple(){
        Scenario scenario = new Scenario("scenario");
        scenario.addStep(new Step("this is is good"));
        scenario.addStep(new Step("word is is good"));
        Step step = new Step("there is no");
        step.addSubStep(new Step("here is word"));
        step.addSubStep(new Step("and here is not"));
        scenario.addStep(step);

        StepsWithGivenWordFinder finder = new StepsWithGivenWordFinder("word");
        scenario.accept(finder);

        assertEquals(2, finder.getSteps().size());
    }
}
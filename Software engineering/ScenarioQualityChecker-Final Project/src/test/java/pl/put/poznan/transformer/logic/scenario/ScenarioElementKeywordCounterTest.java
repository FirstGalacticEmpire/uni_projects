package pl.put.poznan.transformer.logic.scenario;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class ScenarioElementKeywordCounterTest {

    @Test
    void testEmpty() {
        Scenario mockScenario = new Scenario("scenario");
        ScenarioElementKeywordCounter counter = new ScenarioElementKeywordCounter();
        mockScenario.accept(counter);
        assertEquals(0, counter.getCount());
    }

    @Test
    void testOne() {
        Scenario mockScenario = new Scenario("scenario");
        Step step = new Step("IF sth.");
        mockScenario.addStep(step);
        ScenarioElementKeywordCounter counter = new ScenarioElementKeywordCounter();
        mockScenario.accept(counter);
        assertEquals(1, counter.getCount());
    }

    @Test
    void testOneFOREACH() {
        Scenario mockScenario = new Scenario("scenario");
        Step step = new Step("FOR EACH sth.");
        mockScenario.addStep(step);
        ScenarioElementKeywordCounter counter = new ScenarioElementKeywordCounter();
        mockScenario.accept(counter);
        assertEquals(1, counter.getCount());
    }

    @Test
    void testOneFORany() {
        Scenario mockScenario = new Scenario("scenario");
        Step step = new Step("FOR any sth.");
        mockScenario.addStep(step);
        ScenarioElementKeywordCounter counter = new ScenarioElementKeywordCounter();
        mockScenario.accept(counter);
        assertEquals(0, counter.getCount());
    }

    @Test
    void testOneEmpty() {
        Scenario mockScenario = new Scenario("scenario");
        Step step = new Step("sth. IF");
        mockScenario.addStep(step);
        ScenarioElementKeywordCounter counter = new ScenarioElementKeywordCounter();
        mockScenario.accept(counter);
        assertEquals(0, counter.getCount());
    }
}
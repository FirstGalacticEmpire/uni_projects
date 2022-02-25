package pl.put.poznan.transformer.logic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.put.poznan.transformer.logic.scenario.Scenario;
import pl.put.poznan.transformer.logic.scenario.Step;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScenarioEnumeratorTest {
    public Scenario scenario, emptyScenario;
    public ScenarioEnumerator scenarioEnumerator;
    public Step step1, step2, step3;

    @BeforeEach
    void setUp() {
        scenario = mock(Scenario.class);
        step1 = mock(Step.class);
        step2 = mock(Step.class);
        step3 = mock(Step.class);
        when(step1.getText()).thenReturn("step1");
        when(step2.getText()).thenReturn("step2");
        when(step3.getText()).thenReturn("step3");
        when(step1.getSubSteps()).thenReturn(new ArrayList<Step>(List.of(step2, step3)));
        when(step2.getSubSteps()).thenReturn(new ArrayList<Step>());
        when(step3.getSubSteps()).thenReturn(new ArrayList<Step>());

        scenario = mock(Scenario.class);
        emptyScenario = mock(Scenario.class);
        when(scenario.getSteps()).thenReturn(new ArrayList<Step>(List.of(step1)));


    }

    @Test
    void generateStepsAsATextEmptyScenario() {
        scenarioEnumerator = new ScenarioEnumerator(emptyScenario);
        scenarioEnumerator.enumerateSteps();
        assertEquals("", scenarioEnumerator.generateStepsAsAText());
    }

    @Test
    void generateStepsAsAText() {
        scenarioEnumerator = new ScenarioEnumerator(scenario);
        scenarioEnumerator.enumerateSteps();
        assertEquals("1. step1\n\t1.1. step2\n\t1.2. step3\n", scenarioEnumerator.generateStepsAsAText());
    }

    @Test
    void generateJSONString() {
        scenarioEnumerator = new ScenarioEnumerator(scenario);
        scenarioEnumerator.enumerateSteps();
        assertEquals("[{\"text\":\"step1\",\n" +
                "\"sub_steps\": [{\"text\":\"step2\",\n" +
                "\"sub_steps\": []},\n" +
                "{\"text\":\"step3\",\n" +
                "\"sub_steps\": []}]}]", scenarioEnumerator.generateJSONString(2));
    }

    @Test
    void generateJSONStringMinus() {
        scenarioEnumerator = new ScenarioEnumerator(scenario);
        scenarioEnumerator.enumerateSteps();
        assertEquals("[]", scenarioEnumerator.generateJSONString(-1));
    }

    @Test
    void generateJSONStringEmpty() {
        scenarioEnumerator = new ScenarioEnumerator(emptyScenario);
        scenarioEnumerator.enumerateSteps();
        assertEquals("[]", scenarioEnumerator.generateJSONString(0));
    }

    @Test
    void generateJSONStringDeeperThanActual() {
        scenarioEnumerator = new ScenarioEnumerator(scenario);
        scenarioEnumerator.enumerateSteps();
        assertEquals("[{\"text\":\"step1\",\n" +
                "\"sub_steps\": [{\"text\":\"step2\",\n" +
                "\"sub_steps\": []},\n" +
                "{\"text\":\"step3\",\n" +
                "\"sub_steps\": []}]}]",scenarioEnumerator.generateJSONString(4));
    }
}
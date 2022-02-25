package pl.put.poznan.transformer.json;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.put.poznan.transformer.logic.scenario.Step;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StepJsonConverterTest {
    Step step1, step2, step3, emptystep, stepA, stepB;
    StepJsonConverter stepJsonConverter;

    @BeforeEach
    void setUp() {
        step1 = mock(Step.class);
        step2 = mock(Step.class);
        step3 = mock(Step.class);
        emptystep = mock(Step.class);
        stepA = mock(Step.class);
        stepB = mock(Step.class);
        when(step1.getText()).thenReturn("step1");
        when(step2.getText()).thenReturn("step2");
        when(step3.getText()).thenReturn("step3");
        when(stepA.getText()).thenReturn("stepA");
        when(stepB.getText()).thenReturn("stepB");
        when(step1.getSubSteps()).thenReturn(new ArrayList<Step>(List.of(step2, step3)));
        when(step2.getSubSteps()).thenReturn(new ArrayList<Step>());
        when(step3.getSubSteps()).thenReturn(new ArrayList<Step>());
        when(stepA.getSubSteps()).thenReturn(new ArrayList<Step>(List.of(stepB)));
        when(stepB.getSubSteps()).thenReturn(new ArrayList<Step>(List.of(stepA)));
    }

    @Test
    void testToString() {
        stepJsonConverter = new StepJsonConverter(step1);
        assertEquals("{\"text\": \"step1\", \"sub_steps\": [{\"text\": \"step2\", \"sub_steps\": null}, {\"text\": \"step3\", \"sub_steps\": null}]}", stepJsonConverter.toString());
    }

    @Test
    void testToStringEmpty() {
        stepJsonConverter = new StepJsonConverter(emptystep);
        assertEquals("{\"text\": \"null\", \"sub_steps\": null}", stepJsonConverter.toString());
    }
    @Test
    void testToStringLoop() {
        stepJsonConverter = new StepJsonConverter(stepA);
        assertThrows(StackOverflowError.class, () -> stepJsonConverter.toString());
    }
}
package pl.put.poznan.transformer.logic;

import org.junit.jupiter.api.*;
import pl.put.poznan.transformer.logic.scenario.Step;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.mockito.stubbing.Answer;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class StepEnumeratorTest {
    StepEnumerator stepEnumerator;
    Step step1, step2, step3, emptystep;

    @BeforeEach
    void setUp() {
        step1 = mock(Step.class);
        step2 = mock(Step.class);
        step3 = mock(Step.class);
        emptystep = mock(Step.class);
        when(step1.getText()).thenReturn("step1");
        when(step2.getText()).thenReturn("step2");
        when(step3.getText()).thenReturn("step3");
        when(step1.getSubSteps()).thenReturn(new ArrayList<Step>(List.of(step2, step3)));
        when(step2.getSubSteps()).thenReturn(new ArrayList<Step>());
        when(step3.getSubSteps()).thenReturn(new ArrayList<Step>());

    }

    @Test
    void testToStringEmpty() {
        stepEnumerator = new StepEnumerator(emptystep, 1, null);
        assertEquals("1. null\n", stepEnumerator.toString());
    }

    @Test
    void testToString() {
        stepEnumerator = new StepEnumerator(step1, 1, null);
        assertEquals("1. step1\n" + "\t1.1. step2\n" + "\t1.2. step3\n", stepEnumerator.toString());
    }

    @Test
    void testToJsonStringEmpty() {
        stepEnumerator = new StepEnumerator(emptystep, 1, null);
        assertEquals("{\"text\":\"null\",\n" +
                "\"sub_steps\": []},\n", stepEnumerator.toJsonString(1));
    }

    @Test
    void testToJsonStringMinus() {
        stepEnumerator = new StepEnumerator(emptystep, -10, null);
        assertEquals("{\"text\":\"null\",\n" +
                "\"sub_steps\": []},\n", stepEnumerator.toJsonString(-10));
    }

    @Test
    void toJsonString() {
        stepEnumerator = new StepEnumerator(step1, 1, null);
        assertEquals("{\"text\":\"step1\",\n" +
                "\"sub_steps\": [{\"text\":\"step2\",\n" +
                "\"sub_steps\": []},\n" +
                "{\"text\":\"step3\",\n" +
                "\"sub_steps\": []}]},\n", stepEnumerator.toJsonString(1));
    }

    @Test
    void toJsonStringDeeperThanActual() {
        stepEnumerator = new StepEnumerator(step1, 5, null);
        assertEquals("{\"text\":\"step1\",\n" +
                "\"sub_steps\": [{\"text\":\"step2\",\n" +
                "\"sub_steps\": []},\n" +
                "{\"text\":\"step3\",\n" +
                "\"sub_steps\": []}]},\n", stepEnumerator.toJsonString(1));
    }
}
package pl.put.poznan.transformer.logic.scenario;

/**
 * Interface that all elements that are part of scenario need to implement,
 * so that {@link ScenarioElementVisitor} could examine them
 */
public interface ScenarioElement {
    void accept(ScenarioElementVisitor visitor);
}

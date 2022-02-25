package pl.put.poznan.transformer.logic.scenario;

/**
 * {@link ScenarioElementVisitor} that counts {@link Step} in a {@link Scenario}
 */
public class ScenarioElementCounter implements ScenarioElementVisitor{
    private int count;

    /**
     * Standard constructor that starts counting from 0.
     */
    public ScenarioElementCounter() {
        this.count = 0;
    }

    /**
     * Method that processes scenario
     * @param scenario currently visited Scenario
     */
    @Override
    public void visit(Scenario scenario) {
    }

    /**
     * Method that processes step, here it increments count.
     * @param step currently visited Step
     */
    @Override
    public void visit(Step step) {
        incrementCounter();
    }

    protected void incrementCounter() {
        count += 1;
    }

    /**
     * @return number of steps
     */
    public int getCount() {
        return count;
    }
}

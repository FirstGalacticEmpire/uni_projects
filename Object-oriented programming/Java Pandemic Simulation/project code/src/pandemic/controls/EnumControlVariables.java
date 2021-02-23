package pandemic.controls;

/**
 * Stores information, is a singielton.
 * lockdownThreshold - how many agents need to be sick to turn on lockdown.
 * numOfVisitsBeforeRecovering - how many visits before recovering
 * Rest as the name suggests.
 */
public class EnumControlVariables {
    private static EnumControlVariables instance;
    private double diseaseTransmissionRateWithOutMask;
    private double diseaseTransmissionRateWithMask;
    private int lockdownThreshold;
    private int numOfVisitsBeforeRecovering;
    private double vaccineEffectiveness;
    private double percentageOfAgentsWithMask;
    private double percentageOfAgentsVaccinated;
    private int numOfAgents;
    private double chanceOnCreationToBeInfected;
    private boolean isPaused;

    private EnumControlVariables() {
        this.diseaseTransmissionRateWithOutMask = 90;
        this.diseaseTransmissionRateWithMask = 70;
        this.lockdownThreshold = 10;
        this.numOfVisitsBeforeRecovering = 1;
        this.vaccineEffectiveness = 15;
        this.percentageOfAgentsVaccinated = 30;
        this.percentageOfAgentsWithMask = 30;
        this.isPaused = false;
    }

    /**
     * @return instance of EnumControlVariables
     */
    public static synchronized EnumControlVariables getInstance() {
        if (instance == null) {
            instance = new EnumControlVariables();
        }
        return instance;
    }


    public void incrementNumOfAgents() {
        numOfAgents += 1;
    }

    public void decrementNumOfAgents() {
        numOfAgents -= 1;
    }

    public void setDiseaseTransmissionRateWithOutMask(double diseaseTransmissionRateWithOutMask) {
        this.diseaseTransmissionRateWithOutMask = diseaseTransmissionRateWithOutMask;
    }

    public double getDiseaseTransmissionRateWithOutMask() {
        return diseaseTransmissionRateWithOutMask;
    }

    public double getDiseaseTransmissionRateWithMask() {
        return diseaseTransmissionRateWithMask;
    }

    public void setDiseaseTransmissionRateWithMask(double diseaseTransmissionRateWithMask) {
        this.diseaseTransmissionRateWithMask = diseaseTransmissionRateWithMask;
    }

    public int getLockdownThreshold() {
        return lockdownThreshold;
    }

    public void setLockdownThreshold(double lockdownThreshold) {
        this.lockdownThreshold =
                (int) lockdownThreshold;
    }

    public int getNumOfVisitsBeforeRecovering() {
        return numOfVisitsBeforeRecovering;
    }

    public void setNumOfVisitsBeforeRecovering(double numOfVisitsBeforeRecovering) {
        this.numOfVisitsBeforeRecovering = (int) numOfVisitsBeforeRecovering;
    }

    public double getVaccineEffectiveness() {
        return vaccineEffectiveness;
    }

    public void setVaccineEffectiveness(double vaccineEffectiveness) {
        this.vaccineEffectiveness = vaccineEffectiveness;
    }


    public double getPercentageOfAgentsWithMask() {
        return percentageOfAgentsWithMask;
    }

    public void setPercentageOfAgentsWithMask(double percentageOfAgentsWithMask) {
        this.percentageOfAgentsWithMask = percentageOfAgentsWithMask;
    }

    public double getPercentageOfAgentsVaccinated() {
        return percentageOfAgentsVaccinated;
    }

    public void setPercentageOfAgentsVaccinated(double percentageOfAgentsVaccinated) {
        this.percentageOfAgentsVaccinated = percentageOfAgentsVaccinated;
    }

    public double getChanceOnCreationToBeInfected() {
        return chanceOnCreationToBeInfected;
    }

    public void setChanceOnCreationToBeInfected(double chanceOnCreationToBeInfected) {
        this.chanceOnCreationToBeInfected = chanceOnCreationToBeInfected;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }
}

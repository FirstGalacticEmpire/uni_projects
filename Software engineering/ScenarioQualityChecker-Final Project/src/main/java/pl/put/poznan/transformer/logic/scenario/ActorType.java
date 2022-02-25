package pl.put.poznan.transformer.logic.scenario;

/**
 * ActorType - an enum representing the type of the {@link pl.put.poznan.transformer.logic.scenario.Actor}.
 * Can be one of the following: external, system.
 */
public enum ActorType {
    EXTERNAL("external"),
    SYSTEM("system");

    private String typeName;

    /**
     * Default constructor.
     * @param typeName can be either "external" or "system".
     */
    ActorType(String typeName){
        this.typeName = typeName;
    }

    /**
     * @return typeName
     */
    public String getTypeName() {
        return typeName;
    }
}

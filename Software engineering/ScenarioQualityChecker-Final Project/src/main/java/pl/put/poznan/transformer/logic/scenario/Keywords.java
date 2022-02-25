package pl.put.poznan.transformer.logic.scenario;

public enum Keywords {
    IF("IF"),
    ELSE("ELSE"),
    FOR_EACH("FOR EACH");

    private String keyword;

    Keywords(String keyword){
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }
}

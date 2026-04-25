package firstrentverdict.model.apply;

public enum ApplicationDecision {
    APPLY("Apply", "apply"),
    PAUSE("Pause", "pause"),
    DO_NOT_APPLY("Do Not Apply", "do-not-apply");

    private final String label;
    private final String cssClass;

    ApplicationDecision(String label, String cssClass) {
        this.label = label;
        this.cssClass = cssClass;
    }

    public String label() {
        return label;
    }

    public String cssClass() {
        return cssClass;
    }
}

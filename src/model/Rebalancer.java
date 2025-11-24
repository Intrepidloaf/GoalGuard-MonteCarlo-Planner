package model;

public class Rebalancer {
    public enum RebalanceFrequency { NONE, MONTHLY, QUARTERLY, YEARLY }

    public static RebalanceFrequency parse(String s) {
        if (s == null) return RebalanceFrequency.NONE;
        s = s.trim().toLowerCase();
        switch (s) {
            case "monthly": return RebalanceFrequency.MONTHLY;
            case "quarterly": return RebalanceFrequency.QUARTERLY;
            case "yearly": return RebalanceFrequency.YEARLY;
            default: return RebalanceFrequency.NONE;
        }
    }

    public static int toStepsPerYear(RebalanceFrequency f, int stepsPerYear) {
        switch (f) {
            case MONTHLY: return Math.max(1, stepsPerYear/12);
            case QUARTERLY: return Math.max(1, stepsPerYear/4);
            case YEARLY: return stepsPerYear;
            default: return 0;
        }
    }
}

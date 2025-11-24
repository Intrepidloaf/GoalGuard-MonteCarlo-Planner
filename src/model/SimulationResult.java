package model;

public class SimulationResult {
    public final double[] finalBalances;
    public final double successProbability;
    public final double median;
    public final double p25;
    public final double p75;
    public final double mean;
    public final double[] sampleTrialTrace;

    public SimulationResult(double[] finalBalances, double successProbability,
                            double median, double p25, double p75, double mean,
                            double[] sampleTrialTrace) {
        this.finalBalances = finalBalances;
        this.successProbability = successProbability;
        this.median = median;
        this.p25 = p25;
        this.p75 = p75;
        this.mean = mean;
        this.sampleTrialTrace = sampleTrialTrace;
    }
}

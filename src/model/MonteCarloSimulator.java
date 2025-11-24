package model;

import java.util.*;
import java.util.stream.*;

public class MonteCarloSimulator {
    private final Portfolio template;
    private final double initialBalance;
    private final double contribution;
    private final int contribFreqPerYear;
    private final double targetGoal;
    private final int years;
    private final int stepsPerYear;
    private final int trials;
    private final Rebalancer.RebalanceFrequency rebalanceFreq;
    private final Random rng = new Random(42);

    public MonteCarloSimulator(Portfolio template, double initialBalance, double contribution,
                               int contribFreqPerYear, double targetGoal, int years,
                               int stepsPerYear, int trials, Rebalancer.RebalanceFrequency rebalanceFreq) {
        this.template = template;
        this.initialBalance = initialBalance;
        this.contribution = contribution;
        this.contribFreqPerYear = contribFreqPerYear;
        this.targetGoal = targetGoal;
        this.years = years;
        this.stepsPerYear = stepsPerYear;
        this.trials = trials;
        this.rebalanceFreq = rebalanceFreq;
    }

    public SimulationResult run() {
        int totalSteps = years * stepsPerYear;
        double dt = 1.0 / stepsPerYear;
        List<Asset> assets = template.getAssets();

        double[] finalBalances = new double[trials];
        double[] sampleTrace = new double[totalSteps];

        for (int t = 0; t < trials; t++) {
            Portfolio p = cloneEmptyPortfolio(template);
            // initial allocation
            double init = initialBalance;
            p.deposit(init);

            int contribStepInterval = stepsPerYear / Math.max(1, contribFreqPerYear);
            int nextContribStep = 0;

            int rebalanceInterval = Rebalancer.toStepsPerYear(rebalanceFreq, stepsPerYear);
            int nextRebalance = rebalanceInterval > 0 ? rebalanceInterval : Integer.MAX_VALUE;

            for (int step = 0; step < totalSteps; step++) {
                // apply returns for this step using GBM discrete step
                double[] returns = new double[assets.size()];
                for (int i = 0; i < assets.size(); i++) {
                    Asset a = assets.get(i);
                    double mu = a.annualReturn / stepsPerYear;
                    double sigma = a.annualVol / Math.sqrt(stepsPerYear);
                    double z = rng.nextGaussian();
                    // discrete return approx: exp((mu - 0.5*sigma^2) + sigma*z) -1
                    double ret = Math.exp((a.annualReturn - 0.5*a.annualVol*a.annualVol)*dt + a.annualVol*Math.sqrt(dt)*z) - 1.0;
                    returns[i] = ret;
                }
                p.applyReturns(returns);

                // contribution?
                if (step == nextContribStep) {
                    p.deposit(contribution);
                    nextContribStep += Math.max(1, contribStepInterval);
                }

                // rebalancing?
                if (rebalanceInterval > 0 && (step+1) % rebalanceInterval == 0) {
                    p.rebalance();
                }

                // record first trial sample trace
                if (t == 0) sampleTrace[step] = p.totalValue();
            }

            finalBalances[t] = p.totalValue();
        }

        // aggregate statistics
        Arrays.sort(finalBalances);
        double successProb = 0;
        for (double v : finalBalances) if (v >= targetGoal) successProb += 1;
        successProb /= trials;

        double median = finalBalances[trials/2];
        double p25 = finalBalances[(int)(trials*0.25)];
        double p75 = finalBalances[(int)(trials*0.75)];
        double mean = Arrays.stream(finalBalances).average().orElse(0);

        return new SimulationResult(finalBalances, successProb, median, p25, p75, mean, sampleTrace);
    }

    private Portfolio cloneEmptyPortfolio(Portfolio proto) {
        Portfolio p = new Portfolio();
        for (Asset a : proto.getAssets()) p.addAsset(new Asset(a.name, a.targetWeight, a.annualReturn, a.annualVol));
        return p;
    }
}

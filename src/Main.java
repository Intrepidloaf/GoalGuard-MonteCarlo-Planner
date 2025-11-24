import model.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("=== GoalGuard — Monte Carlo Micro-Investment Planner ===");

        // Read sample files from data/ (change path if needed)
        String assetPath = "data/sample_assets.csv";
        String configPath = "data/sample_config.txt";

        List<Asset> assets = Utils.loadAssetsFromCsv(assetPath);
        Map<String,String> cfg = Utils.loadConfig(configPath);

        double initial = Double.parseDouble(cfg.getOrDefault("initial_balance","1000.0"));
        double contrib = Double.parseDouble(cfg.getOrDefault("contribution","100.0"));
        int contribFreq = Integer.parseInt(cfg.getOrDefault("contrib_frequency","12"));
        double goal = Double.parseDouble(cfg.getOrDefault("target_goal","50000.0"));
        int years = Integer.parseInt(cfg.getOrDefault("years","10"));
        int trials = Integer.parseInt(cfg.getOrDefault("trials","1000"));
        int stepsPerYear = Integer.parseInt(cfg.getOrDefault("steps_per_year","12"));
        String rebalanceStr = cfg.getOrDefault("rebalance","monthly");
        Rebalancer.RebalanceFrequency rebalanceFreq = Rebalancer.parse(rebalanceStr);

        // Build initial portfolio allocation from assets
        Portfolio template = new Portfolio();
        for (Asset a : assets) template.addAsset(a);

        // Simulation
        MonteCarloSimulator sim = new MonteCarloSimulator(template, initial, contrib, contribFreq,
                goal, years, stepsPerYear, trials, rebalanceFreq);

        System.out.println("Running simulation... trials=" + trials + " years=" + years);
        SimulationResult result = sim.run();

        // Output summary
        System.out.println();
        System.out.println("=== Simulation Summary ===");
        System.out.printf("Probability of reaching goal %.2f in %d years: %.2f%%\n", goal, years, result.successProbability*100);
        System.out.printf("Median final balance: %.2f\n", result.median);
        System.out.printf("25th percentile: %.2f, 75th percentile: %.2f\n", result.p25, result.p75);
        System.out.printf("Average final balance: %.2f\n", result.mean);

        // Show sample trial trace (first trial)
        System.out.println("\nSample trial (first 12 months balance snapshot):");
        double[] sample = result.sampleTrialTrace;
        for (int i = 0; i < Math.min(sample.length, 12); i++) {
            System.out.printf("Month %2d: %.2f\n", i+1, sample[i]);
        }

        // Save results CSV
        Files.createDirectories(Paths.get("output"));
        String outCsv = "output/final_balances.csv";
        Utils.saveDoubles(result.finalBalances, outCsv);
        System.out.println("\nSaved final balances to " + outCsv);
        System.out.println("Done.");
    }
}

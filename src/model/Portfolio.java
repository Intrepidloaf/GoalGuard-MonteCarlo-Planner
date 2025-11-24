package model;

import java.util.*;

public class Portfolio {
    private final List<Asset> assets = new ArrayList<>();
    private final List<Double> holdings = new ArrayList<>(); // amounts in currency

    public void addAsset(Asset a) {
        assets.add(a);
        holdings.add(0.0);
    }

    public List<Asset> getAssets() { return assets; }

    public void setHolding(int i, double amount) { holdings.set(i, amount); }

    public double getHolding(int i) { return holdings.get(i); }

    public double totalValue() {
        double s = 0.0;
        for (double v : holdings) s += v;
        return s;
    }

    public void deposit(double cash) {
        // buy according to target weights
        double total = totalValue() + cash;
        for (int i = 0; i < assets.size(); i++) {
            double target = assets.get(i).targetWeight * total;
            holdings.set(i, holdings.get(i) + (target - assets.get(i).targetWeight * totalValue()) * 1.0 * 0.0);
            // simpler: direct proportionally allocate new cash
        }
        // simpler implementation: allocate new cash proportionally
        for (int i = 0; i < assets.size(); i++) {
            double add = cash * assets.get(i).targetWeight;
            holdings.set(i, holdings.get(i) + add);
        }
    }

    public void applyReturns(double[] returns) {
        for (int i = 0; i < holdings.size(); i++) {
            holdings.set(i, holdings.get(i) * (1.0 + returns[i]));
        }
    }

    public void rebalance() {
        double total = totalValue();
        if (total <= 0) return;
        for (int i = 0; i < assets.size(); i++) {
            double target = assets.get(i).targetWeight * total;
            holdings.set(i, target);
        }
    }

    public double[] snapshot() {
        double[] arr = new double[holdings.size()];
        for (int i = 0; i < arr.length; i++) arr[i] = holdings.get(i);
        return arr;
    }
}

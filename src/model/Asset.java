package model;

public class Asset {
    public final String name;
    public final double targetWeight; // 0..1
    public final double annualReturn; // as decimal, e.g., 0.08
    public final double annualVol;    // as decimal, e.g., 0.15

    public Asset(String name, double targetWeight, double annualReturn, double annualVol) {
        this.name = name;
        this.targetWeight = targetWeight;
        this.annualReturn = annualReturn;
        this.annualVol = annualVol;
    }

    @Override
    public String toString() {
        return String.format("Asset(%s, w=%.2f, r=%.3f, vol=%.3f)", name, targetWeight, annualReturn, annualVol);
    }
}

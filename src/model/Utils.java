package model;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Utils {
    public static List<Asset> loadAssetsFromCsv(String path) throws IOException {
        List<Asset> out = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(path));
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;
            String[] parts = line.split(",");
            if (parts.length < 4) continue;
            String name = parts[0].trim();
            double w = Double.parseDouble(parts[1].trim());
            double r = Double.parseDouble(parts[2].trim())/100.0;
            double vol = Double.parseDouble(parts[3].trim())/100.0;
            out.add(new Asset(name, w, r, vol));
        }
        return out;
    }

    public static Map<String,String> loadConfig(String path) throws IOException {
        Map<String,String> cfg = new HashMap<>();
        List<String> lines = Files.readAllLines(Paths.get(path));
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;
            if (!line.contains("=")) continue;
            String[] kv = line.split("=",2);
            cfg.put(kv[0].trim(), kv[1].trim().split("#")[0].trim());
        }
        return cfg;
    }

    public static void saveDoubles(double[] arr, String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (double d : arr) sb.append(String.format(Locale.US, "%.6f\n", d));
        Files.write(Paths.get(path), sb.toString().getBytes());
    }
}

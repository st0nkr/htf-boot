package com.teto.domain.version;

import java.util.ArrayList;
import java.util.List;

/** Works with dot-separated numeric versions and generates inclusive ranges. */
public final class VersionRange {
    private VersionRange() {
    }

    public static String increment(String version) {
        int[] components = parseVersion(version);
        int lastIndex = components.length - 1;
        if (components[lastIndex] == Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Version component cannot be incremented: " + version);
        }
        components[lastIndex]++;
        return formatVersion(components);
    }

    public static String decrement(String version) {
        int[] components = parseVersion(version);
        int lastIndex = components.length - 1;
        if (components[lastIndex] == 0) {
            throw new IllegalArgumentException("Version cannot be decremented below zero: " + version);
        }
        components[lastIndex]--;
        return formatVersion(components);
    }

    public static List<String> generate(String startVersion, String endVersion) {
        int[] start = parseVersion(startVersion);
        int[] end = parseVersion(endVersion);

        if (start.length != end.length) {
            throw new IllegalArgumentException("Start and end versions must have the same number of components.");
        }

        for (int index = 0; index < start.length - 1; index++) {
            if (start[index] != end[index]) {
                throw new IllegalArgumentException(
                        "Only the last version component may differ: " + startVersion + " to " + endVersion);
            }
        }

        if (start[start.length - 1] > end[end.length - 1]) {
            throw new IllegalArgumentException("Start version must not be greater than end version.");
        }

        List<String> versions = new ArrayList<>();
        for (long value = start[start.length - 1]; value <= end[end.length - 1]; value++) {
            int[] current = start.clone();
            current[current.length - 1] = (int) value;
            versions.add(formatVersion(current));
        }
        return versions;
    }

    private static int[] parseVersion(String version) {
        if (version == null || version.trim().isEmpty()) {
            throw new IllegalArgumentException("Version cannot be null or empty.");
        }

        String[] components = version.trim().split("\\.", -1);
        int[] parsed = new int[components.length];
        for (int index = 0; index < components.length; index++) {
            if (!components[index].matches("\\d+")) {
                throw new IllegalArgumentException("Invalid version: " + version);
            }
            try {
                parsed[index] = Integer.parseInt(components[index]);
            } catch (NumberFormatException exception) {
                throw new IllegalArgumentException("Version component is too large: " + components[index], exception);
            }
        }
        return parsed;
    }

    private static String formatVersion(int[] version) {
        StringBuilder result = new StringBuilder();
        for (int index = 0; index < version.length; index++) {
            if (index > 0) {
                result.append('.');
            }
            result.append(version[index]);
        }
        return result.toString();
    }

    public static void main(String[] args) {
        if (args.length == 2 && ("increment".equalsIgnoreCase(args[0]) || "decrement".equalsIgnoreCase(args[0]))) {
            try {
                String result = "increment".equalsIgnoreCase(args[0])
                        ? increment(args[1])
                        : decrement(args[1]);
                System.out.println(result);
            } catch (IllegalArgumentException exception) {
                System.err.println("Error: " + exception.getMessage());
            }
            return;
        }

        if (args.length == 2) {
            try {
                for (String version : generate(args[0], args[1])) {
                    System.out.println(version);
                }
            } catch (IllegalArgumentException exception) {
                System.err.println("Error: " + exception.getMessage());
            }
            return;
        }

        System.out.println("Usage: java VersionRange <start-version> <end-version>");
        System.out.println("   or: java VersionRange <increment|decrement> <version>");
    }
}

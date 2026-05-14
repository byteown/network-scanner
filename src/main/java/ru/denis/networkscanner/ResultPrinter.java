package ru.denis.networkscanner;

import java.util.List;
import java.util.Locale;

public class ResultPrinter {

    public void print(List<CheckResult> results, long totalElapsedMs) {
        int aliveCount = 0;
        int deadCount = 0;
        int errorCount = 0;

        for (CheckResult result : results) {
            System.out.println(formatLine(result));

            switch (result.status()) {
                case ALIVE -> aliveCount++;
                case DEAD -> deadCount++;
                case ERROR -> errorCount++;
            }
        }

        System.out.printf("%nTotal: %d / Alive: %d / Dead: %d / Errors: %d%n",
                results.size(), aliveCount, deadCount, errorCount);
        System.out.printf(Locale.ROOT, "Scan completed in %.2f seconds%n",
                totalElapsedMs / 1000.0);
    }

    private String formatLine(CheckResult result) {
        return switch (result.status()) {
            case ALIVE -> String.format("<%s> -> ALIVE (%d ms)",
                    result.host(), result.elapsedMs());
            case DEAD -> String.format("<%s> -> DEAD (%d ms)",
                    result.host(), result.elapsedMs());
            case ERROR -> String.format("<%s> -> ERROR: %s (%d ms)",
                    result.host(), result.errorMessage(), result.elapsedMs());
        };
    }
}

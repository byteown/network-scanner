package ru.denis.networkscanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    private static final String TIMEOUT_FLAG = "--timeout=";
    private static final String USAGE = "Usage: java Main [--timeout=ms] <cidr>";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(USAGE);
            return;
        }

        int timeoutMs = 1000;
        String cidr = null;

        for (String arg : args) {
            if (arg.startsWith(TIMEOUT_FLAG)) {
                String value = arg.substring(TIMEOUT_FLAG.length());
                try {
                    timeoutMs = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    System.out.printf("Invalid timeout value: '%s'. Must be a number.%n", value);
                    return;
                }
            } else {
                cidr = arg;
            }
        }

        if (cidr == null) {
            System.out.println(USAGE);
            return;
        }

        CidrRange cidrRange = new CidrRange(cidr);
        List<String> hosts = cidrRange.listHosts();

        int aliveCount = 0;
        int deadCount = 0;
        int errorCount = 0;
        int threadPoolSize = 50;
        long timeNow = System.nanoTime();

        List<Future<CheckResult>> futures = new ArrayList<>();

        try (ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize)) {
            for (String host : hosts) {
                int finalTimeoutMs = timeoutMs;
                Future<CheckResult> future = executor.submit(() -> HostChecker.check(host, finalTimeoutMs));
                futures.add(future);
            }

            for (Future<CheckResult> future : futures) {
                try {
                    CheckResult result = future.get();
                    String line = switch (result.status()) {
                        case ALIVE -> {
                            aliveCount++;
                            yield String.format("<%s> -> ALIVE (%d ms)", result.host(), result.elapsedMs());
                        }
                        case DEAD -> {
                            deadCount++;
                            yield String.format("<%s> -> DEAD (%d ms)", result.host(), result.elapsedMs());
                        }
                        case ERROR -> {
                            errorCount++;
                            yield String.format("<%s> -> ERROR: %s (%d ms)", result.host(), result.errorMessage(), result.elapsedMs());
                        }
                    };
                    System.out.println(line);
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    System.err.println("Task failed: " + cause.getMessage());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Interrupted while waiting");
                    return;
                }
            }
        }

        long elapsedMs = (System.nanoTime() - timeNow) / 1_000_000;

        System.out.printf("%nTotal: %d / Alive: %d / Dead: %d / Errors: %d", hosts.size(), aliveCount, deadCount, errorCount);
        System.out.printf(Locale.ROOT, "%nScan completed in %.2f seconds", elapsedMs / 1000.0);
    }
}

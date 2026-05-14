package ru.denis.networkscanner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NetworkScanner {
    private final int timeoutMs;
    private final int threadPoolSize;

    public NetworkScanner(int timeoutMs, int threadPoolSize) {
        this.timeoutMs = timeoutMs;
        this.threadPoolSize = threadPoolSize;
    }

    public List<CheckResult> scan(List<String> hosts) {
        List<Future<CheckResult>> futures = new ArrayList<>();
        List<CheckResult> results = new ArrayList<>();

        try (ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize)) {
            for (String host : hosts) {
                futures.add(executor.submit(() -> HostChecker.check(host, timeoutMs)));
            }

            for (Future<CheckResult> future : futures) {
                try {
                    results.add(future.get());
                } catch (ExecutionException e) {
                    throw new RuntimeException("Unexpected task failure", e);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Scan interrupted", e);
                }
            }
        }

        return results;
    }
}

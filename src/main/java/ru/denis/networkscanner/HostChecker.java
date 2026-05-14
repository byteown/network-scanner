package ru.denis.networkscanner;

import java.io.IOException;
import java.net.InetAddress;

public class HostChecker {

    public static CheckResult check(String host, int timeoutMs) {
        long startNanos = System.nanoTime();
        try {
            InetAddress address = InetAddress.getByName(host);
            boolean reachable = address.isReachable(timeoutMs);
            long elapsedMs = (System.nanoTime() - startNanos) / 1_000_000;
            return new CheckResult(host, reachable ? Status.ALIVE : Status.DEAD, elapsedMs, null);
        } catch (IOException e) {
            long elapsedMs = (System.nanoTime() - startNanos) / 1_000_000;
            return new CheckResult(host, Status.ERROR, elapsedMs, e.getMessage());
        }
    }

    public enum Status {
        ALIVE, DEAD, ERROR
    }
}

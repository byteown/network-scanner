package ru.denis.networkscanner;

import java.util.List;
import java.util.Locale;

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
        long timeNow = System.nanoTime();
        for (String host : hosts) {
            CheckResult result = HostChecker.check(host, timeoutMs);
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
        }
        long elapsedMs = (System.nanoTime() - timeNow) / 1_000_000;
        System.out.printf("%nTotal: %d / Alive: %d / Dead: %d / Errors: %d", hosts.size(), aliveCount, deadCount, errorCount);
        System.out.printf(Locale.ROOT, "%nScan completed in %.2f seconds", elapsedMs / 1000.0);
    }
}

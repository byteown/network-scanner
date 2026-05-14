package ru.denis.networkscanner;

import java.util.List;

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
        int alive = 0;
        int dead = 0;
        int error = 0;
        long timeNow = System.nanoTime();
        for (String host : hosts) {
            CheckResult result = HostChecker.check(host, timeoutMs);
            String line = "";
            switch (result.status()) {
                case ALIVE -> {
                    line = String.format("<%s> -> ALIVE (%d ms)", result.host(), result.elapsedMs());
                    alive++;
                }
                case DEAD -> {
                    line = String.format("<%s> -> DEAD (%d ms)", result.host(), result.elapsedMs());
                    dead++;
                }
                case ERROR -> {
                    line = String.format("<%s> -> ERROR: %s (%d ms)", result.host(), result.errorMessage(), result.elapsedMs());
                    error++;
                }
            }
            System.out.println(line);
        }
        long elapsedTime = (System.nanoTime() - timeNow) / 1_000_000_000;
        System.out.printf("\nTotal: %d / Alive: %d / Dead: %d / Errors: %d", hosts.size(), alive, dead, error);
        System.out.printf("\nScan completed in %d seconds", elapsedTime);
    }
}

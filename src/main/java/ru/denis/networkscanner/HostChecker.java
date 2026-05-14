package ru.denis.networkscanner;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class HostChecker {
    private static final String TIMEOUT_FLAG = "--timeout=";
    private static final String USAGE = "Usage: java HostChecker [--timeout=ms] <ip1> <ip2> ...";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(USAGE);
            return;
        }

        List<String> ips = new ArrayList<>();
        int timeoutMs = 1000;
        int success = 0;
        int failed = 0;

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
                ips.add(arg);
            }
        }

        if (ips.isEmpty()) {
            System.out.println(USAGE);
            return;
        }

        for (String host : ips) {
            try {
                long startNanos = System.nanoTime();
                InetAddress address = InetAddress.getByName(host);
                boolean reachable = address.isReachable(timeoutMs);
                long elapsedMs = (System.nanoTime() - startNanos) / 1_000_000;
                System.out.printf("<%s> -> %s (%d ms)%n", host, reachable ? "ALIVE" : "DEAD", elapsedMs);
                if (reachable) {
                    success++;
                } else {
                    failed++;
                }
            } catch (IOException e) {
                System.out.printf("<%s> -> ERROR: %s%n", host, e.getMessage());
            }
        }

        System.out.println("\nTotal: "+ips.size()+", alive: "+success+", dead: "+failed);
    }
}

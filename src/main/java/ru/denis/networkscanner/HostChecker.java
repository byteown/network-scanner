package ru.denis.networkscanner;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class HostChecker {
    private static final String TIMEOUT_FLAG = "--timeout=";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java HostChecker --timeout=500 <ip1> <ip2> ...");
            return;
        }

        List<String> ips = new ArrayList<>();
        int timeoutMs = 1000;
        int success = 0;
        int failed = 0;

        for (String arg : args) {
            if (arg.contains(TIMEOUT_FLAG)) {
                timeoutMs = Integer.parseInt(arg.substring(TIMEOUT_FLAG.length()));
            } else {
                ips.add(arg);
            }
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

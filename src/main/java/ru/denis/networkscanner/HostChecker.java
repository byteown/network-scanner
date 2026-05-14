package ru.denis.networkscanner;

import java.io.IOException;
import java.net.InetAddress;

public class HostChecker {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java HostChecker <ip1> <ip2> ...");
            return;
        }

        int timeoutMs = 1000;

        int success = 0;
        int failed = 0;

        for (String host : args) {
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

        System.out.println("\nTotal: "+args.length+", alive: "+success+", dead: "+failed);
    }
}

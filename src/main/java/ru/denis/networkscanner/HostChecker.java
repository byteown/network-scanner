package ru.denis.networkscanner;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;

public class HostChecker {
    public static void main(String[] args) {
        String[] ips = new String[args.length-1];
        int timeoutMs = 1000;
        int success = 0;
        int failed = 0;

        if (args.length == 0) {
            System.out.println("Usage: java HostChecker --timeout=500 <ip1> <ip2> ...");
            return;
        } else {
            timeoutMs = Integer.parseInt(args[0].substring(10));
            for (int i = 1; i < args.length; i++) {
                ips[i-1] = args[i];
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

        System.out.println("\nTotal: "+ips.length+", alive: "+success+", dead: "+failed);
    }
}

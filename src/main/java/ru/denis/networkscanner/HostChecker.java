package ru.denis.networkscanner;

import java.io.IOException;
import java.net.InetAddress;

public class HostChecker {
    public static void main(String[] args) {
        String[] ipArr = {"8.8.8.8", "1.1.1.1", "192.168.0.1", "10.255.255.1"};
        int timeoutMs = 1000;

        int success = 0;
        int failed = 0;

        for (String host : ipArr) {
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

        System.out.println("\nTotal: "+ipArr.length+", alive: "+success+", dead: "+failed);
    }
}

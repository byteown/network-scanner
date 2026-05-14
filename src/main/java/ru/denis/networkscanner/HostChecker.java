package ru.denis.networkscanner;

import java.io.IOException;
import java.net.InetAddress;

public class HostChecker {
    public static void main(String[] args) {
        String[] ipArr = {"8.8.8.8", "1.1.1.1", "192.168.0.1", "10.255.255.1"};
        int timeoutMs = 1000;

        int total = ipArr.length;
        int success = 0;
        int failed = 0;

        for (String host : ipArr) {
            try {
                long currentTime = System.currentTimeMillis();
                InetAddress address = InetAddress.getByName(host);
                boolean reachable = address.isReachable(timeoutMs);
                long pastTime = System.currentTimeMillis();
                System.out.println("<" + host + "> -> " + (reachable ? "ALIVE" : "DEAD") + " ("+(pastTime-currentTime)+" ms)");
                if (reachable) {
                    success++;
                } else {
                    failed++;
                }
            } catch (IOException e) {
                System.out.println("<"+host+"> -> ERROR: "+e);
            }
        }

        System.out.println("\nTotal: "+total+", alive: "+success+", dead: "+failed);
    }
}

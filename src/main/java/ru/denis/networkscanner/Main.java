package ru.denis.networkscanner;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        CliArgs cliArgs;
        try {
            cliArgs = CliParser.parse(args);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.exit(1);
            return;
        }

        CidrRange cidrRange;
        try {
            cidrRange = new CidrRange(cliArgs.cidr());
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.exit(1);
            return;
        }

        List<String> hosts = cidrRange.listHosts();

        NetworkScanner scanner = new NetworkScanner(
                cliArgs.timeoutMs(), cliArgs.threadPoolSize()
        );
        ResultPrinter printer = new ResultPrinter();

        long startNanos = System.nanoTime();
        List<CheckResult> results = scanner.scan(hosts);
        long elapsedMs = (System.nanoTime() - startNanos) / 1_000_000;

        printer.print(results, elapsedMs);
    }
}

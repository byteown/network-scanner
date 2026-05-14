package ru.denis.networkscanner;

public class CliParser {
    private static final String TIMEOUT_FLAG = "--timeout=";
    private static final String THREADS_FLAG = "--threads=";

    private static final int DEFAULT_TIMEOUT_MS = 1000;
    private static final int DEFAULT_THREAD_POOL_SIZE = 50;

    public static final String USAGE =
            "Usage: java -jar scanner.jar [--timeout=ms] [--threads=N] <cidr>";

    public static CliArgs parse(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException(USAGE);
        }

        int timeoutMs = DEFAULT_TIMEOUT_MS;
        int threadPoolSize = DEFAULT_THREAD_POOL_SIZE;
        String cidr = null;

        for (String arg : args) {
            if (arg.startsWith(TIMEOUT_FLAG)) {
                timeoutMs = parseIntFlag(arg, TIMEOUT_FLAG);
            } else if (arg.startsWith(THREADS_FLAG)) {
                threadPoolSize = parseIntFlag(arg, THREADS_FLAG);
            } else {
                cidr = arg;
            }
        }

        if (cidr == null) {
            throw new IllegalArgumentException(USAGE);
        }

        return new CliArgs(cidr, timeoutMs, threadPoolSize);
    }

    private static int parseIntFlag(String arg, String flag) {
        String value = arg.substring(flag.length());
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Ivalid value for " + flag + ": '" + value + "'. Must be a number."
            );
        }
    }
}

package ru.denis.networkscanner;

public record CliArgs(String cidr, int timeoutMs, int threadPoolSize) {
}

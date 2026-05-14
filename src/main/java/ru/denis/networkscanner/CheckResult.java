package ru.denis.networkscanner;

public record CheckResult(String host, HostChecker.Status status, long elapsedMs, String errorMessage) {
}

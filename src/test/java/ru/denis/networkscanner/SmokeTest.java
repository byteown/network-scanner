package ru.denis.networkscanner;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SmokeTest {

    @Test
    void simpleAssertionShouldPass() {
        int result = 2 + 2;
        assertThat(result).isEqualTo(4);
    }
}

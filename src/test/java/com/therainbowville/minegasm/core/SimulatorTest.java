package com.therainbowville.minegasm.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SimulatorTest {
    @Test
    void appHasAGreeting() {
        Simulator classUnderTest = new Simulator();
        assertNotNull(classUnderTest.getGreeting(), "app should have a greeting");
    }
}

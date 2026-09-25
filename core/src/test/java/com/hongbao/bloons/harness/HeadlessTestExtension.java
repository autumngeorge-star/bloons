package com.hongbao.bloons.harness;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * JUnit 5 Extension that initializes the headless application context
 * before executing tests in a test class.
 */
public class HeadlessTestExtension implements BeforeAllCallback {
    @Override
    public void beforeAll(ExtensionContext context) {
        HeadlessTestHarness.initialize();
    }
}

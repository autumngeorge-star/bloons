package com.hongbao.bloons.harness;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import org.mockito.Mockito;

/**
 * Headless test harness to manage LibGDX graphics, file system,
 * and application context during headless unit test runs.
 */
public class HeadlessTestHarness {
    private static Application application;

    public static synchronized void initialize() {
        if (application == null) {
            HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
            application = new HeadlessApplication(new ApplicationAdapter() {}, config);
            GL20 gl = Mockito.mock(GL20.class);
            Gdx.gl = gl;
            Gdx.gl20 = gl;
        }
    }

    public static Application getApplication() {
        initialize();
        return application;
    }
}

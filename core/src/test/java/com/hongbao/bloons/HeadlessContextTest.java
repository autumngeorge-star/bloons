package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.hongbao.bloons.harness.HeadlessTestExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(HeadlessTestExtension.class)
public class HeadlessContextTest {

    @Test
    @DisplayName("Headless application context provides non-null Gdx references")
    void testHeadlessApplicationContext() {
        assertNotNull(Gdx.app, "Gdx.app should be initialized in headless context");
        assertNotNull(Gdx.graphics, "Gdx.graphics should be initialized in headless context");
        assertNotNull(Gdx.files, "Gdx.files should be initialized in headless context");
        assertNotNull(Gdx.gl, "Gdx.gl should be mocked/initialized in headless context");
    }

    @Test
    @DisplayName("Internal files can be resolved in headless test harness")
    void testFileAccessInHeadlessContext() {
        assertNotNull(Gdx.files.internal("img/bloons/red_bloon.png"));
    }
}

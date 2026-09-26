package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class TextureAtlasTest {

    @BeforeClass
    public static void setUp() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        new HeadlessApplication(new ApplicationAdapter() {}, config);
        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = Gdx.gl;
    }

    @Test
    public void testAtlasRegionLookups() {
        AssetManager assetManager = new AssetManager();
        assetManager.load("assets/game.atlas", TextureAtlas.class);
        assetManager.finishLoading();

        TextureAtlas atlas = assetManager.get("assets/game.atlas", TextureAtlas.class);
        assertNotNull("TextureAtlas should not be null", atlas);

        // Test bloons regions
        assertNotNull("red_bloon region should exist", atlas.findRegion("bloons/red_bloon"));
        assertNotNull("blue_bloon region should exist", atlas.findRegion("bloons/blue_bloon"));
        assertNotNull("moab_bloon region should exist", atlas.findRegion("bloons/moab_bloon"));

        // Test character regions
        assertNotNull("reimu character region should exist", atlas.findRegion("characters/reimu"));
        assertNotNull("yukari character region should exist", atlas.findRegion("characters/yukari"));

        // Test projectile regions
        assertNotNull("red_spell_card projectile region should exist", atlas.findRegion("projectiles/red_spell_card"));
        assertNotNull("purple_energy projectile region should exist", atlas.findRegion("projectiles/purple_energy"));

        // Test UI regions
        assertNotNull("header UI region should exist", atlas.findRegion("ui/header"));
        assertNotNull("reimu_box UI region should exist", atlas.findRegion("ui/reimu_box"));

        // Test maps regions
        assertNotNull("heater map region should exist", atlas.findRegion("maps/heater"));

        assetManager.dispose();
    }
}

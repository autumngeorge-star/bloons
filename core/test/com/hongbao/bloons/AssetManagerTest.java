package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class AssetManagerTest {

    private static HeadlessApplication application;

    @BeforeClass
    public static void setUp() {
        Gdx.gl = Mockito.mock(GL20.class);
        Gdx.gl20 = Gdx.gl;
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        application = new HeadlessApplication(new com.badlogic.gdx.ApplicationAdapter() {}, config);
    }

    @AfterClass
    public static void tearDown() {
        if (application != null) {
            application.exit();
        }
    }

    @Test
    public void testAssetManagerLifecycle() {
        AssetManager assetManager = new AssetManager();

        // Queue assets
        assetManager.load("uiskins/uiskin.json", Skin.class);
        assetManager.load("music/title.mp3", Music.class);
        assetManager.load("music/demystify_feast.mp3", Music.class);
        assetManager.load("music/night_falls.mp3", Music.class);
        assetManager.load("music/pop.mp3", Sound.class);
        assetManager.load("img/ui/header.png", Texture.class);
        assetManager.load("img/ui/girl_details_template.png", Texture.class);
        assetManager.load("img/characters/reimu.png", Texture.class);
        assetManager.load("img/bloons/red_bloon.png", Texture.class);

        // Load synchronously
        assetManager.finishLoading();

        // Assert loaded
        assertTrue("Skin uiskins/uiskin.json should be loaded", assetManager.isLoaded("uiskins/uiskin.json", Skin.class));
        assertTrue("Music music/title.mp3 should be loaded", assetManager.isLoaded("music/title.mp3", Music.class));
        assertTrue("Music music/demystify_feast.mp3 should be loaded", assetManager.isLoaded("music/demystify_feast.mp3", Music.class));
        assertTrue("Music music/night_falls.mp3 should be loaded", assetManager.isLoaded("music/night_falls.mp3", Music.class));
        assertTrue("Sound music/pop.mp3 should be loaded", assetManager.isLoaded("music/pop.mp3", Sound.class));
        assertTrue("Header texture should be loaded", assetManager.isLoaded("img/ui/header.png", Texture.class));
        assertTrue("Girl details template should be loaded", assetManager.isLoaded("img/ui/girl_details_template.png", Texture.class));
        assertTrue("Reimu texture should be loaded", assetManager.isLoaded("img/characters/reimu.png", Texture.class));
        assertTrue("Red bloon texture should be loaded", assetManager.isLoaded("img/bloons/red_bloon.png", Texture.class));

        // Test MusicPlayer queries AssetManager
        MusicPlayer musicPlayer = new MusicPlayer(assetManager);
        musicPlayer.playTitleMusic();
        musicPlayer.playStageMusic();
        musicPlayer.playFinalBossMusic();
        musicPlayer.stopMusic();

        // Verify disposal
        assetManager.dispose();
        assertEquals("Loaded assets count should be zero after assetManager.dispose()", 0, assetManager.getLoadedAssets());
    }
}

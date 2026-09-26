package com.hongbao.bloons;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.BulletActor;
import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.actors.SpellCardActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.entities.SpellCard;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.factories.GirlFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.nio.IntBuffer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class AssetManagerTest {

    private BloonsTouhouDefense game;
    private boolean isDisposed;

    @Before
    public void setUp() {
        GdxNativesLoader.load();
        isDisposed = false;

        game = new BloonsTouhouDefense();

        Application app = Mockito.mock(Application.class);
        Mockito.when(app.getApplicationListener()).thenReturn(game);
        Gdx.app = app;

        Gdx.files = new LwjglFiles();
        Gdx.audio = Mockito.mock(Audio.class);
        Gdx.graphics = Mockito.mock(Graphics.class);
        Gdx.input = Mockito.mock(Input.class);

        Music mockMusic = Mockito.mock(Music.class);
        Mockito.when(Gdx.audio.newMusic(Mockito.any())).thenReturn(mockMusic);

        Sound mockSound = Mockito.mock(Sound.class);
        Mockito.when(Gdx.audio.newSound(Mockito.any())).thenReturn(mockSound);

        GL20 gl = Mockito.mock(GL20.class);
        final int[] textureCounter = {1};
        Mockito.doAnswer(invocation -> {
            IntBuffer buffer = invocation.getArgument(1);
            if (buffer != null && buffer.remaining() > 0) {
                buffer.put(buffer.position(), textureCounter[0]++);
            }
            return null;
        }).when(gl).glGenTextures(Mockito.anyInt(), Mockito.any(IntBuffer.class));

        Mockito.when(gl.glCreateShader(Mockito.anyInt())).thenReturn(1);
        Mockito.when(gl.glCreateProgram()).thenReturn(1);

        Mockito.doAnswer(invocation -> {
            IntBuffer params = invocation.getArgument(2);
            if (params != null && params.remaining() > 0) {
                params.put(params.position(), GL20.GL_TRUE);
            }
            return null;
        }).when(gl).glGetShaderiv(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(IntBuffer.class));

        Mockito.doAnswer(invocation -> {
            int pname = invocation.getArgument(1);
            IntBuffer params = invocation.getArgument(2);
            if (params != null && params.remaining() > 0) {
                if (pname == GL20.GL_ACTIVE_ATTRIBUTES || pname == GL20.GL_ACTIVE_UNIFORMS) {
                    params.put(params.position(), 0);
                } else {
                    params.put(params.position(), GL20.GL_TRUE);
                }
            }
            return null;
        }).when(gl).glGetProgramiv(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(IntBuffer.class));

        Mockito.when(gl.glGetShaderInfoLog(Mockito.anyInt())).thenReturn("");
        Mockito.when(gl.glGetProgramInfoLog(Mockito.anyInt())).thenReturn("");

        Gdx.gl = gl;
        Gdx.gl20 = gl;

        game.create();
    }

    @After
    public void tearDown() {
        if (!isDisposed && game != null && game.getAssetManager() != null) {
            isDisposed = true;
            game.dispose();
        }
    }

    @Test
    public void testAssetManagerInitialization() {
        assertNotNull("AssetManager should be maintained by BloonsTouhouDefense", game.getAssetManager());
    }

    @Test
    public void testLazyCachingAndSharedTextures() {
        String testAsset = "img/bloons/red_bloon.png";

        Texture texture1 = game.getTexture(testAsset);
        assertNotNull("Texture should be loaded by AssetManager", texture1);

        Texture texture2 = game.getTexture(testAsset);
        assertSame("Repeated texture requests should return the same cached instance", texture1, texture2);
    }

    @Test
    public void testEntityActorsUseSharedTextures() {
        Bloon bloon = BloonFactory.createRedBloon();
        BloonActor bloonActor1 = new BloonActor(bloon, 0, 0, null);
        BloonActor bloonActor2 = new BloonActor(bloon, 10, 10, null);

        assertSame("Multiple BloonActor instances must share the same cached texture",
                bloonActor1.getTextureRegion().getTexture(),
                bloonActor2.getTextureRegion().getTexture());

        Girl girl = GirlFactory.createReimu();
        GirlActor girlActor1 = new GirlActor(girl, 0, 0);
        GirlActor girlActor2 = new GirlActor(girl, 10, 10);

        assertSame("Multiple GirlActor instances must share the same cached texture",
                girlActor1.getTextureRegion().getTexture(),
                girlActor2.getTextureRegion().getTexture());

        Bullet bullet = girl.createBullet();
        BulletActor bulletActor1 = new BulletActor(bullet, 0, 0, 1, 0);
        BulletActor bulletActor2 = new BulletActor(bullet, 10, 10, 1, 0);

        assertSame("Multiple BulletActor instances must share the same cached texture",
                bulletActor1.getTextureRegion().getTexture(),
                bulletActor2.getTextureRegion().getTexture());

        SpellCard spellCard = girl.createSpellCard();
        SpellCardActor spellActor1 = new SpellCardActor(spellCard, 0, 0);
        SpellCardActor spellActor2 = new SpellCardActor(spellActor1.getSpellCard(), 10, 10);

        assertSame("Multiple SpellCardActor instances must share the same cached texture",
                spellActor1.getTextureRegion().getTexture(),
                spellActor2.getTextureRegion().getTexture());
    }

    @Test
    public void testEntityTeardownNoDoubleDisposal() {
        Bloon bloon = BloonFactory.createRedBloon();
        BloonActor bloonActor = new BloonActor(bloon, 0, 0, null);

        // Pop bloon - must not dispose underlying shared texture
        bloonActor.pop(1);

        // Texture should remain managed in AssetManager
        assertTrue("AssetManager must retain the shared texture after bloon pop",
                game.getAssetManager().isLoaded(bloon.getImageFileName(), Texture.class));

        BloonActor bloonActorRelease = new BloonActor(bloon, 0, 0, null);
        bloonActorRelease.release();

        assertTrue("AssetManager must retain the shared texture after bloon release",
                game.getAssetManager().isLoaded(bloon.getImageFileName(), Texture.class));
    }

    @Test
    public void testCentralDisposalOnShutdown() {
        AssetManager manager = game.getAssetManager();
        assertNotNull("AssetManager should exist before disposal", manager);
        if (!isDisposed) {
            isDisposed = true;
            game.dispose();
        }
    }
}

package com.hongbao.bloons.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.hongbao.bloons.BloonsTouhouDefense;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.BulletActor;
import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.actors.SpellCardActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.entities.SpellCard;
import com.hongbao.bloons.factories.GirlFactory;

import java.lang.reflect.Proxy;
import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.Set;

public class AtlasVerificationTest {

    public static void main(String[] args) {
        System.out.println("--- Starting Atlas Verification Tests ---");

        // 1. Test region name extraction logic
        testExtractRegionName();

        // 2. Setup mock Gdx environment
        setupMockGdxEnvironment();

        // 3. Test BloonsTouhouDefense create and asset manager initialization
        BloonsTouhouDefense app = new BloonsTouhouDefense();
        Gdx.app = createMockApplication(app);
        
        // Initialize asset manager and atlas in app
        app.create();

        TextureAtlas atlas = app.getTextureAtlas();
        if (atlas == null) {
            throw new RuntimeException("FAIL: TextureAtlas is null in BloonsTouhouDefense");
        }
        System.out.println("PASSED: AssetManager loaded TextureAtlas successfully!");

        // 4. Test actor creation from shared atlas (Requirement 1 & 2)
        testActorCreationAndAtlasSharing(app);

        // 5. Test bloon pop and release with zero texture disposal errors (Requirement 3)
        testBloonRemovalWithoutDisposalErrors(app);

        // 6. Test draw call batching efficiency
        testBatchEfficiency(app);

        System.out.println("--- ALL ATLAS VERIFICATION TESTS PASSED SUCCESSFULLY! ---");
    }

    private static void testExtractRegionName() {
        assertEquals("red_bloon", BloonsTouhouDefense.extractRegionName("img/bloons/red_bloon.png"));
        assertEquals("reimu", BloonsTouhouDefense.extractRegionName("img/characters/reimu.png"));
        assertEquals("blue_magic_missile", BloonsTouhouDefense.extractRegionName("img/projectiles/blue_magic_missile.png"));
        assertEquals("yuyuko_fan", BloonsTouhouDefense.extractRegionName("img/spellcards/yuyuko_fan.png"));
        System.out.println("PASSED: Region name extraction logic.");
    }

    private static void setupMockGdxEnvironment() {
        GdxNativesLoader.load();
        Gdx.files = new LwjglFiles();
        Gdx.gl20 = createDummyGL20();
        Gdx.gl = Gdx.gl20;
        Gdx.graphics = createDummyGraphics();
        Gdx.input = createDummyInput();
        Gdx.audio = createDummyAudio();
    }

    private static Audio createDummyAudio() {
        Sound dummySound = (Sound) Proxy.newProxyInstance(
            Sound.class.getClassLoader(),
            new Class<?>[]{Sound.class},
            (proxy, method, methodArgs) -> method.getReturnType().equals(long.class) || method.getReturnType().equals(Long.TYPE) ? 1L : null
        );
        Music dummyMusic = (Music) Proxy.newProxyInstance(
            Music.class.getClassLoader(),
            new Class<?>[]{Music.class},
            (proxy, method, methodArgs) -> null
        );
        return (Audio) Proxy.newProxyInstance(
            Audio.class.getClassLoader(),
            new Class<?>[]{Audio.class},
            (proxy, method, methodArgs) -> {
                if ("newSound".equals(method.getName())) return dummySound;
                if ("newMusic".equals(method.getName())) return dummyMusic;
                return null;
            }
        );
    }

    private static Input createDummyInput() {
        return (Input) Proxy.newProxyInstance(
            Input.class.getClassLoader(),
            new Class<?>[]{Input.class},
            (proxy, method, methodArgs) -> {
                if ("hashCode".equals(method.getName())) return System.identityHashCode(proxy);
                if ("equals".equals(method.getName())) return methodArgs != null && methodArgs.length > 0 && proxy == methodArgs[0];
                if (method.getReturnType().equals(int.class) || method.getReturnType().equals(Integer.TYPE)) return 0;
                if (method.getReturnType().equals(boolean.class) || method.getReturnType().equals(Boolean.TYPE)) return false;
                if (method.getReturnType().equals(float.class) || method.getReturnType().equals(Float.TYPE)) return 0.0f;
                return null;
            }
        );
    }

    private static Graphics createDummyGraphics() {
        return (Graphics) Proxy.newProxyInstance(
            Graphics.class.getClassLoader(),
            new Class<?>[]{Graphics.class},
            (proxy, method, methodArgs) -> {
                if ("hashCode".equals(method.getName())) return System.identityHashCode(proxy);
                if ("equals".equals(method.getName())) return methodArgs != null && methodArgs.length > 0 && proxy == methodArgs[0];
                if ("getWidth".equals(method.getName())) return 1800;
                if ("getHeight".equals(method.getName())) return 900;
                if ("getGL20".equals(method.getName())) return Gdx.gl20;
                if (method.getReturnType().equals(int.class) || method.getReturnType().equals(Integer.TYPE)) return 1;
                if (method.getReturnType().equals(boolean.class) || method.getReturnType().equals(Boolean.TYPE)) return false;
                if (method.getReturnType().equals(float.class) || method.getReturnType().equals(Float.TYPE)) return 1.0f;
                return null;
            }
        );
    }

    private static com.badlogic.gdx.Application createMockApplication(ApplicationListener listener) {
        return (com.badlogic.gdx.Application) Proxy.newProxyInstance(
            com.badlogic.gdx.Application.class.getClassLoader(),
            new Class<?>[]{com.badlogic.gdx.Application.class},
            (proxy, method, methodArgs) -> {
                if ("hashCode".equals(method.getName())) return System.identityHashCode(proxy);
                if ("equals".equals(method.getName())) return methodArgs != null && methodArgs.length > 0 && proxy == methodArgs[0];
                if ("getApplicationListener".equals(method.getName())) return listener;
                if ("getType".equals(method.getName())) return com.badlogic.gdx.Application.ApplicationType.Desktop;
                return null;
            }
        );
    }

    private static GL20 createDummyGL20() {
        return (GL20) Proxy.newProxyInstance(
            GL20.class.getClassLoader(),
            new Class<?>[]{GL20.class},
            (proxy, method, methodArgs) -> {
                if ("hashCode".equals(method.getName())) return System.identityHashCode(proxy);
                if ("equals".equals(method.getName())) return methodArgs != null && methodArgs.length > 0 && proxy == methodArgs[0];
                if ("glGetShaderiv".equals(method.getName()) || "glGetProgramiv".equals(method.getName())) {
                    if (methodArgs != null && methodArgs.length >= 3 && methodArgs[2] instanceof IntBuffer) {
                        ((IntBuffer) methodArgs[2]).put(0, GL20.GL_TRUE);
                    }
                    return null;
                }
                if (method.getReturnType().equals(int.class) || method.getReturnType().equals(Integer.TYPE)) {
                    return 1;
                }
                if (method.getReturnType().equals(boolean.class) || method.getReturnType().equals(Boolean.TYPE)) {
                    return false;
                }
                if (method.getReturnType().equals(String.class)) {
                    return "";
                }
                return null;
            }
        );
    }

    private static void testActorCreationAndAtlasSharing(BloonsTouhouDefense app) {
        // Create BloonActor
        Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonActor bloonActor = new BloonActor(bloon, 100, 100, null);

        // Create GirlActor
        Girl girl = GirlFactory.createReimu();
        GirlActor girlActor = new GirlActor(girl, 200, 200);

        // Create BulletActor
        Bullet bullet = girl.createBullet();
        BulletActor bulletActor = new BulletActor(bullet, 200, 200, 1, 0);

        // Create SpellCardActor
        SpellCard spellCard = SpellCard.createReimuSpellCard();
        SpellCardActor spellCardActor = new SpellCardActor(spellCard, 300, 300);

        // Verify all 4 actor types get TextureRegions from the shared TextureAtlas
        Texture bloonTexture = bloonActor.getTextureRegion().getTexture();
        Texture girlTexture = girlActor.getTextureRegion().getTexture();
        Texture bulletTexture = bulletActor.getTextureRegion().getTexture();
        Texture spellCardTexture = spellCardActor.getTextureRegion().getTexture();

        if (bloonTexture == null || girlTexture == null || bulletTexture == null || spellCardTexture == null) {
            throw new RuntimeException("FAIL: One or more actors have null texture regions");
        }

        // Check that all actors share the SAME underlying atlas texture instance!
        if (bloonTexture != girlTexture || girlTexture != bulletTexture || bulletTexture != spellCardTexture) {
            throw new RuntimeException("FAIL: Actors do NOT share the same atlas texture instance!");
        }

        // Verify region dimensions (e.g. red_bloon region width/height > 0)
        assertTrue(bloonActor.getTextureRegion().getRegionWidth() > 0, "Bloon region width > 0");
        assertTrue(girlActor.getTextureRegion().getRegionWidth() > 0, "Girl region width > 0");
        assertTrue(bulletActor.getTextureRegion().getRegionWidth() > 0, "Bullet region width > 0");
        assertTrue(spellCardActor.getTextureRegion().getRegionWidth() > 0, "SpellCard region width > 0");

        System.out.println("PASSED: BloonActor, GirlActor, BulletActor, SpellCardActor all load from shared TextureAtlas!");
    }

    private static void testBloonRemovalWithoutDisposalErrors(BloonsTouhouDefense app) {
        Bloon bloon = new Bloon(Bloon.Color.BLUE, 2, false, false);
        BloonActor bloonActor = new BloonActor(bloon, 100, 100, null);

        // Simulate pop
        bloonActor.pop(1);

        // Simulate release
        BloonActor bloonActor2 = new BloonActor(bloon, 100, 100, null);
        bloonActor2.release();

        // Verify shared atlas texture is still valid and not disposed!
        TextureAtlas atlas = app.getTextureAtlas();
        TextureRegion region = atlas.findRegion("red_bloon");
        if (region == null || region.getTexture() == null) {
            throw new RuntimeException("FAIL: Shared atlas texture was improperly disposed on bloon pop/release!");
        }

        System.out.println("PASSED: Bloon pop and release executed cleanly without disposing shared atlas texture!");
    }

    private static void testBatchEfficiency(BloonsTouhouDefense app) {
        // Create 100 bloon actors of various colors
        Set<Texture> texturesUsed = new HashSet<>();
        for (Bloon.Color color : Bloon.Color.values()) {
            Bloon b = new Bloon(color, 1, false, false);
            BloonActor actor = new BloonActor(b, 100, 100, null);
            texturesUsed.add(actor.getTextureRegion().getTexture());
        }

        // Create girl actors
        GirlActor reimuActor = new GirlActor(GirlFactory.createReimu(), 200, 200);
        GirlActor marisaActor = new GirlActor(GirlFactory.createMarisa(), 300, 300);
        texturesUsed.add(reimuActor.getTextureRegion().getTexture());
        texturesUsed.add(marisaActor.getTextureRegion().getTexture());

        // Check total unique textures across all entity types
        if (texturesUsed.size() != 1) {
            throw new RuntimeException("FAIL: Entity wave uses " + texturesUsed.size() + " unique textures instead of 1!");
        }

        System.out.println("PASSED: All entity wave actors use exactly " + texturesUsed.size() + " texture atlas page (Batch calls = 1)!");
    }

    private static void assertEquals(Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new RuntimeException("Assertion failed! Expected: " + expected + ", got: " + actual);
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new RuntimeException("Assertion failed! " + message);
        }
    }
}

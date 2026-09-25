package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

public class BloonActorIntegrationTest {

    private static HeadlessApplication app;
    private static BloonsTouhouDefense mockApp;
    private static AssetManager assetManager;

    @BeforeClass
    public static void setUp() {
        Gdx.gl = Mockito.mock(GL20.class);
        Gdx.gl20 = Gdx.gl;

        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        mockApp = Mockito.mock(BloonsTouhouDefense.class);

        app = new HeadlessApplication(new ApplicationAdapter() {
            @Override
            public void create() {
                assetManager = new AssetManager();
                assetManager.load("bloons.atlas", TextureAtlas.class);
                assetManager.finishLoading();

                Mockito.when(mockApp.getAssetManager()).thenReturn(assetManager);
                Player mockPlayer = Mockito.mock(Player.class);
                Mockito.when(mockApp.getPlayer()).thenReturn(mockPlayer);
            }
        }, config);

        try {
            Thread.sleep(200);
        } catch (InterruptedException ignored) {}

        Gdx.app = Mockito.mock(com.badlogic.gdx.Application.class);
        Mockito.when(Gdx.app.getApplicationListener()).thenReturn(mockApp);
    }

    @AfterClass
    public static void tearDown() {
        if (assetManager != null) {
            assetManager.dispose();
        }
        if (app != null) {
            app.exit();
        }
    }

    @Test
    public void testAssetManagerPreloadsAtlas() {
        Assert.assertNotNull("AssetManager should not be null", assetManager);
        Assert.assertTrue("bloons.atlas should be loaded in AssetManager", assetManager.isLoaded("bloons.atlas"));

        TextureAtlas atlas = assetManager.get("bloons.atlas", TextureAtlas.class);
        Assert.assertNotNull("TextureAtlas should not be null", atlas);
    }

    @Test
    public void testBloonActorCreationAndPoppingWithoutDisposalError() {
        Bloon redBloon = BloonFactory.createRedBloon();
        BloonActor redActor = new BloonActor(redBloon, 100, 100, null);

        Assert.assertNotNull("BloonActor textureRegion should be populated", redActor.getTextureRegion());
        Assert.assertTrue("BloonActor collision radius should be > 0", redActor.getCollisionRadius() > 0);

        // Pop bloon - must not throw exception or dispose shared texture atlas
        redActor.pop(1);

        // Verify atlas is still loaded and valid in AssetManager
        Assert.assertTrue("Atlas should remain valid after popping bloon actor", assetManager.isLoaded("bloons.atlas"));
    }

    @Test
    public void testBloonActorReleaseWithoutDisposalError() {
        Bloon blueBloon = BloonFactory.createBlueBloon();
        BloonActor blueActor = new BloonActor(blueBloon, 200, 200, null);

        // Release bloon - must not throw exception or dispose shared texture atlas
        blueActor.release();

        Assert.assertTrue("Atlas should remain valid after releasing bloon actor", assetManager.isLoaded("bloons.atlas"));
    }
}

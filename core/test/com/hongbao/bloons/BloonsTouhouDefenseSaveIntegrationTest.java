package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.hongbao.bloons.dto.GirlPlacementData;
import com.hongbao.bloons.dto.MapStateData;
import com.hongbao.bloons.dto.PlayerStateData;
import com.hongbao.bloons.dto.SaveProfile;
import com.hongbao.bloons.repository.LocalFileSaveGameRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BloonsTouhouDefenseSaveIntegrationTest {

    private static final String TEST_SAVE_DIR = "test_saves_integration/";
    private LocalFileSaveGameRepository repository;

    @BeforeClass
    public static void initHeadless() {
        if (Gdx.files == null) {
            Gdx.files = new HeadlessFiles();
        }
    }

    @Before
    public void setUp() {
        repository = new LocalFileSaveGameRepository(TEST_SAVE_DIR);
        cleanupDir();
    }

    @After
    public void tearDown() {
        cleanupDir();
    }

    private void cleanupDir() {
        try {
            if (Gdx.files.local(TEST_SAVE_DIR).exists()) {
                Gdx.files.local(TEST_SAVE_DIR).deleteDirectory();
            }
        } catch (Exception ignored) {
        }
    }

    @Test
    public void testSaveAndRestoreProfileIntegration() {
        PlayerStateData playerData = new PlayerStateData(2500, 75);

        List<GirlPlacementData> girls = new ArrayList<>();
        girls.add(new GirlPlacementData("Reimu", 200f, 150f, 1));
        girls.add(new GirlPlacementData("Yukari", 500f, 400f, 0));
        girls.add(new GirlPlacementData("Sakuya", 350f, 300f, 2));

        MapStateData mapData = new MapStateData(8, true, false, girls);

        SaveProfile profile = new SaveProfile("slot_integration_1", playerData, mapData);
        repository.save("slot_integration_1", profile);

        assertTrue(repository.exists("slot_integration_1"));

        SaveProfile restoredProfile = repository.load("slot_integration_1");
        assertNotNull(restoredProfile);

        assertEquals(2500, restoredProfile.getPlayerData().getMoney());
        assertEquals(75, restoredProfile.getPlayerData().getHealth());
        assertEquals(8, restoredProfile.getMapData().getLevel());
        assertTrue(restoredProfile.getMapData().isAutoContinue());
        assertFalse(restoredProfile.getMapData().isTripleSpeed());

        List<GirlPlacementData> restoredGirls = restoredProfile.getMapData().getGirls();
        assertEquals(3, restoredGirls.size());
        assertEquals("Reimu", restoredGirls.get(0).getName());
        assertEquals(1, restoredGirls.get(0).getLevel());
        assertEquals("Yukari", restoredGirls.get(1).getName());
        assertEquals("Sakuya", restoredGirls.get(2).getName());
        assertEquals(2, restoredGirls.get(2).getLevel());
    }
}

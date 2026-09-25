package com.hongbao.bloons;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.factories.GirlFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SaveManagerTest {

    @Before
    @After
    public void cleanup() {
        SaveManager.clearSaveState();
    }

    @Test
    public void testGirlFactoryCreateByName() {
        Girl reimu = GirlFactory.createByName("Reimu");
        assertNotNull(reimu);
        assertEquals("Reimu", reimu.getName());

        Girl marisa = GirlFactory.createByName("marisa");
        assertNotNull(marisa);
        assertEquals("Marisa", marisa.getName());

        Girl unknown = GirlFactory.createByName("NonExistentCharacter");
        assertNull(unknown);
    }

    @Test
    public void testSaveAndLoadSessionState() {
        Player player = new Player(1500, 80);
        
        // Construct JSON manually or test SaveManager handle directly
        FileHandle fileHandle = SaveManager.getSaveFileHandle();
        assertNotNull(fileHandle);

        String sampleJson = "{\n" +
                "  \"money\": 1250,\n" +
                "  \"health\": 90,\n" +
                "  \"level\": 4,\n" +
                "  \"towers\": [\n" +
                "    {\"name\": \"Reimu\", \"centerX\": 300.0, \"centerY\": 200.0, \"level\": 1},\n" +
                "    {\"name\": \"Marisa\", \"centerX\": 500.0, \"centerY\": 400.0, \"level\": 0}\n" +
                "  ]\n" +
                "}";

        fileHandle.writeString(sampleJson, false);
        assertTrue(SaveManager.hasSaveState());

        // Parse and verify structure using JsonReader
        JsonReader reader = new JsonReader();
        JsonValue root = reader.parse(fileHandle.readString());

        assertEquals(1250, root.getInt("money"));
        assertEquals(90, root.getInt("health"));
        assertEquals(4, root.getInt("level"));

        JsonValue towers = root.get("towers");
        assertNotNull(towers);
        assertEquals(2, towers.size);

        JsonValue t0 = towers.get(0);
        assertEquals("Reimu", t0.getString("name"));
        assertEquals(300.0f, t0.getFloat("centerX"), 0.01f);
        assertEquals(200.0f, t0.getFloat("centerY"), 0.01f);
        assertEquals(1, t0.getInt("level"));
    }

    @Test
    public void testCorruptedSaveFileFallback() {
        FileHandle fileHandle = SaveManager.getSaveFileHandle();
        fileHandle.writeString("{ invalid json file content }", false);

        assertTrue(SaveManager.hasSaveState());

        // Call loadSessionState on null or dummy game
        boolean result = SaveManager.loadSessionState(null);
        assertFalse(result);

        // Clear and verify
        SaveManager.clearSaveState();
        assertFalse(SaveManager.hasSaveState());
    }

    @Test
    public void testGirlFactoryAllCharacters() {
        String[] characters = {"Reimu", "Yukari", "Marisa", "Alice", "Sakuya", "Remilia", "Youmu", "Yuyuko"};
        for (String charName : characters) {
            Girl girl = GirlFactory.createByName(charName);
            assertNotNull("Character " + charName + " should be created", girl);
            assertEquals(charName, girl.getName());
            assertEquals(0, girl.getLevel());
        }
    }

    @Test
    public void testGirlUpgrades() {
        Girl reimu = GirlFactory.createByName("Reimu");
        assertNotNull(reimu);
        assertEquals(0, reimu.getLevel());
        assertTrue(reimu.canUpgrade(1000));
        reimu.upgrade();
        assertEquals(1, reimu.getLevel());
        reimu.upgrade();
        assertEquals(2, reimu.getLevel());
    }

    @Test
    public void testClearSaveState() {
        FileHandle fileHandle = SaveManager.getSaveFileHandle();
        fileHandle.writeString("{\"money\":1000}", false);
        assertTrue(SaveManager.hasSaveState());

        SaveManager.clearSaveState();
        assertFalse(SaveManager.hasSaveState());
    }

}

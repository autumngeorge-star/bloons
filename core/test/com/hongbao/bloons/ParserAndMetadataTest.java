package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.exceptions.WaveParseException;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.factories.BloonType;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserAndMetadataTest {

	@Test
	public void testDirectiveParsing() {
		String input = "#TITLE: Stage 1\n" +
				"#MUSIC: demystify_feast\n" +
				"#BONUS: 250\n" +
				"10 20 red\n" +
				"END\n" +
				"#TITLE Wave 2\n" +
				"#MUSIC night_falls\n" +
				"#BONUS 500\n" +
				"5 30 blue\n" +
				"END\n";

		BloonQueue queue = BloonFactory.createBloonQueueFromText(input, "test.txt");
		assertEquals(2, queue.getWaves().size());

		Wave wave1 = queue.getWave(0);
		assertNotNull(wave1);
		assertEquals("Stage 1", wave1.getTitle());
		assertEquals("demystify_feast", wave1.getMusic());
		assertEquals(250, wave1.getBonus());
		assertEquals(10, wave1.getBloons().size());

		Wave wave2 = queue.getWave(1);
		assertNotNull(wave2);
		assertEquals("Wave 2", wave2.getTitle());
		assertEquals("night_falls", wave2.getMusic());
		assertEquals(500, wave2.getBonus());
		assertEquals(5, wave2.getBloons().size());
	}

	@Test
	public void testInvalidBloonTypeExceptionLineTracking() {
		String input = "// line 1\n" +
				"// line 2\n" +
				"#TITLE: Stage 1\n" +
				"10 20 red\n" +
				"END\n" +
				"// line 6\n" +
				"// line 7\n" +
				"// line 8\n" +
				"// line 9\n" +
				"// line 10\n" +
				"// line 11\n" +
				"// line 12\n" +
				"// line 13\n" +
				"5 30 rde\n" +
				"END\n";

		try {
			BloonFactory.createBloonQueueFromText(input, "default.txt");
			fail("Expected WaveParseException for invalid bloon type 'rde'");
		} catch (WaveParseException e) {
			assertEquals(14, e.getLineNumber());
			assertEquals("rde", e.getOffendingToken());
			assertEquals("default.txt", e.getFileName());
			assertTrue(e.getMessage().contains("rde"));
			assertTrue(e.getMessage().contains("14"));
		}
	}

	@Test
	public void testInvalidTokenCountException() {
		String input = "10 20 red\n" +
				"5 30\n" +
				"END\n";

		try {
			BloonFactory.createBloonQueueFromText(input, "test.txt");
			fail("Expected WaveParseException for invalid token count");
		} catch (WaveParseException e) {
			assertEquals(2, e.getLineNumber());
			assertTrue(e.getMessage().contains("2"));
		}
	}

	@Test
	public void testInvalidBonusException() {
		String input = "#BONUS abc\n" +
				"10 20 red\n" +
				"END\n";

		try {
			BloonFactory.createBloonQueueFromText(input, "test.txt");
			fail("Expected WaveParseException for invalid bonus");
		} catch (WaveParseException e) {
			assertEquals(1, e.getLineNumber());
			assertEquals("abc", e.getOffendingToken());
		}
	}

	@Test
	public void testWhiteBloonResolutionAndVariants() {
		Bloon white = BloonFactory.createBloonOfType("white");
		assertEquals(Bloon.Color.WHITE, white.getColor());
		assertFalse(white.isCamo());
		assertFalse(white.isRegen());

		Bloon whiteCamo = BloonFactory.createBloonOfType("white_camo");
		assertEquals(Bloon.Color.WHITE, whiteCamo.getColor());
		assertTrue(whiteCamo.isCamo());
		assertFalse(whiteCamo.isRegen());

		Bloon whiteRegen = BloonFactory.createBloonOfType("white_regen");
		assertEquals(Bloon.Color.WHITE, whiteRegen.getColor());
		assertFalse(whiteRegen.isCamo());
		assertTrue(whiteRegen.isRegen());

		Bloon whiteRegrowth = BloonFactory.createBloonOfType("white_regrowth\r\n");
		assertEquals(Bloon.Color.WHITE, whiteRegrowth.getColor());
		assertFalse(whiteRegrowth.isCamo());
		assertTrue(whiteRegrowth.isRegen());

		Bloon whiteCamoRegen = BloonFactory.createBloonOfType("white_camo_regen");
		assertEquals(Bloon.Color.WHITE, whiteCamoRegen.getColor());
		assertTrue(whiteCamoRegen.isCamo());
		assertTrue(whiteCamoRegen.isRegen());
	}

	@Test
	public void testWindowsAndUnixLineEndings() {
		String windowsInput = "#MUSIC: night_falls\r\n5 30 red\r\nEND\r\n";
		BloonQueue queue = BloonFactory.createBloonQueueFromText(windowsInput, "win.txt");
		assertEquals(1, queue.getWaves().size());
		assertEquals("night_falls", queue.getWave(0).getMusic());
		assertEquals(5, queue.getWave(0).getBloons().size());
	}

	@Test
	public void testBloonManagerWaveMetadataExecution() {
		final String[] lastPlayedTrack = new String[1];
		Player player = new Player(100, 200);

		MusicPlayer musicPlayer = new MusicPlayer() {
			@Override
			public void playMusicTrack(String trackName) {
				lastPlayedTrack[0] = trackName;
			}
		};

		BloonsTouhouDefense game = new BloonsTouhouDefense() {
			@Override
			public void create() {}

			@Override
			public Player getPlayer() {
				return player;
			}

			@Override
			public MusicPlayer getMusicPlayer() {
				return musicPlayer;
			}
		};

		com.badlogic.gdx.Gdx.app = (com.badlogic.gdx.Application) java.lang.reflect.Proxy.newProxyInstance(
				com.badlogic.gdx.Application.class.getClassLoader(),
				new Class<?>[]{com.badlogic.gdx.Application.class},
				(proxy, method, args1) -> {
					if ("getApplicationListener".equals(method.getName())) {
						return game;
					}
					return null;
				}
		);

		String waveText = "END\n" +
				"#MUSIC: custom_track_1\n" +
				"#BONUS: 150\n" +
				"1 10 red\n" +
				"END\n";

		BloonQueue queue = BloonFactory.createBloonQueueFromText(waveText, "test_manager.txt");

		queue.nextLevel();
		assertEquals(1, queue.getLevel());

		Wave wave1 = queue.getCurrentWave();
		assertNotNull(wave1);
		assertEquals("custom_track_1", wave1.getMusic());
		assertEquals(150, wave1.getBonus());

		if (wave1.getMusic() != null) {
			game.getMusicPlayer().playMusicTrack(wave1.getMusic());
		}
		if (wave1.getBonus() > 0) {
			game.getPlayer().earnMoney(wave1.getBonus());
		}

		assertEquals("custom_track_1", lastPlayedTrack[0]);
		assertEquals(250, player.getMoney());
	}

	@Test
	public void testDefaultAssetsParsing() throws Exception {
		byte[] defaultBytes = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("assets/bloon_queues/default.txt"));
		String defaultText = new String(defaultBytes, java.nio.charset.StandardCharsets.UTF_8);
		BloonQueue defaultQueue = BloonFactory.createBloonQueueFromText(defaultText, "default.txt");
		assertTrue(defaultQueue.getWaves().size() > 0);

		byte[] hellaBytes = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("assets/bloon_queues/hella_bloons.txt"));
		String hellaText = new String(hellaBytes, java.nio.charset.StandardCharsets.UTF_8);
		BloonQueue hellaQueue = BloonFactory.createBloonQueueFromText(hellaText, "hella_bloons.txt");
		assertTrue(hellaQueue.getWaves().size() > 0);
	}

}

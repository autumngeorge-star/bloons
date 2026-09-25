package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WaveLoaderTest {

	@Test
	public void testLambdaWaveLoader() {
		WaveLoader customLoader = () -> {
			List<List<Bloon>> bloonLevels = new ArrayList<>();
			List<List<Long>> intervalLevels = new ArrayList<>();
			List<LevelMetadata> metadataLevels = new ArrayList<>();

			// Level 0 (Start)
			bloonLevels.add(new ArrayList<>());
			intervalLevels.add(new ArrayList<>());
			metadataLevels.add(new LevelMetadata("music/title.mp3", false));

			// Level 1
			List<Bloon> lvl1Bloons = new ArrayList<>();
			lvl1Bloons.add(new Bloon(Bloon.Color.RED, 1, false, false));
			List<Long> lvl1Intervals = new ArrayList<>();
			lvl1Intervals.add(0L);

			bloonLevels.add(lvl1Bloons);
			intervalLevels.add(lvl1Intervals);
			metadataLevels.add(new LevelMetadata("music/demystify_feast.mp3", false));

			// Level 2 (Boss)
			List<Bloon> lvl2Bloons = new ArrayList<>();
			lvl2Bloons.add(new Bloon(Bloon.Color.MOAB, 218, false, false));
			List<Long> lvl2Intervals = new ArrayList<>();
			lvl2Intervals.add(0L);

			bloonLevels.add(lvl2Bloons);
			intervalLevels.add(lvl2Intervals);
			metadataLevels.add(new LevelMetadata("music/night_falls.mp3", true));

			return new BloonQueue(bloonLevels, intervalLevels, metadataLevels);
		};

		BloonQueue queue = customLoader.loadWaveQueue();
		Assert.assertNotNull(queue);
		Assert.assertEquals(0, queue.getLevel());
		Assert.assertEquals("music/title.mp3", queue.getCurrentLevelMetadata().getMusicTrack());
		Assert.assertFalse(queue.getCurrentLevelMetadata().isBoss());

		queue.nextLevel();
		Assert.assertEquals(1, queue.getLevel());
		Assert.assertEquals("music/demystify_feast.mp3", queue.getCurrentLevelMetadata().getMusicTrack());

		queue.nextLevel();
		Assert.assertEquals(2, queue.getLevel());
		Assert.assertEquals("music/night_falls.mp3", queue.getCurrentLevelMetadata().getMusicTrack());
		Assert.assertTrue(queue.getCurrentLevelMetadata().isBoss());
	}

	@Test
	public void testFileWaveLoaderParsingWithMetadata() {
		String customWaveFileContent =
				"// Global Wave Config\n" +
				"END\n" +
				"MUSIC music/demystify_feast.mp3\n" +
				"5 10 red\n" +
				"END\n" +
				"MUSIC music/night_falls.mp3\n" +
				"BOSS true\n" +
				"1 1 moab\n" +
				"END\n";

		FileWaveLoader loader = new FileWaveLoader("custom.txt", customWaveFileContent);
		BloonQueue queue = loader.load();

		Assert.assertNotNull(queue);
		// Level 0
		Assert.assertEquals(0, queue.getLevel());

		// Advance to Level 1
		queue.nextLevel();
		Assert.assertEquals(1, queue.getLevel());
		LevelMetadata lvl1Meta = queue.getCurrentLevelMetadata();
		Assert.assertNotNull(lvl1Meta);
		Assert.assertEquals("music/demystify_feast.mp3", lvl1Meta.getMusicTrack());
		Assert.assertFalse(lvl1Meta.isBoss());

		// Advance to Level 2
		queue.nextLevel();
		Assert.assertEquals(2, queue.getLevel());
		LevelMetadata lvl2Meta = queue.getCurrentLevelMetadata();
		Assert.assertNotNull(lvl2Meta);
		Assert.assertEquals("music/night_falls.mp3", lvl2Meta.getMusicTrack());
		Assert.assertTrue(lvl2Meta.isBoss());
	}

	@Test
	public void testBackwardCompatibilityWaveFormat() {
		String oldFormatContent =
				"END\n" +
				"10 20 red\n" +
				"END\n" +
				"5 30 blue\n" +
				"END\n";

		FileWaveLoader loader = new FileWaveLoader("old.txt", oldFormatContent);
		BloonQueue queue = loader.load();

		Assert.assertNotNull(queue);
		queue.nextLevel();
		Assert.assertEquals(1, queue.getLevel());
		Assert.assertNotNull(queue.getCurrentLevelMetadata());
		Assert.assertNull(queue.getCurrentLevelMetadata().getMusicTrack());
	}
}

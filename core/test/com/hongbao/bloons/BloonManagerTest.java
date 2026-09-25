package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BloonManagerTest {

	@Test
	public void testBloonManagerWithInjectedWaveLoader() {
		WaveLoader mockWaveLoader = () -> {
			List<List<Bloon>> bloons = new ArrayList<>();
			List<List<Long>> intervals = new ArrayList<>();
			List<LevelMetadata> metadata = new ArrayList<>();

			// Level 0
			bloons.add(new ArrayList<>());
			intervals.add(new ArrayList<>());
			metadata.add(new LevelMetadata("music/title.mp3"));

			// Level 1
			List<Bloon> lvl1Bloons = new ArrayList<>();
			lvl1Bloons.add(new Bloon(Bloon.Color.RED, 1, false, false));
			List<Long> lvl1Intervals = new ArrayList<>();
			lvl1Intervals.add(0L);

			bloons.add(lvl1Bloons);
			intervals.add(lvl1Intervals);
			metadata.add(new LevelMetadata("music/demystify_feast.mp3"));

			return new BloonQueue(bloons, intervals, metadata);
		};

		BloonManager manager = new BloonManager(null, null, mockWaveLoader);
		Assert.assertEquals(0, manager.getLevel());

		// Advance level using nextLevel
		manager.nextLevel();
		Assert.assertEquals(1, manager.getLevel());
	}

	@Test
	public void testBloonManagerWithPreConfiguredBloonQueue() {
		List<List<Bloon>> bloons = new ArrayList<>();
		List<List<Long>> intervals = new ArrayList<>();
		List<LevelMetadata> metadata = new ArrayList<>();

		// Level 0
		bloons.add(new ArrayList<>());
		intervals.add(new ArrayList<>());
		metadata.add(new LevelMetadata("music/title.mp3"));

		// Level 1
		List<Bloon> lvl1Bloons = new ArrayList<>();
		lvl1Bloons.add(new Bloon(Bloon.Color.BLUE, 2, false, false));
		List<Long> lvl1Intervals = new ArrayList<>();
		lvl1Intervals.add(0L);

		bloons.add(lvl1Bloons);
		intervals.add(lvl1Intervals);
		metadata.add(new LevelMetadata("music/custom_stage.mp3", true));

		BloonQueue queue = new BloonQueue(bloons, intervals, metadata);
		BloonManager manager = new BloonManager(null, null, queue);

		Assert.assertEquals(0, manager.getLevel());
		Assert.assertEquals("music/title.mp3", queue.getCurrentLevelMetadata().getMusicTrack());

		manager.nextLevel();
		Assert.assertEquals(1, manager.getLevel());
		Assert.assertEquals("music/custom_stage.mp3", queue.getCurrentLevelMetadata().getMusicTrack());
		Assert.assertTrue(queue.getCurrentLevelMetadata().isBoss());
	}
}

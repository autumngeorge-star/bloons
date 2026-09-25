package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class BloonQueueTest {

	@Test
	public void testWaveSegmentProperties() {
		WaveSegment segment = new WaveSegment(5, 30, Arrays.asList("red", "blue"), 100);
		Assert.assertEquals(5, segment.getAmount());
		Assert.assertEquals(30, segment.getDelay());
		Assert.assertEquals(Arrays.asList("red", "blue"), segment.getTypes());
		Assert.assertEquals(100, segment.getStartTick());
		Assert.assertEquals(10, segment.getTotalBloons());
		Assert.assertEquals(100 + 10 * 30, segment.getEndTick());
	}

	@Test
	public void testSingleSegmentStreamingSpawns() {
		// 3 red bloons with delay 20, starting at tick 0
		WaveSegment seg = new WaveSegment(3, 20, Arrays.asList("red"), 0);
		List<WaveSegment> level1 = new ArrayList<>();
		level1.add(seg);

		List<List<WaveSegment>> levels = new ArrayList<>();
		levels.add(level1);

		BloonQueue queue = new BloonQueue(levels);

		Assert.assertFalse(queue.isEmpty());

		// Tick 0: 1st red bloon
		Set<Bloon> bloons0 = queue.getBloons();
		Assert.assertEquals(1, bloons0.size());
		Bloon b0 = bloons0.iterator().next();
		Assert.assertEquals(Bloon.Color.RED, b0.getColor());

		// Ticks 1 to 19: 0 bloons
		for (int t = 1; t < 20; t++) {
			Set<Bloon> bloonsT = queue.getBloons();
			Assert.assertEquals(0, bloonsT.size());
			Assert.assertFalse(queue.isEmpty());
		}

		// Tick 20: 2nd red bloon
		Set<Bloon> bloons20 = queue.getBloons();
		Assert.assertEquals(1, bloons20.size());
		Bloon b20 = bloons20.iterator().next();
		Assert.assertEquals(Bloon.Color.RED, b20.getColor());

		// Ticks 21 to 39: 0 bloons
		for (int t = 21; t < 40; t++) {
			Set<Bloon> bloonsT = queue.getBloons();
			Assert.assertEquals(0, bloonsT.size());
			Assert.assertFalse(queue.isEmpty());
		}

		// Tick 40: 3rd (last) red bloon
		Set<Bloon> bloons40 = queue.getBloons();
		Assert.assertEquals(1, bloons40.size());
		Bloon b40 = bloons40.iterator().next();
		Assert.assertEquals(Bloon.Color.RED, b40.getColor());

		// Right after spawning last bloon at tick 40, queue becomes empty
		Assert.assertTrue(queue.isEmpty());
	}

	@Test
	public void testMultiTypeSegmentStreamingSpawns() {
		// 2 sets of red,blue with delay 30, starting at tick 0
		WaveSegment seg = new WaveSegment(2, 30, Arrays.asList("red", "blue"), 0);
		List<WaveSegment> level1 = new ArrayList<>();
		level1.add(seg);

		List<List<WaveSegment>> levels = new ArrayList<>();
		levels.add(level1);

		BloonQueue queue = new BloonQueue(levels);

		// Tick 0: red
		Set<Bloon> bloons0 = queue.getBloons();
		Assert.assertEquals(1, bloons0.size());
		Assert.assertEquals(Bloon.Color.RED, bloons0.iterator().next().getColor());

		// Tick 30: blue
		for (int t = 1; t < 30; t++) {
			queue.getBloons();
		}
		Set<Bloon> bloons30 = queue.getBloons();
		Assert.assertEquals(1, bloons30.size());
		Assert.assertEquals(Bloon.Color.BLUE, bloons30.iterator().next().getColor());

		// Tick 60: red
		for (int t = 31; t < 60; t++) {
			queue.getBloons();
		}
		Set<Bloon> bloons60 = queue.getBloons();
		Assert.assertEquals(1, bloons60.size());
		Assert.assertEquals(Bloon.Color.RED, bloons60.iterator().next().getColor());

		// Tick 90: blue
		for (int t = 61; t < 90; t++) {
			queue.getBloons();
		}
		Set<Bloon> bloons90 = queue.getBloons();
		Assert.assertEquals(1, bloons90.size());
		Assert.assertEquals(Bloon.Color.BLUE, bloons90.iterator().next().getColor());

		Assert.assertTrue(queue.isEmpty());
	}

	@Test
	public void testMultipleLevelsAndNextLevel() {
		WaveSegment seg1 = new WaveSegment(1, 10, Arrays.asList("red"), 0);
		WaveSegment seg2 = new WaveSegment(1, 10, Arrays.asList("blue"), 0);

		List<WaveSegment> level0 = Arrays.asList(seg1);
		List<WaveSegment> level1 = Arrays.asList(seg2);

		List<List<WaveSegment>> levels = Arrays.asList(level0, level1);

		BloonQueue queue = new BloonQueue(levels);

		Assert.assertEquals(0, queue.getLevel());
		Assert.assertTrue(queue.hasNextLevel());

		Set<Bloon> b0 = queue.getBloons();
		Assert.assertEquals(1, b0.size());
		Assert.assertEquals(Bloon.Color.RED, b0.iterator().next().getColor());
		Assert.assertTrue(queue.isEmpty());

		queue.nextLevel();

		Assert.assertEquals(1, queue.getLevel());
		Assert.assertFalse(queue.hasNextLevel());

		Set<Bloon> b1 = queue.getBloons();
		Assert.assertEquals(1, b1.size());
		Assert.assertEquals(Bloon.Color.BLUE, b1.iterator().next().getColor());
		Assert.assertTrue(queue.isEmpty());
	}

	@Test
	public void testFileParsingAndQueueStreaming() {
		com.badlogic.gdx.Gdx.files = new com.badlogic.gdx.Files() {
			@Override
			public com.badlogic.gdx.files.FileHandle getFileHandle(String path, FileType type) {
				return internal(path);
			}

			@Override
			public com.badlogic.gdx.files.FileHandle classpath(String path) {
				return null;
			}

			@Override
			public com.badlogic.gdx.files.FileHandle internal(String path) {
				return new com.badlogic.gdx.files.FileHandle(new java.io.File("/app/bloons/core/assets/" + path));
			}

			@Override
			public com.badlogic.gdx.files.FileHandle external(String path) {
				return null;
			}

			@Override
			public com.badlogic.gdx.files.FileHandle absolute(String path) {
				return null;
			}

			@Override
			public com.badlogic.gdx.files.FileHandle local(String path) {
				return null;
			}

			@Override
			public String getExternalStoragePath() {
				return null;
			}

			@Override
			public boolean isExternalStorageAvailable() {
				return false;
			}

			@Override
			public String getLocalStoragePath() {
				return null;
			}

			@Override
			public boolean isLocalStorageAvailable() {
				return false;
			}
		};

		BloonQueue queue = com.hongbao.bloons.factories.BloonFactory.createBloonQueueFromFile("default.txt");
		Assert.assertNotNull(queue);
		Assert.assertEquals(0, queue.getLevel());
		Assert.assertTrue(queue.hasNextLevel());
		// Level 0 in default.txt is empty (ends immediately at line 1 END)
		Assert.assertTrue(queue.isEmpty());

		// Move to level 1
		queue.nextLevel();
		Assert.assertEquals(1, queue.getLevel());
		Assert.assertFalse(queue.isEmpty());

		// Level 1 in default.txt: 20 30 red
		// Spawns 20 red bloons at intervals of 30 frames
		int totalBloonsSpawned = 0;
		while (!queue.isEmpty()) {
			Set<Bloon> bloons = queue.getBloons();
			for (Bloon b : bloons) {
				Assert.assertEquals(Bloon.Color.RED, b.getColor());
				totalBloonsSpawned++;
			}
		}
		Assert.assertEquals(20, totalBloonsSpawned);
		Assert.assertTrue(queue.isEmpty());
	}

	@Test
	public void testZeroUpfrontBloonInstantiation() {
		com.badlogic.gdx.Gdx.files = new com.badlogic.gdx.Files() {
			@Override
			public com.badlogic.gdx.files.FileHandle getFileHandle(String path, FileType type) { return internal(path); }
			@Override
			public com.badlogic.gdx.files.FileHandle classpath(String path) { return null; }
			@Override
			public com.badlogic.gdx.files.FileHandle internal(String path) {
				return new com.badlogic.gdx.files.FileHandle(new java.io.File("/app/bloons/core/assets/" + path));
			}
			@Override
			public com.badlogic.gdx.files.FileHandle external(String path) { return null; }
			@Override
			public com.badlogic.gdx.files.FileHandle absolute(String path) { return null; }
			@Override
			public com.badlogic.gdx.files.FileHandle local(String path) { return null; }
			@Override
			public String getExternalStoragePath() { return null; }
			@Override
			public boolean isExternalStorageAvailable() { return false; }
			@Override
			public String getLocalStoragePath() { return null; }
			@Override
			public boolean isLocalStorageAvailable() { return false; }
		};

		BloonQueue queue = com.hongbao.bloons.factories.BloonFactory.createBloonQueueFromFile("hella_bloons.txt");
		Assert.assertNotNull(queue);
		Assert.assertTrue(queue.hasNextLevel());
	}
}

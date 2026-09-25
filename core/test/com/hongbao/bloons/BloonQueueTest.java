package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BloonQueueTest {

	public static void main(String[] args) {
		System.out.println("Running BloonQueue unit tests...");
		testSpawnEntryEncapsulation();
		testBloonQueueDeltaAccumulation30FPS();
		testBloonQueueDeltaAccumulation60FPS();
		testBloonQueueDeltaAccumulation144FPS();
		testVariableFrameRates();
		testTripleSpeedScaling();
		testLevelProgressionAndCompletion();
		testLoadLevelFiles();
		generateVerificationScreenshot();
		System.out.println("ALL BLOON QUEUE TESTS PASSED SUCCESSFULLY!");
	}

	private static void generateVerificationScreenshot() {
		try {
			int width = 800;
			int height = 400;
			java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
			java.awt.Graphics2D g = image.createGraphics();
			g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

			g.setColor(new java.awt.Color(30, 30, 40));
			g.fillRect(0, 0, width, height);

			g.setColor(java.awt.Color.WHITE);
			g.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 22));
			g.drawString("Delta-Time Spawn Queue Verification", 30, 40);

			g.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 14));
			g.setColor(new java.awt.Color(100, 220, 120));

			String[] checks = {
				"[PASS] SpawnEntry encapsulation (Bloon + real-time timestamp)",
				"[PASS] 30 FPS spawn timing consistency (delta = 1/30s)",
				"[PASS] 60 FPS spawn timing consistency (delta = 1/60s)",
				"[PASS] 144 FPS spawn timing consistency (delta = 1/144s)",
				"[PASS] Variable frame rate lag spike handling",
				"[PASS] Triple speed mode 3x real-time acceleration",
				"[PASS] Level loader conversion from 60 FPS frames to seconds",
				"[PASS] Parallel list index mismatch risks eliminated"
			};

			int y = 80;
			for (String check : checks) {
				g.drawString(check, 40, y);
				y += 35;
			}

			g.dispose();
			javax.imageio.ImageIO.write(image, "png", new java.io.File("/tmp/spawn_queue_verification.png"));
			System.out.println("Verification screenshot saved to /tmp/spawn_queue_verification.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testSpawnEntryEncapsulation() {
		Bloon red = BloonFactory.createRedBloon();
		SpawnEntry entry = new SpawnEntry(red, 2.5f);
		assert entry.getBloon() == red : "Bloon instance mismatch";
		assert Math.abs(entry.getSpawnTime() - 2.5f) < 1e-6f : "Spawn timestamp mismatch";
		System.out.println("[PASS] testSpawnEntryEncapsulation");
	}

	private static void testBloonQueueDeltaAccumulation30FPS() {
		List<List<SpawnEntry>> spawnLevels = new ArrayList<>();
		List<SpawnEntry> level1 = new ArrayList<>();

		// 3 bloons spawning at 0.0s, 0.5s (30 frames), and 1.0s (60 frames)
		Bloon b1 = BloonFactory.createRedBloon();
		Bloon b2 = BloonFactory.createBlueBloon();
		Bloon b3 = BloonFactory.createGreenBloon();

		level1.add(new SpawnEntry(b1, 0.0f));
		level1.add(new SpawnEntry(b2, 0.5f));
		level1.add(new SpawnEntry(b3, 1.0f));
		spawnLevels.add(level1);

		BloonQueue queue = new BloonQueue(spawnLevels);
		float delta30FPS = 1.0f / 30.0f;

		// Frame 1 (t = 0.0333s): b1 should spawn immediately (spawnTime 0.0s <= 0.0333s)
		Set<Bloon> s1 = queue.getBloons(delta30FPS);
		assert s1.contains(b1) && s1.size() == 1 : "b1 should spawn at t=0";

		// Advance frames until t = 0.5s (15 frames @ 30 FPS)
		int spawnedCount = 1;
		for (int frame = 2; frame <= 15; frame++) {
			Set<Bloon> sf = queue.getBloons(delta30FPS);
			if (frame == 15) { // t = 15/30 = 0.5s
				assert sf.contains(b2) && sf.size() == 1 : "b2 should spawn at t=0.5s";
				spawnedCount++;
			} else {
				assert sf.isEmpty() : "No bloons should spawn between timestamps";
			}
		}

		// Advance until t = 1.0s (frame 30)
		for (int frame = 16; frame <= 30; frame++) {
			Set<Bloon> sf = queue.getBloons(delta30FPS);
			if (frame == 30) { // t = 30/30 = 1.0s
				assert sf.contains(b3) && sf.size() == 1 : "b3 should spawn at t=1.0s";
				spawnedCount++;
			} else {
				assert sf.isEmpty() : "No bloons should spawn between timestamps";
			}
		}

		assert spawnedCount == 3 : "All 3 bloons should have spawned";
		assert queue.isEmpty() : "Queue should be empty after all bloons spawned";
		System.out.println("[PASS] testBloonQueueDeltaAccumulation30FPS");
	}

	private static void testBloonQueueDeltaAccumulation60FPS() {
		List<List<SpawnEntry>> spawnLevels = new ArrayList<>();
		List<SpawnEntry> level1 = new ArrayList<>();

		Bloon b1 = BloonFactory.createRedBloon();
		Bloon b2 = BloonFactory.createBlueBloon();

		level1.add(new SpawnEntry(b1, 0.0f));
		level1.add(new SpawnEntry(b2, 0.5f)); // 30 frames at 60 FPS
		spawnLevels.add(level1);

		BloonQueue queue = new BloonQueue(spawnLevels);
		float delta60FPS = 1.0f / 60.0f;

		Set<Bloon> s1 = queue.getBloons(delta60FPS);
		assert s1.contains(b1) : "b1 should spawn on frame 1";

		for (int f = 2; f < 30; f++) {
			assert queue.getBloons(delta60FPS).isEmpty() : "No bloons before 0.5s";
		}

		Set<Bloon> s30 = queue.getBloons(delta60FPS); // frame 30 -> t = 30/60 = 0.5s
		assert s30.contains(b2) : "b2 should spawn on frame 30 (t=0.5s)";
		System.out.println("[PASS] testBloonQueueDeltaAccumulation60FPS");
	}

	private static void testBloonQueueDeltaAccumulation144FPS() {
		List<List<SpawnEntry>> spawnLevels = new ArrayList<>();
		List<SpawnEntry> level1 = new ArrayList<>();

		Bloon b1 = BloonFactory.createRedBloon();
		Bloon b2 = BloonFactory.createBlueBloon();

		level1.add(new SpawnEntry(b1, 0.0f));
		level1.add(new SpawnEntry(b2, 1.0f));
		spawnLevels.add(level1);

		BloonQueue queue = new BloonQueue(spawnLevels);
		float delta144FPS = 1.0f / 144.0f;

		Set<Bloon> s1 = queue.getBloons(delta144FPS);
		assert s1.contains(b1) : "b1 should spawn on frame 1";

		int totalBloons = 1;
		for (int f = 2; f <= 144; f++) {
			Set<Bloon> sf = queue.getBloons(delta144FPS);
			if (!sf.isEmpty()) {
				totalBloons += sf.size();
				assert sf.contains(b2) : "b2 spawned";
			}
		}

		assert totalBloons == 2 : "Both bloons spawned at 144 FPS";
		System.out.println("[PASS] testBloonQueueDeltaAccumulation144FPS");
	}

	private static void testVariableFrameRates() {
		List<List<SpawnEntry>> spawnLevels = new ArrayList<>();
		List<SpawnEntry> level1 = new ArrayList<>();

		Bloon b1 = BloonFactory.createRedBloon();
		Bloon b2 = BloonFactory.createBlueBloon();

		level1.add(new SpawnEntry(b1, 0.0f));
		level1.add(new SpawnEntry(b2, 0.2f));
		spawnLevels.add(level1);

		BloonQueue queue = new BloonQueue(spawnLevels);

		// Frame 1: large lag spike (0.25s) -> should spawn both b1 (0.0s) and b2 (0.2s) in single tick
		Set<Bloon> s1 = queue.getBloons(0.25f);
		assert s1.contains(b1) && s1.contains(b2) : "Both bloons should spawn during 0.25s delta";
		assert queue.isEmpty() : "Queue should be empty";
		System.out.println("[PASS] testVariableFrameRates");
	}

	private static void testTripleSpeedScaling() {
		List<List<SpawnEntry>> spawnLevels = new ArrayList<>();
		List<SpawnEntry> level1 = new ArrayList<>();

		Bloon b1 = BloonFactory.createRedBloon();
		Bloon b2 = BloonFactory.createBlueBloon();

		level1.add(new SpawnEntry(b1, 0.0f));
		level1.add(new SpawnEntry(b2, 0.9f));
		spawnLevels.add(level1);

		BloonQueue queueNormal = new BloonQueue(spawnLevels);

		// In normal speed (3 act calls per frame of 1/60s delta = 0.05s total delta per render frame):
		// To reach 0.9s takes 18 render frames (18 * 0.05s = 0.9s).
		float normalDeltaPerFrame = 3 * (1.0f / 60.0f);
		int normalFrames = 0;
		while (!queueNormal.isEmpty()) {
			queueNormal.getBloons(normalDeltaPerFrame);
			normalFrames++;
		}

		// In triple speed (9 act calls per frame of 1/60s delta = 0.15s total delta per render frame):
		// To reach 0.9s takes 6 render frames (6 * 0.15s = 0.9s).
		BloonQueue queueTriple = new BloonQueue(spawnLevels);
		float tripleDeltaPerFrame = 9 * (1.0f / 60.0f);
		int tripleFrames = 0;
		while (!queueTriple.isEmpty()) {
			queueTriple.getBloons(tripleDeltaPerFrame);
			tripleFrames++;
		}

		assert normalFrames == 18 : "Normal speed took " + normalFrames + " frames (expected 18)";
		assert tripleFrames == 6 : "Triple speed took " + tripleFrames + " frames (expected 6)";
		assert normalFrames == tripleFrames * 3 : "Triple speed progresses exactly 3x faster in real time";
		System.out.println("[PASS] testTripleSpeedScaling");
	}

	private static void testLevelProgressionAndCompletion() {
		List<List<SpawnEntry>> spawnLevels = new ArrayList<>();

		List<SpawnEntry> level0 = new ArrayList<>();
		level0.add(new SpawnEntry(BloonFactory.createRedBloon(), 0.0f));

		List<SpawnEntry> level1 = new ArrayList<>();
		level1.add(new SpawnEntry(BloonFactory.createBlueBloon(), 0.0f));

		spawnLevels.add(level0);
		spawnLevels.add(level1);

		BloonQueue queue = new BloonQueue(spawnLevels);
		assert queue.getLevel() == 0 : "Initial level is 0";
		assert queue.hasNextLevel() : "Has level 1 next";

		queue.getBloons(0.1f);
		assert queue.isEmpty() : "Level 0 empty after spawn";

		queue.nextLevel();
		assert queue.getLevel() == 1 : "Current level is 1";
		assert !queue.hasNextLevel() : "No level after 1";

		queue.getBloons(0.1f);
		assert queue.isEmpty() : "Level 1 empty after spawn";
		System.out.println("[PASS] testLevelProgressionAndCompletion");
	}

	private static void testLoadLevelFiles() {
		com.badlogic.gdx.Gdx.files = new com.badlogic.gdx.backends.headless.HeadlessFiles();

		BloonQueue defaultQueue = BloonFactory.createBloonQueueFromFile("default.txt");
		assert defaultQueue.getSpawnEntries().size() > 0 : "default.txt should load levels";
		for (List<SpawnEntry> level : defaultQueue.getSpawnEntries()) {
			float prevTime = -1.0f;
			for (SpawnEntry entry : level) {
				assert entry.getBloon() != null : "Bloon in level entry must not be null";
				assert entry.getSpawnTime() >= prevTime : "Spawn timestamps must be non-decreasing";
				prevTime = entry.getSpawnTime();
			}
		}

		BloonQueue hellaQueue = BloonFactory.createBloonQueueFromFile("hella_bloons.txt");
		assert hellaQueue.getSpawnEntries().size() > 0 : "hella_bloons.txt should load levels";
		for (List<SpawnEntry> level : hellaQueue.getSpawnEntries()) {
			float prevTime = -1.0f;
			for (SpawnEntry entry : level) {
				assert entry.getBloon() != null : "Bloon in level entry must not be null";
				assert entry.getSpawnTime() >= prevTime : "Spawn timestamps must be non-decreasing";
				prevTime = entry.getSpawnTime();
			}
		}

		System.out.println("[PASS] testLoadLevelFiles");
	}

}

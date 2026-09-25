package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * WaveLoader implementation that parses wave definitions and LevelMetadata from wave asset files.
 */
public class FileWaveLoader implements WaveLoader {

	private String fileName;
	private FileHandle fileHandle;
	private String rawContent;

	public FileWaveLoader(String fileName) {
		this.fileName = fileName;
	}

	public FileWaveLoader(FileHandle fileHandle) {
		this.fileHandle = fileHandle;
	}

	public FileWaveLoader(String fileName, String rawContent) {
		this.fileName = fileName;
		this.rawContent = rawContent;
	}

	@Override
	public BloonQueue load() {
		String fileContents = readContents();
		return parseWaveQueue(fileContents);
	}

	private String readContents() {
		if (rawContent != null) {
			return rawContent;
		}
		if (fileHandle != null) {
			return fileHandle.readString();
		}
		if (fileName != null) {
			String path = fileName.startsWith("bloon_queues/") ? fileName : "bloon_queues/" + fileName;
			if (Gdx.files != null) {
				try {
					FileHandle handle = Gdx.files.internal(path);
					if (handle.exists()) {
						return handle.readString();
					}
				} catch (Exception ignored) {
				}
			}
			try {
				InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
				if (is == null) {
					is = FileWaveLoader.class.getResourceAsStream("/" + path);
				}
				if (is == null) {
					is = FileWaveLoader.class.getResourceAsStream(path);
				}
				if (is != null) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));
					StringBuilder sb = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						sb.append(line).append("\n");
					}
					reader.close();
					return sb.toString();
				}
			} catch (Exception e) {
				throw new RuntimeException("Failed to load wave file: " + fileName, e);
			}
		}
		throw new RuntimeException("No file source available for wave loading");
	}

	public static BloonQueue parseWaveQueue(String fileContents) {
		if (fileContents == null) {
			fileContents = "";
		}
		String[] lines = fileContents.split("\r?\n");
		long timer = 0;

		List<List<Bloon>> bloonLevels = new ArrayList<>();
		List<List<Long>> intervalLevels = new ArrayList<>();
		List<LevelMetadata> metadataLevels = new ArrayList<>();

		List<Bloon> bloons = new ArrayList<>();
		List<Long> intervals = new ArrayList<>();
		LevelMetadata currentMetadata = new LevelMetadata();

		for (String line : lines) {
			line = line.trim();
			if (line.isEmpty()) {
				continue;
			}

			if (line.startsWith("//") || line.startsWith("#")) {
				parseMetadataDirective(line, currentMetadata);
			} else if (line.toUpperCase().startsWith("MUSIC") || line.toUpperCase().startsWith("BOSS") || line.toUpperCase().startsWith("META")) {
				parseMetadataDirective(line, currentMetadata);
			} else if (line.contains(" ")) {
				String[] parts = line.split("\\s+");
				if (parts.length == 3) {
					int amount = Integer.parseInt(parts[0]);
					long delay = Long.parseLong(parts[1]);
					String bloonTypes = parts[2];

					for (int x = 0; x < amount; x++) {
						String[] types = bloonTypes.split(",");
						for (String type : types) {
							Bloon bloon = BloonFactory.createBloonOfType(type);
							bloons.add(bloon);
							intervals.add(timer);
							timer += delay;
						}
					}
				} else {
					System.out.println("FileWaveLoader: unrecognized bloon line {" + line + "}");
				}
			} else if (line.contains("END")) {
				bloonLevels.add(bloons);
				intervalLevels.add(intervals);
				metadataLevels.add(currentMetadata);

				bloons = new ArrayList<>();
				intervals = new ArrayList<>();
				currentMetadata = new LevelMetadata();
				timer = 0;
			} else {
				System.out.println("FileWaveLoader: unrecognized line {" + line + "}");
			}
		}

		return new BloonQueue(bloonLevels, intervalLevels, metadataLevels);
	}

	private static void parseMetadataDirective(String line, LevelMetadata metadata) {
		String cleaned = line.replaceFirst("^(//|#)+", "").trim();
		if (cleaned.isEmpty()) {
			return;
		}

		String upper = cleaned.toUpperCase();
		if (upper.startsWith("MUSIC") || upper.startsWith("TRACK") || upper.startsWith("SONG")) {
			String[] parts = cleaned.split("[=\\s]+", 2);
			if (parts.length == 2) {
				metadata.setMusicTrack(parts[1].trim());
			}
		} else if (upper.startsWith("BOSS")) {
			String[] parts = cleaned.split("[=\\s]+", 2);
			if (parts.length == 2) {
				metadata.setBoss(Boolean.parseBoolean(parts[1].trim()));
			} else {
				metadata.setBoss(true);
			}
		} else if (upper.startsWith("META")) {
			String body = cleaned.substring(4).trim();
			if (body.contains("=")) {
				String[] parts = body.split("=", 2);
				String key = parts[0].trim().toLowerCase();
				String val = parts[1].trim();
				if ("music".equals(key) || "track".equals(key)) {
					metadata.setMusicTrack(val);
				} else if ("boss".equals(key)) {
					metadata.setBoss(Boolean.parseBoolean(val));
				}
			}
		}
	}
}

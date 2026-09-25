package com.hongbao.bloons.descriptors;

public class BloonSpawnDescriptor {

	private final String bloonType;
	private final long spawnTick;
	private final boolean camo;
	private final boolean regen;

	public BloonSpawnDescriptor(String bloonType, long spawnTick, boolean camo, boolean regen) {
		if (bloonType != null && bloonType.endsWith("\r")) {
			this.bloonType = bloonType.substring(0, bloonType.length() - 1);
		} else {
			this.bloonType = bloonType;
		}
		this.spawnTick = spawnTick;
		this.camo = camo;
		this.regen = regen;
	}

	public BloonSpawnDescriptor(String bloonType, long spawnTick) {
		this(bloonType, spawnTick,
				bloonType != null && bloonType.contains("_camo"),
				bloonType != null && (bloonType.contains("_regen") || bloonType.contains("_regrowth")));
	}

	public String getBloonType() {
		return bloonType;
	}

	public long getSpawnTick() {
		return spawnTick;
	}

	public boolean isCamo() {
		return camo;
	}

	public boolean isRegen() {
		return regen;
	}

	public boolean isRegrowth() {
		return regen;
	}

	@Override
	public String toString() {
		return "BloonSpawnDescriptor{" +
				"bloonType='" + bloonType + '\'' +
				", spawnTick=" + spawnTick +
				", camo=" + camo +
				", regen=" + regen +
				'}';
	}

}

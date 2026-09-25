package com.hongbao.bloons;

import java.util.Collections;
import java.util.List;

public class WaveSegment {

	private final int amount;
	private final long delay;
	private final List<String> types;
	private final long startTick;

	public WaveSegment(int amount, long delay, List<String> types, long startTick) {
		this.amount = amount;
		this.delay = delay;
		this.types = types != null ? Collections.unmodifiableList(types) : Collections.emptyList();
		this.startTick = startTick;
	}

	public int getAmount() {
		return amount;
	}

	public long getDelay() {
		return delay;
	}

	public List<String> getTypes() {
		return types;
	}

	public long getStartTick() {
		return startTick;
	}

	public int getTotalBloons() {
		return amount * types.size();
	}

	public long getEndTick() {
		return startTick + (long) amount * types.size() * delay;
	}
}

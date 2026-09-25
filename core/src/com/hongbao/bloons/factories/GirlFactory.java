package com.hongbao.bloons.factories;

import com.hongbao.bloons.entities.Girl;

import java.util.Arrays;
import java.util.List;

import static com.hongbao.bloons.entities.Girl.NO_UPGRADES_AVAILABLE;


public class GirlFactory {

	public static class GirlBuilder {
		private String name;
		private List<Integer> attackDelay;
		private List<Float> bulletSpeed;
		private List<Integer> damage;
		private List<Integer> pierce;
		private List<Float> range;
		private List<Float> visualRange;
		private List<Boolean> homing;
		private String imageFileName;
		private String bulletFileName;
		private int cost;
		private List<Integer> upgradeCosts;

		public GirlBuilder name(String name) {
			this.name = name;
			return this;
		}

		public GirlBuilder attackDelay(List<Integer> attackDelay) {
			this.attackDelay = attackDelay;
			return this;
		}

		public GirlBuilder attackDelay(Integer... attackDelay) {
			this.attackDelay = Arrays.asList(attackDelay);
			return this;
		}

		public GirlBuilder bulletSpeed(List<Float> bulletSpeed) {
			this.bulletSpeed = bulletSpeed;
			return this;
		}

		public GirlBuilder bulletSpeed(Float... bulletSpeed) {
			this.bulletSpeed = Arrays.asList(bulletSpeed);
			return this;
		}

		public GirlBuilder damage(List<Integer> damage) {
			this.damage = damage;
			return this;
		}

		public GirlBuilder damage(Integer... damage) {
			this.damage = Arrays.asList(damage);
			return this;
		}

		public GirlBuilder pierce(List<Integer> pierce) {
			this.pierce = pierce;
			return this;
		}

		public GirlBuilder pierce(Integer... pierce) {
			this.pierce = Arrays.asList(pierce);
			return this;
		}

		public GirlBuilder range(List<Float> range) {
			this.range = range;
			return this;
		}

		public GirlBuilder range(Float... range) {
			this.range = Arrays.asList(range);
			return this;
		}

		public GirlBuilder visualRange(List<Float> visualRange) {
			this.visualRange = visualRange;
			return this;
		}

		public GirlBuilder visualRange(Float... visualRange) {
			this.visualRange = Arrays.asList(visualRange);
			return this;
		}

		public GirlBuilder homing(List<Boolean> homing) {
			this.homing = homing;
			return this;
		}

		public GirlBuilder homing(Boolean... homing) {
			this.homing = Arrays.asList(homing);
			return this;
		}

		public GirlBuilder image(String imageFileName) {
			this.imageFileName = imageFileName;
			return this;
		}

		public GirlBuilder bulletImage(String bulletFileName) {
			this.bulletFileName = bulletFileName;
			return this;
		}

		public GirlBuilder cost(int cost) {
			this.cost = cost;
			return this;
		}

		public GirlBuilder upgradeCosts(List<Integer> upgradeCosts) {
			this.upgradeCosts = upgradeCosts;
			return this;
		}

		public GirlBuilder upgradeCosts(Integer... upgradeCosts) {
			this.upgradeCosts = Arrays.asList(upgradeCosts);
			return this;
		}

		public void validateProgression() {
			if (upgradeCosts == null || damage == null || pierce == null || range == null) {
				return;
			}
			int maxTier = Math.min(upgradeCosts.size(), Math.min(damage.size(), Math.min(pierce.size(), range.size())));
			for (int i = 1; i < maxTier; i++) {
				Integer upgradeCost = upgradeCosts.get(i - 1);
				if (upgradeCost != null && upgradeCost > 0) {
					if (damage.get(i) <= damage.get(i - 1)) {
						throw new IllegalStateException("Stagnant damage progression for tower '" + name + "' at tier " + i);
					}
					if (pierce.get(i) <= pierce.get(i - 1)) {
						throw new IllegalStateException("Stagnant pierce progression for tower '" + name + "' at tier " + i);
					}
					if (range.get(i) <= range.get(i - 1)) {
						throw new IllegalStateException("Stagnant range progression for tower '" + name + "' at tier " + i);
					}
				}
			}
		}

		public Girl build() {
			validateProgression();
			return new Girl(
					name,
					attackDelay,
					bulletSpeed,
					damage,
					pierce,
					range,
					visualRange,
					homing,
					imageFileName,
					bulletFileName,
					cost,
					upgradeCosts
			);
		}
	}

	public static Girl createReimu() {
		return new GirlBuilder()
				.name("Reimu")
				.attackDelay(Arrays.asList(86, 86, 75))
				.bulletSpeed(Arrays.asList(20f, 20f, 20f))
				.damage(Arrays.asList(1, 2, 3))
				.pierce(Arrays.asList(4, 8, 13))
				.range(Arrays.asList(500f, 550f, 600f))
				.visualRange(Arrays.asList(200f, 220f, 250f))
				.homing(Arrays.asList(true, true, true))
				.image("reimu.png")
				.bulletImage("red_spell_card.png")
				.cost(325)
				.upgradeCosts(Arrays.asList(200, 280, NO_UPGRADES_AVAILABLE))
				.build();
	}

	public static Girl createYukari() {
		return new GirlBuilder()
				.name("Yukari")
				.attackDelay(Arrays.asList(4, 4, 4))
				.bulletSpeed(Arrays.asList(100f, 100f, 100f))
				.damage(Arrays.asList(1, 2, 3))
				.pierce(Arrays.asList(1, 2, 4))
				.range(Arrays.asList(600f, 700f, 1000f))
				.visualRange(Arrays.asList(300f, 400f, 500f))
				.homing(Arrays.asList(false, false, false))
				.image("yukari.png")
				.bulletImage("purple_energy.png")
				.cost(2500)
				.upgradeCosts(Arrays.asList(2500, 4500, NO_UPGRADES_AVAILABLE))
				.build();
	}

	public static Girl createMarisa() {
		return new GirlBuilder()
				.name("Marisa")
				.attackDelay(Arrays.asList(56, 56, 56))
				.bulletSpeed(Arrays.asList(35f, 50f, 75f))
				.damage(Arrays.asList(1, 2, 3))
				.pierce(Arrays.asList(1, 2, 4))
				.range(Arrays.asList(500f, 550f, 600f))
				.visualRange(Arrays.asList(100f, 125f, 150f))
				.homing(Arrays.asList(false, false, false))
				.image("marisa.png")
				.bulletImage("blue_magic_missile.png")
				.cost(200)
				.upgradeCosts(Arrays.asList(140, 220, NO_UPGRADES_AVAILABLE))
				.build();
	}

	public static Girl createAlice() {
		return new GirlBuilder()
				.name("Alice")
				.attackDelay(Arrays.asList(53, 53, 53))
				.bulletSpeed(Arrays.asList(50f, 50f, 50f))
				.damage(Arrays.asList(1, 2, 3))
				.pierce(Arrays.asList(2, 3, 4))
				.range(Arrays.asList(600f, 650f, 700f))
				.visualRange(Arrays.asList(200f, 250f, 300f))
				.homing(Arrays.asList(false, true, true))
				.image("alice.png")
				.bulletImage("magic_spike.png")
				.cost(450)
				.upgradeCosts(Arrays.asList(150, 600, NO_UPGRADES_AVAILABLE))
				.build();
	}

	public static Girl createSakuya() {
		return new GirlBuilder()
				.name("Sakuya")
				.attackDelay(Arrays.asList(30, 20, 20))
				.bulletSpeed(Arrays.asList(20f, 20f, 20f))
				.damage(Arrays.asList(1, 2, 3))
				.pierce(Arrays.asList(2, 3, 4))
				.range(Arrays.asList(500f, 550f, 600f))
				.visualRange(Arrays.asList(150f, 200f, 250f))
				.homing(Arrays.asList(false, false, false))
				.image("sakuya.png")
				.bulletImage("blue_knives.png")
				.cost(500)
				.upgradeCosts(Arrays.asList(300, 350, NO_UPGRADES_AVAILABLE))
				.build();
	}

	public static Girl createRemilia() {
		return new GirlBuilder()
				.name("Remilia")
				.attackDelay(Arrays.asList(4, 2, 2))
				.bulletSpeed(Arrays.asList(100f, 100f, 100f))
				.damage(Arrays.asList(1, 2, 3))
				.pierce(Arrays.asList(1, 2, 4))
				.range(Arrays.asList(600f, 700f, 1000f))
				.visualRange(Arrays.asList(300f, 400f, 500f))
				.homing(Arrays.asList(false, false, false))
				.image("remilia.png")
				.bulletImage("bat.png")
				.cost(2500)
				.upgradeCosts(Arrays.asList(4000, 5000, NO_UPGRADES_AVAILABLE))
				.build();
	}

	public static Girl createYoumu() {
		return new GirlBuilder()
				.name("Youmu")
				.attackDelay(Arrays.asList(90, 90, 90))
				.bulletSpeed(Arrays.asList(20f, 60f, 60f))
				.damage(Arrays.asList(1, 2, 3))
				.pierce(Arrays.asList(18, 28, 38))
				.range(Arrays.asList(200f, 300f, 400f))
				.visualRange(Arrays.asList(200f, 300f, 400f))
				.homing(Arrays.asList(false, false, false))
				.image("youmu.png")
				.bulletImage("sword_slash.png")
				.cost(600)
				.upgradeCosts(Arrays.asList(400, 800, NO_UPGRADES_AVAILABLE))
				.build();
	}

	public static Girl createYuyuko() {
		return new GirlBuilder()
				.name("Yuyuko")
				.attackDelay(Arrays.asList(40, 30, 20))
				.bulletSpeed(Arrays.asList(10f, 11f, 12f))
				.damage(Arrays.asList(20, 25, 30))
				.pierce(Arrays.asList(1, 2, 3))
				.range(Arrays.asList(500f, 600f, 700f))
				.visualRange(Arrays.asList(200f, 250f, 300f))
				.homing(Arrays.asList(true, true, true))
				.image("yuyuko.png")
				.bulletImage("pink_butterfly.png")
				.cost(2000)
				.upgradeCosts(Arrays.asList(500, 1500, NO_UPGRADES_AVAILABLE))
				.build();
	}

}

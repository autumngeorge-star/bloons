package com.hongbao.bloons.factories;

import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.modifiers.StatModifier;
import com.hongbao.bloons.modifiers.StatType;
import com.hongbao.bloons.modifiers.UpgradeDefinition;

import java.util.Arrays;

public class GirlFactory {

	public static Girl createReimu() {
		return new Girl(
				"Reimu",
				86,
				20f,
				1,
				4,
				500f,
				200f,
				true,
				"reimu.png",
				"red_spell_card.png",
				325,
				Arrays.asList(
						UpgradeDefinition.builder()
								.name("Focused Ofuda")
								.cost(200)
								.addFlatModifier(StatType.PIERCE, 4f)
								.addFlatModifier(StatType.VISUAL_RANGE, 20f)
								.build(),
						UpgradeDefinition.builder()
								.name("Fantasy Seal")
								.cost(280)
								.addFlatModifier(StatType.ATTACK_DELAY, -11f)
								.addFlatModifier(StatType.PIERCE, 5f)
								.addFlatModifier(StatType.VISUAL_RANGE, 30f)
								.build()
				)
		);
	}

	public static Girl createYukari() {
		return new Girl(
				"Yukari",
				4,
				100f,
				1,
				1,
				600f,
				300f,
				false,
				"yukari.png",
				"purple_energy.png",
				2500,
				Arrays.asList(
						UpgradeDefinition.builder()
								.name("Boundary Shift")
								.cost(2500)
								.addFlatModifier(StatType.PIERCE, 1f)
								.addFlatModifier(StatType.RANGE, 100f)
								.addFlatModifier(StatType.VISUAL_RANGE, 100f)
								.build(),
						UpgradeDefinition.builder()
								.name("Quadruple Gap")
								.cost(4500)
								.addFlatModifier(StatType.DAMAGE, 1f)
								.addFlatModifier(StatType.PIERCE, 2f)
								.addFlatModifier(StatType.RANGE, 300f)
								.addFlatModifier(StatType.VISUAL_RANGE, 100f)
								.build()
				)
		);
	}

	public static Girl createMarisa() {
		return new Girl(
				"Marisa",
				56,
				35f,
				1,
				1,
				500f,
				100f,
				false,
				"marisa.png",
				"blue_magic_missile.png",
				200,
				Arrays.asList(
						UpgradeDefinition.builder()
								.name("Magic Missile")
								.cost(140)
								.addFlatModifier(StatType.BULLET_SPEED, 15f)
								.addFlatModifier(StatType.PIERCE, 1f)
								.addFlatModifier(StatType.VISUAL_RANGE, 25f)
								.build(),
						UpgradeDefinition.builder()
								.name("Master Spark")
								.cost(220)
								.addFlatModifier(StatType.BULLET_SPEED, 25f)
								.addFlatModifier(StatType.PIERCE, 2f)
								.addFlatModifier(StatType.VISUAL_RANGE, 25f)
								.build()
				)
		);
	}

	public static Girl createAlice() {
		return new Girl(
				"Alice",
				53,
				50f,
				1,
				2,
				600f,
				200f,
				false,
				"alice.png",
				"magic_spike.png",
				450,
				Arrays.asList(
						UpgradeDefinition.builder()
								.name("Homing Dolls")
								.cost(150)
								.addFlatModifier(StatType.VISUAL_RANGE, 50f)
								.addModifier(StatModifier.homing(true))
								.build(),
						UpgradeDefinition.builder()
								.name("Shanghai Doll")
								.cost(600)
								.addFlatModifier(StatType.DAMAGE, 1f)
								.addFlatModifier(StatType.VISUAL_RANGE, 50f)
								.build()
				)
		);
	}

	public static Girl createSakuya() {
		return new Girl(
				"Sakuya",
				30,
				20f,
				1,
				2,
				500f,
				150f,
				false,
				"sakuya.png",
				"blue_knives.png",
				500,
				Arrays.asList(
						UpgradeDefinition.builder()
								.name("Quick Throw")
								.cost(300)
								.addFlatModifier(StatType.ATTACK_DELAY, -10f)
								.addFlatModifier(StatType.VISUAL_RANGE, 50f)
								.build(),
						UpgradeDefinition.builder()
								.name("Knife Fan")
								.cost(350)
								.addFlatModifier(StatType.PIERCE, 2f)
								.addFlatModifier(StatType.VISUAL_RANGE, 50f)
								.build()
				)
		);
	}

	public static Girl createRemilia() {
		return new Girl(
				"Remilia",
				4,
				100f,
				1,
				1,
				600f,
				300f,
				false,
				"remilia.png",
				"bat.png",
				2500,
				Arrays.asList(
						UpgradeDefinition.builder()
								.name("Demon Lord Walk")
								.cost(4000)
								.addFlatModifier(StatType.ATTACK_DELAY, -2f)
								.addFlatModifier(StatType.PIERCE, 1f)
								.addFlatModifier(StatType.RANGE, 100f)
								.addFlatModifier(StatType.VISUAL_RANGE, 100f)
								.build(),
						UpgradeDefinition.builder()
								.name("Scarlet Fate")
								.cost(5000)
								.addFlatModifier(StatType.PIERCE, 2f)
								.addFlatModifier(StatType.RANGE, 300f)
								.addFlatModifier(StatType.VISUAL_RANGE, 100f)
								.build()
				)
		);
	}

	public static Girl createYoumu() {
		return new Girl(
				"Youmu",
				90,
				20f,
				1,
				18,
				200f,
				200f,
				false,
				"youmu.png",
				"sword_slash.png",
				600,
				Arrays.asList(
						UpgradeDefinition.builder()
								.name("Roukanken Cut")
								.cost(400)
								.addFlatModifier(StatType.BULLET_SPEED, 40f)
								.addFlatModifier(StatType.PIERCE, 10f)
								.addFlatModifier(StatType.RANGE, 100f)
								.addFlatModifier(StatType.VISUAL_RANGE, 100f)
								.build(),
						UpgradeDefinition.builder()
								.name("Hakurouken Precision")
								.cost(800)
								.addFlatModifier(StatType.DAMAGE, 1f)
								.addFlatModifier(StatType.RANGE, 100f)
								.addFlatModifier(StatType.VISUAL_RANGE, 100f)
								.build()
				)
		);
	}

	public static Girl createYuyuko() {
		return new Girl(
				"Yuyuko",
				40,
				10f,
				20,
				1,
				500f,
				200f,
				true,
				"yuyuko.png",
				"pink_butterfly.png",
				2000,
				Arrays.asList(
						UpgradeDefinition.builder()
								.name("Ghastly Dream")
								.cost(500)
								.addFlatModifier(StatType.ATTACK_DELAY, -10f)
								.addFlatModifier(StatType.BULLET_SPEED, 1f)
								.addFlatModifier(StatType.RANGE, 100f)
								.addFlatModifier(StatType.VISUAL_RANGE, 50f)
								.build(),
						UpgradeDefinition.builder()
								.name("Saigyouji Flurry")
								.cost(1500)
								.addFlatModifier(StatType.ATTACK_DELAY, -10f)
								.addFlatModifier(StatType.BULLET_SPEED, 1f)
								.addFlatModifier(StatType.RANGE, 100f)
								.addFlatModifier(StatType.VISUAL_RANGE, 50f)
								.build()
				)
		);
	}

}

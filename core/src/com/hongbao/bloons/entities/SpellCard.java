package com.hongbao.bloons.entities;

import com.hongbao.bloons.factories.GirlFactory;

import java.util.*;


public class SpellCard {
	
	public static final String IMAGE_FOLDER = "img/spellcards/";
	
	private Map<Integer, List<Bullet>> bulletsToCreate;
	private String overrideName;
	private String imageFileName;
	private int frame;
	private int lastFrame;
	private float scale;
	
	public SpellCard(String overrideName, Map<Integer, List<Bullet>> bulletsToCreate, String imageFileName, float scale) {
		this.overrideName = overrideName;
		this.bulletsToCreate = bulletsToCreate;
		this.imageFileName = IMAGE_FOLDER + imageFileName;
		this.scale = scale;
		frame = 0;
		
		lastFrame = bulletsToCreate.keySet().stream()
		 .max(Comparator.naturalOrder())
		 .orElse(Integer.MAX_VALUE);
	}
	
	public String getOverrideName() {
		return overrideName;
	}
	
	public String getImageFileName() {
		return extractRegionKey(imageFileName);
	}

	public static String extractRegionKey(String path) {
		if (path == null) return "";
		String name = path;
		if (name.contains("/")) {
			name = name.substring(name.lastIndexOf('/') + 1);
		}
		if (name.endsWith(".png")) {
			name = name.substring(0, name.length() - 4);
		}
		return name;
	}
	
	public List<Bullet> getBulletsToCreateAndIncrementFrame() {
		List<Bullet> bullets = bulletsToCreate.get(frame);
		frame++;
		return bullets;
	}
	
	public boolean isExpired() {
		return frame > lastFrame;
	}

	public float getScale() {
		return scale;
	}
	
	public static SpellCard createReimuSpellCard() {
		Map<Integer, List<Bullet>> bulletsToCreate = new HashMap<>();
		for (int x = 0; x < 1000; x += 25) {
			Bullet bullet1 = GirlFactory.createReimu().createBullet();
			bullet1.setMaxRange(5000);
			bullet1.setInitialDXOverride(0);
			bullet1.setInitialDYOverride(1);
			
			Bullet bullet2 = GirlFactory.createReimu().createBullet();
			bullet2.setMaxRange(5000);
			bullet2.setInitialDXOverride((float)(Math.sqrt(3) / 2));
			bullet2.setInitialDYOverride(0.5f);
			
			Bullet bullet3 = GirlFactory.createReimu().createBullet();
			bullet3.setMaxRange(5000);
			bullet3.setInitialDXOverride((float)(-Math.sqrt(3) / 2));
			bullet3.setInitialDYOverride(0.5f);
			
			Bullet bullet4 = GirlFactory.createReimu().createBullet();
			bullet4.setMaxRange(5000);
			bullet4.setInitialDXOverride(0);
			bullet4.setInitialDYOverride(-1);
			
			Bullet bullet5 = GirlFactory.createReimu().createBullet();
			bullet5.setMaxRange(5000);
			bullet5.setInitialDXOverride((float)(Math.sqrt(3) / 2));
			bullet5.setInitialDYOverride(-0.5f);
			
			Bullet bullet6 = GirlFactory.createReimu().createBullet();
			bullet6.setMaxRange(5000);
			bullet6.setInitialDXOverride((float)(-Math.sqrt(3) / 2));
			bullet6.setInitialDYOverride(-0.5f);
			
			bulletsToCreate.put(x, Arrays.asList(bullet1, bullet2, bullet3, bullet4, bullet5, bullet6));
		}
		return new SpellCard("Reimu", bulletsToCreate, "reimu_spell.png", 5.0f);
	}

	public static SpellCard createYuyukoSpellCard() {
		Map<Integer, List<Bullet>> bulletsToCreate = new HashMap<>();
		Girl yuyuko = GirlFactory.createYuyuko();

		for (int x = 0; x < 1500; x += 25) {
			List<Bullet> bulletsForCurrentFrame = new ArrayList<>();
			double offset;
			int bullets;

			// Shoots more bullets as time goes on
			if (x < 500) {
				offset = 2 * Math.PI / 4; // Quarter rotation
				bullets = 4;
			} else if (x < 1000) {
				offset = 2 * Math.PI / 8; // 1/8th rotation
				bullets = 8;
			} else {
				offset = 2 * Math.PI / 16; // 1/16th rotation
				bullets = 16;
			}

			for (int i = 0; i < bullets; i++) {
				Bullet bullet = yuyuko.createBullet();
				bullet.setSpeed(5f);
				bullet.setMaxRange(5000);
				double currentAngle = offset * i;
				double desiredAngle = currentAngle + (x * Math.PI / 500);
				bullet.setInitialXOffset(-125);
				bullet.setInitialDXOverride((float) Math.cos(desiredAngle));
				bullet.setInitialDYOverride((float) Math.sin(desiredAngle));

				bulletsForCurrentFrame.add(bullet);
			}

			for (int i = 0; i < bullets; i++) {
				Bullet bullet = yuyuko.createBullet();
				bullet.setSpeed(5f);
				bullet.setMaxRange(5000);
				double currentAngle = offset * i;
				double desiredAngle = currentAngle - (x * Math.PI / 500);
				bullet.setInitialXOffset(125);
				bullet.setInitialDXOverride((float) Math.cos(desiredAngle));
				bullet.setInitialDYOverride((float) Math.sin(desiredAngle));

				bulletsForCurrentFrame.add(bullet);
			}

			bulletsToCreate.put(x, bulletsForCurrentFrame);
		}
		return new SpellCard("Yuyuko", bulletsToCreate, "yuyuko_fan.png", 1.0f);
	}

}

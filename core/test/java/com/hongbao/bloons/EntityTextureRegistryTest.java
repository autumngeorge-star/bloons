package com.hongbao.bloons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EntityTextureRegistryTest {

	@Before
	public void setUp() {
		EntityTextureRegistry.dispose();
	}

	@Test
	public void testFlyweightPatternReturnsSameInstance() {
		TextureRegion mockRegion = new TextureRegion();
		String path = "img/bloons/red_bloon.png";

		EntityTextureRegistry.getInstance().register(path, mockRegion);

		assertTrue(EntityTextureRegistry.isRegistered(path));

		TextureRegion region1 = EntityTextureRegistry.getTextureRegion(path);
		TextureRegion region2 = EntityTextureRegistry.getTextureRegion(path);

		assertNotNull(region1);
		assertSame("Registry must return identical TextureRegion flyweight instance", region1, region2);
		assertSame("Returned region must match registered mock region", mockRegion, region1);
	}

	@Test
	public void testDisposeClearsRegistry() {
		TextureRegion mockRegion1 = new TextureRegion();
		TextureRegion mockRegion2 = new TextureRegion();

		EntityTextureRegistry.getInstance().register("path1", mockRegion1);
		EntityTextureRegistry.getInstance().register("path2", mockRegion2);

		assertEquals(2, EntityTextureRegistry.getRegisteredCount());

		EntityTextureRegistry.dispose();

		assertEquals(0, EntityTextureRegistry.getRegisteredCount());
		assertFalse(EntityTextureRegistry.isRegistered("path1"));
		assertFalse(EntityTextureRegistry.isRegistered("path2"));
	}
}

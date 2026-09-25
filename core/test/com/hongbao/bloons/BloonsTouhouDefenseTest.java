package com.hongbao.bloons;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.entities.GirlConfig;
import com.hongbao.bloons.factories.GirlFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class BloonsTouhouDefenseTest {

	@BeforeClass
	public static void setUpClass() {
		Gdx.files = new Files() {
			@Override
			public FileHandle getFileHandle(String path, Files.FileType type) {
				return new FileHandle(new File("core/assets/" + path));
			}

			@Override
			public FileHandle classpath(String path) {
				return new FileHandle(new File("core/assets/" + path));
			}

			@Override
			public FileHandle internal(String path) {
				return new FileHandle(new File(path.startsWith("core/assets/") ? path : "core/assets/" + path));
			}

			@Override
			public FileHandle external(String path) {
				return new FileHandle(new File(path));
			}

			@Override
			public FileHandle absolute(String path) {
				return new FileHandle(new File(path));
			}

			@Override
			public FileHandle local(String path) {
				return new FileHandle(new File(path));
			}

			@Override
			public String getExternalStoragePath() {
				return "";
			}

			@Override
			public boolean isExternalStorageAvailable() {
				return false;
			}

			@Override
			public String getLocalStoragePath() {
				return "";
			}

			@Override
			public boolean isLocalStorageAvailable() {
				return false;
			}
		};
	}

	@Test
	public void testGdxFileLoadingAndTowerCreation() {
		GirlFactory.reloadTowers();
		List<GirlConfig> configs = GirlFactory.getLoadedConfigs();
		assertNotNull(configs);
		assertEquals(8, configs.size());

		Girl reimu = GirlFactory.createReimu();
		assertNotNull(reimu);
		assertEquals("Reimu", reimu.getName());
		assertEquals(325, reimu.getCost());

		Girl yuyuko = GirlFactory.createYuyuko();
		assertNotNull(yuyuko);
		assertEquals("Yuyuko", yuyuko.getName());
		assertEquals(2000, yuyuko.getCost());
	}

}

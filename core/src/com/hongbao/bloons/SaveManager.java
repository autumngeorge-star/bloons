package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.dto.GirlSaveData;
import com.hongbao.bloons.dto.SaveStateData;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.factories.GirlFactory;

import java.util.ArrayList;
import java.util.List;

public class SaveManager {

	public static final String SAVE_FILE_NAME = "savegame.json";

	public static boolean saveGame(BloonsTouhouDefense game) {
		if (game == null || game.getPlayer() == null || game.getMap() == null) {
			return false;
		}

		Player player = game.getPlayer();
		if (player.getHealth() <= 0) {
			deleteSave();
			return false;
		}

		SaveStateData saveData = new SaveStateData();
		saveData.setMoney(player.getMoney());
		saveData.setHealth(player.getHealth());
		saveData.setLevel(game.getMap().getBloonManager().getLevel());

		List<GirlSaveData> girlSaveDataList = new ArrayList<>();
		if (game.getMap().getOnStageGirls() != null) {
			for (GirlActor girlActor : game.getMap().getOnStageGirls()) {
				if (girlActor != null && girlActor.isActive() && girlActor.getGirl() != null) {
					GirlSaveData girlData = new GirlSaveData(
							girlActor.getGirl().getName(),
							girlActor.getCenterX(),
							girlActor.getCenterY(),
							girlActor.getGirl().getLevel(),
							girlActor.getRotationAngle()
					);
					girlSaveDataList.add(girlData);
				}
			}
		}
		saveData.setGirls(girlSaveDataList);

		try {
			Json json = new Json();
			json.setOutputType(JsonWriter.OutputType.json);
			FileHandle file = Gdx.files.local(SAVE_FILE_NAME);
			file.writeString(json.toJson(saveData), false);
			return true;
		} catch (Exception e) {
			if (Gdx.app != null) {
				Gdx.app.error("SaveManager", "Error saving game to " + SAVE_FILE_NAME, e);
			}
			return false;
		}
	}

	public static boolean hasSave() {
		try {
			FileHandle file = Gdx.files.local(SAVE_FILE_NAME);
			return file.exists() && file.length() > 0;
		} catch (Exception e) {
			return false;
		}
	}

	public static SaveStateData loadSaveData() {
		if (!hasSave()) {
			return null;
		}
		try {
			FileHandle file = Gdx.files.local(SAVE_FILE_NAME);
			Json json = new Json();
			return json.fromJson(SaveStateData.class, file.readString());
		} catch (Exception e) {
			if (Gdx.app != null) {
				Gdx.app.error("SaveManager", "Error reading " + SAVE_FILE_NAME, e);
			}
			deleteSave();
			return null;
		}
	}

	public static boolean loadGame(BloonsTouhouDefense game) {
		SaveStateData saveData = loadSaveData();
		if (saveData == null) {
			return false;
		}

		if (saveData.getHealth() <= 0) {
			deleteSave();
			return false;
		}

		Player player = game.getPlayer();
		if (player != null) {
			player.setMoney(saveData.getMoney());
			player.setHealth(saveData.getHealth());
		}

		if (game.getMap() != null && game.getMap().getBloonManager() != null) {
			game.getMap().getBloonManager().setLevel(saveData.getLevel());
		}

		if (game.getMap() != null && saveData.getGirls() != null) {
			for (GirlSaveData girlData : saveData.getGirls()) {
				Girl girl = GirlFactory.createGirlByName(girlData.getName());
				if (girl != null) {
					for (int i = 0; i < girlData.getLevel(); i++) {
						girl.upgrade();
					}
					GirlActor girlActor = new GirlActor(girl, girlData.getX(), girlData.getY());
					girlActor.setRotationAngle(girlData.getRotationAngle());
					game.getMap().placeGirl(girlActor);
				}
			}
			game.getMap().setSelectedGirl(null);
		}

		return true;
	}

	public static void deleteSave() {
		try {
			FileHandle file = Gdx.files.local(SAVE_FILE_NAME);
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			if (Gdx.app != null) {
				Gdx.app.error("SaveManager", "Error deleting " + SAVE_FILE_NAME, e);
			}
		}
	}

}

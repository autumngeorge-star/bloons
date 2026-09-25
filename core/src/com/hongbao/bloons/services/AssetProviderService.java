package com.hongbao.bloons.services;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.entities.SpellCard;

public interface AssetProviderService extends Disposable {

	void loadAssets();

	void finishLoading();

	Texture getTexture(String filePath);

	Texture getBloonTexture(Bloon bloon);

	Texture getGirlTexture(Girl girl);

	Texture getBulletTexture(Bullet bullet);

	Texture getSpellCardTexture(SpellCard spellCard);

	Texture getInstructionTexture(String instructionName);

	Texture getUiTexture(String uiName);

	Skin getUiSkin();

	Sound getSound(String soundPath);

	Sound getPopSound();

	Music getMusic(String musicPath);

	Music getTitleMusic();

	Music getStageMusic();

	Music getFinalBossMusic();

	FileHandle getFileHandle(String filePath);

	FileHandle getBloonQueueFile(String fileName);
}

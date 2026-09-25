package com.hongbao.bloons.loader;

import com.badlogic.gdx.files.FileHandle;
import com.hongbao.bloons.BloonQueue;

public interface WaveLoader {

	BloonQueue loadWaveQueue(FileHandle file);

}

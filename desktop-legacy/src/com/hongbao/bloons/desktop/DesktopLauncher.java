package com.hongbao.bloons.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.hongbao.bloons.BloonsTouhouDefense;


public class DesktopLauncher {
	
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "bloons-tower-defence";
		config.width = 1800;
		config.height = 900;
		config.resizable = false;
		new LwjglApplication(new BloonsTouhouDefense(), config);
	}
}

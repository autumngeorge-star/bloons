package com.hongbao.bloons.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.hongbao.bloons.BloonsTouhouDefense;


public class DesktopLauncher {
	
	public static void main(String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Bloons Touhou Defense");
		config.setWindowedMode(800, 600);
		config.setForegroundFPS(60);
		new Lwjgl3Application(new BloonsTouhouDefense(), config);
	}
}

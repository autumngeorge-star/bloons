package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.lang.reflect.Proxy;

public class UIManagerTest {

	public static void main(String[] args) {
		HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
		new HeadlessApplication(new ApplicationAdapter() {
			@Override
			public void create() {
				try {
					GL20 mockGL = (GL20) Proxy.newProxyInstance(
						GL20.class.getClassLoader(),
						new Class<?>[]{GL20.class},
						(proxy, method, methodArgs) -> {
							String name = method.getName();
							if ("glGenTexture".equals(name) || "glCreateShader".equals(name) || "glCreateProgram".equals(name)) return 1;
							if ("glGetActiveAttrib".equals(name)) {
								return "a_position";
							}
							if ("glGetActiveUniform".equals(name)) {
								return "u_texture";
							}
							if (name.startsWith("glGetShader") || name.startsWith("glGetProgram")) {
								if (methodArgs != null && methodArgs.length >= 3) {
									Object arg = methodArgs[2];
									if (arg instanceof java.nio.IntBuffer) {
										((java.nio.IntBuffer) arg).put(0, 1);
									} else if (arg instanceof int[]) {
										((int[]) arg)[0] = 1;
									}
								}
								if (method.getReturnType().equals(String.class)) {
									return "";
								}
								return null;
							}
							if (method.getReturnType().equals(String.class)) {
								return "";
							}
							if (method.getReturnType().equals(boolean.class)) return false;
							if (method.getReturnType().equals(int.class)) return 0;
							return null;
						}
					);
					Gdx.gl = mockGL;
					Gdx.gl20 = mockGL;

					testUIManager();
					System.out.println("ALL UIManager TESTS PASSED SUCCESSFULLY!");
					System.exit(0);
				} catch (Throwable t) {
					t.printStackTrace();
					System.exit(1);
				}
			}
		}, config);
	}

	public static void testUIManager() {
		UIManager uiManager = UIManager.getInstance();

		// 1. Test skin sharing (single load, cached instance)
		Skin skin1 = uiManager.getSkin();
		Skin skin2 = uiManager.getSkin();
		if (skin1 == null || skin2 == null) {
			throw new AssertionError("Skin failed to load!");
		}
		if (skin1 != skin2) {
			throw new AssertionError("Skin instances are different! Skin was not cached properly.");
		}
		if (!uiManager.isSkinLoaded(UIManager.DEFAULT_SKIN_PATH)) {
			throw new AssertionError("isSkinLoaded returned false for DEFAULT_SKIN_PATH!");
		}

		// 2. Test texture and drawable caching
		String headerPath = "img/ui/header.png";
		if (uiManager.isTextureLoaded(headerPath)) {
			throw new AssertionError("header.png should not be loaded yet!");
		}
		uiManager.getTexture(headerPath);
		if (!uiManager.isTextureLoaded(headerPath)) {
			throw new AssertionError("header.png was not loaded into cache!");
		}

		// 3. Test explicit instruction texture unloading
		String instructionPath = "img/instructions/title.png";
		uiManager.getTexture(instructionPath);
		if (!uiManager.isTextureLoaded(instructionPath)) {
			throw new AssertionError("Instruction texture title.png was not loaded!");
		}
		uiManager.unloadInstructionTexture(instructionPath);
		if (uiManager.isTextureLoaded(instructionPath)) {
			throw new AssertionError("Instruction texture title.png was not unloaded!");
		}
		// Confirm header.png is still loaded
		if (!uiManager.isTextureLoaded(headerPath)) {
			throw new AssertionError("Unloading instruction texture accidentally removed header.png!");
		}

		// 4. Test Map integration with UIManager
		Map testMap = new Map("heater.png", null, uiManager);
		if (!uiManager.isTextureLoaded("img/ui/girl_details_template.png")) {
			throw new AssertionError("Map failed to load girl_details_template.png into UIManager!");
		}

		// 5. Test BloonsTouhouDefense instruction screen flow and explicit unloading
		BloonsTouhouDefense game = new BloonsTouhouDefense();
		// Manually initialize stage to avoid window creation in headless test
		game.create();

		String[] instructionPaths = new String[]{
			"img/instructions/title.png",
			"img/instructions/objective.png",
			"img/instructions/bloons.png",
			"img/instructions/blimps.png",
			"img/instructions/girls.png",
			"img/instructions/shortcuts.png"
		};

		for (String path : instructionPaths) {
			if (!uiManager.isTextureLoaded(path)) {
				throw new AssertionError("Instruction texture " + path + " was not loaded!");
			}
		}

		// Simulate user progressing through and dismissing instructions
		for (int i = 0; i < instructionPaths.length; i++) {
			String dismissedPath = instructionPaths[i];
			// Dismiss one instruction screen
			game.updateInstructions();

			if (uiManager.isTextureLoaded(dismissedPath)) {
				throw new AssertionError("Dismissed instruction texture " + dismissedPath + " was not explicitly unloaded!");
			}
		}

		// Verify menu assets remain cached after instructions are dismissed
		if (!uiManager.isTextureLoaded("img/ui/header.png")) {
			throw new AssertionError("header.png was lost after dismissing instructions!");
		}
		if (!uiManager.isTextureLoaded("img/ui/reimu_box.png")) {
			throw new AssertionError("reimu_box.png was lost after dismissing instructions!");
		}

		// 6. Test disposal cleans up all remaining resources
		game.dispose();
		if (uiManager.isTextureLoaded(headerPath)) {
			throw new AssertionError("header.png was still loaded after game.dispose()!");
		}
		if (uiManager.isSkinLoaded(UIManager.DEFAULT_SKIN_PATH)) {
			throw new AssertionError("Default skin was still loaded after game.dispose()!");
		}
	}
}

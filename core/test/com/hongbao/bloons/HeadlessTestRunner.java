package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;

import java.lang.reflect.Proxy;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class HeadlessTestRunner {

    public static class TestBloonsTouhouDefense extends BloonsTouhouDefense {
        private Player testPlayer;

        public TestBloonsTouhouDefense() {
            instructions = new ArrayList<>();
            testPlayer = new Player(MONEY, HEALTH);
        }

        @Override
        public Player getPlayer() {
            return testPlayer;
        }

        @Override
        public void create() {
            // No-op for headless unit test initialization
        }

        @Override
        public void render() {
            // No-op for headless unit test initialization
        }
    }

    public static void initialize() {
        if (Gdx.gl == null) {
            Gdx.gl = Gdx.gl20 = (GL20) Proxy.newProxyInstance(
                GL20.class.getClassLoader(),
                new Class<?>[]{GL20.class},
                (proxy, method, args) -> {
                    String name = method.getName();
                    if (name.equals("glGetShaderiv") || name.equals("glGetProgramiv")) {
                        if (args != null && args.length >= 3 && args[2] instanceof IntBuffer) {
                            ((IntBuffer) args[2]).put(0, 1);
                        }
                        return null;
                    }
                    if (name.equals("glGetActiveAttrib")) {
                        return "a_position";
                    }
                    if (name.equals("glGetActiveUniform")) {
                        return "u_projModelView";
                    }
                    if (method.getReturnType().equals(String.class)) {
                        return "dummy";
                    }
                    if (method.getReturnType().equals(int.class)) {
                        return 1;
                    }
                    if (method.getReturnType().equals(boolean.class)) {
                        return false;
                    }
                    return null;
                }
            );
        }

        if (Gdx.app == null) {
            HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
            new HeadlessApplication(new TestBloonsTouhouDefense(), config);
        }
    }
}

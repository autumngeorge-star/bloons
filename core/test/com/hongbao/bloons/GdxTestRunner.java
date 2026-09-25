package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.badlogic.gdx.backends.lwjgl.LwjglNativesLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import static org.mockito.Mockito.mock;

public class GdxTestRunner {
    private static boolean initialized = false;

    public static synchronized void initGdx() {
        if (initialized) return;
        LwjglNativesLoader.load();

        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        new HeadlessApplication(new ApplicationListener() {
            @Override public void create() {}
            @Override public void resize(int width, int height) {}
            @Override public void render() {}
            @Override public void pause() {}
            @Override public void resume() {}
            @Override public void dispose() {}
        }, config);

        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = Gdx.gl;

        final HeadlessFiles delegate = new HeadlessFiles();

        Gdx.files = new Files() {
            @Override
            public FileHandle getFileHandle(String path, FileType type) {
                FileHandle fh = delegate.getFileHandle(path, type);
                if (fh.exists()) return fh;

                fh = delegate.getFileHandle("core/assets/" + path, type);
                if (fh.exists()) return fh;

                fh = delegate.getFileHandle("assets/" + path, type);
                if (fh.exists()) return fh;

                fh = delegate.classpath(path);
                if (fh.exists()) return fh;

                return delegate.getFileHandle(path, type);
            }

            @Override
            public FileHandle classpath(String path) {
                return delegate.classpath(path);
            }

            @Override
            public FileHandle internal(String path) {
                return getFileHandle(path, FileType.Internal);
            }

            @Override
            public FileHandle external(String path) {
                return delegate.external(path);
            }

            @Override
            public FileHandle absolute(String path) {
                return delegate.absolute(path);
            }

            @Override
            public FileHandle local(String path) {
                return delegate.local(path);
            }

            @Override
            public String getExternalStoragePath() {
                return delegate.getExternalStoragePath();
            }

            @Override
            public boolean isExternalStorageAvailable() {
                return delegate.isExternalStorageAvailable();
            }

            @Override
            public String getLocalStoragePath() {
                return delegate.getLocalStoragePath();
            }

            @Override
            public boolean isLocalStorageAvailable() {
                return delegate.isLocalStorageAvailable();
            }
        };
        initialized = true;
    }
}

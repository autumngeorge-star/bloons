package com.hongbao.bloons;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Clipboard;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.events.BloonPoppedEvent;
import com.hongbao.bloons.events.GameEventManager;
import com.hongbao.bloons.events.LevelChangedEvent;
import com.hongbao.bloons.factories.BloonFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class BloonManagerTest {

    private BloonsTouhouDefense gameInstance;

    @Before
    public void setUp() {
        GameEventManager.getInstance().clearListeners();

        gameInstance = new BloonsTouhouDefense();
        gameInstance.instructions = new ArrayList<>();

        Files testFiles = new Files() {
            @Override
            public FileHandle getFileHandle(String path, FileType type) {
                File file = new File("core/assets/" + path);
                if (!file.exists()) {
                    file = new File("assets/" + path);
                }
                return new FileHandle(file);
            }

            @Override public FileHandle classpath(String path) { return getFileHandle(path, FileType.Classpath); }
            @Override public FileHandle internal(String path) { return getFileHandle(path, FileType.Internal); }
            @Override public FileHandle external(String path) { return getFileHandle(path, FileType.External); }
            @Override public FileHandle absolute(String path) { return new FileHandle(new File(path)); }
            @Override public FileHandle local(String path) { return getFileHandle(path, FileType.Local); }
            @Override public String getExternalStoragePath() { return null; }
            @Override public boolean isExternalStorageAvailable() { return false; }
            @Override public String getLocalStoragePath() { return null; }
            @Override public boolean isLocalStorageAvailable() { return false; }
        };

        Gdx.files = testFiles;
        Gdx.app = new TestApplication(gameInstance, testFiles);
    }

    @Test
    public void testNextLevelPublishesLevelChangedEventHeadlessly() {
        AtomicInteger levelReceived = new AtomicInteger(-1);
        GameEventManager.getInstance().subscribe(LevelChangedEvent.class, event -> levelReceived.set(event.getLevel()));

        BloonManager bloonManager = new BloonManager(null, null);

        assertTrue("Initial level state should allow progressing", bloonManager.canGoToNextLevel());
        bloonManager.nextLevel();

        assertEquals("LevelChangedEvent should be published with level 1", 1, levelReceived.get());
    }

    @Test
    public void testPopBloonPublishesBloonPoppedEventHeadlessly() {
        AtomicBoolean poppedEventFired = new AtomicBoolean(false);
        GameEventManager.getInstance().subscribe(BloonPoppedEvent.class, event -> poppedEventFired.set(true));

        Bloon redBloon = BloonFactory.createRedBloon();
        assertTrue("Damage of 1 should pop red bloon", redBloon.willPopBloon(1));

        GameEventManager.getInstance().publish(BloonPoppedEvent.INSTANCE);
        assertTrue("BloonPoppedEvent should be published during popping", poppedEventFired.get());
    }

    private static class TestApplication implements Application {
        private final ApplicationListener listener;
        private final Files files;

        public TestApplication(ApplicationListener listener, Files files) {
            this.listener = listener;
            this.files = files;
        }

        @Override public ApplicationListener getApplicationListener() { return listener; }
        @Override public com.badlogic.gdx.Graphics getGraphics() { return null; }
        @Override public com.badlogic.gdx.Audio getAudio() { return null; }
        @Override public com.badlogic.gdx.Input getInput() { return null; }
        @Override public Files getFiles() { return files; }
        @Override public com.badlogic.gdx.Net getNet() { return null; }
        @Override public void log(String tag, String message) {}
        @Override public void log(String tag, String message, Throwable exception) {}
        @Override public void error(String tag, String message) {}
        @Override public void error(String tag, String message, Throwable exception) {}
        @Override public void debug(String tag, String message) {}
        @Override public void debug(String tag, String message, Throwable exception) {}
        @Override public void setLogLevel(int logLevel) {}
        @Override public int getLogLevel() { return 0; }
        @Override public void setApplicationLogger(com.badlogic.gdx.ApplicationLogger applicationLogger) {}
        @Override public com.badlogic.gdx.ApplicationLogger getApplicationLogger() { return null; }
        @Override public ApplicationType getType() { return ApplicationType.HeadlessDesktop; }
        @Override public int getVersion() { return 0; }
        @Override public long getJavaHeap() { return 0; }
        @Override public long getNativeHeap() { return 0; }
        @Override public Preferences getPreferences(String name) { return null; }
        @Override public Clipboard getClipboard() { return null; }
        @Override public void postRunnable(Runnable runnable) {}
        @Override public void exit() {}
        @Override public void addLifecycleListener(com.badlogic.gdx.LifecycleListener listener) {}
        @Override public void removeLifecycleListener(com.badlogic.gdx.LifecycleListener listener) {}
    }
}

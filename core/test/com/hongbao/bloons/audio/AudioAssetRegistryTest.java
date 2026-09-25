package com.hongbao.bloons.audio;

import com.badlogic.gdx.utils.Disposable;
import com.hongbao.bloons.events.ApplicationDisposeEvent;
import com.hongbao.bloons.events.GameEventBus;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

public class AudioAssetRegistryTest {

    private GameEventBus eventBus;
    private AudioAssetRegistry registry;

    @Before
    public void setUp() {
        eventBus = new GameEventBus();
        registry = new AudioAssetRegistry(eventBus);
    }

    private static class MockDisposable implements Disposable {
        private final AtomicBoolean disposed = new AtomicBoolean(false);

        @Override
        public void dispose() {
            disposed.set(true);
        }

        public boolean isDisposed() {
            return disposed.get();
        }
    }

    @Test
    public void testRegisterAndDisposeOnEvent() {
        MockDisposable asset1 = new MockDisposable();
        MockDisposable asset2 = new MockDisposable();

        registry.register(asset1);
        registry.register(asset2);

        assertEquals(2, registry.getManagedAssetCount());

        eventBus.publish(new ApplicationDisposeEvent());

        assertTrue(asset1.isDisposed());
        assertTrue(asset2.isDisposed());
        assertEquals(0, registry.getManagedAssetCount());
    }

    @Test
    public void testUnregister() {
        MockDisposable asset = new MockDisposable();

        registry.register(asset);
        assertEquals(1, registry.getManagedAssetCount());

        registry.unregister(asset);
        assertEquals(0, registry.getManagedAssetCount());

        eventBus.publish(new ApplicationDisposeEvent());
        assertFalse(asset.isDisposed());
    }
}

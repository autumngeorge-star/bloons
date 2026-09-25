package com.hongbao.bloons;

import com.badlogic.gdx.utils.Disposable;
import com.hongbao.bloons.helpers.DisposableRegistry;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

public class BloonsTouhouDefenseTest {

    static class MockDisposable implements Disposable {
        final AtomicBoolean disposed = new AtomicBoolean(false);

        @Override
        public void dispose() {
            disposed.set(true);
        }
    }

    @Test
    public void testBloonsTouhouDefenseDisposableRegistryIntegration() {
        BloonsTouhouDefense defense = new BloonsTouhouDefense();
        // Before create(), getDisposableRegistry() is null
        Assert.assertNull(defense.getDisposableRegistry());
    }

    @Test
    public void testMapDisposableRegistryInstantiation() {
        DisposableRegistry registry = new DisposableRegistry();
        MockDisposable skin = registry.register(new MockDisposable());
        MockDisposable texture = registry.register(new MockDisposable());

        Assert.assertEquals(2, registry.size());
        Assert.assertFalse(registry.isDisposed());

        registry.dispose();

        Assert.assertTrue(registry.isDisposed());
        Assert.assertTrue(skin.disposed.get());
        Assert.assertTrue(texture.disposed.get());
        Assert.assertEquals(0, registry.size());
    }
}

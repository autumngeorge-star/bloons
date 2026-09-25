package com.hongbao.bloons.helpers;

import com.badlogic.gdx.utils.Disposable;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class DisposableRegistryTest {

    static class MockDisposable implements Disposable {
        final AtomicInteger disposeCount = new AtomicInteger(0);
        final boolean throwOnDispose;

        MockDisposable() {
            this(false);
        }

        MockDisposable(boolean throwOnDispose) {
            this.throwOnDispose = throwOnDispose;
        }

        @Override
        public void dispose() {
            disposeCount.incrementAndGet();
            if (throwOnDispose) {
                throw new RuntimeException("Simulated GPU native disposal error");
            }
        }
    }

    @Test
    public void testRegisterAndDispose() {
        DisposableRegistry registry = new DisposableRegistry();
        Assert.assertEquals(0, registry.size());
        Assert.assertFalse(registry.isDisposed());

        MockDisposable d1 = registry.register(new MockDisposable());
        MockDisposable d2 = registry.register(new MockDisposable());

        Assert.assertNotNull(d1);
        Assert.assertNotNull(d2);
        Assert.assertEquals(2, registry.size());
        Assert.assertEquals(0, d1.disposeCount.get());
        Assert.assertEquals(0, d2.disposeCount.get());

        registry.dispose();

        Assert.assertTrue(registry.isDisposed());
        Assert.assertEquals(0, registry.size());
        Assert.assertEquals(1, d1.disposeCount.get());
        Assert.assertEquals(1, d2.disposeCount.get());
    }

    @Test
    public void testDuplicateRegistrationPrevented() {
        DisposableRegistry registry = new DisposableRegistry();
        MockDisposable d1 = new MockDisposable();

        registry.register(d1);
        registry.register(d1);

        Assert.assertEquals(1, registry.size());

        registry.dispose();
        Assert.assertEquals(1, d1.disposeCount.get());
    }

    @Test
    public void testUnregister() {
        DisposableRegistry registry = new DisposableRegistry();
        MockDisposable d1 = registry.register(new MockDisposable());
        MockDisposable d2 = registry.register(new MockDisposable());

        Assert.assertEquals(2, registry.size());

        boolean removed = registry.unregister(d1);
        Assert.assertTrue(removed);
        Assert.assertEquals(1, registry.size());

        registry.dispose();

        Assert.assertEquals(0, d1.disposeCount.get());
        Assert.assertEquals(1, d2.disposeCount.get());
    }

    @Test
    public void testDoubleDisposalSafetyOnRegistry() {
        DisposableRegistry registry = new DisposableRegistry();
        MockDisposable d1 = registry.register(new MockDisposable());

        registry.dispose();
        registry.dispose();

        Assert.assertTrue(registry.isDisposed());
        Assert.assertEquals(1, d1.disposeCount.get());
    }

    @Test
    public void testExceptionToleranceDuringTeardown() {
        DisposableRegistry registry = new DisposableRegistry();
        MockDisposable faulty = registry.register(new MockDisposable(true));
        MockDisposable healthy = registry.register(new MockDisposable(false));

        // Should not throw an exception despite faulty throwOnDispose
        registry.dispose();

        Assert.assertEquals(1, faulty.disposeCount.get());
        Assert.assertEquals(1, healthy.disposeCount.get());
        Assert.assertTrue(registry.isDisposed());
    }

    @Test
    public void testRegisterAfterDisposedImmediatelyDisposes() {
        DisposableRegistry registry = new DisposableRegistry();
        registry.dispose();

        MockDisposable d1 = new MockDisposable();
        MockDisposable returned = registry.register(d1);

        Assert.assertSame(d1, returned);
        Assert.assertEquals(1, d1.disposeCount.get());
    }

    @Test
    public void testNullRegistrationHandledGracefully() {
        DisposableRegistry registry = new DisposableRegistry();
        Disposable nullDisposable = registry.register(null);

        Assert.assertNull(nullDisposable);
        Assert.assertEquals(0, registry.size());
    }
}

package com.hongbao.bloons.persistence;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class FailoverPersistenceServiceTest {

    private InMemoryStorageAdapter primaryAdapter;
    private InMemoryStorageAdapter secondaryAdapter;
    private FailoverPersistenceService failoverService;

    @Before
    public void setUp() {
        primaryAdapter = new InMemoryStorageAdapter();
        secondaryAdapter = new InMemoryStorageAdapter();
        failoverService = new FailoverPersistenceService(primaryAdapter, secondaryAdapter);
    }

    @Test
    public void testNormalSaveAndLoadUsesPrimaryAdapter() {
        PlayerData data = new PlayerData(1200, 95, 2);
        failoverService.saveData(data);

        assertEquals(primaryAdapter, failoverService.getActiveAdapter());
        assertEquals(1200, primaryAdapter.loadData().getMoney());
        assertEquals(1200, failoverService.loadData().getMoney());
    }

    @Test
    public void testFailoverOnPrimarySaveException() {
        FailingStorageAdapter failingPrimary = new FailingStorageAdapter();
        FailoverPersistenceService service = new FailoverPersistenceService(failingPrimary, secondaryAdapter);

        PlayerData data = new PlayerData(1800, 70, 4);
        service.saveData(data);

        // Active adapter should now be secondaryAdapter
        assertEquals(secondaryAdapter, service.getActiveAdapter());
        assertEquals(1800, secondaryAdapter.loadData().getMoney());
        assertEquals(1800, service.loadData().getMoney());
    }

    @Test
    public void testFailoverOnPrimaryLoadException() {
        FailingStorageAdapter failingPrimary = new FailingStorageAdapter();
        secondaryAdapter.saveData(new PlayerData(2200, 85, 6));

        FailoverPersistenceService service = new FailoverPersistenceService(failingPrimary, secondaryAdapter);

        PlayerData loaded = service.loadData();
        assertEquals(secondaryAdapter, service.getActiveAdapter());
        assertEquals(2200, loaded.getMoney());
    }

    @Test
    public void testAsyncSaveData() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        PersistenceService trackingAdapter = new InMemoryStorageAdapter() {
            @Override
            public void saveData(PlayerData data) {
                super.saveData(data);
                latch.countDown();
            }
        };

        FailoverPersistenceService service = new FailoverPersistenceService(trackingAdapter, secondaryAdapter);
        service.saveDataAsync(new PlayerData(3000, 100, 12));

        boolean completed = latch.await(2, TimeUnit.SECONDS);
        assertTrue("Async save should complete within timeout", completed);
        assertEquals(3000, service.loadData().getMoney());
    }

    private static class FailingStorageAdapter implements PersistenceService {
        @Override
        public PlayerData loadData() {
            throw new PersistenceException("Simulated load failure");
        }

        @Override
        public void saveData(PlayerData data) {
            throw new PersistenceException("Simulated save failure");
        }

        @Override
        public void saveDataAsync(PlayerData data) {
            throw new PersistenceException("Simulated async save failure");
        }

        @Override
        public void resetData() {
            throw new PersistenceException("Simulated reset failure");
        }

        @Override
        public PersistenceService getActiveAdapter() {
            return this;
        }
    }
}

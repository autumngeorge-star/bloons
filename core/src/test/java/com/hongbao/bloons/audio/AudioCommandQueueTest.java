package com.hongbao.bloons.audio;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AudioCommandQueueTest {

    @Test
    public void testEnqueueAndDrain() {
        AudioCommandQueue queue = new AudioCommandQueue(10, new AudioCommandPool());
        Assert.assertTrue(queue.enqueueSound("music/pop.mp3", 0.5f));
        Assert.assertTrue(queue.enqueueMusicPlay("music/title.mp3", 0.5f, true));
        Assert.assertEquals(2, queue.size());

        List<AudioCommand> list = new ArrayList<>();
        queue.drainTo(list);

        Assert.assertEquals(2, list.size());
        Assert.assertEquals(0, queue.size());
        Assert.assertEquals("music/pop.mp3", list.get(0).getSoundPath());
        Assert.assertEquals("music/title.mp3", list.get(1).getSoundPath());
    }

    @Test
    public void testMaxQueueSizeLimit() {
        int maxCap = 5;
        AudioCommandQueue queue = new AudioCommandQueue(maxCap, new AudioCommandPool());

        for (int i = 0; i < maxCap; i++) {
            boolean enqueued = queue.enqueueSound("music/pop.mp3", 0.5f);
            Assert.assertTrue("Should enqueue within limit", enqueued);
        }

        boolean exceeded = queue.enqueueSound("music/pop.mp3", 0.5f);
        Assert.assertFalse("Should drop command when queue is full", exceeded);
        Assert.assertEquals(maxCap, queue.size());
    }

    @Test
    public void testConcurrentEnqueueThreadSafety() throws InterruptedException {
        int threadCount = 10;
        int enqueuesPerThread = 20;
        AudioCommandQueue queue = new AudioCommandQueue(300, new AudioCommandPool());

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                for (int j = 0; j < enqueuesPerThread; j++) {
                    queue.enqueueSound("music/pop.mp3", 0.5f);
                }
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();

        Assert.assertEquals(threadCount * enqueuesPerThread, queue.size());

        List<AudioCommand> list = new ArrayList<>();
        queue.drainTo(list);
        Assert.assertEquals(threadCount * enqueuesPerThread, list.size());
    }
}

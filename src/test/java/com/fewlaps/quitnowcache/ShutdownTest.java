package com.fewlaps.quitnowcache;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class ShutdownTest extends BaseTest {

    @Test
    public void shutdownClearsTheCache() {
        QNCache cache = new QNCache.Builder().autoReleaseInSeconds(1).build();
        cache.shutdown();
        assertEquals(0, cache.size());
    }

    @Test
    public void shutdownFinishesTheAutoreleaserThread() throws InterruptedException {
        QNCache cache = new QNCache.Builder().autoReleaseInSeconds(1).build();

        int threadsBeforeShutdown = Thread.activeCount();

        cache.shutdown();
        waitUntilThreadDies();

        int threadsAfterShutdown = Thread.activeCount();

        assertTrue(threadsBeforeShutdown > threadsAfterShutdown);
    }
    
    @Test
    public void shutdownDoesntCrashIfAutoreleaserIsOff() {
        QNCache cache = new QNCache.Builder().build();
        cache.shutdown();
    }

    private void waitUntilThreadDies() throws InterruptedException {
        Thread.sleep(1000);
    }
}

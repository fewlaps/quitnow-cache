package com.fewlaps.quitnowcache;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MemoryReleaseTest extends BaseTest {

    @Before
    public void init() {
    }

    @Test
    public void manualReleaseMemoryWorks() throws InterruptedException {
        QNCache<String> cache = new QNCacheBuilder().createQNCache();

        //adding 3 values that will be alive for 1 second, 2 seconds, 3 seconds.
        cache.set("1", A_VALUE, 1000);
        cache.set("2", A_VALUE, 2000);
        cache.set("3", A_VALUE, 3000);

        //checking that forgettingOldValues work
        cache.purge();
        assertEquals(3, cache.sizeDeadAndAliveElements());

        Thread.sleep(1000);
        cache.purge();
        assertEquals(2, cache.sizeDeadAndAliveElements());

        Thread.sleep(1000);
        cache.purge();
        assertEquals(1, cache.sizeDeadAndAliveElements());

        Thread.sleep(1000);
        cache.purge();
        assertEquals(0, cache.sizeDeadAndAliveElements());
    }

    @Test
    public void autoReleaseMemoryWorks() throws InterruptedException {
        QNCache<String> cache = new QNCacheBuilder().setAutoReleaseInSeconds(1).createQNCache();

        //adding 3 values that will be alive for 1 second, 2 seconds, 3 seconds.
        cache.set("1", A_VALUE, 1000);
        cache.set("2", A_VALUE, 2000);
        cache.set("3", A_VALUE, 3000);

        //checking that forgettingOldValues work
        assertEquals(3, cache.sizeDeadAndAliveElements());
        Thread.sleep(5000);
        assertEquals(0, cache.sizeDeadAndAliveElements());
    }
}

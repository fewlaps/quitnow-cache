package com.fewlaps.quitnowcache;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MemoryReleaseTest extends BaseTest {

    @Test
    public void manualReleaseMemoryWorks() {
        try {
            QNCache cache = new QNCacheBuilder().createQNCache();

            int iterations = 3;

            //adding 10 values that will be alive for 1 second, 2 seconds, 3 seconds...
            for (int i = 0; i < iterations; i++) {
                cache.set("" + i, A_VALUE, i * 1000);
            }

            //checking that forgettingOldValues work
            for (int i = iterations; i > 0; i--) {
                cache.removeTooOldValues();
                assertEquals(i, cache.sizeCountingDeadAndAliveElements());
                Thread.sleep(1000);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void autoReleaseMemoryWorks() {
        try {
            QNCache cache = new QNCacheBuilder().setAutoReleaseInSeconds(1).createQNCache();

            int iterations = 3;

            //adding 10 values that will be alive for 1 second, 2 seconds, 3 seconds...
            for (int i = 0; i < iterations; i++) {
                cache.set("" + i, A_VALUE, i * 1000);
            }

            //checking that forgettingOldValues work
            Thread.sleep(500);
            for (int i = iterations; i > 0; i--) {
                assertEquals(i, cache.sizeCountingDeadAndAliveElements());
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package com.fewlaps.quitnowcache;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MemoryReleaseTest extends BaseTest {

    @Test
    public void manualReleaseMemoryWorks() {
        QNCache cache = new QNCacheBuilder().createQNCache();

        //adding 10 values that will be alive for 1 second, 2 seconds, 3 seconds...
        for (int i = 0; i < 5; i++) {
            cache.set("" + i, A_VALUE, i * 1000);
        }

        //checking that forgettingOldValues work
        for (int i = 5; i > 0; i--) {
            cache.removeTooOldValues();
            try {
                Thread.sleep(1000);
                assertEquals(i, cache.sizeCountingDeadAndAliveElements());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void autoReleaseMemoryWorks() {
        QNCache cache = new QNCacheBuilder().setAutoReleaseInSeconds(1).createQNCache();

        //adding 10 values that will be alive for 1 second, 2 seconds, 3 seconds...
        for (int i = 0; i < 5; i++) {
            cache.set("" + i, A_VALUE, i * 1000);
        }

        //checking that forgettingOldValues work
        for (int i = 5; i > 0; i--) {
            try {
                Thread.sleep(1000);
                assertEquals(i, cache.sizeCountingDeadAndAliveElements());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.fewlaps.quitnowcache;

import com.fewlaps.quitnowcache.util.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class ThreadSafeTest extends BaseTest {

    static final int ITERATIONS = 10000;

    QNCache cache;

    @Before
    public void init() {
        cache = new QNCacheBuilder().createQNCache();
    }

    @Test
    public void canEditWhileIteratingWithRandomKeys() {
        final AtomicInteger errors = new AtomicInteger(0);
        final AtomicBoolean writerFinished1 = new AtomicBoolean(false);
        final AtomicBoolean writerFinished2 = new AtomicBoolean(false);
        final AtomicBoolean readerFinished = new AtomicBoolean(false);
        final AtomicBoolean removerFinished = new AtomicBoolean(false);

        Thread writer1 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < ITERATIONS; i++) {
                    try {
                        cache.set(RandomGenerator.generateRandomString(), A_VALUE, 0);
                    } catch (Exception e) {
                        errors.set(errors.get() + 1);
                    }
                }
                writerFinished1.set(true);
            }
        };
        Thread writer2 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < ITERATIONS; i++) {
                    try {
                        cache.set(RandomGenerator.generateRandomString(), ANOTHER_VALUE, 0);
                    } catch (Exception e) {
                        errors.set(errors.get() + 1);
                    }
                }
                writerFinished2.set(true);
            }
        };
        Thread reader = new Thread() {
            @Override
            public void run() {
                while (!writerFinished1.get() || !writerFinished2.get()) {
                    try {
                        cache.get(RandomGenerator.generateRandomString());
                    } catch (Exception e) {
                        errors.set(errors.get() + 1);
                    }
                }
                readerFinished.set(true);
            }
        };
        Thread remover = new Thread() {
            @Override
            public void run() {
                while (!writerFinished1.get() || !writerFinished2.get()) {
                    try {
                        cache.remove(RandomGenerator.generateRandomString());
                    } catch (Exception e) {
                        errors.set(errors.get() + 1);
                    }
                }
                removerFinished.set(true);
            }
        };
        writer1.start();
        writer2.start();
        reader.start();
        remover.start();

        while (!removerFinished.get() || !readerFinished.get() || !writerFinished1.get() || !writerFinished2.get()) {
            try {
                Thread.sleep(100);
                assertEquals(0, errors.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void canEditWhileIteratingWithOneKey() {
        final AtomicInteger errors = new AtomicInteger(0);
        final AtomicBoolean writerFinished1 = new AtomicBoolean(false);
        final AtomicBoolean writerFinished2 = new AtomicBoolean(false);
        final AtomicBoolean readerFinished = new AtomicBoolean(false);
        final AtomicBoolean removerFinished = new AtomicBoolean(false);

        Thread writer1 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < ITERATIONS; i++) {
                    try {
                        cache.set(A_KEY, RandomGenerator.generateRandomString(), 0);
                    } catch (Exception e) {
                        errors.set(errors.get() + 1);
                    }
                }
                writerFinished1.set(true);
            }
        };
        Thread writer2 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < ITERATIONS; i++) {
                    try {
                        cache.set(A_KEY, RandomGenerator.generateRandomString(), 0);
                    } catch (Exception e) {
                        errors.set(errors.get() + 1);
                    }
                }
                writerFinished2.set(true);
            }
        };
        Thread reader = new Thread() {
            @Override
            public void run() {
                while (!writerFinished1.get() || !writerFinished2.get()) {
                    try {
                        cache.get(A_KEY);
                    } catch (Exception e) {
                        errors.set(errors.get() + 1);
                    }
                }
                readerFinished.set(true);
            }
        };
        Thread remover = new Thread() {
            @Override
            public void run() {
                while (!writerFinished1.get() || !writerFinished2.get()) {
                    try {
                        cache.remove(A_KEY);
                    } catch (Exception e) {
                        errors.set(errors.get() + 1);
                    }
                }
                removerFinished.set(true);
            }
        };
        writer1.start();
        writer2.start();
        reader.start();
        remover.start();

        while (!removerFinished.get() || !readerFinished.get() || !writerFinished1.get() || !writerFinished2.get()) {
            try {
                Thread.sleep(100);
                assertEquals(0, errors.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

package com.fewlaps.quitnowcache;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CaseSensitiveKeyTest extends BaseTest {

    @Test
    public void shouldReturnTheSameIfIgnoringCaseSensitive() {
        QNCache<String> cache = new QNCache.Builder().caseSensitiveKeys(false).build();
        cache.set(A_KEY.toLowerCase(), A_VALUE, FOREVER);

        assertEquals(A_VALUE, cache.get(A_KEY.toUpperCase()));
    }

    @Test
    public void shouldReturnNullIfUsingCaseSensitive() {
        QNCache<String> cache = new QNCache.Builder().caseSensitiveKeys(true).build();
        cache.set(A_KEY.toLowerCase(), A_VALUE, FOREVER);

        assertNull(cache.get(A_KEY.toUpperCase()));
    }

    @Test
    public void shouldReturnNullIfUsingDefaultBuilder() {
        QNCache<String> cache = new QNCache.Builder().build();
        cache.set(A_KEY.toLowerCase(), A_VALUE, FOREVER);

        assertNull(cache.get(A_KEY.toUpperCase()));
    }
}

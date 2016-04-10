package com.fewlaps.quitnowcache;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class CaseSensitiveKeyTest extends BaseTest {

    @Test
    public void shouldReturnTheSameIfIgnoringCaseSensitive() {
        QNCache<String> cache = new QNCacheBuilder().setCaseSensitiveKeys(false).createQNCache();
        cache.set(A_KEY.toLowerCase(), A_VALUE, FOREVER);

        assertEquals(A_VALUE, cache.get(A_KEY.toUpperCase()).get());
    }

    @Test
    public void shouldReturnEmptyIfUsingCaseSensitive() {
        QNCache<String> cache = new QNCacheBuilder().setCaseSensitiveKeys(true).createQNCache();
        cache.set(A_KEY.toLowerCase(), A_VALUE, FOREVER);

        assertFalse(cache.get(A_KEY.toUpperCase()).isPresent());
    }

    @Test
    public void shouldReturnEmptyIfUsingDefaultBuilder() {
        QNCache<String> cache = new QNCacheBuilder().createQNCache();
        cache.set(A_KEY.toLowerCase(), A_VALUE, FOREVER);

        assertFalse(cache.get(A_KEY.toUpperCase()).isPresent());
    }
}

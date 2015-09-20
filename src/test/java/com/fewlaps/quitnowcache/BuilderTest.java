package com.fewlaps.quitnowcache;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Roc Boronat on 18/09/2015.
 */
public class BuilderTest {

    @Test
    public void testDefaultBuilder() {
        QNCache cache = new QNCacheBuilder().createQNCache();

        assertTrue(cache.isCaseSensitiveKeys());
        assertNull(cache.getAutoReleaseInSeconds());
    }

    @Test
    public void testSettingTrueCaseSensitiveKeysBuilder() {
        QNCache cache = new QNCacheBuilder().setCaseSensitiveKeys(true).createQNCache();

        assertTrue(cache.isCaseSensitiveKeys());
        assertNull(cache.getAutoReleaseInSeconds());
    }

    @Test
    public void testSettingFalseCaseSensitiveKeysBuilder() {
        QNCache cache = new QNCacheBuilder().setCaseSensitiveKeys(false).createQNCache();

        assertFalse(cache.isCaseSensitiveKeys());
        assertNull(cache.getAutoReleaseInSeconds());
    }

    @Test
    public void testSetting10AutoReleaseSecondsBuilder() {
        QNCache cache = new QNCacheBuilder().setAutoReleaseInSeconds(10).createQNCache();

        assertTrue(cache.isCaseSensitiveKeys());
        assertEquals(Integer.valueOf(10), cache.getAutoReleaseInSeconds());
    }

}

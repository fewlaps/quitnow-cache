package com.fewlaps.quitnowcache;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Roc Boronat on 18/09/2015.
 */
public class BuilderTest {

    @Test
    public void testDefaultBuilder() {
        QNCache cache = new QNCacheBuilder().createQNCache();

        assertTrue(cache.isCaseSensitiveKeys());
    }

    @Test
    public void testSettingTrueCaseSensitiveKeysBuilder() {
        QNCache cache = new QNCacheBuilder().setCaseSensitiveKeys(true).createQNCache();

        assertTrue(cache.isCaseSensitiveKeys());
    }

    @Test
    public void testSettingFalseCaseSensitiveKeysBuilder() {
        QNCache cache = new QNCacheBuilder().setCaseSensitiveKeys(false).createQNCache();

        assertFalse(cache.isCaseSensitiveKeys());
    }
}

package com.fewlaps.quitnowcache;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class BuilderTest {

    @Test
    public void testDefaultBuilder() {
        QNCache<Object> cache = new QNCacheBuilder().createQNCache();

        assertTrue(cache.isCaseSensitiveKeys());
        assertNull(cache.getAutoReleaseInSeconds());
        assertNull(cache.getDefaultKeepaliveInMillis());
    }

    @Test
    public void testSettingTrueCaseSensitiveKeysBuilder() {
        QNCache<Object>  cache = new QNCacheBuilder().setCaseSensitiveKeys(true).createQNCache();

        assertTrue(cache.isCaseSensitiveKeys());
    }

    @Test
    public void testSettingFalseCaseSensitiveKeysBuilder() {
        QNCache<Object>  cache = new QNCacheBuilder().setCaseSensitiveKeys(false).createQNCache();

        assertFalse(cache.isCaseSensitiveKeys());
    }

    @Test
    public void testSettingMinusOneAutoReleaseSecondsBuilder() {
        QNCache<Object>  cache = new QNCacheBuilder().setAutoReleaseInSeconds(-1).createQNCache();

        assertNull(cache.getAutoReleaseInSeconds());
    }

    @Test
    public void testSetting10AutoReleaseSecondsBuilder() {
        QNCache<Object>  cache = new QNCacheBuilder().setAutoReleaseInSeconds(10).createQNCache();

        assertEquals(Integer.valueOf(10), cache.getAutoReleaseInSeconds());
    }

    @Test
    public void testSetting10AutoReleaseSecondsBuilder_usingSecondsTimeUnit() {
        QNCache<Object>  cache = new QNCacheBuilder().setAutoRelease(10, TimeUnit.SECONDS).createQNCache();

        assertEquals(Integer.valueOf(10), cache.getAutoReleaseInSeconds());
    }

    @Test
    public void testSetting10AutoReleaseSecondsBuilder_usingMillisTimeUnit() {
        QNCache<Object>  cache = new QNCacheBuilder().setAutoRelease(10000, TimeUnit.MILLISECONDS).createQNCache();

        assertEquals(Integer.valueOf(10), cache.getAutoReleaseInSeconds());
    }

    @Test
    public void testSetting10AutoReleaseSecondsBuilder_usingMicrosTimeUnit() {
        QNCache<Object>  cache = new QNCacheBuilder().setAutoRelease(10000000, TimeUnit.MICROSECONDS).createQNCache();

        assertEquals(Integer.valueOf(10), cache.getAutoReleaseInSeconds());
    }

    @Test
    public void testSettingDefaultKeepaliveBuilder_with10() {
        QNCache<Object>  cache = new QNCacheBuilder().setDefaultKeepaliveInMillis(10).createQNCache();

        assertEquals(Long.valueOf(10), cache.getDefaultKeepaliveInMillis());
    }

    @Test
    public void testSettingDefaultKeepaliveBuilder_with10000MillisTimeUnits() {
        QNCache<Object>  cache = new QNCacheBuilder().setDefaultKeepalive(10000, TimeUnit.MILLISECONDS).createQNCache();

        assertEquals(Long.valueOf(10000), cache.getDefaultKeepaliveInMillis());
    }

    @Test
    public void testSettingDefaultKeepaliveBuilder_with10SecondsTimeUnits() {
        QNCache<Object>  cache = new QNCacheBuilder().setDefaultKeepalive(10, TimeUnit.SECONDS).createQNCache();

        assertEquals(Long.valueOf(10000), cache.getDefaultKeepaliveInMillis());
    }

    @Test
    public void testSettingDefaultKeepaliveBuilder_withZero() {
        QNCache<Object>  cache = new QNCacheBuilder().setDefaultKeepaliveInMillis(0).createQNCache();

        assertNull(cache.getDefaultKeepaliveInMillis());
    }

    @Test
    public void testSettingDefaultKeepaliveBuilder_withZeroSecondsTimeUnit() {
        QNCache<Object>  cache = new QNCacheBuilder().setDefaultKeepalive(0, TimeUnit.SECONDS).createQNCache();

        assertNull(cache.getDefaultKeepaliveInMillis());
    }

    @Test
    public void testSettingDefaultKeepaliveBuilder_withMinusTen() {
        QNCache<Object>  cache = new QNCacheBuilder().setDefaultKeepaliveInMillis(-10).createQNCache();

        assertNull(cache.getDefaultKeepaliveInMillis());
    }

    @Test
    public void testSettingDefaultKeepaliveBuilder_withMinusTenSecondsTimeUnit() {
        QNCache<Object>  cache = new QNCacheBuilder().setDefaultKeepalive(-10, TimeUnit.SECONDS).createQNCache();

        assertNull(cache.getDefaultKeepaliveInMillis());
    }

    @Test
    public void testQNCacheDefaultKeepaliveIsForever() {
        QNCache<Object>  cache = new QNCacheBuilder().setDefaultKeepaliveInMillis(QNCache.KEEPALIVE_FOREVER).createQNCache();

        assertNull(cache.getDefaultKeepaliveInMillis());
    }
}

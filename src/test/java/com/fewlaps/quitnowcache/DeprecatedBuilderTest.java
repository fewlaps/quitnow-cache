package com.fewlaps.quitnowcache;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.fewlaps.quitnowcache.QNCache.KEEPALIVE_FOREVER;
import static com.fewlaps.quitnowcache.QNCache.WITHOUT_AUTORELEASE;
import static org.junit.Assert.*;

public class DeprecatedBuilderTest {

    @Test
    public void testDefaultBuilder() {
        QNCache<Object> cache = new QNCacheBuilder().createQNCache();

        assertTrue(cache.getCaseSensitiveKeys());
        assertEquals(WITHOUT_AUTORELEASE, cache.getAutoReleaseInSeconds());
        assertEquals(KEEPALIVE_FOREVER, cache.getDefaultKeepaliveInMillis());
    }

    @Test
    public void testSettingTruesetCaseSensitiveKeysBuilder() {
        QNCache<Object> cache = new QNCacheBuilder().setCaseSensitiveKeys(true).createQNCache();

        assertTrue(cache.getCaseSensitiveKeys());
    }

    @Test
    public void testSettingFalsesetCaseSensitiveKeysBuilder() {
        QNCache<Object> cache = new QNCacheBuilder().setCaseSensitiveKeys(false).createQNCache();

        assertFalse(cache.getCaseSensitiveKeys());
    }

    @Test
    public void testSettingMinusOnesetAutoReleaseSecondsBuilder() {
        QNCache<Object> cache = new QNCacheBuilder().setAutoReleaseInSeconds(-1).createQNCache();

        assertEquals(WITHOUT_AUTORELEASE, cache.getAutoReleaseInSeconds());
    }

    @Test
    public void testSetting10setAutoReleaseSecondsBuilder() {
        QNCache<Object> cache = new QNCacheBuilder().setAutoReleaseInSeconds(10).createQNCache();

        assertEquals(10, cache.getAutoReleaseInSeconds());
    }

    @Test
    public void testSetting10setAutoReleaseSecondsBuilder_usingSecondsTimeUnit() {
        QNCache<Object> cache = new QNCacheBuilder().setAutoRelease(10, TimeUnit.SECONDS).createQNCache();

        assertEquals(10, cache.getAutoReleaseInSeconds());
    }

    @Test
    public void testSetting10setAutoReleaseSecondsBuilder_usingMillisTimeUnit() {
        QNCache<Object> cache = new QNCacheBuilder().setAutoRelease(10000, TimeUnit.MILLISECONDS).createQNCache();

        assertEquals(10, cache.getAutoReleaseInSeconds());
    }

    @Test
    public void testSetting10setAutoReleaseSecondsBuilder_usingMicrosTimeUnit() {
        QNCache<Object> cache = new QNCacheBuilder().setAutoRelease(10000000, TimeUnit.MICROSECONDS).createQNCache();

        assertEquals(10, cache.getAutoReleaseInSeconds());
    }

    @Test
    public void testSettingsetDefaultKeepaliveBuilder_with10() {
        QNCache<Object> cache = new QNCacheBuilder().setDefaultKeepaliveInMillis(10).createQNCache();

        assertEquals(10, cache.getDefaultKeepaliveInMillis());
    }

    @Test
    public void testSettingsetDefaultKeepaliveBuilder_with10000MillisTimeUnits() {
        QNCache<Object> cache = new QNCacheBuilder().setDefaultKeepalive(10000, TimeUnit.MILLISECONDS).createQNCache();

        assertEquals(10000, cache.getDefaultKeepaliveInMillis());
    }

    @Test
    public void testSettingsetDefaultKeepaliveBuilder_with10SecondsTimeUnits() {
        QNCache<Object> cache = new QNCacheBuilder().setDefaultKeepalive(10, TimeUnit.SECONDS).createQNCache();

        assertEquals(10000, cache.getDefaultKeepaliveInMillis());
    }

    @Test
    public void testSettingsetDefaultKeepaliveBuilder_withZero() {
        QNCache<Object> cache = new QNCacheBuilder().setDefaultKeepaliveInMillis(0).createQNCache();

        assertEquals(KEEPALIVE_FOREVER, cache.getDefaultKeepaliveInMillis());
    }

    @Test
    public void testSettingsetDefaultKeepaliveBuilder_withZeroSecondsTimeUnit() {
        QNCache<Object> cache = new QNCacheBuilder().setDefaultKeepalive(0, TimeUnit.SECONDS).createQNCache();

        assertEquals(KEEPALIVE_FOREVER, cache.getDefaultKeepaliveInMillis());
    }

    @Test
    public void testSettingsetDefaultKeepaliveBuilder_withMinusTen() {
        QNCache<Object> cache = new QNCacheBuilder().setDefaultKeepaliveInMillis(-10).createQNCache();

        assertEquals(KEEPALIVE_FOREVER, cache.getDefaultKeepaliveInMillis());
    }

    @Test
    public void testSettingsetDefaultKeepaliveBuilder_withMinusTenSecondsTimeUnit() {
        QNCache<Object> cache = new QNCacheBuilder().setDefaultKeepalive(-10, TimeUnit.SECONDS).createQNCache();

        assertEquals(KEEPALIVE_FOREVER, cache.getDefaultKeepaliveInMillis());
    }

    @Test
    public void testQNCachesetDefaultKeepaliveIsForever() {
        QNCache<Object> cache = new QNCacheBuilder().setDefaultKeepaliveInMillis(KEEPALIVE_FOREVER).createQNCache();

        assertEquals(KEEPALIVE_FOREVER, cache.getDefaultKeepaliveInMillis());
    }
}

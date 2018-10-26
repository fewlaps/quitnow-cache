package com.fewlaps.quitnowcache

import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class QNCache<T>(
        val caseSensitiveKeys: Boolean = true,
        val autoReleaseInSeconds: Int = WITHOUT_AUTORELEASE,
        val defaultKeepaliveInMillis: Long = KEEPALIVE_FOREVER) {

    private val cache: ConcurrentHashMap<String, QNCacheBean<T>> = ConcurrentHashMap()

    private var executorService: ScheduledExecutorService? = null

    var dateProvider = DateProvider.SYSTEM!!

    init {
        startAutoReleaseServiceIfNeeded()
    }

    /**
     * The common isEmpty() method, but only looking for alive elements
     */
    fun isEmpty() = sizeAliveElements() == 0

    fun shutdown() {
        clear()
        executorService?.shutdown()
    }

    private fun startAutoReleaseServiceIfNeeded() {
        if (autoReleaseInSeconds > 0) {
            executorService = Executors.newSingleThreadScheduledExecutor()
            executorService?.scheduleAtFixedRate({ purge() }, autoReleaseInSeconds.toLong(), autoReleaseInSeconds.toLong(), TimeUnit.SECONDS)
        }
    }

    operator fun set(key: String, value: T) {
        set(key, value, defaultKeepaliveInMillis)
    }

    operator fun set(key: String, value: T, keepAliveUnits: Long, timeUnit: TimeUnit) {
        set(key, value, timeUnit.toMillis(keepAliveUnits))
    }

    operator fun set(key: String, value: T, keepAliveInMillis: Long) {
        if (keepAliveInMillis >= 0) {
            cache[getEffectiveKey(key)] = QNCacheBean(value, now(), keepAliveInMillis)
        }
    }

    /**
     * Gets an element from the cache. If it's null, the default value will be returned instead.
     */
    fun getOrDefault(key: String, defaultValue: T): T = get(key) ?: defaultValue

    /**
     * Gets an element from the cache.
     */
    operator fun get(key: String) = get(key, purgeIfDead = false)

    /**
     * Gets an element from the cache. If the element exists but is dead,
     * it will be removed from the cache to free memory. It could call
     * an internal synchronized method, so avoid calling this method if
     * you are not storing huge objects in terms of memory.
     */
    fun getAndPurgeIfDead(key: String) = get(key, purgeIfDead = true)

    private fun get(key: String, purgeIfDead: Boolean): T? {
        val effectiveKey = getEffectiveKey(key)
        val retrievedValue = cache[effectiveKey]

        return when {
            retrievedValue == null -> null
            retrievedValue.isAlive(now()) -> retrievedValue.value
            else -> {
                if (purgeIfDead) {
                    cache.remove(effectiveKey)
                }
                null
            }
        }
    }

    fun remove(key: String) {
        val effectiveKey = getEffectiveKey(key)
        cache.remove(effectiveKey)
    }

    /**
     * Removes all the elements of the cache, ignoring if they're dead or alive
     */
    fun clear() = cache.clear()

    fun keySet() = keySetAlive()

    fun keySetDeadAndAlive(): List<String> = cache.keys().toList()

    fun keySetAlive(): List<String> {
        return keySetDeadAndAlive().filter {
            isKeyAlive(it)
        }
    }

    fun keySetDead(): List<String> {
        return keySetDeadAndAlive().filter {
            isKeyDead(it)
        }
    }

    fun keySetStartingWith(start: String?): List<String> {
        if (start == null) return Collections.emptyList()

        val effectiveKeyStartingWith = getEffectiveKey(start)

        return keySetDeadAndAlive().filter {
            it.startsWith(effectiveKeyStartingWith)
        }
    }

    fun keySetAliveStartingWith(start: String?): List<String> {
        if (start == null) return Collections.emptyList()

        return keySetStartingWith(start).filter {
            isKeyAlive(it)
        }
    }

    fun isKeyAlive(key: String): Boolean {
        val value = cache[key] ?: return false
        return value.isAlive(now())
    }

    fun isKeyDead(key: String) = !isKeyAlive(key)

    /**
     * Counts how much alive elements are living in the cache
     */
    fun size() = sizeAliveElements()

    /**
     * Counts how much alive elements are living in the cache
     */
    fun sizeAliveElements(): Int {
        return cache.values.filter { it.isAlive(now()) }.count()
    }

    /**
     * Counts how much dead elements exist in the cache
     */
    fun sizeDeadElements() = cache.size - sizeAliveElements()

    /**
     * Counts how much elements are living in the cache, ignoring if they are dead or alive
     */
    fun sizeDeadAndAliveElements() = cache.size

    /**
     * The common contains() method that looks for alive elements
     */
    operator fun contains(key: String) = get(getEffectiveKey(key)) != null

    /**
     * Removes the dead elements of the cache to free memory
     */
    fun purge() = cache.entries.removeIf { isKeyDead(it.key) }

    /**
     * If caseSensitiveKeys is false, it returns a key in lowercase. It will be
     * the key of all stored values, so the cache will be totally caseinsensitive
     */
    private fun getEffectiveKey(key: String): String {
        return when {
            caseSensitiveKeys -> key
            else -> key.toLowerCase()
        }
    }

    private fun now(): Long {
        return dateProvider.now()
    }

    class Builder {
        private var caseSensitiveKeys = true
        private var autoReleaseInSeconds: Int = QNCache.WITHOUT_AUTORELEASE
        private var defaultKeepaliveInMillis = QNCache.KEEPALIVE_FOREVER

        fun caseSensitiveKeys(caseSensitiveKeys: Boolean): Builder {
            this.caseSensitiveKeys = caseSensitiveKeys
            return this
        }

        fun autoRelease(units: Int, timeUnit: TimeUnit): Builder {
            this.autoReleaseInSeconds = java.lang.Long.valueOf(timeUnit.toSeconds(units.toLong())).toInt()
            return this
        }

        fun autoReleaseInSeconds(autoReleaseInSeconds: Int): Builder {
            this.autoReleaseInSeconds = autoReleaseInSeconds
            return this
        }

        fun defaultKeepalive(units: Int, timeUnit: TimeUnit): Builder {
            this.defaultKeepaliveInMillis = timeUnit.toMillis(units.toLong())
            return this
        }

        fun defaultKeepaliveInMillis(defaultKeepaliveInMillis: Long): Builder {
            this.defaultKeepaliveInMillis = defaultKeepaliveInMillis
            return this
        }

        fun <T> build(): QNCache<T> {
            if (autoReleaseInSeconds < 0) {
                this.autoReleaseInSeconds = WITHOUT_AUTORELEASE
            }
            if (defaultKeepaliveInMillis < 0) {
                this.defaultKeepaliveInMillis = KEEPALIVE_FOREVER
            }
            return QNCache(caseSensitiveKeys, autoReleaseInSeconds, defaultKeepaliveInMillis)
        }
    }

    companion object {
        const val WITHOUT_AUTORELEASE: Int = 0
        const val KEEPALIVE_FOREVER: Long = 0
    }
}
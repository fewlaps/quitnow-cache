package com.fewlaps.quitnowcache;

import org.junit.Test;

public class IntroducingQNCacheTest {

    @SuppressWarnings("UnusedAssignment")
    @Test
    public void theCodeOfTheReadmeWorks() {
        //##The sample

        QNCache cache = new QNCacheBuilder().createQNCache();

        cache.set("key", "value", 60 * 1000); // It can store things for a minute,
        cache.set("key", "value", 60 * 60 * 1000); // for an hour,
        cache.set("key", "value", 0); // or forever.
        cache.set("key", "value"); // And also for the short version of forever.

        cache.get("key"); // It can get them again,
        cache.remove("key"); // and remove it if you want.

        cache.get("unExistingKey"); // If something doesn't exists, it returns null
        cache.get("tooOldKey"); // The same if a key is too old

        cache.clear(); // You can also clean it,
        cache.size(); // and ask it how many elements it has

        QNCache<String> stringCache = new QNCacheBuilder().createQNCache(); //You can also make it typesafe
        //stringCache.set("key", 42); //so this will not compile :)

        //##Let's talk about the memory
        cache = new QNCacheBuilder().setAutoReleaseInSeconds(1).createQNCache(); //frees the memory every second
        cache = new QNCacheBuilder().setAutoReleaseInSeconds(60).createQNCache(); //frees the memory every minute
        cache = new QNCacheBuilder().setAutoReleaseInSeconds(60*60).createQNCache(); //frees the memory every hour
        cache = new QNCacheBuilder().createQNCache(); //never frees the memory

        //##Are the keys case sensitive?
        cache = new QNCacheBuilder().setCaseSensitiveKeys(true).createQNCache(); //"key" and "KEY" will be different items
        cache = new QNCacheBuilder().setCaseSensitiveKeys(false).createQNCache(); //"key" and "KEY" will be the same
        cache = new QNCacheBuilder().createQNCache(); //"key" and "KEY" will be different items

        //##It's possible to change the default keepalive?
        cache = new QNCacheBuilder().setDefaultKeepaliveInMillis(1000).createQNCache(); //a keepalive of one second
        cache = new QNCacheBuilder().setDefaultKeepaliveInMillis(1000 * 60).createQNCache(); //a keepalive of one minute
        cache = new QNCacheBuilder().createQNCache(); //the default keepalive: remember it forever!
    }
}

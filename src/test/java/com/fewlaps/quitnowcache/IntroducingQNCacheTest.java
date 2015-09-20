package com.fewlaps.quitnowcache;

import org.junit.Test;

import java.util.ArrayList;

public class IntroducingQNCacheTest {

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

        cache.set("AnInteger", new Integer(42)); // You can save all kind of Objects...
        cache.set("ACollection", new ArrayList()); // ...whatever you want

        cache.removeAll(); // You can also clean it,
        cache.size(); // and ask it how many elements it has

        //##Let's talk about the memory
        cache = new QNCacheBuilder().setAutoReleaseInSeconds(1).createQNCache(); //frees the memory every second
        cache = new QNCacheBuilder().setAutoReleaseInSeconds(60).createQNCache(); //frees the memory every minute
        cache = new QNCacheBuilder().setAutoReleaseInSeconds(60*60).createQNCache(); //frees the memory every hour
        cache = new QNCacheBuilder().createQNCache(); //never frees the memory

        //##Are the keys case sensitive?
        cache = new QNCacheBuilder().setCaseSensitiveKeys(true).createQNCache(); //"key" and "KEY" will be different items
        cache = new QNCacheBuilder().setCaseSensitiveKeys(false).createQNCache(); //"key" and "KEY" will be the same
        cache = new QNCacheBuilder().createQNCache(); //"key" and "KEY" will be different items
    }
}

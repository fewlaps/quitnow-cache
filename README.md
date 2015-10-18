[![Build Status](https://travis-ci.org/Fewlaps/quitnow-cache.svg?branch=master)](https://travis-ci.org/Fewlaps/quitnow-cache)
[![Coverage Status](https://coveralls.io/repos/Fewlaps/quitnow-cache/badge.svg?branch=master&service=github)](https://coveralls.io/github/Fewlaps/quitnow-cache?branch=master)
[![Download](https://api.bintray.com/packages/fewlaps/maven/quitnow-cache/images/download.svg) ](https://bintray.com/fewlaps/maven/quitnow-cache/_latestVersion)
[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/Fewlaps/quitnow-cache?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

# [QuitNow!](http://quitnowapp.com)'s cache
A memcached-like Java cache, focused on portability, great for Android.

Before this library, QuitNow! and lots of apps we've seen are caching things until the system kills the app. That's not cool. In the other hand, developing a cache for that little improvement is not trivial. So, we decided to return the work to the open source community by writing this really simple cache, allowing developers to keep information only for a limited time.

And we've done it using TDD, so it's totally tested. [Check the tests!](https://github.com/Fewlaps/quitnow-cache/tree/master/src/test/java/com/fewlaps/quitnowcache) :·)

The sample
----------

```java
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
```

Let's talk about the memory
---------------------------
By default, the cache stores a reference to all stored instances, doesn't matter if they're alive or they are too old. If you plan to store huge datasets, you can create it with an auto releaser. Then the cache will remove the old elements every 60 seconds, for example.

```java
QNCache cache = new QNCacheBuilder().setAutoReleaseInSeconds(1).createQNCache(); //frees the memory every second
QNCache cache = new QNCacheBuilder().setAutoReleaseInSeconds(60).createQNCache(); //frees the memory every minute
QNCache cache = new QNCacheBuilder().setAutoReleaseInSeconds(60*60).createQNCache(); //frees the memory every hour
QNCache cache = new QNCacheBuilder().createQNCache(); //never frees the memory
```

Are the keys case sensitive?
---------------------------
By default, yes. But you can also specify it at building time.

```java
QNCache cache = new QNCacheBuilder().setCaseSensitiveKeys(true).createQNCache(); //"key" and "KEY" will be different items
QNCache cache = new QNCacheBuilder().setCaseSensitiveKeys(false).createQNCache(); //"key" and "KEY" will be the same
QNCache cache = new QNCacheBuilder().createQNCache(); //"key" and "KEY" will be different items
```

#Download

* Get <a href="https://github.com/Fewlaps/quitnow-cache/releases/download/v1.3/quitnow-cache-1.3.jar">the last .jar</a> 

* Grab via Gradle:
```groovy
repositories { jcenter() }
    
compile 'com.fewlaps.quitnowcache:quitnow-cache:1.3'
```
* Grab via Maven:
```xml
<repository>
  <id>jcenter</id>
  <url>http://jcenter.bintray.com</url>
</repository>

<dependency>
    <groupId>com.fewlaps.quitnowcache</groupId>
    <artifactId>quitnow-cache</artifactId>
    <version>1.3</version>
</dependency>
```


## LICENSE ##

The MIT License (MIT)

Copyright (c) 2015 Fewlaps

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

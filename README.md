[![Build Status](https://travis-ci.org/Fewlaps/quitnow-cache.svg?branch=master)](https://travis-ci.org/Fewlaps/quitnow-cache)
[![Coverage Status](https://coveralls.io/repos/Fewlaps/quitnow-cache/badge.svg?branch=master&service=github)](https://coveralls.io/github/Fewlaps/quitnow-cache?branch=master)
[![Download](https://api.bintray.com/packages/fewlaps/maven/quitnow-cache/images/download.svg) ](https://bintray.com/fewlaps/maven/quitnow-cache/_latestVersion)

# [QuitNow!](http://quitnowapp.com)'s cache
A memcached-like Java cache, focused on portability, great for Android.

Before this library, QuitNow! app (and lots of apps we've seen) are caching things until the user or the system kills the app. And we know that this isn't not cool, but developing a cache for that little improvement is not a trivial task. So, we decided to return the work to the open source community by writing this really simple cache, allowing developers to keep information only for a limited time.

And we've done it using TDD, so it's totally tested. [Check the tests!](https://github.com/Fewlaps/quitnow-cache/tree/master/src/test/java/com/fewlaps/quitnowcache) :·)

Using it
--------

```java
QNCache cache; // Let me introduce you: The simplest memcached-like cache!

//It has a fancy builder with fancy default options.
cache = new QNCacheBuilder().createQNCache();

cache.set("Key", "Value"); //you can save things in the cache,
cache.get("Key"); //get it again,
cache.remove("Key"); //and remove it if you want.

//By the way, you can save only it for a second, as you do with memcached.
cache.set("Key", "Value", 1000); //save it for a second
cache.get("Key"); //it will work as usual,
Thread.sleep(1000); //but after a second,
cache.get("Key"); //it will become null.

//In addition, you can save all kinds of Objects:
cache.set("API response", new ApiResponse, 60*1000); //This is the most interesting use of the library! :·)

//What about the memory? By default, all the instances will be stored unless you remove it.
//But there's a way to remove all the instances that are too old: the autorelease parameter.
//You can create an autoreleased QNCache using its builder.
//For example, cleaning it every minute:
cache = new QNCacheBuilder().setAutoReleaseInSeconds(60).createQNCache();

//And, what about the keys? Are they case sensitive?
//By default, yes, as it is the common Java behaviour.
//But you can also define it at building time:
cache = new QNCacheBuilder().setCaseSensitiveKeys(false).createQNCache();

//To end this beautiful writing, let's detail the builder default values:
cache = new QNCacheBuilder()
    .setAutoReleaseInSeconds(60) //default null: it means autorelease disabled
    .setCaseSensitiveKeys(false) //default true
    .createQNCache();
```


Download
--------

* Get <a href="https://github.com/Fewlaps/quitnow-cache/releases/download/v1.0/quitnow-cache-1.0.jar">the last .jar</a> 

* Grab via Gradle:
```groovy
repositories { jcenter() }
    
compile 'com.fewlaps.quitnowcache:quitnow-cache:1.0'
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
    <version>1.0</version>
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

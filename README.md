# StickyHeader
It was done mainly because almost all popular libraries for sticky headers are too complex or offer a lot more features than needed. Some of them also breaks DiffUtils. For performance reason a header view is created only once. View types and click listeners are not supported.
However, you can customize this view via supplied adapter when a sticky header replaces the previous one.

<img src="/images/small_demo.gif" alt="Sample" width="300px" />

Usage
-----

1. Add jcenter() to repositories block in your gradle file.
2. Add `implementation 'com.shuhart.stickyheader:stickyheader:1.0'` to your dependencies.
3. Look into the sample for additional details on how to use and configure the library.

You should provide an adapter that extends [StickyAdapter](https://github.com/shuhart/StickyHeader/blob/master/stickyheader/src/main/java/com/shuhart/stickyheader/StickyAdapter.java) to the StickyHeaderItemDecorator that is used to create and bind sticky headers.


After that just attach it to the RecyclerView:

```java
StickyHeaderItemDecorator decorator = new StickyHeaderItemDecorator(adapter);
decorator.attachToRecyclerView(recyclerView);
```

How it works
-----
A ViewHolder is created by the adapter to reuse and bind every header.
A header view is drawn on top of the RecyclerView using onDrawOver() callback of ItemDecoration.

License
=======

    Copyright 2018 Bogdan Kornev.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

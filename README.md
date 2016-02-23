Android-ExpandableTextView
==========================
An expandable TextView for Android applications (4.0+).

Add dependency
--------------
This library is not yet released in Maven Central, but instead you can use [Bintray](https://www.bintray.com)

```groovy
	repositories {
	    maven {
	        url "https://dl.bintray.com/cliffus/maven"
	    }
	}
```

library dependency

```groovy
	dependencies {
	    compile ('at.blogc.android:android-expandabletextview:1.0.0@aar')
	}
```

Usage
-----

```xml
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <at.blogc.android.views.ExpandableTextView
        android:id="@+id/expandableTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/lorem_ipsum"
        android:maxLines="5"
        android:ellipsize="end"
        app:animation_duration="1000"/>

	<!-- Optional parameter animation_duration -->

</LinearLayout>
```

License
=======

    Copyright 2016 Cliff Ophalvens (Blogc.at)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

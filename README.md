Android-ExpandableTextView
==========================
An expandable TextView for Android applications (4.0+).

[ ![Download](https://api.bintray.com/packages/blogcat/maven/android-expandabletextview/images/download.svg) ](https://bintray.com/blogcat/maven/android-expandabletextview/_latestVersion)

Demo
----
This repository also contains a demo project.

![Demo](https://raw.githubusercontent.com/Blogcat/Android-ExpandableTextView/release/1.0.2/demo.gif)

Add dependency
--------------
This library is released in Maven Central and jCenter:

```groovy
	repositories {
	    mavenCentral()
	}
```

or

```groovy
	repositories {
	    jcenter()
	}
```

library dependency

```groovy
	dependencies {
	    compile ('at.blogc:expandabletextview:1.0.2@aar')
	}
```

Usage
-----
Using the ExpandableTextView is very easy, it's just a regular TextView with some extra functionality added to it. By defining the android:maxLines attribute, you can set the default number of lines for the TextView collapsed state. 

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

	<!-- Optional parameter animation_duration: sets the duration of the expand animation -->

    <Button
        android:id="@+id/button_toggle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/expand"/>

</LinearLayout>
```

In your Activity or Fragment:

```java
final ExpandableTextView expandableTextView = (ExpandableTextView) this.findViewById(R.id.expandableTextView);
final Button buttonToggle = (Button) this.findViewById(R.id.button_toggle);

// set animation duration via code, but preferable in your layout files by using the animation_duration attribute
expandableTextView.setAnimationDuration(1000L);

 // set interpolators for both expanding and collapsing animations
expandableTextView.setInterpolator(new OvershootInterpolator());

// or set them separately
expandableTextView.setExpandInterpolator(new OvershootInterpolator());
expandableTextView.setCollapseInterpolator(new OvershootInterpolator());

// toggle the ExpandableTextView
buttonToggle.setOnClickListener(new View.OnClickListener()
{
    @Override
    public void onClick(final View v)
    {
        expandableTextView.toggle();
        buttonToggle.setText(expandableTextView.isExpanded() ? R.string.collapse : R.string.expand);
    }
});

// but, you can also do the checks yourself
buttonToggle.setOnClickListener(new View.OnClickListener()
{
    @Override
    public void onClick(final View v)
    {
        if (expandableTextView.isExpanded())
        {
            expandableTextView.collapse();
            buttonToggle.setText(R.string.expand);
        }
        else
        {
            expandableTextView.expand();
            buttonToggle.setText(R.string.collapse);
        }
    }
});

// listen for expand / collapse events
expandableTextView.setOnExpandListener(new ExpandableTextView.OnExpandListener()
{
    @Override
    public void onExpand(final ExpandableTextView view)
    {
        Log.d(TAG, "ExpandableTextView expanded");
    }

    @Override
    public void onCollapse(final ExpandableTextView view)
    {
        Log.d(TAG, "ExpandableTextView collapsed");
    }
});
```

Roadmap
=======

* add method to know if the TextView is expandable or not 
* optional fading edge at the bottom of the TextView
* update demo project with more examples

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

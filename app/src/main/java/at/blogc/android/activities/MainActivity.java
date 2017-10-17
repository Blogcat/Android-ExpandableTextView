package at.blogc.android.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;

import at.blogc.android.views.ExpandableTextView;
import at.blogc.android.views.R;

/**
 * Copyright (C) 2016 Cliff Ophalvens (Blogc.at)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "ExpandableTextView";

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        final ExpandableTextView expandableTextView = this.findViewById(R.id.expandableTextView);
        final Button buttonToggle = this.findViewById(R.id.button_toggle);

        // set animation duration via code, but preferable in your layout files by using the animation_duration attribute
        expandableTextView.setAnimationDuration(750L);

        // set interpolators for both expanding and collapsing animations
        expandableTextView.setInterpolator(new OvershootInterpolator());

        // or set them separately
        expandableTextView.setExpandInterpolator(new OvershootInterpolator());
        expandableTextView.setCollapseInterpolator(new OvershootInterpolator());

        // toggle the ExpandableTextView
        buttonToggle.setOnClickListener(new View.OnClickListener()
        {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(final View v)
            {
                buttonToggle.setText(expandableTextView.isExpanded() ? R.string.expand : R.string.collapse);
                expandableTextView.toggle();
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
        expandableTextView.addOnExpandListener(new ExpandableTextView.OnExpandListener()
        {
            @Override
            public void onExpand(@NonNull final ExpandableTextView view)
            {
                Log.d(TAG, "ExpandableTextView expanded");
            }

            @Override
            public void onCollapse(@NonNull final ExpandableTextView view)
            {
                Log.d(TAG, "ExpandableTextView collapsed");
            }
        });
    }
}

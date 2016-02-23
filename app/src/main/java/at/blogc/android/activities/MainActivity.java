package at.blogc.android.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        final ExpandableTextView expandableTextView = (ExpandableTextView) this.findViewById(R.id.expandableTextView);
        expandableTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                expandableTextView.toggle();
            }
        });
    }
}

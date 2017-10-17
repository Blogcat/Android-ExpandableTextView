package at.blogc.android.adapters.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;

import at.blogc.android.views.ExpandableTextView;
import at.blogc.android.views.R;

/**
 * Copyright (C) 2017 Cliff Ophalvens (Blogc.at)
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
public class ExpandableTextViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener
{
    private final ExpandableTextView expandableTextView;
    private final Button buttonToggle;

    public ExpandableTextViewHolder(final View itemView)
    {
        super(itemView);

        // find views
        this.expandableTextView = itemView.findViewById(R.id.expandableTextView);
        this.buttonToggle = itemView.findViewById(R.id.button_toggle);

        // set interpolators for both expanding and collapsing animations
        this.expandableTextView.setInterpolator(new OvershootInterpolator());

        // toggle the ExpandableTextView
        this.buttonToggle.setOnClickListener(this);
    }

    public void bindPosition(final int position)
    {
        final String loremIpsum = position + ": " + this.itemView.getContext().getString(R.string.lorem_ipsum);
        this.expandableTextView.setText(loremIpsum);
    }

    //region View.OnClickListener interface

    @Override
    public void onClick(final View v)
    {
        this.buttonToggle.setText(this.expandableTextView.isExpanded() ? R.string.expand : R.string.collapse);
        this.expandableTextView.toggle();
    }

    //endregion
}

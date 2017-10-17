package at.blogc.android.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.blogc.android.adapters.viewholders.ExpandableTextViewHolder;
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
public class RecyclerViewAdapter extends RecyclerView.Adapter<ExpandableTextViewHolder>
{
    private LayoutInflater layoutInflater;

    private LayoutInflater getLayoutInflater(@NonNull final Context context)
    {
        if (this.layoutInflater == null)
        {
            this.layoutInflater = LayoutInflater.from(context);
        }

        return this.layoutInflater;
    }

    @Override
    public ExpandableTextViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType)
    {
        final View itemView = this.getLayoutInflater(parent.getContext()).inflate(R.layout.listitem_recyclerview, parent, false);

        return new ExpandableTextViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ExpandableTextViewHolder holder, final int position)
    {
        holder.bindPosition(position);
    }

    @Override
    public int getItemCount()
    {
        return 100;
    }
}

package at.blogc.android.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import at.blogc.expandabletextview.BuildConfig;
import at.blogc.expandabletextview.R;

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
 *
 * @author Cliff Ophalvens (Blogc.at)
 */
public class ExpandableTextView extends TextView
{
    private final List<OnExpandListener> onExpandListeners;
    private TimeInterpolator expandInterpolator;
    private TimeInterpolator collapseInterpolator;

    private final int maxLines;
    private long animationDuration;
    private boolean animating;
    private boolean expanded;
    private int collapsedHeight;

    public ExpandableTextView(final Context context)
    {
        this(context, null);
    }

    public ExpandableTextView(final Context context, @Nullable final AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ExpandableTextView(final Context context, @Nullable final AttributeSet attrs, final int defStyle)
    {
        super(context, attrs, defStyle);

        // read attributes
        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView, defStyle, 0);
        this.animationDuration = attributes.getInt(R.styleable.ExpandableTextView_animation_duration, BuildConfig.DEFAULT_ANIMATION_DURATION);
        attributes.recycle();

        // keep the original value of maxLines
        this.maxLines = this.getMaxLines();

        // create bucket of OnExpandListener instances
        this.onExpandListeners = new ArrayList<>();

        // create default interpolators
        this.expandInterpolator = new AccelerateDecelerateInterpolator();
        this.collapseInterpolator = new AccelerateDecelerateInterpolator();
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, int heightMeasureSpec)
    {
        // if this TextView is collapsed and maxLines = 0,
        // than make its height equals to zero
        if (this.maxLines == 0 && !this.expanded && !this.animating)
        {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //region public helper methods

    /**
     * Toggle the expanded state of this {@link ExpandableTextView}.
     * @return true if toggled, false otherwise.
     */
    public boolean toggle()
    {
        return this.expanded
            ? this.collapse()
            : this.expand();
    }

    /**
     * Expand this {@link ExpandableTextView}.
     * @return true if expanded, false otherwise.
     */
    public boolean expand()
    {
        if (!this.expanded && !this.animating && this.maxLines >= 0)
        {
            // notify listener
            this.notifyOnStartExpand();

            // measure collapsed height
            this.measure
            (
                MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            );

            this.collapsedHeight = this.getMeasuredHeight();

            // indicate that we are now animating
            this.animating = true;

            // set maxLines to MAX Integer, so we can calculate the expanded height
            this.setMaxLines(Integer.MAX_VALUE);

            // measure expanded height
            this.measure
            (
                MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            );

            final int expandedHeight = this.getMeasuredHeight();

            // animate from collapsed height to expanded height
            final ValueAnimator valueAnimator = ValueAnimator.ofInt(this.collapsedHeight, expandedHeight);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                @Override
                public void onAnimationUpdate(final ValueAnimator animation)
                {
                    ExpandableTextView.this.setHeight((int) animation.getAnimatedValue());
                }
            });

            // wait for the animation to end
            valueAnimator.addListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(final Animator animation)
                {
                    // reset min & max height (previously set with setHeight() method)
                    ExpandableTextView.this.setMaxHeight(Integer.MAX_VALUE);
                    ExpandableTextView.this.setMinHeight(0);

                    // if fully expanded, set height to WRAP_CONTENT, because when rotating the device
                    // the height calculated with this ValueAnimator isn't correct anymore
                    final ViewGroup.LayoutParams layoutParams = ExpandableTextView.this.getLayoutParams();
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    ExpandableTextView.this.setLayoutParams(layoutParams);

                    // keep track of current status
                    ExpandableTextView.this.expanded = true;
                    ExpandableTextView.this.animating = false;
                    notifyOnEndExpand();
                }
            });

            // set interpolator
            valueAnimator.setInterpolator(this.expandInterpolator);

            // start the animation
            valueAnimator
                .setDuration(this.animationDuration)
                .start();

            return true;
        }

        return false;
    }

    /**
     * Collapse this {@link TextView}.
     * @return true if collapsed, false otherwise.
     */
    public boolean collapse()
    {
        if (this.expanded && !this.animating && this.maxLines >= 0)
        {
            // notify listener
            this.notifyOnStartCollapse();

            // measure expanded height
            final int expandedHeight = this.getMeasuredHeight();

            // indicate that we are now animating
            this.animating = true;

            // animate from expanded height to collapsed height
            final ValueAnimator valueAnimator = ValueAnimator.ofInt(expandedHeight, this.collapsedHeight);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                @Override
                public void onAnimationUpdate(final ValueAnimator animation)
                {
                    ExpandableTextView.this.setHeight((int) animation.getAnimatedValue());
                }
            });

            // wait for the animation to end
            valueAnimator.addListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(final Animator animation)
                {
                    // keep track of current status
                    ExpandableTextView.this.expanded = false;
                    ExpandableTextView.this.animating = false;

                    // set maxLines back to original value
                    ExpandableTextView.this.setMaxLines(ExpandableTextView.this.maxLines);

                    // if fully collapsed, set height back to WRAP_CONTENT, because when rotating the device
                    // the height previously calculated with this ValueAnimator isn't correct anymore
                    final ViewGroup.LayoutParams layoutParams = ExpandableTextView.this.getLayoutParams();
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    ExpandableTextView.this.setLayoutParams(layoutParams);
                    notifyOnEndCollapse();
                }
            });

            // set interpolator
            valueAnimator.setInterpolator(this.collapseInterpolator);

            // start the animation
            valueAnimator
                .setDuration(this.animationDuration)
                .start();

            return true;
        }

        return false;
    }

    //endregion

    //region public getters and setters

    /**
     * Sets the duration of the expand / collapse animation.
     * @param animationDuration duration in milliseconds.
     */
    public void setAnimationDuration(final long animationDuration)
    {
        this.animationDuration = animationDuration;
    }

    /**
     * Adds a listener which receives updates about this {@link ExpandableTextView}.
     * @param onExpandListener the listener.
     */
    public void addOnExpandListener(final OnExpandListener onExpandListener)
    {
        this.onExpandListeners.add(onExpandListener);
    }

    /**
     * Removes a listener which receives updates about this {@link ExpandableTextView}.
     * @param onExpandListener the listener.
     */
    public void removeOnExpandListener(final OnExpandListener onExpandListener)
    {
        this.onExpandListeners.remove(onExpandListener);
    }

    /**
     * Sets a {@link TimeInterpolator} for expanding and collapsing.
     * @param interpolator the interpolator
     */
    public void setInterpolator(final TimeInterpolator interpolator)
    {
        this.expandInterpolator = interpolator;
        this.collapseInterpolator = interpolator;
    }

    /**
     * Sets a {@link TimeInterpolator} for expanding.
     * @param expandInterpolator the interpolator
     */
    public void setExpandInterpolator(final TimeInterpolator expandInterpolator)
    {
        this.expandInterpolator = expandInterpolator;
    }

    /**
     * Returns the current {@link TimeInterpolator} for expanding.
     * @return the current interpolator, null by default.
     */
    public TimeInterpolator getExpandInterpolator()
    {
        return this.expandInterpolator;
    }

    /**
     * Sets a {@link TimeInterpolator} for collpasing.
     * @param collapseInterpolator the interpolator
     */
    public void setCollapseInterpolator(final TimeInterpolator collapseInterpolator)
    {
        this.collapseInterpolator = collapseInterpolator;
    }

    /**
     * Returns the current {@link TimeInterpolator} for collapsing.
     * @return the current interpolator, null by default.
     */
    public TimeInterpolator getCollapseInterpolator()
    {
        return this.collapseInterpolator;
    }

    /**
     * Is this {@link ExpandableTextView} expanded or not?
     * @return true if expanded, false if collapsed.
     */
    public boolean isExpanded()
    {
        return this.expanded;
    }

    //endregion

    /**
     * This method will notify the listener about this view starts to collapse.
     */
    private void notifyOnStartCollapse()
    {
        for (final OnExpandListener onExpandListener : this.onExpandListeners)
        {
            onExpandListener.onStartCollapse(this);
        }
    }

    /**
     * This method will notify the listener about this view starts to expand.
     */
    private void notifyOnStartExpand()
    {
        for (final OnExpandListener onExpandListener : this.onExpandListeners)
        {
            onExpandListener.onStartExpand(this);
        }
    }
    /**
     * This method will notify the listener about this view finishes to collapse.
     */
    private void notifyOnEndCollapse()
    {
        for (final OnExpandListener onExpandListener : this.onExpandListeners)
        {
            onExpandListener.onEndCollapse(this);
        }
    }

    /**
     * This method will notify the listener about this view finishes to expand.
     */
    private void notifyOnEndExpand()
    {
        for (final OnExpandListener onExpandListener : this.onExpandListeners)
        {
            onExpandListener.onEndExpand(this);
        }
    }

    //region public interfaces

    /**
     * Interface definition for a callback to be invoked when
     * a {@link ExpandableTextView} is expanded or collapsed.
     */
    public interface OnExpandListener
    {
        /**
         * The {@link ExpandableTextView} starts to expand.
         * @param view the textview
         */
        void onStartExpand(@NonNull ExpandableTextView view);

        /**
         * The {@link ExpandableTextView} starts to collapse.
         * @param view the textview
         */
        void onStartCollapse(@NonNull ExpandableTextView view);

        /**
         * The {@link ExpandableTextView} finishes to expand.
         * @param view the textview
         */
        void onEndExpand(@NonNull ExpandableTextView view);

        /**
         * The {@link ExpandableTextView} finishes to collapse.
         * @param view the textview
         */
        void onEndCollapse(@NonNull ExpandableTextView view);
    }

    /**
     * Simple implementation of the {@link OnExpandListener} interface with stub
     * implementations of each method. Extend this if you do not intend to override
     * every method of {@link OnExpandListener}.
     */
    public static class SimpleOnExpandListener implements OnExpandListener
    {
        @Override
        public void onStartExpand(@NonNull final ExpandableTextView view)
        {
            // empty implementation
        }

        @Override
        public void onStartCollapse(@NonNull final ExpandableTextView view)
        {
            // empty implementation
        }

        @Override
        public void onEndExpand(@NonNull ExpandableTextView view) {
            // empty implementation
        }

        @Override
        public void onEndCollapse(@NonNull ExpandableTextView view) {
            // empty implementation
        }
    }

    //endregion
}

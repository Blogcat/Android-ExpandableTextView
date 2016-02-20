package blogc.at.android.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Field;

import blogc.at.android.animator.AnimatorListener;

/**
 * Created by cliff on 18/02/16.
 */
public class ExpandableTextView extends TextView
{
    private static final int DEFAULT_DURATION = 750;
    private static final int LINES = 1;

    private OnExpandListener onExpandListener;
    private final int duration;
    private boolean expanded;
    private int maxLines;
    private int originalHeight;

    public ExpandableTextView(Context context)
    {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        // read attributes
        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView, defStyle, 0);
        this.duration = attributes.getInt(R.styleable.ExpandableTextView_duration, DEFAULT_DURATION);
        attributes.recycle();

        // keep the original value of maxLines
        this.maxLines = this.getMaxLines();
    }

    @Override
    public int getMaxLines()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            return super.getMaxLines();
        }

        try
        {
            final Field mMaxMode = TextView.class.getField("mMaxMode");
            mMaxMode.setAccessible(true);
            final Field mMaximum = TextView.class.getField("mMaximum");
            mMaximum.setAccessible(true);

            final int mMaxModeValue = (int) mMaxMode.get(this);
            final int mMaximumValue = (int) mMaximum.get(this);

            return mMaxModeValue == LINES ? mMaximumValue : -1;
        }
        catch (final Exception e)
        {
           return -1;
        }
    }

    public boolean toggle()
    {
        if (this.expanded)
        {
            return this.collapse();
        }

        return this.expand();
    }

    public boolean expand()
    {
        if (!this.expanded && this.maxLines >= 0)
        {
            this.expanded = true;

            // notify listener
            if (this.onExpandListener != null)
            {
                this.onExpandListener.onExpand(this);
            }

            // get original height
            this.measure
            (
                MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            );

            this.originalHeight = this.getMeasuredHeight();

            // set maxLines to MAX Integer
            this.setMaxLines(Integer.MAX_VALUE);

            // get new height
            this.measure
            (
                MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            );

            final int fullHeight = this.getMeasuredHeight();

            final ValueAnimator valueAnimator = ValueAnimator.ofInt(this.originalHeight, fullHeight);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                @Override
                public void onAnimationUpdate(final ValueAnimator animation)
                {
                    final ViewGroup.LayoutParams layoutParams = ExpandableTextView.this.getLayoutParams();
                    layoutParams.height = (int) animation.getAnimatedValue();
                    ExpandableTextView.this.setLayoutParams(layoutParams);
                }
            });

            // start the animation
            valueAnimator
                .setDuration(this.duration)
                .start();

            return true;
        }

        return false;
    }

    public boolean collapse()
    {
        if (this.expanded && this.maxLines >= 0)
        {
            this.expanded = false;

            // notify listener
            if (this.onExpandListener != null)
            {
                this.onExpandListener.onCollapse(this);
            }

            // get new height
            final int fullHeight = this.getMeasuredHeight();

            final ValueAnimator valueAnimator = ValueAnimator.ofInt(fullHeight, this.originalHeight);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                @Override
                public void onAnimationUpdate(final ValueAnimator animation)
                {
                    final ViewGroup.LayoutParams layoutParams = ExpandableTextView.this.getLayoutParams();
                    layoutParams.height = (int) animation.getAnimatedValue();
                    ExpandableTextView.this.setLayoutParams(layoutParams);
                }
            });
            valueAnimator.addListener(new AnimatorListener()
            {
                @Override
                public void onAnimationEnd(final Animator animation)
                {
                    // set maxLines to original value
                    ExpandableTextView.this.setMaxLines(ExpandableTextView.this.maxLines);
                }
            });

            // start the animation
            valueAnimator
                .setDuration(this.duration)
                .start();

            return true;
        }

        return false;
    }

    public void setOnExpandListener(final OnExpandListener onExpandListener)
    {
        this.onExpandListener = onExpandListener;
    }

    public OnExpandListener getOnExpandListener()
    {
        return onExpandListener;
    }

    public boolean isExpanded()
    {
        return this.expanded;
    }

    public interface OnExpandListener
    {
        void onExpand(ExpandableTextView view);
        void onCollapse(ExpandableTextView view);
    }
}

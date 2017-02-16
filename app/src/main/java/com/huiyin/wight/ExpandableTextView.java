package com.huiyin.wight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;

public class ExpandableTextView extends TextView {
	private static final int DEFAULT_TRIM_LENGTH = 220;

	private CharSequence originalText;
	private CharSequence trimmedText;
	private BufferType bufferType;
	private boolean trim = true;
	private int trimLength;
	private ImageView view;
	private ImageView view1;
	

	public ExpandableTextView(Context context) {
		this(context, null);
	}

	public ExpandableTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.ExpandableTextView);
		this.trimLength = typedArray.getInt(
				R.styleable.ExpandableTextView_trimLength, DEFAULT_TRIM_LENGTH);
		typedArray.recycle();

		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				trim = !trim;
				setMyText();
				requestFocusFromTouch();
				if (view != null)
					if (trim) {
						setMaxLines(EXPANDER_MAX_LINES);
						view.setBackgroundResource(R.drawable.arraw_up);
						view1.setVisibility(View.GONE);
					} else {
						setMaxLines(DEFAULT_MIN_LINES);
						view.setBackgroundResource(R.drawable.arraw_next);
						view1.setVisibility(View.VISIBLE);
					}
			}
		});
	}

	public void setMyText() {
		super.setText(getDisplayableText(), bufferType);
	}

	private CharSequence getDisplayableText() {
		return trim ? trimmedText : originalText;
	}

	public void setImg(ImageView view, ImageView view1) {
		this.view = view;
		this.view1 = view1;
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		originalText = text;
		trimmedText = getTrimmedText(text);
		bufferType = type;
		setMyText();
	}

	private CharSequence getTrimmedText(CharSequence text) {
			return originalText;
	}
	
	
	private static final boolean DEFAULT_EXPANDED = false;
    private static final int DEFAULT_MIN_LINES = 2;
    public static final int EXPANDER_MAX_LINES = Integer.MAX_VALUE;

    private boolean mExpanded = DEFAULT_EXPANDED;
    private int mMinLines = DEFAULT_MIN_LINES;
    private int mOriginalWidth;
    private int mOriginalHeight;
    private int mCollapseHeight;
    private boolean mInitialized;
	private boolean mAnimating;
	
	
   @SuppressLint("NewApi") 
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!mInitialized) {
            mOriginalWidth = getMeasuredWidth();
            mOriginalHeight = getMeasuredHeight();
            setMaxLines(mMinLines);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            mCollapseHeight = getMeasuredHeight();
            mInitialized = true;
            setMeasuredDimension(mOriginalWidth, mExpanded ? mOriginalHeight : mCollapseHeight);
        } else if (getTag(R.id.tag_expandable_text_view_reused) != null && !mAnimating) {
            setTag(R.id.tag_expandable_text_view_reused, null);
            mOriginalWidth = getMeasuredWidth();
            final int lineHeight = getLineHeight();
            mOriginalHeight = lineHeight * getLineCount() + 1;
            mCollapseHeight = lineHeight * mMinLines + 1;
            setMeasuredDimension(mOriginalWidth, mExpanded ? mOriginalHeight : mCollapseHeight);
        }
    }

	public CharSequence getOriginalText() {
		return originalText;
	}

	public void setTrimLength(int trimLength) {
		this.trimLength = trimLength;
		trimmedText = getTrimmedText(originalText);
		setMyText();
	}

	public int getTrimLength() {
		return trimLength;
	}
}
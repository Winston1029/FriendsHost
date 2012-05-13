package com.moupress.app.friendshost.uicomponent;

import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.uicomponent.interfaces.TitleProvider;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

/**
 * @author Li Ji
 *
 */
public class TabPageIndicator extends HorizontalScrollView{

	private LinearLayout mTabLayout;
    private ViewPager mViewPager;
    

    private LayoutInflater mInflater;

    int mMaxTabWidth;
    private int mSelectedTabIndex;
    
	Runnable mTabSelector;
	 
    //private ViewPager.OnPageChangeListener mListener;
    
    
	/**
	 * Page Listener - Listener for detailed page on the UI
	 * 
	 */
	private ViewPager.OnPageChangeListener pageListener = new  ViewPager.OnPageChangeListener()
	{

		 @Override
		    public void onPageScrollStateChanged(int arg0) {
		     
		    }

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		 @Override
	    public void onPageSelected(int pagePos) {
	        setCurrentItem(pagePos);
	    }
	};
	
	
	    /**
	     * Listener - OnClick Listener for upper pageIndicator
	     * 
	     */
	    private OnClickListener mTabClickListener = new OnClickListener() {
	        public void onClick(View view) {
	            TabView tabView = (TabView)view;
	            mViewPager.setCurrentItem(tabView.getIndex());
	        }
	    };

	    

	    public TabPageIndicator(Context context) {
	        this(context, null);
	    }

	    public TabPageIndicator(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        setHorizontalScrollBarEnabled(false);

	        mInflater = LayoutInflater.from(context);

	        mTabLayout = new LinearLayout(getContext());
	        addView(mTabLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT));
	    }

	    @Override
	    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	        final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
	        setFillViewport(lockedExpanded);

	        final int childCount = mTabLayout.getChildCount();
	        if (childCount > 1 && (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
	            if (childCount > 2) {
	                mMaxTabWidth = (int)(MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
	            } else {
	                mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
	            }
	        } else {
	            mMaxTabWidth = -1;
	        }

	        final int oldWidth = getMeasuredWidth();
	        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	        final int newWidth = getMeasuredWidth();

	        if (lockedExpanded && oldWidth != newWidth) {
	            // Direct to the tab display if we're at a new (scrollable) size.
	            setCurrentItem(mSelectedTabIndex);
	        }
	    }

	    
	    /**
	     * Animated transition when scroll certian tab
	     * @param position -- The position of tab to scroll to
	     */
	    private void animateToTab(final int position) {
	        final View tabView = mTabLayout.getChildAt(position);
	        if (mTabSelector != null) {
	            removeCallbacks(mTabSelector);
	        }
	        mTabSelector = new Runnable() {
	            public void run() {
	                final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
	                smoothScrollTo(scrollPos, 0);
	                mTabSelector = null;
	            }
	        };
	        post(mTabSelector);
	    }

	    @Override
	    public void onAttachedToWindow() {
	        super.onAttachedToWindow();
	        if (mTabSelector != null) {
	            // Re-post the selector we saved
	            post(mTabSelector);
	        }
	    }

	    @Override
	    public void onDetachedFromWindow() {
	        super.onDetachedFromWindow();
	        if (mTabSelector != null) {
	            removeCallbacks(mTabSelector);
	        }
	    }

	    private void addTab(String text, int index) {
	    	
	        final TabView tabView = (TabView)mInflater.inflate(R.layout.fh_scrollbar_tabview, null);
	        tabView.init(this, text, index);
	        tabView.setFocusable(true);
	        tabView.setOnClickListener(mTabClickListener);

	        mTabLayout.addView(tabView, new LinearLayout.LayoutParams(0, LayoutParams.FILL_PARENT, 1));
	    }

	    public void setViewPager(ViewPager view) {
	        final PagerAdapter adapter = view.getAdapter();
	        if (adapter == null) {
	            throw new IllegalStateException("ViewPager does not have adapter instance.");
	        }
	       
	        mViewPager = view;
	        view.setOnPageChangeListener(pageListener);
	        notifyDataSetChanged();
	    }

	    public void notifyDataSetChanged() {
	        mTabLayout.removeAllViews();
	        TitleProvider adapter = (TitleProvider)mViewPager.getAdapter();
	        final int count = ((PagerAdapter)adapter).getCount();
	        for (int i = 0; i < count; i++) {
	            addTab(adapter.getTitle(i), i);
	        }
	        if (mSelectedTabIndex > count) {
	            mSelectedTabIndex = count - 1;
	        }
	        setCurrentItem(mSelectedTabIndex);
	        requestLayout();
	    }

	    public void setCurrentItem(int item) {
	        if (mViewPager == null) {
	            throw new IllegalStateException("ViewPager has not been bound.");
	        }
	        mSelectedTabIndex = item;
	        final int tabCount = mTabLayout.getChildCount();
	        for (int i = 0; i < tabCount; i++) {
	            final View child = mTabLayout.getChildAt(i);
	            final boolean isSelected = (i == item);
	            child.setSelected(isSelected);
	            if (isSelected) {
	                animateToTab(item);
	            }
	        }
	    }

	
	    public static class TabView extends LinearLayout {
	        private TabPageIndicator mParent;
	        private int mIndex;

	        public TabView(Context context, AttributeSet attrs) {
	            super(context, attrs);
	        }

	        public void init(TabPageIndicator parent, String text, int index) {
	            mParent = parent;
	            mIndex = index;

	            TextView textView = (TextView)findViewById(android.R.id.text1);
	            textView.setText(text);
	        }

	        @Override
	        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	            // Re-measure if we went beyond our maximum size.
	            if (mParent.mMaxTabWidth > 0 && getMeasuredWidth() > mParent.mMaxTabWidth) {
	                super.onMeasure(MeasureSpec.makeMeasureSpec(mParent.mMaxTabWidth, MeasureSpec.EXACTLY),
	                        heightMeasureSpec);
	            }
	        }

	        public int getIndex() {
	            return mIndex;
	        }
	    }

}

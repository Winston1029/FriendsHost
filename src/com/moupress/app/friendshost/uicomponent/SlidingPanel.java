package com.moupress.app.friendshost.uicomponent;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.moupress.app.friendshost.R;

public class SlidingPanel extends LinearLayout{

	private int speed=300;
	private float distance =0;
	private int width;
	private boolean isOpen=false;
	
	private PanelSlidingListener panelSlidingListener;
	private int viewId;
	private ViewGroup refViewGroup;
	
	
	public SlidingPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray a= context.obtainStyledAttributes(attrs, R.styleable.SlidingPanel,0, 0);
		speed=a.getInt(R.styleable.SlidingPanel_speed, 300);
		//distance = a.getDimension(R.styleable.SlidingPanel_targetDistance, 60);
		
		a.recycle();
	}
	
	public void SetAlignViewId(int viewId)
	{
		this.viewId = viewId;
	}
	
	
	public void Slide2Left()
	{
		if(!isOpen)
			toggle();
	}
	
	public void Slide2Right()
	{
		if(isOpen)
			toggle();
	}
	
	public void toggle() {
		TranslateAnimation anim=null;
		
		isOpen=!isOpen;
		
		if(this.refViewGroup == null)
		this.refViewGroup = (ViewGroup) this.getRootView().findViewById(viewId);
		
		if(distance == 0)
		distance = this.refViewGroup.getWidth();
		
		if (isOpen) {
			//setVisibility(View.VISIBLE);
			anim=new TranslateAnimation(0.0f, distance ,0.0f,0.0f);
			anim.setAnimationListener(popUpListener);
			
		}
		else {
			anim=new TranslateAnimation(distance, 0.0f, 0.0f,0.0f);
			anim.setAnimationListener(collapseListener);

		}
		
		//anim.setFillEnabled(true);
		anim.setFillAfter(true);
		//anim.setFillBefore(false);
		
		anim.setDuration(speed);
		anim.setInterpolator(new AccelerateInterpolator(1.0f));
		//v.startAnimation(anim);
		startAnimation(anim);
	}
	
	
	
	Animation.AnimationListener popUpListener=new Animation.AnimationListener() {
		public void onAnimationEnd(Animation animation) {
			
			
			//params.setMargins((int) distance, 0, 0, 0);
			//params.leftMargin = (int) distance;
			
//			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//			params.addRule(RelativeLayout.RIGHT_OF, viewId);
//			params.width = width;
//			setLayoutParams(params);
			
			//setVisibility(View.GONE);
			refViewGroup.bringToFront();
			panelSlidingListener.onSlidingUpEnd();
		}
		
		public void onAnimationRepeat(Animation animation) {
			// not needed
		}
		
		public void onAnimationStart(Animation animation) {
			//width = getWidth();
			
		}
	};
	Animation.AnimationListener collapseListener=new Animation.AnimationListener() {
		public void onAnimationEnd(Animation animation) {
			
//			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//			params.addRule(RelativeLayout.ALIGN_TOP, viewId);
//			setLayoutParams(params);
			
			//setVisibility(View.GONE);
			panelSlidingListener.onSlidingDownEnd();
		}
		
		public void onAnimationRepeat(Animation animation) {
			
		}
		
		public void onAnimationStart(Animation animation) {
			// not needed
			
//			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//			params.width = width;
//			setLayoutParams(params);
			bringToFront();
		}
	};
	
	public void setPanelSlidingListener(PanelSlidingListener panelSlidingListener)
	{
		this.panelSlidingListener = panelSlidingListener;
	}
	
	public interface PanelSlidingListener{
		
		public void onSlidingUpEnd();
		public void onSlidingDownEnd();
	}

	public void setOpen(boolean open) {
		// TODO Auto-generated method stub
		this.isOpen = open;
	}
	
	public boolean getOpen()
	{
		return this.isOpen;
	}
}


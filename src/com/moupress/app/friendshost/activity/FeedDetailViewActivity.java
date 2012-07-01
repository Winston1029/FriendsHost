package com.moupress.app.friendshost.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;

import com.moupress.app.friendshost.ui.DetailView;
import com.moupress.app.friendshost.ui.listeners.ContentViewListener;
import com.moupress.app.friendshost.ui.listeners.TitleBarListener;
import com.moupress.app.friendshost.util.FlurryUtil;

public class FeedDetailViewActivity extends Activity {
	private static final String TAG = "FeedDetailViewActivity";
	private DetailView detailView;
	
	private int iScreenHeight;
	private int iScreenWidth;
	private long lCreateTime;
	private long lLoadedTime;
	private long lStopTime;
	private long lDuration;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lCreateTime = System.currentTimeMillis();
		fInitDetailView();
		// FurryUtil
		lLoadedTime = System.currentTimeMillis();
		lDuration = (lLoadedTime - lCreateTime)/1000;
		FlurryUtil.logEvent(TAG+":onCreate", "Load Duration:" + lDuration);
		iScreenHeight = getWindowManager().getDefaultDisplay().getHeight();
		iScreenWidth = getWindowManager().getDefaultDisplay().getWidth();
	}
	
	private void fInitDetailView() {
		ContentViewListener contentViewListener = new ContentViewListener(){};
		TitleBarListener titleBarListener = new TitleBarListener(){

			@Override
			public void OnTitleBarButtonClick(String viewName, int btnId,
					String snsName) {
				
			}};
		
		detailView = new DetailView();
		detailView.InitTitle(this, titleBarListener);
		detailView.InitContent(this, contentViewListener);
		
		detailView.LoadView(this.getIntent().getExtras());
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		// FurryUtil
		lStopTime = System.currentTimeMillis();
		lDuration = (lStopTime - lCreateTime)/1000;
		FlurryUtil.logEvent(TAG+":onStop", "Browse Duration:" + lDuration);
	}
	
}

package com.moupress.app.friendshost.activity;

import android.app.Activity;
import android.os.Bundle;
import com.moupress.app.friendshost.ui.DetailView;
import com.moupress.app.friendshost.ui.listeners.ContentViewListener;
import com.moupress.app.friendshost.ui.listeners.TitleBarListener;

public class FeedDetailViewActivity extends Activity {

	private DetailView detailView;
	
	private int iScreenHeight;
	private int iScreenWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		fInitDetailView();
		
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
	
}

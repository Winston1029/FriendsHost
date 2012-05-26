package com.moupress.app.friendshost.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.ui.listeners.ContentViewListener;
import com.moupress.app.friendshost.ui.listeners.TitleBarListener;

public class DetailView extends View{
	
	public  DetailView()
	{
		this.TitleLayoutId = R.layout.fh_title_bar;
		this.ContentLayoutId = R.layout.feed_item_detail;
	}
	
	@Override
	public void InitTitle(Activity activity, TitleBarListener titleBarListener) {
		super.InitTitle(activity, titleBarListener);
	     
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
	}
	
	@Override
	public void InitContent(Activity activity, ContentViewListener contentViewListener) {
		
	}
	
	

	@Override
	protected void LoadView(Bundle loadData) {
		
	}

	@Override
	protected void RefreshView() {
		
	}
}

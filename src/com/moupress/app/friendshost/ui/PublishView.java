package com.moupress.app.friendshost.ui;

import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.ui.listeners.ContentViewListener;
import com.moupress.app.friendshost.ui.listeners.TitleBarListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class PublishView extends View{

	public PublishView()
	{
		this.ContentLayoutId = R.layout.feed_publish_layout;
	}
	
	
	
	@Override
	public void InitContent(Activity activity,
			ContentViewListener contentViewListener) {
		// TODO Auto-generated method stub
		super.InitContent(activity, contentViewListener);
		
	}

	@Override
	public void InitTitle(Activity activity, TitleBarListener titleBarListener) {
		// TODO Auto-generated method stub
		super.InitTitle(activity, titleBarListener);
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	protected void LoadView(Bundle loadData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void RefreshView() {
		// TODO Auto-generated method stub
		
	}

}

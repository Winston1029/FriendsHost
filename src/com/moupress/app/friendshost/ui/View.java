package com.moupress.app.friendshost.ui;

import android.app.Activity;
import android.os.Bundle;

import com.moupress.app.friendshost.ui.listeners.ContentViewListener;
import com.moupress.app.friendshost.ui.listeners.TitleBarListener;

public abstract class View {

	protected int TitleLayoutId = -1;
	protected int ContentLayoutId = -1;
	
	//protected int titleId = -1;
	//protected int setBtnMsgId = -1;
	
	protected TitleBarListener titleBarListener;
	
	protected ContentViewListener contentViewListener;
	
	protected Bundle LoadData;
	
	public void InitTitle(Activity activity,TitleBarListener titleBarListener)
	{
		this.titleBarListener = titleBarListener;
		
	}
	
	public void InitContent(Activity activity, ContentViewListener contentViewListener)
	{
		this.contentViewListener = contentViewListener;
		
	}
	
	protected abstract void LoadView(Bundle loadData);
	
	protected abstract void RefreshView();
}

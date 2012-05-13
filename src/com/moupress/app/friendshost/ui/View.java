package com.moupress.app.friendshost.ui;

import android.app.Activity;
import android.os.Bundle;

import com.moupress.app.friendshost.ui.listeners.DetailViewListener;
import com.moupress.app.friendshost.ui.listeners.TitleBarListener;

public abstract class View {

	protected int TitleLayoutId = -1;
	
	protected int DetailLayoutId = -1;
	
	protected TitleBarListener titleBarListener;
	
	protected DetailViewListener detailViewListener;
	
	protected Bundle LoadData;
	
	public void InitTitle(Activity activity,TitleBarListener titleBarListener)
	{
		this.titleBarListener = titleBarListener;
		
	}
	
	public void InitDetail(Activity activity, DetailViewListener detailViewListener)
	{
		this.detailViewListener = detailViewListener;
		
	}
	
	protected void LoadView(Bundle loadData)
	{
		
	}
}

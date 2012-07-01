package com.moupress.app.friendshost.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class RateView extends DialogView{
	
	private Activity zActivity;
	private Intent  intentBack;
	private OnClickListener selBtnOnClickListener;
	
	public RateView(final Activity zActivity, Intent intentBack) {
		 
		// TODO Auto-generated constructor stub
		this.zActivity = zActivity;
		this.intentBack = intentBack;
		
		selBtnOnClickListener = new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.moupress.app.friendshost"));
				zActivity.startActivity(intent);
				zActivity.finish();
			}};
	}
	
	

	@Override
	public int GetSetBtnTxId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public OnClickListener GetSetOnClickListener() {
		// TODO Auto-generated method stub
		return  this.selBtnOnClickListener;
	}

	@Override
	public int GetTitleId() {
		// TODO Auto-generated method stub
		return 0;
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

package com.moupress.app.friendshost.activity;

import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.sns.FeedItem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FeedResendActivity extends Activity{
	
	private final String TAG = "FeedResendActivity";
	private Activity zActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.feed_resend_layout);
		zActivity = this;
		initUI();
	}

	private void initUI() {
		Intent intent = this.getIntent();
		FeedItem feed = intent.getParcelableExtra(Const.FEED_ITEM);
		Log.i(TAG, " Name: "+feed.getsName()+" Body: "+ feed.getsMsgBody());
		Button resendBtn = (Button) this.findViewById(R.id.btn_resend_asis);
		resendBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				
			}});
		
		Button cmtAsIsBtn = (Button) this.findViewById(R.id.btn_resend_cmtadd);
		cmtAsIsBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
			}});
		
		Button cancelBtn = (Button) this.findViewById(R.id.btn_resend_cancel);
		cancelBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Log.i(TAG, "Cancel Btn is clicked!");
				//zActivity.finishActivity(Const.FEED_RESEND_REQ_CD);
				zActivity.finish();
			}});
	}
}

package com.moupress.app.friendshost.util;

import com.moupress.app.friendshost.FriendsHostActivity;
import com.moupress.app.friendshost.R;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.TextView;

public class FeedScheduler {
	
	private Activity zActivity;
	private Context zContext;
	
	private long lretrieveInterval;
	private long cntTimedTask;

	public FeedScheduler(Activity activity, Context context) {
		this.zActivity = activity;
		this.zContext = context;
		
		lretrieveInterval = 1000*30;
		cntTimedTask = 0;
	}
	
	public void fSetRetrieveInterval(long interval) {
		lretrieveInterval = interval;
	}
	public long fGetRetriveInterval() { return lretrieveInterval;}
	
	private Handler zHandler = new Handler();
	private Runnable zTimedTask = new Runnable(){

		@Override
		public void run() {
			TextView uTxvCounter = (TextView) zActivity.findViewById(R.id.txv_counter);
			cntTimedTask++;
			((FriendsHostActivity)zActivity).zPubsub.fGetArrAdapterFeed().clear();
			((FriendsHostActivity)zActivity).zPubsub.fGetFacebookUtil().fGetNewsFeed();
			uTxvCounter.setText("Counter: " + cntTimedTask);
			zHandler.postDelayed(zTimedTask, lretrieveInterval);
		}
		
	};

	public void start() {
		zHandler.post(zTimedTask);
	}
	
	public void stop() {
		zHandler.removeCallbacks(zTimedTask);
		
	}
}

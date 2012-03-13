package com.moupress.app.friendshost.util;

import com.moupress.app.friendshost.PubSub;
import com.moupress.app.friendshost.R;

import android.os.Handler;
import android.widget.TextView;

public class FeedScheduler {
	
	private PubSub zPubSub;
	
	private long lretrieveInterval;
	private long cntTimedTask;
	
	public FeedScheduler(PubSub zpubSub) {
		this.zPubSub = zpubSub;
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
			TextView uTxvCounter = (TextView) zPubSub.fGetActivity().findViewById(R.id.txv_counter);
			cntTimedTask++;
			zPubSub.fGetArrAdapterFeed().clear();
			zPubSub.fGetFacebookUtil().fGetNewsFeed(zPubSub.fGetContext());
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

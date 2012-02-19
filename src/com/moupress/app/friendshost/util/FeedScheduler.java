package com.moupress.app.friendshost.util;

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
		
		lretrieveInterval = 500;
		cntTimedTask = 0;
	}
	
	public void fSetRetrieveInterval(long interval) {
		lretrieveInterval = interval;
	}
	public long fGetRetriveInterval() { return lretrieveInterval;}
	
	private Handler handler = new Handler();
	private Runnable timedTask = new Runnable(){

		@Override
		public void run() {
			TextView uTxvCounter = (TextView) zActivity.findViewById(R.id.txv_counter);
			cntTimedTask++;
			uTxvCounter.setText("Counter: " + cntTimedTask);
			handler.postDelayed(timedTask, lretrieveInterval);
		}
		
	};

	public void start() {
		handler.post(timedTask);		
	}
	
	public void stop() {
		handler.removeCallbacks(timedTask);
		
	}
}

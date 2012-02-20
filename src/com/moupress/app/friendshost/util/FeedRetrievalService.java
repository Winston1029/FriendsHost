package com.moupress.app.friendshost.util;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class FeedRetrievalService extends Service {

	private Timer timer = new Timer();
	private static final long UPDATE_INTERVAL = 5000;
	
	private void pollForUpdates() {
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
			}
		}, 0, UPDATE_INTERVAL);
		Log.i(getClass().getSimpleName(), "Timer started.");

	}

	@Override
	public IBinder onBind(Intent intent) {
		return new MyBinder();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
		}
		Log.i(getClass().getSimpleName(), "Timer stopped.");

	}
	
	public class MyBinder extends Binder {
		Service getService() {
			return FeedRetrievalService.this;
		}
	}

}

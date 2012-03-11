package com.moupress.app.friendshost;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class FeedRetrievalService extends Service {

	private Timer timer;
	private long update_interval = 15000;
	private static int counter = 0;
	
	private void pollForUpdates() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				counter++;
				if (PubSub.zFacebook != null && PubSub.zFacebook.isSessionValid() ) {
					PubSub.zFacebook.fGetNewsFeed();
				}
				if (PubSub.zRenrenUtil != null && PubSub.zRenrenUtil.isSessionValid() ) {
					PubSub.zRenrenUtil.fGetNewsFeed();
				}
				if (PubSub.zRenrenUtil == null && PubSub.zFacebook == null) {
					stopSelf();
				}
			}
		}, 30000, update_interval);
		Log.i(getClass().getSimpleName(), "Timer started.");

	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		 super.onStartCommand(intent, flags, startId);
		 return START_STICKY;
	}

	@Override
	public void onCreate() {
		Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show();
		pollForUpdates();
		
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
		Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
		Log.i(getClass().getSimpleName(), "Timer stopped.");

	}
	
	public class MyBinder extends Binder {
		Service getService() {
			return FeedRetrievalService.this;
		}
	}

}
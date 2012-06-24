package com.moupress.app.friendshost.service;

import java.util.Timer;
import java.util.TimerTask;

import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.PubSub;
import com.moupress.app.friendshost.util.Pref;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

public class FeedRetrievalService extends Service {

	public static final String TAG = "FeedRetrievalService";
	
	private Timer timer;
	private long update_interval = 15000;
	private TimerTask zTimedTask;
	
	private Messenger incomMessenger = new Messenger(new IncomMsgHandler());
	
	private void fPollForUpdates() {
		
		if(this.update_interval > 0)
		{
			timer.scheduleAtFixedRate(zTimedTask, 3000, update_interval);
		}
		
		Log.i(TAG, "Timer scheduled! Internal:" + update_interval);
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		 super.onStartCommand(intent, flags, startId);
		 
//		 if(intent != null)
//		 {
//			 int index = intent.getIntExtra(Const.SETTING_BASIC+"_UPT_FREQ", -1);
//			 
//			 Log.v(TAG, "Update Freq Reset index: "+index);
//			 this.setUpdateDuration(index);
//			 this.rescheduleTimer();
//		 }
		 
		 return START_STICKY;
	}

	
	@Override
	public void onCreate() {
		Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show();
		Log.v(TAG, "Service is created! ");
		this.iniUptInterval();
		//this.setTimer();
		this.rescheduleTimer();
//		timer = new Timer();
//		zTimedTask = new TimerTask() {
//			@Override
//			public void run() {
//				PubSub.zSnsOrg.SnsGetNewFeed(getApplicationContext());
////				if (PubSub.zFacebook != null && PubSub.zFacebook.isSessionValid() ) {
////					PubSub.zFacebook.fGetNewsFeed(getApplicationContext());
////				}
////				if (PubSub.zRenrenUtil != null && PubSub.zRenrenUtil.isSessionValid() ) {
////					PubSub.zRenrenUtil.fGetNewsFeed(getApplicationContext());
////				}
////				if (PubSub.zSinaUtil != null && PubSub.zSinaUtil.isSessionValid()) {
////					PubSub.zSinaUtil.fGetNewsFeed(getApplicationContext());
////				}
////				if(PubSub.zTwitterUtil != null && PubSub.zTwitterUtil.isSessionValid()) {
////					PubSub.zTwitterUtil.fGetNewsFeed(getApplicationContext());
////				}
//				
//				
//				
////				if (PubSub.zRenrenUtil == null && 
////						PubSub.zFacebook == null && 
////						PubSub.zSinaUtil == null &&
////						PubSub.zTwitterUtil == null) {
////						//stopSelf();
////					}
//				
//			}
//		};
		//fPollForUpdates();
		
	}
	

	private void iniUptInterval() {
		// TODO Auto-generated method stub
		int index = Pref.getMyIntPref(this.getApplicationContext(), Const.SETTING_BASIC+"_UPT_FREQ");
		if(index < 0) index = 1;
		this.setUpdateDuration(index);
	}


	@Override
	public IBinder onBind(Intent intent) {
		//return new MyBinder();
		return this.incomMessenger.getBinder();
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
	
	
	private void setTimer()
	{
		timer = new Timer();
		zTimedTask = new TimerTask() {
			@Override
			public void run() {
				Log.v(TAG, "Feed Retrieved Triggered! ");
				PubSub.zSnsOrg.SnsGetNewFeed(getApplicationContext());
				}
			};
	}
	
	private void rescheduleTimer()
	{
		if(timer != null)
			timer.cancel();
		
		this.setTimer();
		this.fPollForUpdates();
	}
	
	public class MyBinder extends Binder {
		Service getService() {
			return FeedRetrievalService.this;
		}
	}

	private class IncomMsgHandler extends Handler
	{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			//super.handleMessage(msg);
			switch(msg.what)
			{
				case Const.SERVICE_REFRESH_FEED:
					Log.v(TAG, "Service Refresh Message");
				break;
				case Const.SERVICE_UPDATE_FREQ:
					Log.v(TAG, "Update Frequency Message");
					setUpdateDuration(msg.arg1);
					rescheduleTimer();
				break;
			}
		}
	}
	 
	private void setUpdateDuration(int index) {
		// TODO Auto-generated method stub
		//index = -1;
		
		if(index >=0 && index < Const.SETTING_UPT_FREQ_DURATION.length)
		{
			this.update_interval = Const.SETTING_UPT_FREQ_DURATION[index] * 1000;
		}
	}
}

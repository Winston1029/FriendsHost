package com.moupress.app.friendshost.service;

import java.util.List;

import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.PubSub;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class FeedRetrieveServiceConnection {

	public static final String TAG = "ServiceConnection";
	private Messenger incomMessenger;
	private Messenger outcomMessenger;
	
	//Members from Context
	private PubSub zPubSub;
	private Activity zActivity;
	
	public FeedRetrieveServiceConnection(PubSub pubSub)
	{
		this.incomMessenger = new Messenger(new IncommingMsgHandler());
		this.zPubSub = pubSub;
		this.zActivity = pubSub.fGetActivity();
	}
	
	private class IncommingMsgHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			//super.handleMessage(msg);
			Log.v(TAG, "Message Received !" + msg);
			
		}
	}
	
	/** Flag indicating whether we have called bind on the service. */
    boolean mBound;
    
	private ServiceConnection mConnection = new ServiceConnection()
	{

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// TODO Auto-generated method stub
			outcomMessenger = new Messenger(service);
			mBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			// TODO Auto-generated method stub
			outcomMessenger = null;
			mBound = false;
		}
	};
	
	
	public void BindToService()
	{
		if(this.zActivity != null)
		{
			this.zActivity.bindService(new Intent(zActivity, FeedRetrievalService.class), mConnection, Context.BIND_AUTO_CREATE);
		}
		else
		{
			Log.v(TAG, " Activity is not created ");
		}
	}
	
	public void UnBindToService()
	{
		if(this.zActivity != null)
		{
			this.zActivity.unbindService(mConnection);
		}
	}
	
	public void SendMessage(int msgWhat, int msgArg1, int msgArg2)
	{
		if(outcomMessenger != null)
		{
			Message msg = Message.obtain(null, msgWhat, msgArg1, msgArg2);
			msg.replyTo = incomMessenger;
			
			try {
				
				outcomMessenger.send(msg);
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				Log.v(TAG, " Message Send Error " + e.toString());
			}
		}
	}

	public boolean CheckSvcRunning() {

		final ActivityManager activityManager = (ActivityManager)zActivity.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        final List<RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals("com.moupress.app.friendshost.service.FeedRetrievalService")){
                return true;
            }
        }
		return false;
	}
}

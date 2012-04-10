package com.moupress.app.friendshost.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class NotificationTask extends AsyncTask<Integer, Integer, Void>{
    private NotificationHelper mNotificationHelper;
    private boolean isTaskDone = false;
    private int notificationId;
    private String sns;
    private String fileType;
    
    public NotificationTask(Context context){
        mNotificationHelper = new NotificationHelper(context);
        isTaskDone = false;
    }

    public void SetNotificationTask(int notificationId, String sns, String fileType)
    {
    	this.notificationId = notificationId;
    	this.sns = sns;
    	this.fileType = fileType;
    }
    
    protected void onPreExecute(){
        //Create the notification in the statusbar
        mNotificationHelper.createNotification(this.notificationId,this.sns, this.fileType);
    }

    @Override
    protected Void doInBackground(Integer... integers) {
        //This is where we would do the actual download stuff
        //for now I'm just going to loop for 10 seconds
        // publishing progress every second
        while(!this.isTaskDone)
        {
                try {
                    Thread.sleep(1000);
                    publishProgress(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
         }
        return null;
    }
    
    protected void onProgressUpdate(Integer... progress) {
        //This method runs on the UI thread, it receives progress updates
        //from the background thread and publishes them to the status bar
    	System.out.println("Progress "+progress[0]);
        mNotificationHelper.progressUpdate(progress[0]);
    }
    
    protected void onPostExecute(Void result)    {
        //The task is complete, tell the status bar about it
        mNotificationHelper.completed();
    }

	public boolean isTaskDone() {
		return isTaskDone;
	}

	public void setTaskDone(boolean isTaskDone) {
		this.isTaskDone = isTaskDone;
	}
    
	class NotificationHelper {
	    private Context mContext;
	    //private int NOTIFICATION_ID = 1;
	    private int notificationId;
	    private Notification mNotification;
	    private NotificationManager mNotificationManager;
	    private PendingIntent mContentIntent;
	    private CharSequence mContentTitle;
	    private String sns;
	    private String fileType;
	    
	    public NotificationHelper(Context context)
	    {
	        mContext = context;
	    }

	    /**
	     * Put the notification into the status bar
	     */
	    public void createNotification(int notificationId,String sns, String fileType) {
	        //get the notification manager
	    	this.notificationId = notificationId;
	    	this.sns = sns;
	    	this.fileType = fileType;
	    	
	        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

	        //create the notification
	        int icon = android.R.drawable.stat_sys_upload;
	        //Initial text that appears in the status bar
	        CharSequence tickerText = "Uploading to "+this.sns;
	        long when = System.currentTimeMillis();
	        mNotification = new Notification(icon, tickerText, when);

	        //create the content which is shown in the notification pulldown
	        //Full title of the notification in the pull down
	        mContentTitle = this.fileType + " is uploading to "+this.sns;
	        //CharSequence contentText = "0% complete"; //Text of the notification in the pull down
	        CharSequence contentText = "Uploading "+this.fileType;
	        		
	        //you have to set a PendingIntent on a notification to tell the system what you want it to do when the notification is selected
	        //I don't want to use this here so I'm just creating a blank one
	        Intent notificationIntent = new Intent();
	        mContentIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);

	        //add the additional content and intent to the notification
	        mNotification.setLatestEventInfo(mContext, mContentTitle, contentText, mContentIntent);

	        //make this notification appear in the 'Ongoing events' section
	        mNotification.flags |= Notification.FLAG_ONLY_ALERT_ONCE | Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONGOING_EVENT;
	        //show the notification
	        mNotificationManager.notify(notificationId, mNotification);
	    }

	    /**
	     * Receives progress updates from the background task and updates the status bar notification appropriately
	     * @param percentageComplete
	     */
	    public void progressUpdate(int percentageComplete) {
	        //build up the new status message
	        //CharSequence contentText = percentageComplete + "% complete";
	    	CharSequence contentText = "Uploading "+this.fileType;
	        //publish it to the status bar
	        mNotification.setLatestEventInfo(mContext, mContentTitle, contentText, mContentIntent);
	        mNotificationManager.notify(notificationId, mNotification);
	    }

	    /**
	     * called when the background task is complete, this removes the notification from the status bar.
	     * We could also use this to add a new ‘task complete’ notification
	     */
	    public void completed()    {
	        //remove the notification from the status bar
	         //mNotificationManager.cancel(NOTIFICATION_ID);
	    	 CharSequence contentText = "One "+this.fileType+" is uploaded to "+this.sns;
	    	 CharSequence contentTitle = this.fileType + " is uploaded";
	    	 mNotification.icon = android.R.drawable.stat_sys_upload_done;
	    	 mNotification.setLatestEventInfo(mContext, contentTitle, contentText, mContentIntent);
	         mNotificationManager.notify(notificationId, mNotification);
	    }
	}
}


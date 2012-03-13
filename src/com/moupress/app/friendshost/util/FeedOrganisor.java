package com.moupress.app.friendshost.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.FriendsHostActivity;
import com.moupress.app.friendshost.PubSub;
import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.sns.Renren.FeedExtractResponseBean;
import com.moupress.app.friendshost.sns.Renren.RenenFeedElement;
import com.moupress.app.friendshost.sns.facebook.FBHomeFeed;
import com.moupress.app.friendshost.sns.facebook.FBHomeFeedEntry;

public class FeedOrganisor {
	private Activity zActivity;
	private Context zContext;
	private PubSub zPubSub;
	
	private DBHelper zDBHelper;
	
	

	public FeedOrganisor(PubSub pubsub) {
		this.zPubSub = pubsub;
		this.zContext = zPubSub.fGetContext();
		this.zActivity = zPubSub.fGetActivity();
		
		zDBHelper = new DBHelper(zContext);
	}
	
	/**
	 * Sort incoming feeds by column requirement
	 */
	public void fFilterFeeds() {
		
	}
	
	/**
	 * Save new feeds from fGetNewsFeed() into DB
	 * @param context 
	 */
	public void fSaveNewFeeds(String feed, Context context) {
		FBHomeFeed bean = new Gson().fromJson(feed, FBHomeFeed.class);
		
		long res = 0;
		for(int i= 0; i<bean.getData().size();i++) {
			//String msg = ((FBHomeFeedEntry) bean.getData().get(i)).getName()+" : "+((FBHomeFeedEntry) bean.getData().get(i)).getMessage();
			FBHomeFeedEntry entry = (FBHomeFeedEntry) bean.getData().get(i);
			res += zDBHelper.fInsertFeed(entry);
		}
		
		if (res > 0 ) {
			int cntUnReadFeed = fGetUnReadNewsFeedSummary(Const.SNS_FACEBOOK).length;
			fShowNotification(Const.SNS_FACEBOOK, cntUnReadFeed, context);
		}
		
	}
	
	public void fSaveNewFeeds(FeedExtractResponseBean bean, Context context) {

		long res = 0;
		
		for(int i= 0; i<bean.getFeedList().size();i++) {
			//String msg = ((FBHomeFeedEntry) bean.getData().get(i)).getName()+" : "+((FBHomeFeedEntry) bean.getData().get(i)).getMessage();
			RenenFeedElement entry = (RenenFeedElement) bean.getFeedList().get(i);
			res += zDBHelper.fInsertFeed(entry);
		}
		
		if (res > 0 ) {
			int cntUnReadFeed = fGetUnReadNewsFeedSummary(Const.SNS_RENREN).length;
			fShowNotification(Const.SNS_RENREN, cntUnReadFeed, context);
		}
		
	}
	
	/**
	 * Mark feeds that has been read
	 */
	public void fUpdateReadFeeds() {
		
	}
	
	/**
	 * Purge old feed in local client periodically
	 */
	public void fPurgeHistoricalFeed() {
		
	}

	public String[] fGetUnReadNewsFeedSummary(String sns) {
		String[] result = zDBHelper.fGetFeedSummary(sns);
		if (result == null || result.length == 0) {
			result = new String[] {"No Unread Feed in Local"};
		}
		return result;
	}
	
	public String[][] fGetUnReadNewsFeed(String sns) {
		String[][] result = zDBHelper.fGetFeedPreview(sns);
		if (result == null || result.length == 0) {
			result = new String[][] {{"No Unread Feed in Local"}};
		}
		return result;
	}
	
	
	private void fShowNotification(String sFromSNS, int NumUnRead, Context context) {
		// create notification manager
		NotificationManager notificationMgr = (NotificationManager) zActivity.getSystemService(Context.NOTIFICATION_SERVICE);
		
		// create notification
		CharSequence tickerText = "Unread Feed From " + sFromSNS;
		long when = System.currentTimeMillis();
		Notification notification = new Notification(R.drawable.ic_launcher, tickerText, when);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		// preparation
		//Context context = zActivity.getApplicationContext();
		CharSequence contentTitle = "New Feed From " + sFromSNS;
		CharSequence contentText = NumUnRead + " unread updates";
		Intent notificationIntent = new Intent(context, FriendsHostActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString(Const.ACTION_DISPLAYFEED, sFromSNS);
		notificationIntent.putExtras(bundle);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		
		// notify
		notificationMgr.notify(sFromSNS, Const.FRIENDSHOST_NOTIFY_ID, notification);
	}

}

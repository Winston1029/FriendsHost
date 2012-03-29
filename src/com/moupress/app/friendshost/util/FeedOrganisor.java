package com.moupress.app.friendshost.util;

import java.util.ArrayList;
import java.util.List;

import weibo4andriod.Status;
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
import com.moupress.app.friendshost.LstViewFeedAdapter;
import com.moupress.app.friendshost.PubSub;
import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.sns.FeedItem;
import com.moupress.app.friendshost.sns.UserFriend;
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
			String fromID = entry.getFrom().getId();
			String fromHeadUrl = "https://graph.facebook.com/" + fromID + "/picture";
			entry.getFrom().setHeadurl(fromHeadUrl);
			
			res += zDBHelper.fInsertFeed(entry);
			zDBHelper.fInsertFriend(entry.getFrom());
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
			zDBHelper.fInsertFriend(entry.getFriend());
		}
		
		if (res > 0 ) {
			int cntUnReadFeed = fGetUnReadNewsFeedSummary(Const.SNS_RENREN).length;
			fShowNotification(Const.SNS_RENREN, cntUnReadFeed, context);
		}
		
	}
	
	public void fSaveNewFeeds(List<Status> friendsTimeline, Context context) {
		long res = 0;
		
		for (Status status : friendsTimeline) {
			//String msg = status.getUser().getScreenName() + " : " + status.getText();
			res += zDBHelper.fInsertFeed(status);
			
			UserFriend friend = new UserFriend();
			friend.setId(status.getUser().getId()+"");
			friend.setSNS(Const.SNS_SINA);
			friend.setName(status.getUser().getName());
			friend.setHeadurl(status.getUser().getProfileImageURL().toString());
			zDBHelper.fInsertFriend(friend);
		}
		
		if (res > 0 ) {
			int cntUnReadFeed = fGetUnReadNewsFeedSummary(Const.SNS_SINA).length;
			fShowNotification(Const.SNS_SINA, cntUnReadFeed, context);
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
	
	public ArrayList<FeedItem> fGetUnReadNewsFeed(String sns) {
		String[][] feeds = null;
		String[][] owners = null;
		ArrayList<FeedItem> items = new ArrayList<FeedItem>();
		
		feeds = zDBHelper.fGetFeedPreview(sns);
		
		if (feeds == null || feeds.length == 0) {
			//feeds = new String[][] {{"No Unread Feed in Local"}};
			FeedItem item = new FeedItem();
			item.setsName("No Unread Feed in Local");
			items.add(item);
		}
		else {
			for (int i = 0; i < feeds.length; i++) {
				
				FeedItem item = new FeedItem();
				//feedAdapter.addItem(feed[i]);
				item.setsName(feeds[i][0]);							//name
				item.setsOwnerID(feeds[i][1]);						//feed owner id
				item.setsCreatedTime(feeds[i][2]);					//created time
				item.setsMsgBody(feeds[i][3]);						//message
				item.setsStory(feeds[i][4]);						//story
				//item.setsStory_tags(feeds[i][4]);					//story_tags
				item.setsPhotoPreviewLink(feeds[i][5]);				//pic url
				item.setsPhotoPreviewName(feeds[i][6]);				//pic/album name
				item.setsPhotoPreviewCaption(feeds[i][7]);			//pic/album caption
				item.setsPhotoPreviewDescription(feeds[i][8]);		//pic/album description
				
				String feed_OwnerID = feeds[i][1];
				owners = zDBHelper.fGetFeedOwner(sns, feed_OwnerID); //there should be only 1 owner for each feed
				if (owners.length > 0) {
					item.getzFriend().setName(owners[0][0]);
					item.getzFriend().setHeadurl(owners[0][1]);
				}
				
				items.add(item);
			}
		}
		return items;
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

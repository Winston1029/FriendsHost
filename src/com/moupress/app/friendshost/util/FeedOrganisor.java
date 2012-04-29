package com.moupress.app.friendshost.util;

import java.util.ArrayList;
import java.util.List;

import twitter4j.ResponseList;
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
import com.moupress.app.friendshost.sns.FeedEntry;
import com.moupress.app.friendshost.sns.UserFriend;
import com.moupress.app.friendshost.sns.Renren.FeedExtractResponseBean;
import com.moupress.app.friendshost.sns.Renren.RenenFeedElement;
import com.moupress.app.friendshost.sns.Renren.RenrenFeedElementComments;
import com.moupress.app.friendshost.sns.Renren.RenrenFeedElementComments.RenrenFeedElementComment;
import com.moupress.app.friendshost.sns.Renren.RenrenFeedElementEntry;
import com.moupress.app.friendshost.sns.facebook.FBHomeFeed;
import com.moupress.app.friendshost.sns.facebook.FBHomeFeedEntry;
import com.moupress.app.friendshost.sns.facebook.FBHomeFeedEntryComments;
import com.moupress.app.friendshost.sns.facebook.FBHomeFeedEntryComments.FBFeedEntryComment;

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
	public void fSaveNewFeeds(FBHomeFeed beans, Context context) {
		
		
		long res = 0;
		if(beans != null && beans.getData()!=null)
		{
			for(int i= 0; i<beans.getData().size();i++) {
				//String msg = ((FBHomeFeedEntry) bean.getData().get(i)).getName()+" : "+((FBHomeFeedEntry) bean.getData().get(i)).getMessage();
				FBHomeFeedEntry entry = (FBHomeFeedEntry) beans.getData().get(i);
				String fromID = entry.getFrom().getId();
				String fromHeadUrl = "https://graph.facebook.com/" + fromID + "/picture";
				entry.getFrom().setHeadurl(fromHeadUrl);
				
				res += zDBHelper.fInsertFeed(entry);
				zDBHelper.fInsertFriend(entry.getFrom());
				
				int cntComments = Integer.parseInt(entry.getComments().getCount());
				// comment get from Facebook only shows the 1st and the last entry
				// need more research here
				if (cntComments > 0) {
					cntComments = Math.min(cntComments, entry.getComments().getData().size());
				}
				for (int j = 0; j < cntComments; j++) {
					FBFeedEntryComment comment = entry.getComments().getData().get(j);
					if (comment != null ) {
						comment.setSns(Const.SNS_FACEBOOK);
						comment.setCommetedfeedID(entry.getId());
						zDBHelper.fInsertComments(comment);
					}
				}
				
			}
		}
		beans = null;
		
		if (res > 0 ) {
			int cntUnReadFeed = fGetUnReadNewsFeedSummary(Const.SNS_FACEBOOK).length;
			fShowNotification(Const.SNS_FACEBOOK, cntUnReadFeed, context);
		}
		
	}
	
	public void fSaveNewFeeds(FeedExtractResponseBean bean, Context context) {

		long res = 0;
		
		for(int i= 0; i<bean.getFeedList().size();i++) {
			//String msg = ((FBHomeFeedEntry) bean.getData().get(i)).getName()+" : "+((FBHomeFeedEntry) bean.getData().get(i)).getMessage();
			RenrenFeedElementEntry entry = (RenrenFeedElementEntry) bean.getFeedList().get(i);
			res += zDBHelper.fInsertFeed(entry);
			
			UserFriend friend = entry.getzFriend();
			friend.setSNS(Const.SNS_RENREN);
			friend.setId(entry.getActor_id());
			friend.setName(entry.getName());
			friend.setHeadurl(entry.getHeadurl());
			
			zDBHelper.fInsertFriend(friend);
			
			int cntComments = Integer.parseInt(entry.getComments().getCount());
			// comment get from Renren only shows the 1st and the last entry
			// need more research here
			if (cntComments > 0) {
				cntComments = Math.min(cntComments, entry.getComments().getComment().size());
			}
			for (int j = 0; j < cntComments; j++) {
				RenrenFeedElementComment comment = entry.getComments().getComment().get(j);
				if (comment != null ) {
					comment.setSns(Const.SNS_RENREN);
					comment.setCommetedfeedID(entry.getPost_id());
					zDBHelper.fInsertComments(comment);
				}
			}
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
	
	public void fSaveNewFeeds(ResponseList<twitter4j.Status> statuses,
			Context context) {
		long res = 0;
		
		for(twitter4j.Status status : statuses)
		{
			res += zDBHelper.fInsertFeed(status);
			
			UserFriend friend = new UserFriend();
			friend.setId(status.getUser().getId()+"");
			friend.setSNS(Const.SNS_TWITTER);
			friend.setName(status.getUser().getName());
			friend.setHeadurl(status.getUser().getProfileImageURL().toString());
			zDBHelper.fInsertFriend(friend);
		}
		
		if (res > 0 ) {
			int cntUnReadFeed = fGetUnReadNewsFeedSummary(Const.SNS_TWITTER).length;
			fShowNotification(Const.SNS_TWITTER, cntUnReadFeed, context);
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
	
	public ArrayList<FeedEntry> fGetUnReadNewsFeed(String sns) {
		String[][] feeds = null;
		String[][] owners = null;
		ArrayList<FeedEntry> items = new ArrayList<FeedEntry>();
		
		feeds = zDBHelper.fGetFeedPreview(sns);
		
		if (feeds == null || feeds.length == 0) {
			//feeds = new String[][] {{"No Unread Feed in Local"}};
			FeedEntry item = new FeedEntry();
			item.setsName("No Unread Feed in Local");
			items.add(item);
		}
		else {
			for (int i = 0; i < feeds.length; i++) {
				
				FeedEntry item = new FeedEntry();
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

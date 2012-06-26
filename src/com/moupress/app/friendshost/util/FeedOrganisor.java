package com.moupress.app.friendshost.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.ResponseList;
import weibo4andriod.Status;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.FriendsHostActivity;
import com.moupress.app.friendshost.PubSub;
import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.sns.FeedEntry;
import com.moupress.app.friendshost.sns.FeedEntryComment;
import com.moupress.app.friendshost.sns.UserFriend;
import com.moupress.app.friendshost.sns.Renren.FeedExtractResponseBean;
import com.moupress.app.friendshost.sns.Renren.RenrenFeedElementComments.RenrenFeedElementComment;
import com.moupress.app.friendshost.sns.Renren.RenrenFeedElementEntry;
import com.moupress.app.friendshost.sns.facebook.FBHomeFeed;
import com.moupress.app.friendshost.sns.facebook.FBHomeFeedEntry;
import com.moupress.app.friendshost.sns.facebook.FBHomeFeedEntryComments.FBFeedEntryComment;

public class FeedOrganisor {
	private Activity zActivity;
	private Context zContext;
	private PubSub zPubSub;
	
	private DBHelper zDBHelper;
	
	private HashMap<String, Integer> hmUnreadFeed;

	public FeedOrganisor(PubSub pubsub) {
		this.zPubSub = pubsub;
		this.zContext = zPubSub.fGetContext();
		this.zActivity = zPubSub.fGetActivity();
		
		zDBHelper = new DBHelper(zContext);
		hmUnreadFeed = new HashMap<String,Integer>();
	}
	
	public FeedOrganisor(Context context) {
		zDBHelper = new DBHelper(context);
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
				String fromHeadUrl = String.format(Const.USER_IMG_URL_FB, fromID);
				//String fromHeadUrl = "https://graph.facebook.com/" + fromID + "/picture";
				entry.getFrom().setHeadurl(fromHeadUrl);
				
				if (entry.getType().equals("photo")) {
					entry.setsPhotoLargeLink(fGetFbRawPicUrl(entry.getPicture()));					
				}
				
				res += zDBHelper.fInsertFeed(entry);
				zDBHelper.fInsertFriend(entry.getFrom());
				
				int cntComments = Integer.parseInt(entry.getComments().getCount());
				// comment get from Facebook only shows the 1st and the last entry
				// need more research here
				if (cntComments > 0 && entry.getComments().getData() != null) {
					if(entry.getComments().getData() != null)
					cntComments = Math.min(cntComments, entry.getComments().getData().size());
				} else {
					cntComments = 0;
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
	
	/*
	 * A temp workaround method to get FB pic_raw_url
	 */
	private String fGetFbRawPicUrl(String picUrl) {
		String picRawUrl = null;
		if ( picUrl != null ) {
			String response = "";
			Bundle mBundle = new Bundle();
			String fbToken = Pref.getMyStringPref(zActivity.getApplicationContext(), "fbToken");
			mBundle.putString("access_token", fbToken);
			String url = "https://graph.facebook.com/" + picUrl.split("_")[1];
			try {
				response = com.facebook.android.Util.openUrl(url, "GET", mBundle);
				JSONObject picSrcResponse = new JSONObject(response);
				picRawUrl = picSrcResponse.getString("source");
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return picRawUrl;
	}
	
	public void fSaveNewFeeds(FeedExtractResponseBean bean, Context context) {

		long res = 0;
		if ( bean == null || bean.getFeedList() == null ) {
			return;
		}
		int i = 0;
		try {
		for( i= 0; i<bean.getFeedList().size();i++) {
			
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
			int j = 0;
			try {
				for ( j = 0; j < cntComments; j++) {
				
					RenrenFeedElementComment comment = entry.getComments().getComment().get(j);
				
				
					if (comment != null ) {
						comment.setSns(Const.SNS_RENREN);
						comment.setCommetedfeedID(entry.getPost_id());
						zDBHelper.fInsertComments(comment);
					}
				}
			} catch (Exception e) {
				String from = entry.getName();
				System.out.println("Error for feed index " + i );
				System.out.println("Error for feed from " + from);
				System.out.println("Error for comment index " + j );
			}
			
		}
		} catch (Exception e) {
			System.out.println("Error for feed index " + i );
			}
		
		if (res > 0 ) {
			int cntUnReadFeed = fGetUnReadNewsFeedSummary(Const.SNS_RENREN).length;
			fShowNotification(Const.SNS_RENREN, cntUnReadFeed, context);
		}
		
	}
	
	public void fSaveNewFeeds(List<Status> friendsTimeline, Context context) {
		long res = 0;
		
		if (friendsTimeline == null) {
			return;
		}
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
		
		if (statuses == null) {
			return;
		}
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
	 * At the moment mark all feed as read as user will always start at position 0 when the start the app
	 */
	public void fUpdateReadFeeds(String sns, String updatedTime) {
		int status = zDBHelper.fUpdateFeedRead(sns, updatedTime);
		if (status == -1) {
			Log.e("FeedOrg", "Error in update feed status to read");
		}
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
		//Date d = new Date();
		//CharSequence currentDateTime  = DateFormat.format("yyyy-MM-dd hh:mm:ss", d.getTime());
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
		//sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		//String updateTime = sdf.format(new Date(););
		String updateTime = zDBHelper.fGetDateFormat().format(new Date());
		ArrayList<FeedEntry> items = fGetUnReadNewsFeed(sns, updateTime);
		fUpdateReadFeeds(sns, updateTime);
		return items;
	}
	
	public ArrayList<FeedEntry> fGet10MoreNewsFeed(String sns) {
		String lastItemUpdatedTime = Pref.getMyStringPref(zContext, sns);
		ArrayList<FeedEntry> items = fGetUnReadNewsFeed(sns, lastItemUpdatedTime);
		return items;
	}
	
	private ArrayList<FeedEntry> fGetUnReadNewsFeed(String sns, String updatedTime) {
		String[][] feeds = null;
		ArrayList<FeedEntry> items = new ArrayList<FeedEntry>();
		
		feeds = zDBHelper.fGetFeedPreview(sns, updatedTime);
		
		if (feeds == null || feeds.length == 0) {
			FeedEntry item = new FeedEntry();
			item.setsName("No Unread Feed in Local");
			items.add(item);
		}
		else {
			items = transformDB2Feed(sns, feeds);
		}
		return items;
	}
	
	public FeedEntry fGetFeedByID(String sns, String feed_id) {
		
		String[][] feeds = null;
		feeds = zDBHelper.fGetFeedByID(sns, feed_id);
		ArrayList<FeedEntry> items = transformDB2Feed(sns, feeds);
		
		return items.get(0);
	}
	
	private ArrayList<FeedEntry> transformDB2Feed(String sns, String[][] feeds) {
		String[][] owners = null;
		String[][] comments = null;
		
		ArrayList<FeedEntry> items = new ArrayList<FeedEntry>();
		
		if (feeds == null || feeds.length == 0) {
			//feeds = new String[][] {{"No Unread Feed in Local"}};
			FeedEntry item = new FeedEntry();
			item.setsName("No Unread Feed in Local");
			items.add(item);
		}
		else {
			for (int i = 0; i < feeds.length; i++) {
				int index = 0;
				FeedEntry item = new FeedEntry();
				//feedAdapter.addItem(feed[i]);
				item.setsID(feeds[i][index++]);
				item.setsName(feeds[i][index++]);							//name
				item.setsOwnerID(feeds[i][index++]);						//feed owner id
				item.setsCreatedTime(feeds[i][index++]);					//created time
				item.setsFeedType(feeds[i][index++]);
				item.setsMsgBody(feeds[i][index++]);						//message
				item.setsStory(feeds[i][index++]);						//story
				item.setsLink(feeds[i][index++]);						//links
				//item.setsStory_tags(feeds[i][4]);					//story_tags
				item.setsPhotoPreviewLink(feeds[i][index++]);				//pic url
				item.setsPhotoPreviewName(feeds[i][index++]);				//pic/album name
				item.setsPhotoPreviewCaption(feeds[i][index++]);			//pic/album caption
				item.setsPhotoPreviewDescription(feeds[i][index++]);		//pic/album description
				item.setsCntLikes(feeds[i][index++]);
				item.setsPhotoLargeLink(feeds[i][index++]);
				
				String feed_OwnerID = feeds[i][2];
				owners = zDBHelper.fGetFeedOwner(sns, feed_OwnerID); //there should be only 1 owner for each feed
				if (owners.length > 0) {
					index = 0;
					item.getzFriend().setName(owners[0][index++]);
					item.getzFriend().setHeadurl(owners[0][index++]);
				}
				
				String feed_id = feeds[i][0];
				comments = zDBHelper.fGetFeedComments(sns, feed_id);
				if (comments != null) {
					for (int j = 0; j < comments.length; j++) {
						index = 0;
						FeedEntryComment comment = new FeedEntryComment();
						comment.setCommentedID(comments[j][index++]);
						comment.setCommentedUserID(comments[j][index++]);
						comment.setCommentedName(comments[j][index++]);
						comment.setCommentedHeadUrl(comments[j][index++]);
						comment.setCommentedMsg(comments[j][index++]);
						comment.setCommentedTime(comments[j][index++]);
						item.getzComments().add(comment);
					}
				}
				
				items.add(item);
			}
		}
		return items;
	}
	
	private void fShowNotification(String sFromSNS, int NumUnRead, Context context) {
		// update unread feed maps
		hmUnreadFeed.put(sFromSNS, NumUnRead);
		
		// create notification manager
		NotificationManager notificationMgr = (NotificationManager) zActivity.getSystemService(Context.NOTIFICATION_SERVICE);
		
		// create notification
		CharSequence tickerText = "Unread Feed From " + sFromSNS;
		long when = System.currentTimeMillis();
		Notification notification = new Notification(R.drawable.ic_launcher, tickerText, when);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		// preparation
		//Context context = zActivity.getApplicationContext();
		CharSequence contentTitle = "New Feeds";
		CharSequence contentText = "Unread updates ";
		for ( String key: hmUnreadFeed.keySet()) {
			int iUnreadFeed = hmUnreadFeed.get(key);
			if ( iUnreadFeed > 0) {
				contentText = contentText + " " + key + ":" + iUnreadFeed + " ";
			}
		}
		Intent notificationIntent = new Intent(context, FriendsHostActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString(Const.ACTION_DISPLAYFEED, sFromSNS);
		notificationIntent.putExtras(bundle);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		
		// notify
		notificationMgr.notify(Const.FRIENDSHOST_NOTIFY_ID, notification);
	}

}

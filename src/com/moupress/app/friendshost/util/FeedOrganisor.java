package com.moupress.app.friendshost.util;

import com.google.gson.Gson;
import com.moupress.app.friendshost.PubSub;
import com.moupress.app.friendshost.sns.Renren.RenenFeedElement;
import com.moupress.app.friendshost.sns.Renren.FeedExtractResponseBean;
import com.moupress.app.friendshost.sns.facebook.FBHomeFeed;
import com.moupress.app.friendshost.sns.facebook.FBHomeFeedEntry;

import android.app.Activity;
import android.content.Context;

public class FeedOrganisor {
	private Activity zActivity;
	private Context zContext;
	private PubSub zPubSub;
	
	private DBHelper zDBHelper;

	public FeedOrganisor(PubSub pubsub) {
		this.zPubSub = pubsub;
		this.zContext = zPubSub.fGetContext();
		
		zDBHelper = new DBHelper(zContext);
	}
	
	/**
	 * Sort incoming feeds by column requirement
	 */
	public void fFilterFeeds() {
		
	}
	
	/**
	 * Save new feeds from fGetNewsFeed() into DB
	 */
	public void fSaveNewFeeds(String feed) {
		FBHomeFeed bean = new Gson().fromJson(feed, FBHomeFeed.class);
		
		for(int i= 0; i<bean.getData().size();i++) {
			//String msg = ((FBHomeFeedEntry) bean.getData().get(i)).getName()+" : "+((FBHomeFeedEntry) bean.getData().get(i)).getMessage();
			FBHomeFeedEntry entry = (FBHomeFeedEntry) bean.getData().get(i);
			zDBHelper.fInsertFeed(entry);
		}
	}
	
	public void fSaveNewFeeds(FeedExtractResponseBean bean) {

		for(int i= 0; i<bean.getFeedList().size();i++) {
			//String msg = ((FBHomeFeedEntry) bean.getData().get(i)).getName()+" : "+((FBHomeFeedEntry) bean.getData().get(i)).getMessage();
			RenenFeedElement entry = (RenenFeedElement) bean.getFeedList().get(i);
			zDBHelper.fInsertFeed(entry);
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

	public String[] fGetUnReadNewsFeed(String sns) {
		String[] result = zDBHelper.fGetFeedSummary(sns);
		if (result == null || result.length == 0) {
			result = new String[] {"No Unread Feed in Local"};
		}
		return result;
	}

}

package com.moupress.app.friendshost.util;

import com.google.gson.Gson;
import com.moupress.app.friendshost.sns.facebook.FBHomeFeed;
import com.moupress.app.friendshost.sns.facebook.FBHomeFeedEntry;

import android.app.Activity;
import android.content.Context;

public class FeedOrganisor {
	private Activity zActivity;
	private Context zContext;

	public FeedOrganisor(Activity activity, Context context) {
		this.zActivity = activity;
		this.zContext = context;
	}
	
	/**
	 * Sort incoming feeds by column requirement
	 */
	public void fFilterFeeds() {
		
	}
	
	/**
	 * Save new feeds from fGetNewsFeed() into DB
	 */
	public static void fSaveNewFeeds(String feed) {
		FBHomeFeed bean = new Gson().fromJson(feed, FBHomeFeed.class);
		
		for(int i= 0; i<bean.getData().size();i++) {
			//String msg = ((FBHomeFeedEntry) bean.getData().get(i)).getName()+" : "+((FBHomeFeedEntry) bean.getData().get(i)).getMessage();
			FBHomeFeedEntry entry = (FBHomeFeedEntry) bean.getData().get(i);
			DBHelper.fInsertFeed(entry);
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
}

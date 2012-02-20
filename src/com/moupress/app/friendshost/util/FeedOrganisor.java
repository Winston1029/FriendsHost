package com.moupress.app.friendshost.util;

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
	public void fSaveNewFeeds() {
		
	}
	
	/**
	 * Mark feeds that has been read
	 */
	public void fUpdateReadFeeds() {
		
	}
}

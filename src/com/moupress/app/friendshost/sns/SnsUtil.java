package com.moupress.app.friendshost.sns;

import java.util.ArrayList;

import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.activity.LstViewFeedAdapter;
import com.moupress.app.friendshost.PubSub;
import com.moupress.app.friendshost.R;

import android.app.Activity;
import android.content.Context;

public abstract class SnsUtil {
 
	
	protected PubSub zPubSub;
	protected Context zContext;
	protected Activity zActivity;
	
	protected String SnsName;
	
	public SnsUtil(PubSub pubsub,String SnsName)
	{
		this.zPubSub = pubsub;
		this.zContext = pubsub.fGetContext();
		this.zActivity = pubsub.fGetActivity();
		this.SnsName = SnsName;
		this.FeedAdapter = new LstViewFeedAdapter(zActivity, R.layout.feed_item_preview);
		
	}
	
	protected boolean Selected = true;
	public boolean isSelected() {
		return Selected;
	}
	
	protected LstViewFeedAdapter FeedAdapter;
	public LstViewFeedAdapter getFeedAdapter() {
		return FeedAdapter;
	}
	
	public void InitFeedAdapter(){};
	public void fResend(FeedEntry feed){};
	protected boolean isSessionValid(){return false;};
	public void fGetNewsFeed(Context context){};
	public void fDisplayFeed(){};
	public void fPublishFeeds(String message){};
	
	
	//===============Adapter Related Functions Begin=================
	public void CleanAdapter()
	{
		FeedAdapter.clear();
	}
	public void LoadAdapter()
	{
		ArrayList<FeedEntry> feeds = zPubSub.fGetFeedOrganisor().fGetUnReadNewsFeed(this.SnsName);
		for (FeedEntry item : feeds ) {
			FeedAdapter.addItem(item);
		}
	}
	public void AdapterNotifcation()
	{
		FeedAdapter.notifyDataSetChanged();
	}
	
	public void RefreshAdapter()
	{
		this.CleanAdapter();
		this.LoadAdapter();
		this.AdapterNotifcation();
	}
	//===============Adapter Related Functions End=================
}

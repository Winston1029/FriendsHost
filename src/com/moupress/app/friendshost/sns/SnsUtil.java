package com.moupress.app.friendshost.sns;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.moupress.app.friendshost.PubSub;
import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.activity.LstViewFeedAdapter;
import com.moupress.app.friendshost.sns.Listener.SnsEventListener;
import com.moupress.app.friendshost.util.FlurryUtil;
import com.moupress.app.friendshost.util.Pref;

public abstract class SnsUtil {
	public static final String TAG = "SnsUtil";
	
	protected PubSub zPubSub;
	protected Context zContext;
	protected Activity zActivity;
	
	protected String SnsName;
	protected int logImg = -1;
	
	public int GetLogImg() {
		return logImg;
	}

	public SnsUtil(PubSub pubsub,String SnsName)
	{
		this.zPubSub = pubsub;
		this.zContext = pubsub.fGetContext();
		this.zActivity = pubsub.fGetActivity();
		this.SnsName = SnsName;
		
		bSelectToPublish = false;
		//Update IsSelected Variable
		this.GetSelectedPref();
		
		this.FeedAdapter = new LstViewFeedAdapter(zActivity, R.layout.feed_item_preview, this.SnsName);
	}
	
	//publish intention for each SNS
	protected boolean bSelectToPublish;
	public boolean fIsSelectedToPublish() {
		return bSelectToPublish;
	}
	public void fToogleSelectToPublish() {
		bSelectToPublish = !bSelectToPublish;
	}
	public void fUnSelectToPublish() {
		bSelectToPublish = false;
	}
	public void fSelectToPublish() {
		bSelectToPublish = true;
	}
	
	//Login Status for Each SNS
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
	public void fPostComments(Bundle params) {}
	public void fPublishFeeds(Bundle params){};
	public void fUploadPic(String message, String selectedImagePath){};
	public void fLikeFeeds(Bundle params) {}
	public void fUnLikeFeeds(Bundle params) {}
	public void fShareFeeds(Bundle params) {}
	public void fLogout(Bundle params) {}
	
	
	//===============Adapter Related Functions Begin=================
	public void CleanAdapter()
	{
		FeedAdapter.clear();
	}
	public void LoadAdapter()
	{
		ArrayList<FeedEntry> feeds = zPubSub.fGetFeedOrganisor().fGetUnReadNewsFeed(this.SnsName);
		FeedEntry lastItem = null;
		for (FeedEntry item : feeds ) {
			FeedAdapter.addItem(item);
			lastItem = item;
		}
		fSaveLastLoadedFeed(lastItem);
	}
	
	public void LoadAdapter10MoreFeed()
	{
		ArrayList<FeedEntry> feeds = zPubSub.fGetFeedOrganisor().fGet10MoreNewsFeed(this.SnsName);
		FeedEntry lastItem = null;
		for (FeedEntry item : feeds ) {
			if (item.getsName().equals("No Unread Feed in Local")) {
				return;
			}
			FeedAdapter.addItem(item);
			lastItem = item;
		}
		// FlurryUtil
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
		FlurryUtil.logEvent(TAG + ":LoadAdapter10MoreFeed", SnsName + "," + FeedAdapter.getCount());
		fSaveLastLoadedFeed(lastItem);
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
	
	public void RefreshAdapter10MoreFeed()
	{
		//this.CleanAdapter();
		this.LoadAdapter10MoreFeed();
		this.AdapterNotifcation();
	}
	
	
	private void fSaveLastLoadedFeed(FeedEntry lastItem) {
		// TODO Auto-generated method stub
		Pref.setMyStringPref(zContext, SnsName, lastItem.getsCreatedTime());
	}
	
	//===============Adapter Related Functions End=================

	public void GetSelectedPref()
	{
		this.Selected =	Pref.getMyBoolPref(zContext, this.SnsName+"_Selected");
	}
	
	public void SetSelectedPref(boolean Selected)
	{
		this.Selected = Selected;
		Pref.setMyBoolPref(zContext, this.SnsName+"_Selected", this.Selected);
	}
	
	
	public void UnSelectSNS(SnsEventListener snsUpdateListener)
	{
		this.SetSelectedPref(false);
		
		if(snsUpdateListener != null) {
			snsUpdateListener.OnSnsUtilRemoved(this.SnsName);
		}
		
		this.fLogout(null);
	}
	
	
	
	public void SelectSNS(SnsEventListener snsUpdateListener)
	{
		this.fSnsAuth(snsUpdateListener,true);
	}
	
	public void ToggleSelectSNS(SnsEventListener snsUpdateListener)
	{
		if(Selected)
		{
			this.UnSelectSNS(snsUpdateListener);
		}
		else
		{
			this.SelectSNS(snsUpdateListener);
		}
	}
	
	//Sns Authentication method
	protected abstract void fSnsAuth(SnsEventListener snsEventListener, boolean uptPref);
	
	
	protected void SnsAddEventCallback(SnsEventListener snsEventListener, boolean uptPref)
	{
		if(uptPref)
		{
			this.SetSelectedPref(true);
			
			if(snsEventListener != null)
				snsEventListener.OnSnsUtilAdded(this.SnsName);
			
			
		}
	}

	public void DisplayFeedDtl(int position) {

		FeedEntry feed = (FeedEntry) this.getFeedAdapter().getItem(position);
		zPubSub.fFeedDisplayDetailUI(feed, this.SnsName);
	}

	
	//===============Use Selection Setting Functions Start==============
}

package com.moupress.app.friendshost.sns;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.PubSub;
import com.moupress.app.friendshost.sns.Renren.RenrenUtil;
import com.moupress.app.friendshost.sns.facebook.FacebookUtil;
import com.moupress.app.friendshost.sns.sina.SinaUtil;
import com.moupress.app.friendshost.sns.twitter.TwitterUtil;
import com.moupress.app.friendshost.util.FlurryUtil;

public class SnsOrg {

	private static final String TAG = "SnsOrg";
	//private SharedPreferences prefs;
	private Activity zActivity;
	private Context zContext;
	private PubSub zPubSub;
	
	//SnsUtil
	private FacebookUtil zFacebookUtil;
	private TwitterUtil zTwitterUtil;
	private RenrenUtil zRenrenUtil;
	private SinaUtil zSinaUtil;
	
	//Social Networks Names
	//private String[] SnsNames = {Const.SNS_FACEBOOK, Const.SNS_RENREN, Const.SNS_SINA, Const.SNS_TWITTER};
	
	public SnsOrg (PubSub pubsub)
	{
		
		this.zActivity = pubsub.fGetActivity();
		this.zContext = pubsub.fGetContext();
		this.zPubSub = pubsub;
		//this.prefs = PreferenceManager.getDefaultSharedPreferences(zActivity);
		this.InitSns();
	}
	
	public ArrayList<CharSequence> GetSignOnSnsNames()
	{
		ArrayList<CharSequence> signOnSnsNames = new ArrayList<CharSequence>();
		signOnSnsNames.clear();
		String flurry_sns_signedon="";
		for(int i=0 ; i < Const.SNSGROUPS.length; i++)
		{
			if(this.GetSnsInstance(Const.SNSGROUPS[i]).isSelected())
			{
				signOnSnsNames.add(Const.SNSGROUPS[i]);
				flurry_sns_signedon += Const.SNSGROUPS[i] + ",";
			}
		}
		if (flurry_sns_signedon.length() > 0) {
			FlurryUtil.logEvent(TAG + ":GetSignOnSnsNames", flurry_sns_signedon);
		}
		
		return signOnSnsNames;
	}
	
	
	public SnsUtil GetSnsInstance(String SnsName)
	{
		SnsUtil snsUtil = null;
		
		if(zPubSub != null)
		{
			if(SnsName.equals(Const.SNS_FACEBOOK))
			{
				if(this.zFacebookUtil == null) this.zFacebookUtil = new FacebookUtil(this.zPubSub);
				snsUtil = this.zFacebookUtil;
			}
			else if(SnsName.equals(Const.SNS_TWITTER))
			{
				if(this.zTwitterUtil == null) this.zTwitterUtil = new TwitterUtil(this.zPubSub);
				snsUtil = this.zTwitterUtil;
			}
			else if(SnsName.equals(Const.SNS_RENREN))
			{
				if(this.zRenrenUtil == null) this.zRenrenUtil = new RenrenUtil(this.zPubSub);
				snsUtil = this.zRenrenUtil;
			}
			else if(SnsName.equals(Const.SNS_SINA))
			{
				if(this.zSinaUtil == null) this.zSinaUtil = new SinaUtil(this.zPubSub);
				snsUtil = this.zSinaUtil;
			}
		}
		
		return snsUtil;
	}
	
	
	public void InitSns()
	{
		for(int i=0; i< Const.SNSGROUPS.length; i++)
		{
			this.GetSnsInstance(Const.SNSGROUPS[i]);
		}
	}
	
	public void SnsGetNewFeed(Context ctx)
	{
		for(int i=0; i< Const.SNSGROUPS.length; i++) 
		{
			Log.v(TAG,this.GetSnsInstance(Const.SNSGROUPS[i]).SnsName + " "+this.GetSnsInstance(Const.SNSGROUPS[i]).isSessionValid());
			
			if(this.GetSnsInstance(Const.SNSGROUPS[i]).isSessionValid() && this.GetSnsInstance(Const.SNSGROUPS[i]).isSelected())
			{
				this.GetSnsInstance(Const.SNSGROUPS[i]).fGetNewsFeed(ctx);
			}
		}
	}
	
	public boolean SnsPublishNewFeed(Bundle params) {
		boolean bPublished = false;
		String flurry_sns_topublish="";
		for(int i=0; i< Const.SNSGROUPS.length; i++)  {
			if(this.GetSnsInstance(Const.SNSGROUPS[i]).isSessionValid() && this.GetSnsInstance(Const.SNSGROUPS[i]).fIsSelectedToPublish()) {
				this.GetSnsInstance(Const.SNSGROUPS[i]).fPublishFeeds(params);
				flurry_sns_topublish += Const.SNSGROUPS[i] + ":" + params.getString(Const.SMSGBODY).length() + ",";
				bPublished = true;
			}
		}
		//FlurryUtil
		if (bPublished) {
			FlurryUtil.logEvent(TAG + ":SnsPublishNewFeed", flurry_sns_topublish);
		}
		return bPublished;
	}
	
	public void SnsResetPublishNewFeedSelected() {
		for(int i=0; i< Const.SNSGROUPS.length; i++)  {
			if(this.GetSnsInstance(Const.SNSGROUPS[i]).isSessionValid() && this.GetSnsInstance(Const.SNSGROUPS[i]).fIsSelectedToPublish()) {
				this.GetSnsInstance(Const.SNSGROUPS[i]).fUnSelectToPublish();
			}
		}
	}
	
	public boolean SnsUploadPic(String message, String selectedImagePath) {
		boolean bPublished = false;
		String flurry_sns_topublish="";
		for(int i=0; i< Const.SNSGROUPS.length; i++)  {
			if(this.GetSnsInstance(Const.SNSGROUPS[i]).isSessionValid() && this.GetSnsInstance(Const.SNSGROUPS[i]).fIsSelectedToPublish()) {
				this.GetSnsInstance(Const.SNSGROUPS[i]).fUploadPic(message, selectedImagePath);
				flurry_sns_topublish += Const.SNSGROUPS[i] + ":" + message.length() + ",";
				bPublished = true;
			}
		}
		//FlurryUtil
		if (bPublished) {
			FlurryUtil.logEvent(TAG + ":SnsPublishNewFeed", flurry_sns_topublish);
		}
		return bPublished;
	}
	
	
	
}

package com.moupress.app.friendshost.sns;

import java.util.ArrayList;

import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.PubSub;
import com.moupress.app.friendshost.sns.Renren.RenrenUtil;
import com.moupress.app.friendshost.sns.facebook.FacebookUtil;
import com.moupress.app.friendshost.sns.sina.SinaUtil;
import com.moupress.app.friendshost.sns.twitter.TwitterUtil;

import android.app.Activity;
import android.content.Context;

public class SnsOrg {

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
		
		for(int i=0 ; i < Const.SNSGROUPS.length; i++)
		{
			if(this.GetSnsInstance(Const.SNSGROUPS[i]).isSelected())
			{
				signOnSnsNames.add(Const.SNSGROUPS[i]);
			}
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
			if(this.GetSnsInstance(Const.SNSGROUPS[i]).isSessionValid() && this.GetSnsInstance(Const.SNSGROUPS[i]).isSelected())
			{
				this.GetSnsInstance(Const.SNSGROUPS[i]).fGetNewsFeed(ctx);
			}
		}
	}
	
}

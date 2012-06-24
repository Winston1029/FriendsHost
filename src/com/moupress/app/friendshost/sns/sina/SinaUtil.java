package com.moupress.app.friendshost.sns.sina;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import weibo4andriod.AsyncWeibo;
import weibo4andriod.Status;
import weibo4andriod.Weibo;
import weibo4andriod.WeiboException;
import weibo4andriod.http.AccessToken;
import weibo4andriod.http.RequestToken;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.OAuthActivity;
import com.moupress.app.friendshost.PubSub;
import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.activity.LstViewFeedAdapter;
import com.moupress.app.friendshost.sns.FeedEntry;
import com.moupress.app.friendshost.sns.SnsUtil;
import com.moupress.app.friendshost.sns.Listener.SnsEventListener;
import com.moupress.app.friendshost.util.NotificationTask;
import com.moupress.app.friendshost.util.Pref;

public class SinaUtil extends SnsUtil{

	private static final String TAG = "SinaUtil";
    private static String sTokenKey = "";
    private static String sTokenSecret = "";
	//friendshost
    //private static final String CONSUMER_KEY = "1255140182";
	//private static final String CONSUMER_SECRET= "ace86405a2aea9d30c5986d5465e163f";
    //melonfriends
    private static final String CONSUMER_KEY = "194048236";
	private static final String CONSUMER_SECRET= "f224d6f3ee63132ab16459b59dfc9bdf";
	
	//private PubSub zPubSub;
	//private Context zContext;
	
	private Weibo zSina;
	
	private NotificationTask notificationTask;
	
	public SinaUtil(PubSub pubSub) {
		super(pubSub,Const.SNS_SINA);
		//zPubSub = pubSub;
		System.setProperty("weibo4j.oauth.consumerKey", CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", CONSUMER_SECRET);
		zSina = OAuthConstant.getInstance().getWeibo();
		//zContext = pubSub.fGetContext();
		//fSinaAuth();
		this.logImg  = R.drawable.fh_sina_log;
	}
	
	@Override
	public boolean isSessionValid() {
		if ( zSina != null ) {
			try {
				return zSina.test();
			} catch (WeiboException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}
	
	//Variable Passing to Uri Call Back Listener
	
	private SnsEventListener snsEventListener = null;
	private boolean uptPref = false;
	
	@Override
	protected void fSnsAuth(SnsEventListener snsEventListener, boolean uptPref) {
		this.snsEventListener = snsEventListener;
		this.uptPref = uptPref;
		
		sTokenKey = Pref.getMyStringPref(zPubSub.fGetContext().getApplicationContext(), Const.SP_SINA_TOKENKEY);
		sTokenSecret = Pref.getMyStringPref(zPubSub.fGetContext().getApplicationContext(), Const.SP_SINA_TOKENSECRET);
		
		if ( sTokenKey.length() > 0 && sTokenSecret.length() > 0) {
			this.SnsAddEventCallback(snsEventListener, uptPref);
			return;
		}
		try {
			RequestToken requestToken =zSina.getOAuthRequestToken("weibo4andriod://OAuthActivity");
			Uri uri = Uri.parse(requestToken.getAuthenticationURL()+ "&from=xweibo");
			OAuthConstant.getInstance().setRequestToken(requestToken);
			zPubSub.fGetActivity().startActivity(new Intent(Intent.ACTION_VIEW, uri));
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void fGetNewsFeed(final Context context) {
		sTokenKey = Pref.getMyStringPref(zPubSub.fGetContext().getApplicationContext(), Const.SP_SINA_TOKENKEY);
		sTokenSecret = Pref.getMyStringPref(zPubSub.fGetContext().getApplicationContext(), Const.SP_SINA_TOKENSECRET);
		zSina.setToken(sTokenKey, sTokenSecret);
		if (isSessionValid()) {
			try {
				List<Status> friendsTimeline = zSina.getFriendsTimeline();
				//System.out.println("Sina news feed get listener on complete");
				Log.i(TAG, "Sing news feed get listener on complete");
				zPubSub.fGetFeedOrganisor().fSaveNewFeeds(friendsTimeline, context);
			} catch (WeiboException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	//public void fDisplaySinaFeed() {
//	@Override
//	public void fDisplayFeed(){
//		zPubSub.fGetActivity().runOnUiThread(new Runnable() {
//			public void run() {
//				
//				LstViewFeedAdapter feedAdapter = zPubSub.fGetAdapterFeedPreview();
//				feedAdapter.clear();
//				ArrayList<FeedEntry> feeds = zPubSub.fGetFeedOrganisor().fGetUnReadNewsFeed(Const.SNS_SINA);
//				for (FeedEntry item : feeds ) {
//					feedAdapter.addItem(item);
//				}
//				feedAdapter.notifyDataSetChanged();
//			}
//		});
//	}
	
	public void fPostComments(Bundle params) {
		if (zSina != null) {
			try {
				zSina.updateComment(params.getString(Const.COMMENTED_MSG), params.getString(Const.SFEEDID), null);
			} catch (WeiboException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	@Override
	public void fPublishFeeds(Bundle params) {
		if (isSessionValid()) {
			try {
				startNotification(7, "Feed");
				zSina.updateStatus(params.getString(Const.SMSGBODY));
			} catch (WeiboException e) {
				e.printStackTrace();
			} finally {
				stopNotification();
			}
		}
		
	}
	
	public void fUploadPic(String message, String selectedImagePath) {
		if (isSessionValid()) {
			try {
				zSina.uploadStatus(message, new File(selectedImagePath));
			} catch (WeiboException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void fResend(FeedEntry feed) {
		//this.fPublishFeeds(feed.getsMsgBody());
	}
	
	private void startNotification(int notificationId,String fileType)
    {
    	notificationTask = new NotificationTask(this.zContext);
		 if(notificationTask != null)
		{
			notificationTask.SetNotificationTask(notificationId, "Sina", fileType);
			notificationTask.execute(0);
		}
    }
    
    private void stopNotification()
    {
    	if(notificationTask != null)
		{
			notificationTask.setTaskDone(true);
		}
    }
	
    public void CallBackTrigger(Uri uri)
    {
    	try {
		    RequestToken requestToken= OAuthConstant.getInstance().getRequestToken();
			AccessToken accessToken=requestToken.getAccessToken(uri.getQueryParameter("oauth_verifier"));
			OAuthConstant.getInstance().setAccessToken(accessToken);
			Pref.setMyStringPref(this.zContext, Const.SP_SINA_TOKENKEY, accessToken.getToken());
			Pref.setMyStringPref(this.zContext, Const.SP_SINA_TOKENSECRET, accessToken.getTokenSecret());
			
			this.SnsAddEventCallback(snsEventListener, uptPref);
			
			try {
				String headUrl = zSina.verifyCredentials().getProfileImageURL().toString();
				Pref.setMyStringPref(zContext, Const.LOGIN_HEAD_SINA, headUrl);
			} catch (WeiboException e1) {
				e1.printStackTrace();
			}
	    }
    	catch (WeiboException e) {
			e.printStackTrace();
		}
    }
    
    public void fLikeFeeds(Bundle params) {
    	try {
			zSina.createFavorite(Long.valueOf(params.getString(Const.SFEEDID)));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
	
	public void fUnLikeFeeds(Bundle params) {
		try {
			zSina.destroyFavorite(Long.valueOf(params.getString(Const.SFEEDID)));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
	
	public void fShareFeeds(Bundle params) {
		try {
			zSina.retweetStatus(Long.valueOf(params.getString(Const.SFEEDID)));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
}

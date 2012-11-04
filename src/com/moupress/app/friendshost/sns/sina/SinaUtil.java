package com.moupress.app.friendshost.sns.sina;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.PubSub;
import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.activity.LstViewFeedAdapter;
import com.moupress.app.friendshost.sns.FeedEntry;
import com.moupress.app.friendshost.sns.SnsUtil;
import com.moupress.app.friendshost.sns.Listener.SnsEventListener;
import com.moupress.app.friendshost.util.NotificationTask;
import com.moupress.app.friendshost.util.Pref;
import com.weibo.net.AccessToken;
import com.weibo.net.AsyncWeiboRunner;
import com.weibo.net.DialogError;
import com.weibo.net.Oauth2AccessTokenHeader;
import com.weibo.net.Status;
import com.weibo.net.Utility;
import com.weibo.net.WBStatus;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboDialogListener;
import com.weibo.net.WeiboException;
import com.weibo.net.WeiboParameters;

public class SinaUtil extends SnsUtil{

	private static final String TAG = "SinaUtil";
    //private static String sTokenKey = "";
    //private static String sTokenSecret = "";
	
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
		//zSina = OAuthConstant.getInstance().getWeibo();
    	zSina = Weibo.getInstance();
		//zContext = pubSub.fGetContext();
		//fSinaAuth();
		this.logImg  = R.drawable.fh_sina_logo;
	}
	
	@Override
	public boolean isSessionValid() {
		if ( zSina != null ) {
			//List<Status> retweetbymestatus = zSina.getRetweetedByMe();
			//return true;
			this.setSinaToken();
	        this.setUpSinaClient();
	        
			return zSina.isSessionValid();
		}
		else
		{
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
		
		this.setSinaToken();
        this.setUpSinaClient();
        
//        if (isSessionValid()) {
//			
//			this.SnsAddEventCallback(snsEventListener,uptPref);
//			
//			return;
//		}

		zSina.authorize(this.zActivity, new AuthDialogListener());
	}
	
	private void setUpSinaClient()
	{
		if(zSina == null) zSina = Weibo.getInstance();
		
		Utility.setAuthorization(new Oauth2AccessTokenHeader());
		zSina.setupConsumerConfig(CONSUMER_KEY, CONSUMER_SECRET);
		zSina.setRedirectUrl("http://www.melonsail.com");
	}
	
	@Override
	public void fGetNewsFeed(final Context context) {


			AsyncWeiboRunner.RequestListener listener = new AsyncWeiboRunner.RequestListener(){

				@Override
				public void onComplete(String response) {
					Log.i(TAG, response);
					
					 //Save Feeds
					  WBHomeFeed bean = new Gson().fromJson(response, WBHomeFeed.class);
					  zPubSub.fGetFeedOrganisor().fSaveNewFeeds(bean.getStatuses(), context);
					  
					  //Bulk Fetch Comments
					  fGetBulkComments(bean.getStatuses(),context);
				}

				@Override
				public void onIOException(IOException e) {
					e.printStackTrace();
					Log.i(TAG, e.getMessage());
				}

				@Override
				public void onError(WeiboException e) {
					e.printStackTrace();
					Log.i(TAG, e.getMessage());
				}};
				
				;
		        WeiboParameters bundle = new WeiboParameters();
		        bundle.add("source", Weibo.getAppKey());
				this.sendWeiboRequest(context, listener, "statuses/friends_timeline.json", "GET", bundle,null);
		
	}
	
	private void sendWeiboRequest(final Context context, AsyncWeiboRunner.RequestListener listener, String partUrl, String httpMethod, WeiboParameters params, String fileType)
	{
		this.setSinaToken();
		this.setUpSinaClient();
		if (isSessionValid()) {
			AsyncWeiboRunner asyncWeibo = new AsyncWeiboRunner(zSina);
			String url = Weibo.SERVER + partUrl;
			
			if(fileType != null) this.startNotification(7, fileType);
			
			asyncWeibo.request(context, url, params, httpMethod, listener);
		}
	}
	
	
	public void fGetBulkComments(List<WBStatus> statuses, final Context context)
	{
		
		AsyncWeiboRunner.RequestListener listener = new AsyncWeiboRunner.RequestListener(){

				@Override
				public void onComplete(String response) {
					Log.i(TAG, response);
					
					 //Save Feeds
					  WBHomeFeedEntryComment bean = new Gson().fromJson(response, WBHomeFeedEntryComment.class);
					  zPubSub.fGetFeedOrganisor().fSaveNewComments(bean.getComments(), context);
					
				}

				@Override
				public void onError(WeiboException e) {
					Log.i(TAG, e.getMessage());
					e.printStackTrace();
				}

				@Override
				public void onIOException(IOException e) {
					Log.i(TAG, e.getMessage());
					e.printStackTrace();
				}};
		
			for(WBStatus status : statuses)
			{
	          WeiboParameters bundle = new WeiboParameters();
	          bundle.add("source", Weibo.getAppKey());
	          bundle.add("id", status.getId().toString());
			  this.sendWeiboRequest(context, listener, "comments/show.json", "GET", bundle, null);
			}
	}
	
	@Override
	public void fPostComments(Bundle params, final Context context) {


	AsyncWeiboRunner.RequestListener listener = new AsyncWeiboRunner.RequestListener(){

					@Override
					public void onComplete(String response) {
						// TODO Auto-generated method stub
						Log.i(TAG, response);
						stopNotification();
					}

					@Override
					public void onError(WeiboException e) {
						e.printStackTrace();
						Log.i(TAG, e.getMessage());
						Toast.makeText(context, e.getMessage(), 0);
					}

					@Override
					public void onIOException(IOException e) {
						e.printStackTrace();
						Log.i(TAG, e.getMessage());
						Toast.makeText(context, "Comments Update Failed. Please reauthenticate Weibo", 0);
					}};
					
					 WeiboParameters bundle = new WeiboParameters();
					 bundle.add("source", Weibo.getAppKey());
					 bundle.add("comment", params.getString(Const.COMMENTED_MSG));
					 bundle.add("id", params.getString(Const.SFEEDID));
			    
					 this.sendWeiboRequest(context, listener, "comments/create.json", "POST", bundle, "Comment");
					 
	}
	
	
	@Override
	public void fPublishFeeds(Bundle params, final Context context) {
		
		
		AsyncWeiboRunner.RequestListener listener = new AsyncWeiboRunner.RequestListener(){

					@Override
					public void onComplete(String response) {
						// TODO Auto-generated method stub
						Log.i(TAG, response);
						stopNotification();
					}

					@Override
					public void onError(WeiboException e) {
						e.printStackTrace();
						Log.i(TAG, e.getMessage());
						Toast.makeText(context, e.getMessage(), 0);
					}

					@Override
					public void onIOException(IOException e) {
						e.printStackTrace();
						Log.i(TAG, e.getMessage());
						Toast.makeText(context, "Status Update Failed. Please reauthenticate Weibo", 0);
					}};
					
					 WeiboParameters bundle = new WeiboParameters();
					 bundle.add("source", Weibo.getAppKey());
					 bundle.add("status", params.getString(Const.SMSGBODY));
					 this.sendWeiboRequest(context, listener, "statuses/update.json", "POST", bundle, "Feed");
	}
	
	
	@Override
	public void fUploadPic(String message, String selectedImagePath, final Context context) {
	
           AsyncWeiboRunner.RequestListener listener = new AsyncWeiboRunner.RequestListener(){

					@Override
					public void onComplete(String response) {
						// TODO Auto-generated method stub
						Log.i(TAG, response);
						stopNotification();
					}

					@Override
					public void onError(WeiboException e) {
						e.printStackTrace();
						Log.i(TAG, e.getMessage());
						Toast.makeText(context, "Picture Upload Failed. Please reauthenticate Weibo", 0);
					}

					@Override
					public void onIOException(IOException e) {
						e.printStackTrace();
						Log.i(TAG, e.getMessage());
						Toast.makeText(context, "Picture Upload Failed. Please reauthenticate Weibo", 0);
					}};
					
					  WeiboParameters bundle = new WeiboParameters();
					  bundle.add("source", Weibo.getAppKey());
					  bundle.add("status", message);
					  bundle.add("pic", selectedImagePath);
					  this.sendWeiboRequest(context, listener, "statuses/upload.json", "POST", bundle, "Picture");		

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
	
//    public void CallBackTrigger(Uri uri)
//    {
//    	try {
//		    RequestToken requestToken= OAuthConstant.getInstance().getRequestToken();
//			AccessToken accessToken=requestToken.getAccessToken(uri.getQueryParameter("oauth_verifier"));
//			OAuthConstant.getInstance().setAccessToken(accessToken);
//			Pref.setMyStringPref(this.zContext, Const.SP_SINA_TOKENKEY, accessToken.getToken());
//			Pref.setMyStringPref(this.zContext, Const.SP_SINA_TOKENSECRET, accessToken.getTokenSecret());
//			
//			this.SnsAddEventCallback(snsEventListener, uptPref);
//			
//			try {
//				String headUrl = zSina.verifyCredentials().getProfileImageURL().toString();
//				Pref.setMyStringPref(zContext, Const.LOGIN_HEAD_SINA, headUrl);
//			} catch (WeiboException e1) {
//				e1.printStackTrace();
//			}
//	    }
//    	catch (WeiboException e) {
//			e.printStackTrace();
//		}
//    }
    
    @Override
    public void fLikeFeeds(Bundle params, final Context context) {
    	
    	AsyncWeiboRunner.RequestListener listener = new AsyncWeiboRunner.RequestListener(){

			@Override
			public void onComplete(String response) {
				// TODO Auto-generated method stub
				Log.i(TAG, response);
				//stopNotification();
			}

			@Override
			public void onError(WeiboException e) {
				e.printStackTrace();
				Log.i(TAG, e.getMessage());
				Toast.makeText(context, "Feed favoriate error. Please reauthenticate Weibo", 0);
			}

			@Override
			public void onIOException(IOException e) {
				e.printStackTrace();
				Log.i(TAG, e.getMessage());
				Toast.makeText(context, "Feed favoriate error. Please reauthenticate Weibo", 0);
			}};
			
			  WeiboParameters bundle = new WeiboParameters();
			  bundle.add("source", Weibo.getAppKey());
			  bundle.add("id", params.getString(Const.SFEEDID));
			    
			  this.sendWeiboRequest(context, listener, "favorites/create.json", "POST", bundle, null);
    	
//    	try {
//			zSina.createFavorite(Long.valueOf(params.getString(Const.SFEEDID)));
//		} catch (NumberFormatException e) {
//			e.printStackTrace();
//		} catch (WeiboException e) {
//			e.printStackTrace();
//		}
    
	}
    
    @Override
    public void fUnLikeFeeds(Bundle params, final Context context) {
		
	AsyncWeiboRunner.RequestListener listener = new AsyncWeiboRunner.RequestListener(){

			@Override
			public void onComplete(String response) {
				// TODO Auto-generated method stub
				Log.i(TAG, response);
				//stopNotification();
			}

			@Override
			public void onError(WeiboException e) {
				e.printStackTrace();
				Log.i(TAG, e.getMessage());
				Toast.makeText(context, "Feed favoriate destroy error. Please reauthenticate Weibo", 0);
			}

			@Override
			public void onIOException(IOException e) {
				e.printStackTrace();
				Log.i(TAG, e.getMessage());
				Toast.makeText(context, "Feed favoriate destroy error. Please reauthenticate Weibo", 0);
			}};
		
		     WeiboParameters bundle = new WeiboParameters();
		     bundle.add("source", Weibo.getAppKey());
		     bundle.add("id", params.getString(Const.SFEEDID));
			    
			 this.sendWeiboRequest(context, listener, "favorites/destroy.json", "POST", bundle, null);
//		try {
//			zSina.destroyFavorite(Long.valueOf(params.getString(Const.SFEEDID)));
//		} catch (NumberFormatException e) {
//			e.printStackTrace();
//		} catch (WeiboException e) {
//			e.printStackTrace();
//		}
	}
	
    @Override
	public void fShareFeeds(Bundle params, final Context context) {
	
	AsyncWeiboRunner.RequestListener listener = new AsyncWeiboRunner.RequestListener(){

		@Override
		public void onComplete(String response) {
			// TODO Auto-generated method stub
			Log.i(TAG, response);
			stopNotification();
		}

		@Override
		public void onError(WeiboException e) {
			e.printStackTrace();
			Log.i(TAG, e.getMessage());
			Toast.makeText(context, "Feed Share error. Please reauthenticate Weibo", 0);
		}

		@Override
		public void onIOException(IOException e) {
			e.printStackTrace();
			Log.i(TAG, e.getMessage());
			Toast.makeText(context, "Feed Share error. Please reauthenticate Weibo", 0);
		}};
	
	 WeiboParameters bundle = new WeiboParameters();
     bundle.add("source", Weibo.getAppKey());
     bundle.add("id", params.getString(Const.SFEEDID));
     this.sendWeiboRequest(context, listener, "statuses/repost.json", "POST", bundle, "Repost");
//	try {
//		zSina.retweetStatus(Long.valueOf(params.getString(Const.SFEEDID)));
//	} catch (NumberFormatException e) {
//		e.printStackTrace();
//	} catch (WeiboException e) {
//		e.printStackTrace();
//	}
    }
	
	private void setSinaToken()
	{
		String sTokenKey = Pref.getMyStringPref(zPubSub.fGetContext().getApplicationContext(), Const.SP_SINA_TOKENKEY);
		String sTokenSecret = Pref.getMyStringPref(zPubSub.fGetContext().getApplicationContext(), Const.SP_SINA_TOKENSECRET);
		String sTokenExpire = Pref.getMyStringPref(zPubSub.fGetContext().getApplicationContext(), Const.SP_SINA_TOKENEXPIRE);
		
		AccessToken accessToken = new AccessToken(sTokenKey, sTokenSecret);
		
		if(sTokenExpire.length() == 0) sTokenExpire = "0";
		accessToken.setExpiresIn(sTokenExpire);
		
		if(zSina == null) zSina = Weibo.getInstance();
		zSina.setAccessToken(accessToken);
		
	}
	
	private void saveToken2Pref(String tokenKey, String tokenSecret, String expiresIn)
	{
		Pref.setMyStringPref(zPubSub.fGetContext().getApplicationContext(), Const.SP_SINA_TOKENKEY, tokenKey);
		Pref.setMyStringPref(zPubSub.fGetContext().getApplicationContext(), Const.SP_SINA_TOKENSECRET, tokenSecret);
		Pref.setMyStringPref(zPubSub.fGetContext().getApplicationContext(), Const.SP_SINA_TOKENEXPIRE, expiresIn);
	}
	
	class AuthDialogListener implements WeiboDialogListener {

		@Override
		public void onComplete(Bundle values) {
			
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			saveToken2Pref(token,CONSUMER_SECRET , expires_in);
			setSinaToken();
			SnsAddEventCallback(snsEventListener, uptPref);
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(zActivity,
					"Sina Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

		@Override
		public void onError(DialogError e) {
			Toast.makeText(zActivity,
					"Sina Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
			
		}

		@Override
		public void onCancel() {
			Toast.makeText(zActivity, "Sina Auth cancel", Toast.LENGTH_LONG).show();
			
		}
		
	}
}

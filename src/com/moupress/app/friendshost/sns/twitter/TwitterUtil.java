package com.moupress.app.friendshost.sns.twitter;

import java.io.File;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import twitter4j.AccountTotals;
import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.ProfileImage;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterListener;
import twitter4j.TwitterMethod;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.media.ImageUpload;
import twitter4j.media.ImageUploadFactory;
import twitter4j.media.MediaProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.FriendsHostActivity;
import com.moupress.app.friendshost.PubSub;
import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.sns.FeedEntry;
import com.moupress.app.friendshost.sns.SnsUtil;
import com.moupress.app.friendshost.sns.Listener.SnsEventListener;
import com.moupress.app.friendshost.util.NotificationTask;
import com.moupress.app.friendshost.util.Pref;


public class TwitterUtil extends SnsUtil{

	//private SharedPreferences prefs;
	//private Activity zActivity;
	//private Context zContext;
	
	//friendshost
	//private static final String CONSUMER_KEY = "g6dQOeQIPrT7eXpSE7FGQ";
	//private static final String CONSUMER_SECRET= "8MUOI1ShvCyj1IlUWQDlFEfFzSOjNQIugClDGLLUop4";
	//melonfriends
	private static final String CONSUMER_KEY = "XovyfTP04DfpmxPeeRfbg";
	private static final String CONSUMER_SECRET= "ZmJRoKUKkf69QvXd3zGVRhkdl8VT3FsgBp3Ky5JQJ8";
	
    private OAuthConsumer consumer; 
    private OAuthProvider provider;
    //private String msgSend;
	
	//private static enum AUTH_METHODS {GET_FEEDS, UPDATE_STATUS};
	//private AUTH_METHODS authMethod = AUTH_METHODS.GET_FEEDS;
	
	//private PubSub zPubSub;
	private static final String TAG = "TwitterUtil";
	
	private boolean isAuthenticated = false; 
	private AsyncTwitter twitterAsync;
	private Twitter twitter;
	private NotificationTask notificationTask;
	
	public TwitterUtil(PubSub zPubSub) {
		super(zPubSub,Const.SNS_TWITTER);
	    //this.prefs = PreferenceManager.getDefaultSharedPreferences(zActivity);
	    this.logImg = R.drawable.fh_twitter_logo;
	    
	    if(this.isSelected())
	    	fSnsAuth(null, false);
	}

	private TwitterListener listener = new TwitterAdapter() {

		@Override
		public void onException(TwitterException ex, TwitterMethod method) {
			
			if(method == TwitterMethod.ACCOUNT_TOTALS) {
				if(!isAuthenticated) {
					isAuthenticated = true;
					//Authentication();
				}
			}
			else if(method == TwitterMethod.UPDATE_STATUS) {
				Log.i(TAG, "Update Status Exception");
				stopNotification();
			}
			else if(method == TwitterMethod.HOME_TIMELINE)
			{
				Log.i(TAG, "Home Timeline Exception");
			}
		}

		@Override
		public void updatedStatus(Status statuses) {
			
			Log.i(TAG, "Status Published" + statuses.getText());
			//mTwitterHandler.post(mUpdateTwitterNotification);
			stopNotification();
		}

		@Override
		public void gotHomeTimeline(ResponseList<Status> statuses) {
			
			//mTwitterHandler.post(mFetchTwitterNotification);
			//onSnsListener.OnSnsTwitterFeedsLoaded(statuses);
			Log.i(TAG, "Twitters are downloaded! " + statuses.size());
			zPubSub.fGetFeedOrganisor().fSaveNewFeeds(statuses, zContext);
		}
		
		@Override
		public void gotProfileImage(ProfileImage image) {
			String headUrl = image.getURL();
			Pref.setMyStringPref(zContext, Const.LOGIN_HEAD_TWITTER, headUrl);
		}
	 };
	
	public class RetrieveAccessTokenTask extends AsyncTask<Uri, Void, Void> {
		private Context	context;
		private OAuthProvider provider;
		private OAuthConsumer consumer;
		private SharedPreferences prefs;
		
		public RetrieveAccessTokenTask(Context context, OAuthConsumer consumer,OAuthProvider provider, SharedPreferences prefs) {
			this.context = context;
			this.consumer = consumer;
			this.provider = provider;
			this.prefs=prefs;
		}


		/**
		 * Retrieve the oauth_verifier, and store the oauth and oauth_token_secret 
		 * for future API calls.
		 */
		@Override
		protected Void doInBackground(Uri...params) {
			final Uri uri = params[0];
			final String oauth_verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);

			try {
				provider.retrieveAccessToken(consumer, oauth_verifier);

				final Editor edit = prefs.edit();
				edit.putString(OAuth.OAUTH_TOKEN, consumer.getToken());
				edit.putString(OAuth.OAUTH_TOKEN_SECRET, consumer.getTokenSecret());
				edit.commit();
				
				String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
				String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
				
				consumer.setTokenWithSecret(token, secret);
				context.startActivity(new Intent(context,FriendsHostActivity.class));

				//executeAfterAccessTokenRetrieval();
				
			} catch (Exception e) {
			}

			return null;
		}


//		private void executeAfterAccessTokenRetrieval() {
//			Log.i(TAG, "Execute after access token Retrieval");
//			//followActions();
//		}
	}

	public void CallBackTrigger(Uri uri, int requestCode, int resultCode,
			Intent data) {
		//new RetrieveAccessTokenTask(zActivity,consumer,provider,prefs).execute(uri);
		String oauth_verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);
		try {
			provider.retrieveAccessToken(consumer, oauth_verifier);
			this.sTokenKey = consumer.getToken();
			this.sTokenSecret = consumer.getTokenSecret();
			Pref.setMyStringPref(this.zContext, OAuth.OAUTH_TOKEN, this.sTokenKey);
			Pref.setMyStringPref(this.zContext, OAuth.OAUTH_TOKEN_SECRET, this.sTokenSecret);
			
			sTokenKey = Pref.getMyStringPref(zPubSub.fGetContext().getApplicationContext(), OAuth.OAUTH_TOKEN);
			sTokenSecret = Pref.getMyStringPref(zPubSub.fGetContext().getApplicationContext(), OAuth.OAUTH_TOKEN_SECRET);
			
			this.SnsAddEventCallback(snsEventListener, uptPref);
		} catch (OAuthMessageSignerException e) {
			e.printStackTrace();
		} catch (OAuthNotAuthorizedException e) {
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			e.printStackTrace();
		}
	}

	//Variables that passing to call back function
	private SnsEventListener snsEventListener = null;
	private boolean uptPref = false;
	
	private static String sTokenKey = "";
    private static String sTokenSecret = "";
	
	@Override
	protected void fSnsAuth(SnsEventListener snsEventListener, boolean uptPref) {
		
		this.snsEventListener = snsEventListener;
		this.uptPref = uptPref;
		
		sTokenKey = Pref.getMyStringPref(zPubSub.fGetContext().getApplicationContext(), OAuth.OAUTH_TOKEN);
		sTokenSecret = Pref.getMyStringPref(zPubSub.fGetContext().getApplicationContext(), OAuth.OAUTH_TOKEN_SECRET);
		
		if ( sTokenKey.length() > 0 && sTokenSecret.length() > 0) {
			Authentication();
			if ( isSessionValid() ) {
				this.SnsAddEventCallback(snsEventListener, uptPref);
				return;
			}
		}
		try {
    		this.consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
    	    this.provider = new CommonsHttpOAuthProvider(Const.REQUEST_URL,Const.ACCESS_URL,Const.AUTHORIZE_URL);
    	} catch (Exception e) {
    		System.out.println("Erro creating consumer / provider" + e);
		}
    	
    	zActivity.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				new OAuthRequestTokenTask(zActivity,consumer,provider).execute();
			}});
	}
	
	public void Authentication() {
		//super.Autentication(prefs);
//		String token = Pref.getMyStringPref(this.zContext, OAuth.OAUTH_TOKEN);
//		String secret = Pref.getMyStringPref(this.zContext, OAuth.OAUTH_TOKEN_SECRET);
		
		AccessToken a = new AccessToken(sTokenKey,sTokenSecret);
		//Twitter twitter = new TwitterFactory().getInstance();
		//AsyncTwitterFactory factory = new AsyncTwitterFactory(listener);
		AsyncTwitterFactory factory = new AsyncTwitterFactory();
		twitterAsync = factory.getInstance();
		twitterAsync.addListener(listener);
		
		twitterAsync.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		twitterAsync.setOAuthAccessToken(a);
		
		twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		twitter.setOAuthAccessToken(a);
		
//		try {
//			String screenName= twitter.getScreenName();
//			twitterAsync.getProfileImage(screenName, ProfileImage.NORMAL);
//		} catch (IllegalStateException e) {
//			e.printStackTrace();
//		} catch (TwitterException e) {
//			e.printStackTrace();
//		}
		
	}
	
    @Override
	public boolean isSessionValid() {
		//return checkAuthenticated(prefs,false);
    	if (twitter != null ) {
			try {
				return twitter.test();
			} catch (TwitterException e) {
				return false;
			}
		} else {
			return false;
		}
	}

    @Override
	public void fGetNewsFeed(Context applicationContext) {
    	if(twitterAsync == null) {
			 Authentication();
		}			
		twitterAsync.getHomeTimeline();
	}
    
    @Override
    public void fPublishFeeds(Bundle params, final Context context) {
		if(twitterAsync == null) {
			Authentication();
		}
		startNotification(1,"Tweet");
		twitterAsync.updateStatus(params.getString(Const.SMSGBODY));
    }
    
    public void fPostComments(Bundle params) {
		String message = params.getString(Const.COMMENTED_MSG);
		if(twitterAsync == null) {
			Authentication();
		}
		twitterAsync.updateStatus(new StatusUpdate(message)
			.inReplyToStatusId(Long.valueOf(params.getString(Const.SFEEDID))));
	}

	//Upload Photo to Twitter
    public void fUploadPic(String message, String selectedImagePath) {
	 
		 Configuration conf = new ConfigurationBuilder()
	        .setOAuthConsumerKey( CONSUMER_KEY )
	        .setOAuthConsumerSecret( CONSUMER_SECRET )
	        .setOAuthAccessToken(Pref.getMyStringPref(this.zContext, OAuth.OAUTH_TOKEN))
	        .setOAuthAccessTokenSecret(Pref.getMyStringPref(this.zContext, OAuth.OAUTH_TOKEN_SECRET))
	        .build();
		 
		(new UploadPhotoTaskTask(conf,message,selectedImagePath)).execute("");
		 
	}
	 
    private void startNotification(int notificationId,String fileType) {
    	notificationTask = new NotificationTask(this.zContext);
		if(notificationTask != null) {
			notificationTask.SetNotificationTask(notificationId, "Twitter", fileType);
			notificationTask.execute(0);
		}
    }
    
	private void stopNotification() {
		if(notificationTask != null) {
			notificationTask.setTaskDone(true);
		}
	}
	 
	public class UploadPhotoTaskTask extends AsyncTask<String,String,String> {
		private Configuration conf;
		private String message;
		private String ImagePath;
		
		public UploadPhotoTaskTask(Configuration conf, String message, String ImagePath) {
			this.conf = conf;
			this.message = message;
			this.ImagePath = ImagePath;
		}

		
		@Override
		protected void onPostExecute(String result) {
			stopNotification();
		}


		@Override
		protected void onPreExecute() {
			startNotification(2,"Picture");
		}


		@Override
		protected String doInBackground(String... params) {
			try {
				 ImageUpload upload = new ImageUploadFactory(conf).getInstance(MediaProvider.TWITTER);
				 String mediaUrl;
				 mediaUrl = upload.upload(new File(ImagePath),message);
				 Log.i(TAG, "Media URL is "+mediaUrl);
				} catch (TwitterException e) {
					e.printStackTrace();
				}
			return null;
		}
	}
	 
	@Override
	public void fResend(FeedEntry feed) {
		//this.fPublishFeeds(feed.getsMsgBody());
	}
	
	@Override
	public void fLikeFeeds(Bundle params, final Context context) {
		try {
			twitter.createFavorite(Long.valueOf(params.getString(Const.SFEEDID)));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
	
	public void fUnLikeFeeds(Bundle params) {
		try {
			twitter.destroyFavorite(Long.valueOf(params.getString(Const.SFEEDID)));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void fShareFeeds(Bundle params, final Context context) {
		try {
			twitter.retweetStatus(Long.valueOf(params.getString(Const.SFEEDID)));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
	
	
	//public void fDisplayTwitterFeed() {
//  @Override
//  public void fDisplayFeed(){
//		zPubSub.fGetActivity().runOnUiThread(new Runnable() {
//			public void run() {
//
//				LstViewFeedAdapter feedAdapter = zPubSub.fGetAdapterFeedPreview();
//				feedAdapter.clear();
//				ArrayList<FeedEntry> feeds = zPubSub.fGetFeedOrganisor().fGetUnReadNewsFeed(Const.SNS_TWITTER);
//				for (FeedEntry item : feeds ) {
//					feedAdapter.addItem(item);
//				}
//				feedAdapter.notifyDataSetChanged();
//			}
//		});
//	}
	
//	public boolean checkAuthenticated(SharedPreferences prefs,boolean isFollowUp) {
//	
//	Log.i(TAG, "Authentication " + this.isAuthenticated);
//	if(!this.isAuthenticated) {
//		if(twitterAsync == null) {
//			 twitterAsync = Authentication(prefs);
//			 twitterAsync.getAccountTotals();
//		}
//	} else {
//		//Authentication(prefs,null);
//		if(isFollowUp)
//			this.followActions();
//	}
//	return this.isAuthenticated;
//}
	
//	private void setMessage(String msg)
//	{
//		this.msgSend = msg;
//	}
//	
//	private String getTweetMsg() {
//		if(msgSend.length()>0)
//			return msgSend;
//		else return null;
//	}	
//	
//	/**
//	 * Send Tweets to Twitter , this is called after Authentication
//	 * @param prefs
//	 * @param msg
//	 */
//	public void sendTweet(SharedPreferences prefs,String msg)  {
//		
//		if(twitterAsync == null) {
//			 twitterAsync = Authentication(prefs);
//		}
//		
//	    startNotification(1,"Tweet");
//        twitterAsync.updateStatus(msg);
//	}
//	
//	/**
//	 * Get Tweets from Twitter, this is called after Authentication
//	 * @param prefs
//	 */
//	public void getTweets(SharedPreferences prefs)
//	{
//		if(twitterAsync == null) {
//			 twitterAsync = Authentication(prefs);
//		}			
//		twitterAsync.getHomeTimeline();
//	}
	
//	private void followActions() {
//	if(authMethod.equals(AUTH_METHODS.UPDATE_STATUS)) {
//		Log.i(TAG, "Send Tweets!");
//		sendTweet(prefs,getTweetMsg());
//	}
//	else if(authMethod.equals(AUTH_METHODS.GET_FEEDS)) {
//		getTweets(prefs);
//	}
//}
	
	
//	public void SendFeed(String feed) {
//		setMessage(feed);
//		authMethod = AUTH_METHODS.UPDATE_STATUS;
//		checkAuthenticated(prefs,true);;
//	}
	
}

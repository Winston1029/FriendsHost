package com.moupress.app.friendshost.sns.twitter;

import java.io.File;
import java.util.ArrayList;

import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.FriendsHostActivity;
import com.moupress.app.friendshost.LstViewFeedAdapter;
import com.moupress.app.friendshost.PubSub;
import com.moupress.app.friendshost.sns.FeedItem;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import twitter4j.AccountSettings;
import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterListener;
import twitter4j.TwitterMethod;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.media.ImageUpload;
import twitter4j.media.ImageUploadFactory;
import twitter4j.media.MediaProvider;
//import twitter4j.http.AccessToken;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;


public class TwitterUtil {

	private SharedPreferences prefs;
	private Activity activity;
	private Context context;
    private OAuthConsumer consumer; 
    private OAuthProvider provider;
    private String msgSend;
	
	private static enum AUTH_METHODS {GET_FEEDS, UPDATE_STATUS};
	private AUTH_METHODS authMethod = AUTH_METHODS.GET_FEEDS;
	
	private PubSub zPubSub;
	private static final String TAG = "TwitterUtil";
	
	private boolean isAutenticated = false; 
	private AsyncTwitter twitter;
	
	 private TwitterListener listener = new TwitterAdapter()
	 {

		@Override
		public void onException(TwitterException ex, TwitterMethod method) {
			
			
			if(method == TwitterMethod.ACCOUNT_SETTINGS)
			{
				if(!isAutenticated)
				{
					isAutenticated = true;
					Autentication();
				}
			}
			else if(method == TwitterMethod.UPDATE_STATUS)
			{
				Log.i(TAG, "Update Status Exception");
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
		}

		@Override
		public void gotHomeTimeline(ResponseList<Status> statuses) {
			
			//mTwitterHandler.post(mFetchTwitterNotification);
			//onSnsListener.OnSnsTwitterFeedsLoaded(statuses);
			Log.i(TAG, "Twitters are downloaded! " + statuses.size());
			zPubSub.fGetFeedOrganisor().fSaveNewFeeds(statuses, context);
		}

		@Override
		public void gotAccountSettings(AccountSettings settings) {
			//super.gotAccountSettings(settings);
			isAutenticated = true;
			followActions();
		}

	 };
	
	private void followActions()
	{
		if(authMethod.equals(AUTH_METHODS.UPDATE_STATUS))
		{
			sendTweet(prefs,getTweetMsg());
		}
			else if(authMethod.equals(AUTH_METHODS.GET_FEEDS))
		{
			getTweets(prefs);
		}
	}
	

	//public TwitterUtil(Activity activity,final Context context)
	public TwitterUtil(PubSub zPubSub)
	{
		this.activity = zPubSub.fGetActivity();
		this.context = zPubSub.fGetActivity();
		this.zPubSub = zPubSub;
		
	    this.prefs = PreferenceManager.getDefaultSharedPreferences(activity);
	}
	
	
	private void setMessage(String msg)
	{
		this.msgSend = msg;
	}
	
	private String getTweetMsg() {
		if(msgSend.length()>0)
		return msgSend;
		else return null;
	}	
	

	/**
	 * Send Tweets to Twitter , this is called after Authentication
	 * @param prefs
	 * @param msg
	 */
	public void sendTweet(SharedPreferences prefs,String msg)  {
		
		if(twitter == null)
			 twitter = Autentication(prefs,msg);
			
        twitter.updateStatus(msg);
	}
	
	/**
	 * Get Tweets from Twitter, this is called after Authentication
	 * @param prefs
	 */
	public void getTweets(SharedPreferences prefs)
	{
		if(twitter == null)
			 twitter = Autentication(prefs,null);
			
			Log.i(TAG, "Get Tweets !");
			
		twitter.getHomeTimeline();
	}
				

	public void retrieveToken(Uri uri) {
		new RetrieveAccessTokenTask(activity,consumer,provider,prefs).execute(uri);
	}
	
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

				executeAfterAccessTokenRetrieval();
				
				//Log.i(TAG, "OAuth - Access Token Retrieved");
				
			} catch (Exception e) {
				//Log.e(TAG, "OAuth - Access Token Retrieval Error", e);
			}

			return null;
		}


		private void executeAfterAccessTokenRetrieval() {
			followActions();
		}
	}


	
	public void GetFeed() {
		    authMethod = AUTH_METHODS.GET_FEEDS;
			checkAuthenticated(prefs);
		}

	
	
	public void SendFeed(String feed) {
		// TODO Auto-generated method stub
		setMessage(feed);
		authMethod = AUTH_METHODS.UPDATE_STATUS;
		checkAuthenticated(prefs);;
	}

	
	public void CallBackTrigger(Uri uri, int requestCode, int resultCode,
			Intent data) {
		// TODO Auto-generated method stub
		//super.CallBackTrigger(uri, requestCode, resultCode, data);
		this.retrieveToken(uri);
	}

	
	public void Autentication() {
		try {
    		this.consumer = new CommonsHttpOAuthConsumer(Const.CONSUMER_KEY, Const.CONSUMER_SECRET);
    	    this.provider = new CommonsHttpOAuthProvider(Const.REQUEST_URL,Const.ACCESS_URL,Const.AUTHORIZE_URL);
    	} catch (Exception e) {
    		//Log.e(TAG, "Error creating consumer / provider",e);
    		System.out.println("Erro creating consumer / provider" + e);
		}
    	
    	activity.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				new OAuthRequestTokenTask(activity,consumer,provider).execute();
			}});
    	
	}
	
	
	public AsyncTwitter Autentication(SharedPreferences prefs,String msg) {
		//super.Autentication(prefs);
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		
		AccessToken a = new AccessToken(token,secret);
		//Twitter twitter = new TwitterFactory().getInstance();
		//AsyncTwitterFactory factory = new AsyncTwitterFactory(listener);
		AsyncTwitterFactory factory = new AsyncTwitterFactory();
		twitter = factory.getInstance();
		twitter.addListener(listener);
		
		twitter.setOAuthConsumer(Const.CONSUMER_KEY, Const.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(a);
		return twitter;
	}
	
	public boolean checkAuthenticated(SharedPreferences prefs) {
		
		Log.i(TAG, "Authentication " + this.isAutenticated);
		if(!this.isAutenticated)
		{
			if(twitter == null)
			 twitter = Autentication(prefs,null);
			twitter.getAccountSettings();
		}
		else
		{
			
			this.followActions();
		}
		return this.isAutenticated;
		
	}
	

	public boolean isSessionValid() {
		// TODO Auto-generated method stub
		return checkAuthenticated(prefs);
	}


	public void fGetNewsFeed(Context applicationContext) {
		// TODO Auto-generated method stub
		GetFeed();
	}


	public void fDisplayTwitterFeed() {
		zPubSub.fGetActivity().runOnUiThread(new Runnable() {
			public void run() {

				LstViewFeedAdapter feedAdapter = zPubSub.fGetAdapterFeedPreview();
				feedAdapter.clear();
				ArrayList<FeedItem> feeds = zPubSub.fGetFeedOrganisor().fGetUnReadNewsFeed(Const.SNS_TWITTER);
				for (FeedItem item : feeds ) {
					feedAdapter.addItem(item);
				}
				feedAdapter.notifyDataSetChanged();
			}
		});
	}

	
	//Upload Photo to Twitter
	 public void fUploadPic(String message, String selectedImagePath)
    {
	 
		 Configuration conf = new ConfigurationBuilder()
	        .setOAuthConsumerKey( Const.CONSUMER_KEY )
	        .setOAuthConsumerSecret( Const.CONSUMER_SECRET )
	        .setOAuthAccessToken(prefs.getString(OAuth.OAUTH_TOKEN, ""))
	        .setOAuthAccessTokenSecret(prefs.getString(OAuth.OAUTH_TOKEN_SECRET, ""))
	        .build();
		 
		(new UploadPhotoTaskTask(conf,message,selectedImagePath)).execute("");
		 
    }
	 
	 public class UploadPhotoTaskTask extends AsyncTask<String,String,String>
	 {
		private Configuration conf;
		private String message;
		private String ImagePath;
		public UploadPhotoTaskTask(Configuration conf, String message, String ImagePath)
		{
			this.conf = conf;
			this.message = message;
			this.ImagePath = ImagePath;
		}


		@Override
		protected String doInBackground(String... params) {
			try {
				 ImageUpload upload = new ImageUploadFactory(conf).getInstance(MediaProvider.TWITTER);
				 String mediaUrl;
				 mediaUrl = upload.upload(new File(ImagePath),message);
				 
				 Log.i(TAG, "Media URL is "+mediaUrl);
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return null;
		}
		 
	 }
}

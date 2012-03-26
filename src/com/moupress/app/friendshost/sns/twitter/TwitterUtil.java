package com.moupress.app.friendshost.sns.twitter;

import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.FriendsHostActivity;

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
import twitter4j.http.AccessToken;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;


public class TwitterUtil extends SnsUtil{

	private SharedPreferences prefs;
	private Activity activity;
	private Context context;
    private OAuthConsumer consumer; 
    private OAuthProvider provider;
    private String msgSend;
    
	private final Handler mTwitterHandler = new Handler();
	
	final Runnable mUpdateTwitterNotification;
	final Runnable mFetchTwitterNotification;
	
	private static final String TAG = "TwitterFrameWork";
	
	private static enum AUTH_METHODS {GET_FEEDS, UPDATE_STATUS};
	private AUTH_METHODS authMethod;
	
	 private TwitterListener listener = new TwitterAdapter()
	 {

		@Override
		public void onException(TwitterException ex, TwitterMethod method) {
			
			
			if(method == TwitterMethod.ACCOUNT_SETTINGS)
			{
				Autentication();
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
		}

		@Override
		public void gotAccountSettings(AccountSettings settings) {
			//super.gotAccountSettings(settings);
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
	

	public TwitterUtil(Activity activity,final Context context)
	{
		this.activity = activity;
		this.context = context;
		
		//Notifications When Status Update is Completed
		mUpdateTwitterNotification = new Runnable() {
	        public void run() {
	        	Toast.makeText(context, "Tweet sent !", Toast.LENGTH_LONG).show();
	        }
	    };
	    
	    //Notification When Tweets are loaded
	    mFetchTwitterNotification = new Runnable()
	    {
			@Override
			public void run() {
				Toast.makeText(context, "Tweets are downloaded !", Toast.LENGTH_LONG).show();
			}
	    };
	    this.prefs = PreferenceManager.getDefaultSharedPreferences(activity);
	}
	
	
	public void setMessage(String msg)
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
		AsyncTwitter twitter = Autentication(prefs,msg);
        twitter.updateStatus(msg);
	}
	
	/**
	 * Get Tweets from Twitter, this is called after Authentication
	 * @param prefs
	 */
	public void getTweets(SharedPreferences prefs)
	{
		AsyncTwitter twitter = Autentication(prefs,null);
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


	@Override
	public void GetFeed() {
		    authMethod = AUTH_METHODS.GET_FEEDS;
			checkAuthenticated(prefs);
		}

	
	@Override
	public void SendFeed(String feed) {
		// TODO Auto-generated method stub
		setMessage(feed);
		authMethod = AUTH_METHODS.UPDATE_STATUS;
		checkAuthenticated(prefs);;
	}

	@Override
	public void CallBackTrigger(Uri uri, int requestCode, int resultCode,
			Intent data) {
		// TODO Auto-generated method stub
		//super.CallBackTrigger(uri, requestCode, resultCode, data);
		this.retrieveToken(uri);
	}

	@Override
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
		AsyncTwitterFactory factory = new AsyncTwitterFactory(listener);
		AsyncTwitter twitter = factory.getInstance();
		twitter.setOAuthConsumer(Const.CONSUMER_KEY, Const.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(a);
		return twitter;
	}
	
	public void checkAuthenticated(SharedPreferences prefs) {
		
		AsyncTwitter twitter = Autentication(prefs,null);
		twitter.getAccountSettings();
		
	}

}

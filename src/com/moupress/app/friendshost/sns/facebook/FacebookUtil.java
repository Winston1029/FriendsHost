package com.moupress.app.friendshost.sns.facebook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import com.moupress.app.friendshost.FriendsHostActivity;
import com.moupress.app.friendshost.util.FeedOrganisor;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;


public class FacebookUtil {

	private Activity zActivity;
	private Context zContext;
	
	public static final String APP_ID = "337247706286700";
    private static final String[] PERMISSIONS = new String[] {"publish_stream", "read_stream"};
    private static final String FBTOKEN = "fbToken";
    private static final String FBTOKENEXPIRES = "fbAccessExpires";
    private Facebook zFacebook;
	private String sfbToken;
	
	public FacebookUtil(Activity activity, Context context) {
		this.zActivity = activity;
		this.zContext = context;
		zFacebook = new Facebook(APP_ID);
		fFacebookAuth();
	}
	
	public void fGetNewsFeed() {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(zActivity);
	    sfbToken = mPrefs.getString(FBTOKEN, "");
	
	    if( !zFacebook.isSessionValid() ){
	    	fFacebookAuth();
	    	sfbToken = mPrefs.getString(FBTOKEN, "");
	    }
	    
	    AsyncFacebookRunner asyncFB = new AsyncFacebookRunner(zFacebook);
	    RequestListener listener = new RequestListener() {

			@Override
			public void onComplete(final String response, Object state) {
				System.out.println("Facebook Request Complete");
				FeedOrganisor.fSaveNewFeeds(response); // should run on a seperate thread
				zActivity.runOnUiThread(new Runnable() {
					public void run() {
						FBHomeFeed bean = new Gson().fromJson(response, FBHomeFeed.class);
						System.out.println("Facebook news feed get listener on complete");
						ArrayAdapter<String> adapterFBResponse = ((FriendsHostActivity) zActivity).zPubsub.fGetArrAdapterFeed();
						for(int i= 0; i<bean.getData().size();i++) {
							String msg = ((FBHomeFeedEntry) bean.getData().get(i)).getName()+" : "+((FBHomeFeedEntry) bean.getData().get(i)).getMessage();
							adapterFBResponse.add(msg);
						}
						adapterFBResponse.notifyDataSetChanged();
					}
				});
				
			}

			@Override
			public void onIOException(IOException e, Object state) {
				System.out.println("onIOException: " + e.getMessage());				
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
				System.out.println("onFileNotFoundException: " + e.getMessage());
			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
				System.out.println("onMalformedURLException: " + e.getMessage());
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
				System.out.println("onFacebooError: " + e.getMessage());
			}
	    	
	    };
	    Bundle mBundle = new Bundle();
        mBundle.putString(Facebook.TOKEN,sfbToken);
	    asyncFB.request("me/home", mBundle, listener);
	}
	
/** 
 * To Remove testing code where facebook feed is obtained synchronously	
 * 
	public FBHomeFeed fReadMessage() {
		String sNewsfeedJson = "";
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(zActivity);
	    sfbToken = mPrefs.getString(FBTOKEN, "");
	    //long lfbExpires = mPrefs.getLong(FBTOKENEXPIRES, 0);
	
	    if( !zFacebook.isSessionValid() ){
	    	fFacebookAuth();
	    	sfbToken = mPrefs.getString(FBTOKEN, "");
	    }
	    //user password change && revoke app authentication is not considered
	    //for detail refer to http://developers.facebook.com/docs/mobile/android/build/#sso

	    sNewsfeedJson = fGetNewsFeed(sfbToken);
	    FBHomeFeed data = new Gson().fromJson(sNewsfeedJson, FBHomeFeed.class);
	    return data;
	}

	private String fGetNewsFeed(String accessToken) {
		String sNewsfeed = "";
		Bundle mBundle = new Bundle();
        mBundle.putString(Facebook.TOKEN,accessToken);
		try {
			sNewsfeed=zFacebook.request("me/home", mBundle);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sNewsfeed;
	}
	
*/
	
	public void fPostMessage(String fbMessage) {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(zActivity);
	    sfbToken = mPrefs.getString("fbToken", "");
	
	    if(sfbToken.equals("")){
	    	fFacebookAuth();
	    }
	    
	    fUpdateStatus(sfbToken, fbMessage);
	}
	
	private void fFacebookAuth(){
		
	    zFacebook.authorize(zActivity, PERMISSIONS, new DialogListener() {
	
	        @Override
	        public void onComplete(Bundle values) {
	            Log.d(this.getClass().getName(),"Facebook.authorize Complete: ");
	            sfbToken = zFacebook.getAccessToken();
	            fSaveFBToken(sfbToken, zFacebook.getAccessExpires());
	            //Toast.makeText(zContext, "Updated status on Facebook", Toast.LENGTH_SHORT);
	        }
	
	        @Override
	        public void onFacebookError(FacebookError error) {
	            Log.d(this.getClass().getName(),"Facebook.authorize Error: "+error.toString());
	        }
	
	        @Override
	        public void onError(DialogError e) {
	            Log.d(this.getClass().getName(),"Facebook.authorize DialogError: "+e.toString());
	        }
	
	        @Override
	        public void onCancel() {
	            Log.d(this.getClass().getName(),"Facebook authorization canceled");
	        }
	    });
	}
	
	//updating Status
	private void fUpdateStatus(String accessToken, String message){
	    try {
	        Bundle mBundle = new Bundle();
	        mBundle.putString("message",  message);
	        mBundle.putString(Facebook.TOKEN,accessToken);
	        String response = zFacebook.request("me/feed",mBundle,"POST");
	        Log.d("UPDATE RESPONSE",""+response);
	    } catch (MalformedURLException e) {
	        Log.e("MALFORMED URL",""+e.getMessage());
	    } catch (IOException e) {
	        Log.e("IOEX",""+e.getMessage());
	    }
	}
	
	private void fSaveFBToken(String token, long tokenExpires){
	    SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(zActivity);
	    mPrefs.edit().putString(FBTOKEN, token);
	    mPrefs.edit().putLong(FBTOKENEXPIRES, tokenExpires);
	    mPrefs.edit().commit();
	}
	
	public void onComplete (int requestCode, int resultCode, Intent data) {
		if (zFacebook != null) {
			zFacebook.authorizeCallback(requestCode, resultCode, data);
		}
    }

}

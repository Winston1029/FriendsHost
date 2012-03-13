package com.moupress.app.friendshost.sns.facebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.FriendsHostActivity;
import com.moupress.app.friendshost.LstViewFeedAdapter;
import com.moupress.app.friendshost.PubSub;
import com.moupress.app.friendshost.util.FeedOrganisor;
import com.renren.api.connect.android.Util;
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
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;


public class FacebookUtil {

	private Context zContext;
	private PubSub zPubSub;
	
	public static final String APP_ID = "337247706286700";
	private static final String TAG = "FacebookUtil";
    private static final String[] PERMISSIONS = new String[] {"publish_stream", "read_stream"};
    private static final String FBTOKEN = "fbToken";
    private static final String FBTOKENEXPIRES = "fbAccessExpires";
    
    private Facebook zFacebook;
	private String sfbToken;
	
	//======Feed Params Name ===============
	private static final String FEED_MSG ="message";
	private static final String FEED_LINK = "link";
	private static final String FEED_NAME = "name";
	private static final String FEED_CAPTION = "caption";
	private static final String FEED_DESC = "description";
	private static final String FEED_PICS = "picture";
	private static final String FEED_SRC = "source";
	
	
	public FacebookUtil(PubSub pubSub) {
		zPubSub = pubSub;
		zContext = zPubSub.fGetContext();
		if (zFacebook == null) {
			zFacebook = new Facebook(APP_ID);
		}
		if (!zFacebook.isSessionValid()) {
			fFacebookAuth();
		}
		
	}
	
	public boolean isSessionValid() {
		if (zFacebook != null) {
			return zFacebook.isSessionValid();
		} else {
			return false;
		}
	}

	public void fGetNewsFeed(final Context context) {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(zContext);
	    sfbToken = mPrefs.getString(FBTOKEN, "");
	
	    if( !zFacebook.isSessionValid() ){
	    	fFacebookAuth();
	    	sfbToken = mPrefs.getString(FBTOKEN, "");
	    }
	    
	    AsyncFacebookRunner asyncFB = new AsyncFacebookRunner(zFacebook);
	    RequestListener listener = new RequestListener() {

			@Override
			public void onComplete(final String response, Object state) {
				zPubSub.fGetFeedOrganisor().fSaveNewFeeds(response, context);
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
	
	public void fDisplayFeed() {
		zPubSub.fGetActivity().runOnUiThread(new Runnable() {
			public void run() {
				LstViewFeedAdapter feedAdapter = zPubSub.fGetAdapterFeedPreview();
				feedAdapter.clear();
				String[][] feedMsg = zPubSub.fGetFeedOrganisor().fGetUnReadNewsFeed(Const.SNS_FACEBOOK);
				for (int i = 0; i < feedMsg.length; i++) {
					feedAdapter.addItem(feedMsg[i]);
				}
				feedAdapter.notifyDataSetChanged();
			}
		});
	}
	
	public void fPostMessage(String fbMessage) {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(zContext);
	    sfbToken = mPrefs.getString("fbToken", "");
	
	    if(sfbToken.equals("")){
	    	fFacebookAuth();
	    }
	    
	    fUpdateStatus(sfbToken, fbMessage);
	    
	    
	}
	
	private void fFacebookAuth(){
		
	    zFacebook.authorize(zPubSub.fGetActivity(), PERMISSIONS, new DialogListener() {
	
	        @Override
	        public void onComplete(Bundle values) {
	            Log.d(TAG,"Facebook.authorize Complete: ");
	            sfbToken = zFacebook.getAccessToken();
	            fSaveFBToken(sfbToken, zFacebook.getAccessExpires());
	        }
	
	        @Override
	        public void onFacebookError(FacebookError error) {
	            Log.d(TAG,"Facebook.authorize Error: "+error.toString());
	        }
	
	        @Override
	        public void onError(DialogError e) {
	            Log.d(TAG,"Facebook.authorize DialogError: "+e.toString());
	        }
	
	        @Override
	        public void onCancel() {
	            Log.d(TAG,"Facebook authorization canceled");
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
	    SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(zContext);
	    mPrefs.edit().putString(FBTOKEN, token);
	    mPrefs.edit().putLong(FBTOKENEXPIRES, tokenExpires);
	    mPrefs.edit().commit();
	}
	
	public void onComplete (int requestCode, int resultCode, Intent data) {
		if (zFacebook != null) {
			zFacebook.authorizeCallback(requestCode, resultCode, data);
		}
    }
	
	public Facebook GetFBObject()
	{
		return zFacebook;
	}
	
	public void fPublishFeeds(String name, String description,String url, String imageUrl, String caption, String message)
	{
		if(zFacebook != null)
		{
			 AsyncFacebookRunner asyncFB = new AsyncFacebookRunner(zFacebook);
			 
			 Bundle params = new Bundle();
			 
			 if(message.length() > 0)
			 params.putString(FEED_MSG, message);
			 
			 if(url.length()>0 && url.startsWith("http"))
			 params.putString(FEED_LINK,url);
			 
			 if(name.length()>0)
			 params.putString(FEED_NAME,name);
			 
			 if(caption.length() > 0)
			 params.putString(FEED_CAPTION, caption);
			 
			 if(description.length() > 0)
			 params.putString(FEED_DESC,description);
			 
			 if(imageUrl.length() > 0)
			 params.putString(FEED_PICS,imageUrl);
			 
			 
			 RequestListener listener = new RequestListener() {

				@Override
				public void onComplete(String response, Object state) {
					Log.d(TAG, "complete:"+response);
					
				}

				@Override
				public void onFacebookError(FacebookError e, Object state) {
					Log.d(TAG, "Error: "+e.getErrorCode()+" : "+e.getMessage());
				}

				@Override
				public void onFileNotFoundException(FileNotFoundException e,
						Object state) {
					Log.d(TAG, "Exception: " + e.getMessage());
				}

				@Override
				public void onIOException(IOException e, Object state) {
					Log.d(TAG, "Exception: " + e.getMessage());
				}

				@Override
				public void onMalformedURLException(MalformedURLException e,
						Object state) {
					Log.d(TAG, "Exception: " + e.getMessage());
				}};
				
				asyncFB.request("me/feed", params, "POST", listener,null);
		}
	}

	public void fUploadPic(String message, String selectedImagePath) {
		if(zFacebook != null)
		{
			 AsyncFacebookRunner asyncFB = new AsyncFacebookRunner(zFacebook);
			 Bundle params = new Bundle();
			 params.putString(FEED_MSG, message);
			 
			 params.putByteArray(FEED_SRC, Util.fileToByteArray(new File(selectedImagePath)));
			 
			 RequestListener listener = new RequestListener() {

					@Override
					public void onComplete(String response, Object state) {
						Log.d(TAG, "complete:"+response);
						
					}

					@Override
					public void onFacebookError(FacebookError e, Object state) {
						Log.d(TAG, "Error: "+e.getErrorCode()+" : "+e.getMessage());
					}

					@Override
					public void onFileNotFoundException(FileNotFoundException e,
							Object state) {
						Log.d(TAG, "Exception: " + e.getMessage());
					}

					@Override
					public void onIOException(IOException e, Object state) {
						Log.d(TAG, "Exception: " + e.getMessage());
					}

					@Override
					public void onMalformedURLException(MalformedURLException e,
							Object state) {
						Log.d(TAG, "Exception: " + e.getMessage());
					}};
					
					asyncFB.request("me/photos", params, "POST", listener,null);
		}
	}

}

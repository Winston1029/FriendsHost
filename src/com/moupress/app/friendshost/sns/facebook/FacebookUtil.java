package com.moupress.app.friendshost.sns.facebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.FriendsHostActivity;
import com.moupress.app.friendshost.PubSub;
import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.activity.LstViewFeedAdapter;
import com.moupress.app.friendshost.sns.FeedEntry;
import com.moupress.app.friendshost.sns.SnsUtil;
import com.moupress.app.friendshost.sns.Listener.SnsEventListener;
import com.moupress.app.friendshost.util.FeedOrganisor;
import com.moupress.app.friendshost.util.NotificationTask;
import com.moupress.app.friendshost.util.Pref;
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

public class FacebookUtil extends SnsUtil {

	// private Context zContext;
	// private PubSub zPubSub;

	public static final String APP_ID = "337247706286700";
	private static final String TAG = "FacebookUtil";
	private static final String[] PERMISSIONS = new String[] {
			"publish_stream", "read_stream" };
	private static final String FBTOKEN = "fbToken";
	private static final String FBTOKENEXPIRES = "fbAccessExpires";

	private Facebook zFacebook;
	private String sfbToken;
	private static AsyncFacebookRunner asyncFB;
	private NotificationTask notificationTask;

	// ======Feed Params Name ===============
	private static final String FEED_MSG = "message";
	private static final String FEED_LINK = "link";
	private static final String FEED_NAME = "name";
	private static final String FEED_CAPTION = "caption";
	private static final String FEED_DESC = "description";
	private static final String FEED_PICS = "picture";
	private static final String FEED_SRC = "source";
	
	

	public FacebookUtil(PubSub pubSub) {

		super(pubSub, Const.SNS_FACEBOOK);
		// zPubSub = pubSub;
		// zContext = zPubSub.fGetContext();
		if (zFacebook == null) {
			zFacebook = new Facebook(APP_ID);
		}
		
		this.logImg  = R.drawable.fh_facebook_logo;
		//fFacebookAuth();
	}

	@Override
	public boolean isSessionValid() {
		if (zFacebook != null) {
			return zFacebook.isSessionValid();
		} else {
			return false;
		}
	}

	@Override
	public void fGetNewsFeed(final Context context) {
		SharedPreferences mPrefs = PreferenceManager
				.getDefaultSharedPreferences(zContext);
		sfbToken = mPrefs.getString(FBTOKEN, "");

		// fFacebookAuth();
		sfbToken = mPrefs.getString(FBTOKEN, "");

		// AsyncFacebookRunner asyncFB = new AsyncFacebookRunner(zFacebook);
		asyncFB = fGetAsyncFacebook();
		RequestListener listener = new RequestListener() {

			@Override
			public void onComplete(final String response, Object state) {

				Log.i(TAG, "Facebook news feed get listener on complete");

				FBHomeFeed bean = new Gson().fromJson(response,
						FBHomeFeed.class);
				zPubSub.fGetFeedOrganisor().fSaveNewFeeds(bean, context);
			}

			@Override
			public void onIOException(IOException e, Object state) {
				System.out.println("onIOException: " + e.getMessage());
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
				System.out
						.println("onFileNotFoundException: " + e.getMessage());
			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
				System.out
						.println("onMalformedURLException: " + e.getMessage());
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
				System.out.println("onFacebooError: " + e.getMessage());
			}

		};
		Bundle mBundle = new Bundle();
		mBundle.putString(Facebook.TOKEN, sfbToken);
		asyncFB.request("me/home", mBundle, listener);
	}

	// @Override
	// public void fDisplayFeed() {
	// zPubSub.fGetActivity().runOnUiThread(new Runnable() {
	// public void run() {
	// LstViewFeedAdapter feedAdapter = zPubSub.fGetAdapterFeedPreview();
	// feedAdapter.clear();
	// // String[][] feedMsg =
	// zPubSub.fGetFeedOrganisor().fGetUnReadNewsFeed(Const.SNS_FACEBOOK);
	// // for (int i = 0; i < feedMsg.length; i++) {
	// // feedAdapter.addItem(feedMsg[i]);
	// // }
	// ArrayList<FeedEntry> feeds =
	// zPubSub.fGetFeedOrganisor().fGetUnReadNewsFeed(Const.SNS_FACEBOOK);
	// for (FeedEntry item : feeds ) {
	// feedAdapter.addItem(item);
	// }
	// feedAdapter.notifyDataSetChanged();
	// }
	// });
	// }

	
    

	
	@Override
	protected void fSnsAuth(final SnsEventListener snsEventListener,final boolean uptPref) {

		if (isSessionValid()) {
			
			this.SnsAddEventCallback(snsEventListener,uptPref);
			
			return;
		}

		zFacebook.authorize(zPubSub.fGetActivity(), PERMISSIONS,
				new DialogListener() {

					@Override
					public void onComplete(Bundle values) {
						Log.d(TAG, "Facebook.authorize Complete: ");
						sfbToken = zFacebook.getAccessToken();
						fSaveFBToken(sfbToken, zFacebook.getAccessExpires());
						fSaveLoginProfile();
						SnsAddEventCallback(snsEventListener,uptPref);
					}

					@Override
					public void onFacebookError(FacebookError error) {
						Log.d(TAG, "Facebook.authorize Error: "
								+ error.toString());

					}

					@Override
					public void onError(DialogError e) {
						Log.d(TAG, "Facebook.authorize DialogError: "
								+ e.toString());
					}

					@Override
					public void onCancel() {
						Log.d(TAG, "Facebook authorization canceled");
					}
				});

	}

	private void fSaveFBToken(String token, long tokenExpires) {
		SharedPreferences mPrefs = PreferenceManager
				.getDefaultSharedPreferences(zContext);
		mPrefs.edit().putString(FBTOKEN, token);
		mPrefs.edit().putLong(FBTOKENEXPIRES, tokenExpires);
		mPrefs.edit().commit();
		
	}
	
	private void fSaveLoginProfile() {
		try {
			JSONObject me = new JSONObject(zFacebook.request("me"));
			Pref.setMyStringPref(zActivity.getApplicationContext(), Const.LOGIN_ID_FB, me.getString("id"));
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onComplete(int requestCode, int resultCode, Intent data) {
		if (zFacebook != null) {
			zFacebook.authorizeCallback(requestCode, resultCode, data);
		}
	}

	public AsyncFacebookRunner fGetAsyncFacebook() {
		if (asyncFB == null) {
			fSnsAuth(null,false);
			asyncFB = new AsyncFacebookRunner(zFacebook);
		}
		return asyncFB;
	}

	public void fPublishFeeds(String name, String description, String url,
			String imageUrl, String caption, String message) {
		if (zFacebook != null) {
			// AsyncFacebookRunner asyncFB = new AsyncFacebookRunner(zFacebook);
			asyncFB = fGetAsyncFacebook();

			Bundle params = new Bundle();

			if (message.length() > 0)
				params.putString(FEED_MSG, message);

			if (url != null && url.length() > 0 && url.startsWith("http"))
				params.putString(FEED_LINK, url);

			if (name != null && name.length() > 0)
				params.putString(FEED_NAME, name);

			if (caption != null && caption.length() > 0)
				params.putString(FEED_CAPTION, caption);

			if (description != null && description.length() > 0)
				params.putString(FEED_DESC, description);

			if (imageUrl != null && imageUrl.length() > 0)
				params.putString(FEED_PICS, imageUrl);

			this.startNotification(6, "Feed");
			RequestListener listener = new RequestListener() {

				@Override
				public void onComplete(String response, Object state) {
					Log.d(TAG, "complete:" + response);
					stopNotification();
				}

				@Override
				public void onFacebookError(FacebookError e, Object state) {
					Log.d(TAG, "Error: " + e.getErrorCode() + " : "
							+ e.getMessage());
					stopNotification();
				}

				@Override
				public void onFileNotFoundException(FileNotFoundException e,
						Object state) {
					Log.d(TAG, "Exception: " + e.getMessage());
					stopNotification();
				}

				@Override
				public void onIOException(IOException e, Object state) {
					Log.d(TAG, "Exception: " + e.getMessage());
					stopNotification();
				}

				@Override
				public void onMalformedURLException(MalformedURLException e,
						Object state) {
					Log.d(TAG, "Exception: " + e.getMessage());
					stopNotification();
				}
			};

			asyncFB.request("me/feed", params, "POST", listener, null);
		}
	}

	public void fUploadPic(String message, String selectedImagePath) {
		if (zFacebook != null) {
			// AsyncFacebookRunner asyncFB = new AsyncFacebookRunner(zFacebook);
			asyncFB = fGetAsyncFacebook();
			Bundle params = new Bundle();
			params.putString(FEED_MSG, message);

			params.putByteArray(FEED_SRC, Util.fileToByteArray(new File(
					selectedImagePath)));
			this.startNotification(5, "Picture");
			RequestListener listener = new RequestListener() {

				@Override
				public void onComplete(String response, Object state) {
					Log.d(TAG, "complete:" + response);
					stopNotification();
				}

				@Override
				public void onFacebookError(FacebookError e, Object state) {
					Log.d(TAG, "Error: " + e.getErrorCode() + " : "
							+ e.getMessage());
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
				}
			};

			asyncFB.request("me/photos", params, "POST", listener, null);
		}
	}

	private void startNotification(int notificationId, String fileType) {
		notificationTask = new NotificationTask(this.zContext);
		if (notificationTask != null) {
			notificationTask.SetNotificationTask(notificationId, "Facebook",
					fileType);
			notificationTask.execute(0);
		}
	}

	private void stopNotification() {
		if (notificationTask != null) {
			notificationTask.setTaskDone(true);
		}
	}

	@Override
	public void fResend(FeedEntry feed) {
		this.fPublishFeeds(" ", feed.getsPhotoPreviewDescription(), feed
				.getsPhotoPreviewLink(), feed.getsPhotoPreviewLink(), feed
				.getsPhotoPreviewCaption(), feed.getsMsgBody());
	}

}

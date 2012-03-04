package com.moupress.app.friendshost.sns.Renren;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.FriendsHostActivity;
import com.moupress.app.friendshost.PubSub;
import com.renren.api.connect.android.AsyncRenren;
import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.common.AbstractRequestListener;
import com.renren.api.connect.android.exception.RenrenAuthError;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.feed.FeedPublishRequestParam;
import com.renren.api.connect.android.feed.FeedPublishResponseBean;
import com.renren.api.connect.android.photos.PhotoUploadRequestParam;
import com.renren.api.connect.android.photos.PhotoUploadResponseBean;
import com.renren.api.connect.android.view.RenrenAuthListener;


public class RenrenUtil {
	
	private static final String API_KEY = "7872469fdd144ef792233e56dca0eb31";
	private static final String SECRET_KEY = "938ebb14322d40c89015483b2479d144";
	private static final String APP_ID = "166341";
	private static final String TAG = "RenrenUtil";
	
	private static final String[] PERMISSIONS = new String[] {"read_user_feed", "publish_feed", "publish_share"};
	
	private PubSub zPubSub;
	private Context zContext;
	private Activity zActivity;
	
	private Renren zRenren;
	
		
	public RenrenUtil(PubSub pubSub) {
		zPubSub = pubSub;
		zContext = zPubSub.fGetContext();
		zActivity = zPubSub.fGetActivity();
		this.zRenren = new Renren(API_KEY, SECRET_KEY, APP_ID, zContext);
		if ( !zRenren.isSessionKeyValid() ) {
			fRenrenAuth();
		}
	}
	
	public boolean isSessionValid() {
		if (zRenren != null ) {
			return zRenren.isSessionKeyValid();
		} else {
			return false;
		}
	}

	public void fRenrenAuth() {
		final RenrenAuthListener listener = new RenrenAuthListener() {

			@Override
			public void onComplete(Bundle values) {
				Toast.makeText(zContext, "Renren Auth Complete",Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onRenrenAuthError(RenrenAuthError renrenAuthError) {
				Log.d(TAG, renrenAuthError.getMessage());
				Toast.makeText(zContext, "Renren Auth Failed", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onCancelLogin() {
			}

			@Override
			public void onCancelAuth(Bundle values) {
			}
		};
		
		zRenren.authorize(zActivity, PERMISSIONS, listener);
	}
	
	public void fGetNewsFeed() {
		AsyncRenren asyncRenren = new AsyncRenren(zRenren);
		FeedExtractRequestParam param = new FeedExtractRequestParam("XML", "10", 1);
		AbstractRequestListener<FeedExtractResponseBean> listener = new AbstractRequestListener<FeedExtractResponseBean>(){
			@Override
			public void onComplete(final FeedExtractResponseBean bean) {
				System.out.println("Renren news feed get listener on complete");
				zPubSub.fGetFeedOrganisor().fSaveNewFeeds(bean);
			}

			@Override
			public void onRenrenError(RenrenError renrenError) {
				System.out.println(" onRenrenError Extract Request Response Bean !");
			}

			@Override
			public void onFault(Throwable fault) {
				System.out.println(" onFault Extract Request Response Bean !");
			}
		};
		asyncRenren.getFeed(param, listener, false);
	}
	
	public void fPublishFeeds(String name, String description,String url, String imageUrl, String caption, String message)
	{
		if (zRenren != null) {
			AsyncRenren asyncRenren = new AsyncRenren(zRenren);
			//showProgress();
			
			FeedPublishRequestParam param = new FeedPublishRequestParam(
					name, description, url, imageUrl, caption,
					null, null, message);
			AbstractRequestListener<FeedPublishResponseBean> listener = new AbstractRequestListener<FeedPublishResponseBean>() {

				@Override
				public void onComplete(final FeedPublishResponseBean bean) {
	
							//editTextLog.setText(bean.toString());
							Log.d(TAG, bean.toString());
							
				}


				@Override
				public void onFault(final Throwable fault) {
					
							Log.d(TAG, fault.getMessage());
							
				}

				@Override
				public void onRenrenError(final RenrenError renrenError) {
					
							Log.d(TAG, renrenError.getMessage());
				}
			};
			asyncRenren.publishFeed(param, listener, true);

		}
	}
	
	public void fDisplayRenrenFeed() {
		zActivity.runOnUiThread(new Runnable() {
			public void run() {
				ArrayAdapter<String> adapterRenrenResponse = zPubSub.fGetArrAdapterFeed();
				adapterRenrenResponse.clear();
				String[] feedMsg = zPubSub.fGetFeedOrganisor().fGetUnReadNewsFeed(Const.SNS_RENREN);
				for(int i= 0; i<feedMsg.length;i++) {
					adapterRenrenResponse.add(feedMsg[i]);
				}
				adapterRenrenResponse.notifyDataSetChanged();
			}
		});
	}
	
	public void onComplete (int requestCode, int resultCode, Intent data) {
		if (zRenren != null ) {
			zRenren.authorizeCallback(requestCode, resultCode, data);
		}
    }
	
	public Renren GetRenren()
	{
		return zRenren;
	}

	public void fUploadPic(String message, String selectedImagePath) {
			if (zRenren != null) {
				AsyncRenren asyncRenren = new AsyncRenren(zRenren);
				PhotoUploadRequestParam photoParam = new PhotoUploadRequestParam();
				
				photoParam.setCaption(message);
				photoParam.setFile(new File(selectedImagePath));
				
				asyncRenren.publishPhoto(photoParam,new AbstractRequestListener<PhotoUploadResponseBean>(){

					@Override
					public void onComplete(PhotoUploadResponseBean bean) {
						Log.d(TAG, bean.toString());
					}

					@Override
					public void onFault(Throwable fault) {
						Log.d(TAG, fault.getMessage());
					}

					@Override
					public void onRenrenError(RenrenError renrenError) {
						Log.d(TAG, renrenError.getMessage());
					}});
			}
		}
	
}

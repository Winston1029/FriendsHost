package com.moupress.app.friendshost.sns.Renren;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.renren.api.connect.android.view.RenrenAuthListener;


public class RenrenUtil {
	
	private static final String API_KEY = "7872469fdd144ef792233e56dca0eb31";
	private static final String SECRET_KEY = "938ebb14322d40c89015483b2479d144";
	private static final String APP_ID = "166341";
	
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
	
}

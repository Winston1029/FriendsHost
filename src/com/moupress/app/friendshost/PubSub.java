package com.moupress.app.friendshost;

import com.moupress.app.util.facebook.FBHomeFeed;
import com.moupress.app.util.facebook.FBHomeFeedEntry;
import com.moupress.app.util.facebook.FacebookUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class PubSub {
	private Activity zActivity;
	private Context zContext;
	
	private FacebookUtil zFacebook;
	
	public PubSub(Context context, Activity activity) {
		this.zContext = context;
		this.zActivity = activity;
	}
	
	public PubSub(Activity activity) {
		this.zActivity = activity;
		this.zContext = activity.getBaseContext();
		
		fInitUI();
		fInitAcc();
	}

	private ArrayAdapter<String> arrAdapterFBFeed;
	private void fInitUI() {
		ListView uLstVFBFeed = (ListView) zActivity.findViewById(R.id.uLstVFBFeed);
        arrAdapterFBFeed = new ArrayAdapter<String>(zActivity, R.layout.feed_item);	
        uLstVFBFeed.setAdapter(arrAdapterFBFeed);
        
        Button uBtnGetFeed = (Button) zActivity.findViewById(R.id.btn_getfeed);
        uBtnGetFeed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FBHomeFeed mNewsFeed = zFacebook.fReadMessage();
				int iNumOfFeeds = mNewsFeed.getData().size();
				for (int i=0; i<iNumOfFeeds; i++) {
					FBHomeFeedEntry mHomeFeedEntry = (FBHomeFeedEntry) mNewsFeed.getData().get(i);
					String msg = mHomeFeedEntry.getFrom().getName() + ": ";
					msg = msg + mHomeFeedEntry.getMessage();
					arrAdapterFBFeed.add(msg);
				}
				System.out.print("Feed Parse Complete");
				
			}
		});
	}

	private void fInitAcc() {
		zFacebook = new FacebookUtil(zActivity, zContext);
		
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		zFacebook.onComplete(requestCode, resultCode, data);		
	}
}

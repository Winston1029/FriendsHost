package com.moupress.app.friendshost;

import java.util.ArrayList;

import com.moupress.app.util.Renren.RenrenUtil;
import com.moupress.app.util.facebook.FBHomeFeed;
import com.moupress.app.util.facebook.FBHomeFeedEntry;
import com.moupress.app.util.facebook.FacebookUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class PubSub {
	private Activity zActivity;
	private Context zContext;
	
	private FacebookUtil 	zFacebook;
	private RenrenUtil 		zRenrenUtil;
	ListView uLstFeed;
	public PubSub(Context context, Activity activity) {
		this.zContext = context;
		this.zActivity = activity;
	}
	
	public PubSub(Activity activity) {
		this.zActivity = activity;
		this.zContext = activity.getBaseContext();
		
		uLstFeed = (ListView) zActivity.findViewById(R.id.uLstVFBFeed);
		fInitAcc();
		fFBInitUI();
		fInitRenrenUI();
	}

	private ArrayAdapter<String> arrAdapterFBFeed;
	private void fFBInitUI() {
		//ListView uLstVFBFeed = (ListView) zActivity.findViewById(R.id.uLstVFBFeed);
        arrAdapterFBFeed = new ArrayAdapter<String>(zActivity, R.layout.feed_item);	
        
        
        Button uBtnFBGetFeed = (Button) zActivity.findViewById(R.id.btn_getfbfeed);
        uBtnFBGetFeed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				arrAdapterFBFeed.clear();
				uLstFeed.setAdapter(arrAdapterFBFeed);
				
				FBHomeFeed mNewsFeed = zFacebook.fReadMessage();
				int iNumOfFeeds = mNewsFeed.getData().size();
				for (int i=0; i<iNumOfFeeds; i++) {
					FBHomeFeedEntry mHomeFeedEntry = (FBHomeFeedEntry) mNewsFeed.getData().get(i);
					String msg = mHomeFeedEntry.getFrom().getName() + ": ";
					msg = msg + mHomeFeedEntry.getMessage();
					System.out.println(msg);
					arrAdapterFBFeed.add(msg);
				}
				System.out.print("Feed Parse Complete");
				
			}
		});
	}
	
	private ArrayAdapter<String> arrAdapterRenrenFeed;
	public ArrayAdapter<String> fGetArrAdapterRenrenFeed() {return arrAdapterRenrenFeed; }
	private void fInitRenrenUI() {
		//feedList = new ArrayList<FeedElement>();
		arrAdapterRenrenFeed = new ArrayAdapter<String>(zActivity,R.layout.feed_item);
		Button uBtnRenrenGetFeed = (Button) zActivity.findViewById(R.id.btn_getRenrenfeed);
		
		uBtnRenrenGetFeed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				arrAdapterRenrenFeed.clear();
				uLstFeed.setAdapter(arrAdapterRenrenFeed);
				zRenrenUtil.fGetNewsFeed();
			}
		});
	}

	private void fInitAcc() {
		zFacebook = new FacebookUtil(zActivity, zContext);
		zRenrenUtil = new RenrenUtil(zActivity, zContext);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		zFacebook.onComplete(requestCode, resultCode, data);
		zRenrenUtil.onComplete(requestCode, resultCode, data);
	}
}

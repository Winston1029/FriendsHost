package com.moupress.app.friendshost;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.moupress.app.friendshost.sns.Renren.RenrenUtil;
import com.moupress.app.friendshost.sns.facebook.FacebookUtil;
import com.moupress.app.friendshost.util.FeedScheduler;

public class PubSub {
	private Activity zActivity;
	private Context zContext;
	
	private FacebookUtil 	zFacebook;
	private RenrenUtil 		zRenrenUtil;
	private FeedScheduler	zFeedScheduler;
	
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
		fInitSchedulerUI();
	}

	private ArrayAdapter<String> arrAdapterFBFeed;
	public ArrayAdapter<String> fGetArrAdapterFBFeed() {return arrAdapterFBFeed;}
	private void fFBInitUI() {
        arrAdapterFBFeed = new ArrayAdapter<String>(zActivity, R.layout.feed_item);	
        
        Button uBtnFBGetFeed = (Button) zActivity.findViewById(R.id.btn_getfbfeed);
        uBtnFBGetFeed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				arrAdapterFBFeed.clear();
				uLstFeed.setAdapter(arrAdapterFBFeed);
				
				zFacebook.fGetNewsFeed();
				System.out.print("Feed Parse Complete");
				
			}
		});
	}
	
	private ArrayAdapter<String> arrAdapterRenrenFeed;
	public ArrayAdapter<String> fGetArrAdapterRenrenFeed() {return arrAdapterRenrenFeed; }
	private void fInitRenrenUI() {

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

	private void fInitSchedulerUI() {
		Button uBtnScheduler = (Button) zActivity.findViewById(R.id.btn_Scheduler);
		
		
		uBtnScheduler.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String btnText = ((Button)v).getText().toString();
				if (btnText.equals("Start")) {
					zFeedScheduler.start();
					((Button)v).setText("Stop");
				} else {
					zFeedScheduler.stop();
					((Button)v).setText("Start");
				}
				
			}
		});
	}
	
	private void fInitAcc() {
		zFacebook = new FacebookUtil(zActivity, zContext);
		zRenrenUtil = new RenrenUtil(zActivity, zContext);
		
		zFeedScheduler = new FeedScheduler(zActivity, zContext);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		zRenrenUtil.onComplete(requestCode, resultCode, data);
		zFacebook.onComplete(requestCode, resultCode, data);
		
	}

}

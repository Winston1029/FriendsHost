package com.moupress.app.friendshost;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.moupress.app.friendshost.activity.FeedPublishActivity;
import com.moupress.app.friendshost.sns.Renren.RenrenUtil;
import com.moupress.app.friendshost.sns.facebook.FacebookUtil;
import com.moupress.app.friendshost.sns.sina.SinaUtil;
import com.moupress.app.friendshost.util.FeedOrganisor;
import com.moupress.app.friendshost.util.FeedScheduler;

public class PubSub {
	private static Activity zActivity;
	private static Context zContext;
	
	public static FacebookUtil 	zFacebook;
	public static RenrenUtil 	zRenrenUtil;
	public static SinaUtil		zSinaUtil;
	public static FeedScheduler	zFeedScheduler;
	public static FeedOrganisor zFeedOrg;
	
	ListView uLstFeed;
	public PubSub(Context context, Activity activity) {
		PubSub.zContext = context;
		PubSub.zActivity = activity;
	}
	
	public PubSub(Activity activity) {
		PubSub.zActivity = activity;
		PubSub.zContext = activity.getBaseContext();
		
		uLstFeed = (ListView) zActivity.findViewById(R.id.uLstVFBFeed);
		fInitAcc();
		fInitFeedUI();
		fFBInitUI();
		fInitRenrenUI();
		fInitSinaUI();
		fInitSchedulerUI();
	}

	public PubSub(Service service) {
		PubSub.zContext = service.getBaseContext();
	}
	
	private ArrayAdapter<String> arrAdapterFeed;
	public ArrayAdapter<String> fGetArrAdapterFeed() {return arrAdapterFeed;}
	private void fInitFeedUI() {
		arrAdapterFeed = new ArrayAdapter<String>(zActivity,R.layout.feed_item);
		uLstFeed.setAdapter(arrAdapterFeed);
	}
	
	private void fFBInitUI() {        
        Button uBtnFBGetFeed = (Button) zActivity.findViewById(R.id.btn_getfbfeed);
        Button uBtnFBPubFeed = (Button) zActivity.findViewById(R.id.btn_pubfbfeed);
        
        uBtnFBGetFeed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//arrAdapterFeed.clear();				
				//zFacebook.fGetNewsFeed();
				zFacebook.fDisplayFeed();
				System.out.print("Feed Parse Complete");
				
			}
		});
        
        uBtnFBPubFeed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(zActivity, FeedPublishActivity.class);
				intent.putExtra(Const.SNS, Const.SNS_FACEBOOK);
				zActivity.startActivity(intent);
			}
		});
	}
	
	private void fInitRenrenUI() {
		Button uBtnRenrenGetFeed = (Button) zActivity.findViewById(R.id.btn_getRenrenfeed);
		Button uBtnRenrenPublishFeed = (Button) zActivity.findViewById(R.id.btn_pubrenrenfeed);
		
		uBtnRenrenGetFeed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//arrAdapterFeed.clear();
				//zRenrenUtil.fGetNewsFeed();
				zRenrenUtil.fDisplayRenrenFeed();
			}
		});
		
		uBtnRenrenPublishFeed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(zActivity, FeedPublishActivity.class);
				intent.putExtra(Const.SNS, Const.SNS_RENREN);
				zActivity.startActivity(intent);
			}
		});
	}
	
	private void fInitSinaUI() {
		Button uBtnSinaGetFeed = (Button) zActivity.findViewById(R.id.btn_getSinaFeed);
		
		uBtnSinaGetFeed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				arrAdapterFeed.clear();
				zSinaUtil.fGetNewsFeed();
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
		zFacebook = new FacebookUtil(this);
		zRenrenUtil = new RenrenUtil(this);
		zSinaUtil = new SinaUtil(this);
		
		zFeedScheduler = new FeedScheduler(this);
		zFeedOrg = new FeedOrganisor(this);
	}
	
	public FacebookUtil fGetFacebookUtil() {return zFacebook; }
	public RenrenUtil fGetRenrenUtil() {return zRenrenUtil; }
	public SinaUtil fGetSinaUtil() {return zSinaUtil;}
	public Activity fGetActivity() { return zActivity; }
	public Context fGetContext() { return zContext; }
	public FeedOrganisor fGetFeedOrganisor() {return zFeedOrg; }
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		zRenrenUtil.onComplete(requestCode, resultCode, data);
		zFacebook.onComplete(requestCode, resultCode, data);
	}

}

package com.moupress.app.friendshost;

import com.moupress.app.friendshost.R;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.moupress.app.friendshost.activity.FeedPublishActivity;
import com.moupress.app.friendshost.sns.FeedItem;
import com.moupress.app.friendshost.sns.Renren.RenrenUtil;
import com.moupress.app.friendshost.sns.facebook.FacebookUtil;
import com.moupress.app.friendshost.sns.sina.SinaUtil;
import com.moupress.app.friendshost.sns.twitter.TwitterUtil;
import com.moupress.app.friendshost.util.FeedOrganisor;

public class PubSub {
	private final String TAG = "PubSub"; 
	
	private static Activity zActivity;
	private static Context zContext;
	
	public static FacebookUtil 	zFacebook;
	public static RenrenUtil 	zRenrenUtil;
	public static SinaUtil		zSinaUtil;
	public static TwitterUtil   zTwitterUtil;
	public static FeedOrganisor zFeedOrg;
	
	ListView uLstFeed;
	public PubSub(Context context, Activity activity) {
		PubSub.zContext = context;
		PubSub.zActivity = activity;
	}
	
	public PubSub(Activity activity) {
		PubSub.zActivity = activity;
		PubSub.zContext = activity.getApplicationContext();
		
		uLstFeed = (ListView) zActivity.findViewById(R.id.uLstVFBFeed);
		fInitAcc();
		fInitFeedUI();
		fInitFeedUIPreview();
		fFBInitUI();
		fInitRenrenUI();
		fInitSinaUI();
		fInitTwitter();
		fInitPubUI();
	}

	public PubSub(Service service) {
		PubSub.zContext = service.getBaseContext();
	}
	
	private static ArrayAdapter<String> arrAdapterFeed;
	public ArrayAdapter<String> fGetArrAdapterFeed() {return arrAdapterFeed;}
	public void fInitFeedUI() {
		arrAdapterFeed = new ArrayAdapter<String>(zActivity,R.layout.feed_item);
		//PubSub.zActivity.registerForContextMenu(uLstFeed);
		uLstFeed.setAdapter(arrAdapterFeed);
	}
	
	private LstViewFeedAdapter arrAdapterFeedPreview;
	public void fInitFeedUIPreview() {
		arrAdapterFeedPreview = new LstViewFeedAdapter(zActivity, R.layout.feed_item_preview);
		uLstFeed.setAdapter(arrAdapterFeedPreview);
		uLstFeed.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				FeedItem feed = (FeedItem) arrAdapterFeedPreview.getItem(position);
				Log.i(TAG, " Name: "+feed.getsName()+" Msg: "+feed.getsMsgBody());
				return true;
			}});
	}
	public LstViewFeedAdapter fGetAdapterFeedPreview() {return arrAdapterFeedPreview;}
	
	private void fFBInitUI() {        
		ImageButton uBtnFBGetFeed = (ImageButton) zActivity.findViewById(R.id.imgBtn_Facebook);
        //Button uBtnFBPubFeed = (Button) zActivity.findViewById(R.id.btn_pubfbfeed);
        
        uBtnFBGetFeed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//arrAdapterFeed.clear();				
				//zFacebook.fGetNewsFeed();
				//fInitFeedUIPreview();
				zFacebook.fDisplayFeed();
				System.out.print("Feed Parse Complete");
			}
		});
	}
	
	private void fInitRenrenUI() {
		ImageButton uBtnRenrenGetFeed = (ImageButton) zActivity.findViewById(R.id.imgBtn_Renren);
		//Button uBtnRenrenPublishFeed = (Button) zActivity.findViewById(R.id.btn_pubrenrenfeed);
		
		uBtnRenrenGetFeed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//arrAdapterFeed.clear();
				//zRenrenUtil.fGetNewsFeed();
				//fInitFeedUI();
				//fInitFeedUIPreview();
				zRenrenUtil.fDisplayRenrenFeed();
			}
		});
	}
	
	private void fInitSinaUI() {
		ImageButton uBtnSinaGetFeed = (ImageButton) zActivity.findViewById(R.id.imgBtn_Sina);
		
		uBtnSinaGetFeed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//arrAdapterFeed.clear();
				//zSinaUtil.fGetNewsFeed();
				//fInitFeedUI();
				//fInitFeedUIPreview();
				zSinaUtil.fDisplaySinaFeed();
			}
		});
	}
	
	private void fInitTwitter() {
		ImageButton uBtnTwitterGetFeed = (ImageButton) zActivity.findViewById(R.id.imgBtn_Twitter);
		
		uBtnTwitterGetFeed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				zTwitterUtil.fDisplayTwitterFeed();
			}
		});
	}
	
	private void fInitPubUI() {
		ImageButton uBtnPubFeed = (ImageButton) zActivity.findViewById(R.id.imgBtn_PubFeed);
		
		uBtnPubFeed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(zActivity, FeedPublishActivity.class);
				intent.putExtra(Const.SNS, Const.SNS_RENREN);
				zActivity.startActivity(intent);				
			}
		});
	}

	private void fInitAcc() {
		zFacebook = new FacebookUtil(this);
		zRenrenUtil = new RenrenUtil(this);
		zSinaUtil = new SinaUtil(this);
		zTwitterUtil = new TwitterUtil(this);
		
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

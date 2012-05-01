package com.moupress.app.friendshost;

import com.moupress.app.friendshost.R;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.moupress.app.friendshost.activity.FeedDetailViewActivity;
import com.moupress.app.friendshost.activity.FeedPublishActivity;
import com.moupress.app.friendshost.activity.FeedResendActivity;
import com.moupress.app.friendshost.sns.FeedEntry;
import com.moupress.app.friendshost.sns.Renren.RenrenUtil;
import com.moupress.app.friendshost.sns.facebook.FacebookUtil;
import com.moupress.app.friendshost.sns.sina.SinaUtil;
import com.moupress.app.friendshost.sns.twitter.TwitterUtil;
import com.moupress.app.friendshost.ui.UIManager;
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
	
	public static UIManager uiMgr;
	
	private String displayedSns;
	
	ListView uLstFeed;
	public PubSub(Context context, Activity activity) {
		PubSub.zContext = context;
		PubSub.zActivity = activity;
	}
	
	public PubSub(Activity activity) {
		PubSub.zActivity = activity;
		PubSub.zContext = activity.getApplicationContext();
		
		uLstFeed = (ListView) zActivity.findViewById(R.id.uLstVFBFeed);
		fInitUIMgr();
		fInitAcc();
		fInitFeedUIPreview();
		fFBInitUI();
		fInitRenrenUI();
		fInitSinaUI();
		fInitTwitter();
		fInitPubUI();
	}

	private void fInitUIMgr() {
		
		uiMgr = new UIManager(zActivity);
		
	}

	public PubSub(Service service) {
		PubSub.zContext = service.getBaseContext();
	}
	
	private LstViewFeedAdapter arrAdapterFeedPreview;
	public LstViewFeedAdapter fGetAdapterFeedPreview() {return arrAdapterFeedPreview;}
	public void fInitFeedUIPreview() {
		arrAdapterFeedPreview = new LstViewFeedAdapter(zActivity, R.layout.feed_item_preview);
		uLstFeed.setAdapter(arrAdapterFeedPreview);
		uLstFeed.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				FeedEntry feed = (FeedEntry) arrAdapterFeedPreview.getItem(position);
				//Log.i(TAG, " Name: "+feed.getsName()+" Msg: "+feed.getsMsgBody());
				fFeedResendUI(feed);
				return true;
			}
		});
		uLstFeed.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				FeedEntry feed = (FeedEntry) arrAdapterFeedPreview.getItem(position);
				fFeedDisplayDetailUI(feed);
			}
		});

	}
	
	//Feed Resend UI
	public void fFeedResendUI(FeedEntry feed)
	{
		Intent intent = new Intent(zActivity, FeedResendActivity.class);
		intent.putExtra(Const.FEED_ITEM, feed);
		intent.putExtra(Const.SNS, this.displayedSns);
		zActivity.startActivityForResult(intent, Const.FEED_RESEND_REQ_CD);
	}
	
	//Feed Display Detail View UI
	public void fFeedDisplayDetailUI(FeedEntry feed)
	{
		Intent intent = new Intent(zActivity,FeedDetailViewActivity.class);
		intent.putExtra(Const.FEED_ITEM, feed);
		intent.putExtra(Const.SNS, this.displayedSns);
		zActivity.startActivity(intent);
	}
	
	//Init Facebook UI
	private void fFBInitUI() {
		ImageButton uBtnFBGetFeed = (ImageButton) zActivity.findViewById(R.id.imgBtn_Facebook);
        
        uBtnFBGetFeed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				displayedSns = Const.SNS_FACEBOOK;
				zFacebook.fDisplayFeed();
			}
		});
	}
	
	private void fInitRenrenUI() {
		ImageButton uBtnRenrenGetFeed = (ImageButton) zActivity.findViewById(R.id.imgBtn_Renren);
		
		uBtnRenrenGetFeed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				displayedSns = Const.SNS_RENREN;
				zRenrenUtil.fDisplayRenrenFeed();
			}
		});
	}
	
	private void fInitSinaUI() {
		ImageButton uBtnSinaGetFeed = (ImageButton) zActivity.findViewById(R.id.imgBtn_Sina);
		
		uBtnSinaGetFeed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				displayedSns = Const.SNS_SINA;
				zSinaUtil.fDisplaySinaFeed();
			}
		});
	}
	
	private void fInitTwitter() {
		ImageButton uBtnTwitterGetFeed = (ImageButton) zActivity.findViewById(R.id.imgBtn_Twitter);
		
		uBtnTwitterGetFeed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				displayedSns = Const.SNS_TWITTER;
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
		//total 13M
		zFacebook = new FacebookUtil(this); //10M , 17ps
		zRenrenUtil = new RenrenUtil(this); //6.4M, 16ps
		zSinaUtil = new SinaUtil(this); //6.8M, 9ps
		zTwitterUtil = new TwitterUtil(this); // 7.0M, 10ps
		
		zFeedOrg = new FeedOrganisor(this);
	}
	
	public FacebookUtil fGetFacebookUtil() {return zFacebook; }
	public RenrenUtil fGetRenrenUtil() {return zRenrenUtil; }
	public SinaUtil fGetSinaUtil() {return zSinaUtil;}
	public Activity fGetActivity() { return zActivity; }
	public Context fGetContext() { return zContext; }
	public FeedOrganisor fGetFeedOrganisor() {return zFeedOrg; }
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == Const.FEED_RESEND_REQ_CD)
		{
			Log.i(TAG, "Feed Resend is called back!" + resultCode);
			if(resultCode == Activity.RESULT_OK)
			{
				String sns = data.getStringExtra(Const.SNS);
				FeedEntry feed = data.getParcelableExtra(Const.FEED_ITEM);
				if(sns.equals(Const.SNS_FACEBOOK))
				{
					zFacebook.fResend(feed);
				} 
				else if (sns.equals(Const.SNS_RENREN))
				{
					zRenrenUtil.fResend(feed);
				}
				else if (sns.equals(Const.SNS_TWITTER))
				{
					zTwitterUtil.fResend(feed);
				} else if (sns.equals(Const.SNS_SINA)) {
					zSinaUtil.fResend(feed);
				}
			}
		}
		else 
		{
			zRenrenUtil.onComplete(requestCode, resultCode, data);
			zFacebook.onComplete(requestCode, resultCode, data);
		}
	}
}

package com.moupress.app.friendshost;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.moupress.app.friendshost.activity.FHDialogActivity;
import com.moupress.app.friendshost.activity.FeedDetailViewActivity;
import com.moupress.app.friendshost.activity.FeedPublishActivity;
import com.moupress.app.friendshost.activity.LstViewFeedAdapter;
import com.moupress.app.friendshost.service.FeedRetrieveServiceConnection;
import com.moupress.app.friendshost.sns.FeedEntry;
import com.moupress.app.friendshost.sns.SnsOrg;
import com.moupress.app.friendshost.sns.Renren.RenrenUtil;
import com.moupress.app.friendshost.sns.facebook.FacebookUtil;
import com.moupress.app.friendshost.ui.MainUIView;
import com.moupress.app.friendshost.ui.listeners.ContentViewListener;
import com.moupress.app.friendshost.ui.listeners.TitleBarListener;
import com.moupress.app.friendshost.util.FeedOrganisor;


public class PubSub {
	private final String TAG = "PubSub"; 
	
	private static Activity zActivity;
	private static Context zContext;
	
    //public static FacebookUtil 	zFacebook;
	//public static RenrenUtil 	zRenrenUtil;
	//public static SinaUtil		zSinaUtil;
	//public static TwitterUtil   zTwitterUtil;

	public static FeedOrganisor zFeedOrg;
	public static SnsOrg 		zSnsOrg;
	
	//public static UIManager uiMgr;
	private MainUIView mainUIView;
	
	//private static String displayedSns;
	
	//private ListView uLstFeed;
	
	//====Service Connection==============
	private FeedRetrieveServiceConnection svcConn;
	
	public PubSub(Context context, Activity activity) {
		PubSub.zContext = context;
		PubSub.zActivity = activity;
	}
	
	public PubSub(Activity activity) {
		PubSub.zActivity = activity;
		PubSub.zContext = activity.getApplicationContext();
		
		//hardcoded here to avoid error
		//displayedSns = Const.SNS_RENREN;
		
        //uLstFeed = (ListView) zActivity.findViewById(R.id.uLstVFBFeed);
		//fInitUIMgr();
		fInitAcc();
		fInitMainUI();
		fLoadMainUI();
		//fInitFeedUIPreview();
		//fFBInitUI();
		//fInitRenrenUI();
		//fInitSinaUI();
		//fInitTwitter();
		//fInitPubUI();
		
		//fInitSvc();
	}

//        private void fInitUIMgr() {
//		
//		uiMgr = new UIManager(zActivity);
//		
//	}


	//===================Service Connection Methods =========
	public void fBindSvc() {
		// TODO Auto-generated method stub
		this.svcConn = new FeedRetrieveServiceConnection(this);
		this.svcConn.BindToService();
	}
	
	public void UnBindToService() {
		// TODO Auto-generated method stub
		if(this.svcConn != null)
			this.svcConn.UnBindToService();
	}

	//=====================Main UI Initialization=================
	private void fInitMainUI() {
		mainUIView = new MainUIView();
		mainUIView.InitTitle(PubSub.zActivity, titleBarListener);
		mainUIView.InitContent(PubSub.zActivity, contentViewListener);
	}
	
	
	//=====================Main UI Load =========================
	private void fLoadMainUI(){
		
		if(mainUIView != null)
		{
			Bundle snsFeedBundle = new Bundle();
			snsFeedBundle.putCharSequenceArrayList(Const.SNS_SIGN_ON, zSnsOrg.GetSignOnSnsNames());
			this.mainUIView.LoadView(snsFeedBundle);
			
		}
	}
	
	ContentViewListener contentViewListener = new ContentViewListener()
	{
		
	};
	
	TitleBarListener titleBarListener = new TitleBarListener()
	{

		@Override
		public void OnTitleBarButtonClick(String viewName, int btnId, String snsName) {
			
			if(viewName.equals(Const.VIEW_MAIN) && btnId == 1)
			{
				Intent intent = new Intent(zActivity, FeedPublishActivity.class);
				intent.putExtra(Const.SNS, snsName);
				zActivity.startActivity(intent);	
			}
		}
		
	};

	public PubSub(Service service) {
		PubSub.zContext = service.getBaseContext();
	}
	
	private LstViewFeedAdapter arrAdapterFeedPreview;
	public LstViewFeedAdapter fGetAdapterFeedPreview() {return arrAdapterFeedPreview;}
//	public void fInitFeedUIPreview() {
//		arrAdapterFeedPreview = new LstViewFeedAdapter(zActivity, R.layout.feed_item_preview);
//		uLstFeed.setAdapter(arrAdapterFeedPreview);
//		uLstFeed.setItemsCanFocus(true);
//		uLstFeed.setOnItemLongClickListener(new OnItemLongClickListener(){
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				FeedEntry feed = (FeedEntry) arrAdapterFeedPreview.getItem(position);
//				//Log.i(TAG, " Name: "+feed.getsName()+" Msg: "+feed.getsMsgBody());
//				fFeedResendUI(feed);
//				return true;
//			}
//		});
		
//		uLstFeed.setOnItemClickListener(new OnItemClickListener() {
//			
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				FeedEntry feed = (FeedEntry) arrAdapterFeedPreview.getItem(position);
//				fFeedDisplayDetailUI(feed);
//			}
//		});

//	}
	
//	public static void setSNSDisplayed(String snsName) {
//		displayedSns = snsName;
//	}
	
	//Feed Resend UI
//	public void fFeedResendUI(FeedEntry feed, String snsName)
//	{
//		Intent intent = new Intent(zActivity, FHDialogActivity.class);
//		//intent.putExtra(Const.FEED_ITEM, feed);
//		intent.putExtra(Const.FID, feed.getsID());
//		intent.putExtra(Const.SNS, snsName);
//		zActivity.startActivityForResult(intent, Const.FEED_RESEND_REQ_CD);
//	}
	
	//Feed Display Detail View UI
	public void fFeedDisplayDetailUI(FeedEntry feed, String snsName)
	{
		Intent intent = new Intent(zActivity,FeedDetailViewActivity.class);
//		intent.putExtra(Const.FEED_ITEM, feed);
//		if (feed.getzComments() != null && feed.getzComments().size() > 0) {
//			intent.putExtra(Const.COMMENTS, feed.getzComments());
//		}
		
		intent.putExtra(Const.FID, feed.getsID());
		intent.putExtra(Const.SNS, snsName);
		zActivity.startActivity(intent);
	}
	
//Init Facebook UI
//	private void fFBInitUI() {
//		ImageButton uBtnFBGetFeed = (ImageButton) zActivity.findViewById(R.id.imgBtn_Facebook);
//        
//        uBtnFBGetFeed.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				displayedSns = Const.SNS_FACEBOOK;
//				zFacebook.fDisplayFeed();
//			}
//		});
//	}
//	
//	private void fInitRenrenUI() {
//		ImageButton uBtnRenrenGetFeed = (ImageButton) zActivity.findViewById(R.id.imgBtn_Renren);
//		
//		uBtnRenrenGetFeed.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				displayedSns = Const.SNS_RENREN;
//				zRenrenUtil.fDisplayRenrenFeed();
//			}
//		});
//	}
//	
//	private void fInitSinaUI() {
//		ImageButton uBtnSinaGetFeed = (ImageButton) zActivity.findViewById(R.id.imgBtn_Sina);
//		
//		uBtnSinaGetFeed.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				displayedSns = Const.SNS_SINA;
//				zSinaUtil.fDisplaySinaFeed();
//			}
//		});
//	}
//	
//	private void fInitTwitter() {
//		ImageButton uBtnTwitterGetFeed = (ImageButton) zActivity.findViewById(R.id.imgBtn_Twitter);
//		
//		uBtnTwitterGetFeed.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				displayedSns = Const.SNS_TWITTER;
//				zTwitterUtil.fDisplayTwitterFeed();
//			}
//		});
//	}
//	
//	private void fInitPubUI() {
//		ImageButton uBtnPubFeed = (ImageButton) zActivity.findViewById(R.id.imgBtn_PubFeed);
//		
//		uBtnPubFeed.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(zActivity, FeedPublishActivity.class);
//				intent.putExtra(Const.SNS, Const.SNS_RENREN);
//				zActivity.startActivity(intent);				
//			}
//		});
//	}


	private void fInitAcc() {
		//total 13M
//        zFacebook = new FacebookUtil(this); //10M , 17ps
//		zRenrenUtil = new RenrenUtil(this); //6.4M, 16ps
//		zSinaUtil = new SinaUtil(this); //6.8M, 9ps
//		zTwitterUtil = new TwitterUtil(this); // 7.0M, 10ps
		zFeedOrg = new FeedOrganisor(this);
		zSnsOrg = new SnsOrg(this);
	}
	
//	public FacebookUtil fGetFacebookUtil() {return zFacebook; }
//	public RenrenUtil fGetRenrenUtil() {return zRenrenUtil; }
//	public SinaUtil fGetSinaUtil() {return zSinaUtil;}
	public Activity fGetActivity() { return zActivity; }
	public Context fGetContext() { return zContext; }
	public FeedOrganisor fGetFeedOrganisor() {return zFeedOrg; }
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == Const.FEED_RESEND_REQ_CD)
		{
			Log.i(TAG, "Feed Resend is called back!" + resultCode);
			if(resultCode == Activity.RESULT_OK)
			{
				String snsName = data.getStringExtra(Const.SNS);
				//FeedEntry feed = data.getParcelableExtra(Const.FEED_ITEM);
				String feed_id = data.getStringExtra(Const.FID);
				//FeedEntry feed = zFeedOrg.fGetFeedByID(displayedSns, feed_id);
				FeedEntry feed = zFeedOrg.fGetFeedByID(snsName, feed_id);
				
//				if(sns.equals(Const.SNS_FACEBOOK))
//				{
//					zFacebook.fResend(feed);
//				} 
//				else if (sns.equals(Const.SNS_RENREN))
//				{
//					zRenrenUtil.fResend(feed);
//				}
//				else if (sns.equals(Const.SNS_TWITTER))
//				{
//					zTwitterUtil.fResend(feed);
//				} else if (sns.equals(Const.SNS_SINA)) {
//					zSinaUtil.fResend(feed);
//				}
				zSnsOrg.GetSnsInstance(snsName).fResend(feed);
			}
		}
		else if(requestCode == Const.CD_REQ_DIALOG)
		{
		   if(resultCode == Activity.RESULT_OK)
		   {
			   int index = data.getIntExtra(Const.SETTING_BASIC_GROUPS[0]+"_SET", -1);
			   this.svcConn.SendMessage(Const.SERVICE_UPDATE_FREQ, index, 0);
			   mainUIView.DialogCallBack(data);
		   }
		}
		else
		{  
		//			zRenrenUtil.onComplete(requestCode, resultCode, data);
		//			zFacebook.onComplete(requestCode, resultCode, data);
				((RenrenUtil)zSnsOrg.GetSnsInstance(Const.SNS_RENREN)).onComplete(requestCode, resultCode, data);
				((FacebookUtil)zSnsOrg.GetSnsInstance(Const.SNS_FACEBOOK)).onComplete(requestCode, resultCode, data);
		}

	}

	
	
}

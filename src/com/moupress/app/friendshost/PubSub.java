package com.moupress.app.friendshost;

import java.util.ArrayList;

import com.moupress.app.util.Renren.FeedElement;
import com.moupress.app.util.Renren.FeedExtractRequestParam;
import com.moupress.app.util.Renren.FeedExtractRequestResponseBean;
import com.moupress.app.util.Renren.FeedListAdapter;
import com.moupress.app.util.Renren.RenrenUtil;
import com.moupress.app.util.facebook.FBHomeFeed;
import com.moupress.app.util.facebook.FBHomeFeedEntry;
import com.moupress.app.util.facebook.FacebookUtil;
import com.renren.android.AbstractRequestListener;
import com.renren.android.AsyncRenren;
import com.renren.android.RenrenAuthError;
import com.renren.android.RenrenAuthListener;
import com.renren.android.RenrenError;




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
	
	private FacebookUtil zFacebook;
	private RenrenUtil renrenUtil;
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
		fInitUI();
		RenrenInitAcct();
		RenrenInitUI();
	}

	//private FeedListAdapter arrAdapterRenrenFeed;
	private FeedListAdapter arrAdapterRenrenFeed;
	private ArrayList<FeedElement> feedList;
	private void RenrenInitAcct()
	{
		renrenUtil = new RenrenUtil(Const.API_KEY, Const.SECRET_KEY, Const.APP_ID, zActivity);
	}
	
	private void RenrenInitUI() {

		//uLstFeed = (ListView) zActivity.findViewById(R.id.uLstVFBFeed);
		feedList = new ArrayList<FeedElement>();
		arrAdapterRenrenFeed = new FeedListAdapter(feedList,zActivity);
		
		
		Button uBtnRenrenGetFeed = (Button) zActivity.findViewById(R.id.btn_getRenrenfeed);
		uBtnRenrenGetFeed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				feedList.clear();
				uLstFeed.setAdapter(arrAdapterRenrenFeed);
				//System.out.println("Renren Feeds are loading ~");
				renrenUtil.authorize(zActivity, new RenrenAuthListener() {
					@Override
					public void onComplete(Bundle values) {
						//startApiList();
						System.out.println("Login Completed !");
						AsyncRenren asyncRenren = new AsyncRenren(renrenUtil);
						FeedExtractRequestParam param = new FeedExtractRequestParam("XML", "10", 1);
						AbstractRequestListener<FeedExtractRequestResponseBean> listener = new AbstractRequestListener<FeedExtractRequestResponseBean>(){
							@Override
							public void onComplete(final FeedExtractRequestResponseBean bean) {
								// 
								zActivity.runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										//editTextLog.setText(bean.toString());
										System.out.println("Feed Get Complete !");
										//feedList.clear();
										
										for(int i= 0; i<bean.getFeedList().size();i++)
										{
											//System.out.println(" Title Added "+bean.getFeedList().get(i).getTitle());
											//String msg = bean.getFeedList().get(i).getName()+" : "+bean.getFeedList().get(i).getTitle();
											//System.out.println(msg);
											feedList.add(bean.getFeedList().get(i));
										}
										System.out.println(" Number of Messages " + feedList.size());
										arrAdapterRenrenFeed.notifyDataSetChanged();
									}
								 }
							   );
								
							}

							@Override
							public void onRenrenError(RenrenError renrenError) {
								System.out.println(" Fault Extract Request Response Bean !");
							}

							@Override
							public void onFault(Throwable fault) {
								
								System.out.println(" Feed Extract Request Response Bean !");
								
							}};
						asyncRenren.getFeeds(param, listener);
					}

					@Override
					public void onRenrenAuthError(RenrenAuthError renrenAuthError) {
				
						System.out.println("Login Failed " + renrenAuthError.getError());
					}

					@Override
					public void onCancelLogin() {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onCancelAuth(Bundle values) {
						// TODO Auto-generated method stub
						
					}
				  }
				);
				
			}
		});
	}

	private ArrayAdapter<String> arrAdapterFBFeed;
	private void fInitUI() {
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

	private void fInitAcc() {
		zFacebook = new FacebookUtil(zActivity, zContext);
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		zFacebook.onComplete(requestCode, resultCode, data);		
	}
}

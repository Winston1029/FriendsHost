package com.moupress.app.friendshost.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.PubSub;
import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.activity.FHGuideActivity;
import com.moupress.app.friendshost.sns.SnsUtil;
import com.moupress.app.friendshost.sns.Listener.SnsEventListener;
import com.moupress.app.friendshost.ui.listeners.ContentViewListener;
import com.moupress.app.friendshost.ui.listeners.TitleBarListener;
import com.moupress.app.friendshost.uicomponent.SlidingPanel;
import com.moupress.app.friendshost.uicomponent.PullToRefreshListView;
import com.moupress.app.friendshost.uicomponent.PullToRefreshListView.OnRefreshListener;
import com.moupress.app.friendshost.uicomponent.TabPageIndicator;
import com.moupress.app.friendshost.uicomponent.SlidingPanel.PanelSlidingListener;
import com.moupress.app.friendshost.uicomponent.interfaces.TitleProvider;
import com.moupress.app.friendshost.util.Pref;
import com.moupress.app.friendshost.util.FlurryUtil;


/**
 * @author Li Ji
 *
 */
public class MainUIView extends View{
	private static final String TAG = "MainUIView";

	private ViewPager mPager;
	private TabPageIndicator mIndicator;
	private FragmentStatePagerAdapter mAdapter;
	private SlidingPanel slidingPanel;
	
	private LeftPanelView leftPanelView;
	
	//Interface between Presenter (MainUIView) and Model (SnsUtil)
	
	private SnsEventListener snsEventListener = new SnsEventListener()
	{

		@Override
		public void OnSnsUtilAdded(String snsName) {
			leftPanelView.RefreshView();
			RefreshView();
		}

		@Override
		public void OnSnsUtilRemoved(String snsName) {
			leftPanelView.RefreshView();
			RefreshView();
		}
	};
	
	
	public  MainUIView()
	{
		this.TitleLayoutId = R.layout.fh_title_bar;
		this.ContentLayoutId = R.layout.fh_main_ui;
	}

	
	
	@Override
	public void InitTitle(Activity activity, TitleBarListener titleBarListener) {
		super.InitTitle(activity, titleBarListener);
	     
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
	}

	private static Activity zActivity;
	
	@Override
	protected void RefreshView() 
	{
		if(mAdapter != null)
		{
			this.LoadData.clear();
			LoadData.putCharSequenceArrayList(Const.SNS_SIGN_ON, PubSub.zSnsOrg.GetSignOnSnsNames());
			this.mAdapter.notifyDataSetChanged();
			this.mIndicator.notifyDataSetChanged();
			//this.mIndicator.setViewPager(mPager);
			//this.LoadView(LoadData);
			
		}
	}

	@Override
	public void InitContent(Activity activity, ContentViewListener contentViewListener) {
		// TODO Auto-generated method stub
		super.InitContent(activity, contentViewListener);
		
		if(ContentLayoutId != -1)
			activity.setContentView(ContentLayoutId);
		
		//if(TitleLayoutId != -1)
			//activity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, this.TitleLayoutId);
		
		
		if(activity instanceof FragmentActivity) 
			mAdapter = new SnsAdapter(((FragmentActivity)activity).getSupportFragmentManager());
		
		mPager = (ViewPager) activity.findViewById(R.id.snsPager);
		mIndicator = (TabPageIndicator) activity.findViewById(R.id.snsTabIndicator);
		
		zActivity = activity;
		InitSlidingPanel(activity);
		InitTitleButtons(activity);
		InitLeftPanelView(activity);
		InitGuide(activity);
	}
		private void InitGuide(Activity activity) {
		
		if(!Pref.getMyBoolPref(activity.getApplicationContext(), Const.VIEW_GUIDE))
		{
			this.launchGuide();
		}
	}



	@Override
	public void LoadView(Bundle loadData) {
		
		//Set Adapter for View Pages on the MainUI
		this.LoadData = loadData;
		mPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mPager);
	}
	
	public FragmentStatePagerAdapter getAdapter() {
		return mAdapter;
	}


    private void InitSlidingPanel(Activity activity)
    {
		slidingPanel = (SlidingPanel) activity.findViewById(R.id.slidepanel);
		slidingPanel.SetAlignViewId(R.id.leftPanelLayout);
		slidingPanel.setPanelSlidingListener(new PanelSlidingListener(){

			@Override
			public void onSlidingDownEnd() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSlidingUpEnd() {
				// TODO Auto-generated method stub
				
			}});
    }
    
    public boolean SlidePanelBack()
    {
    	return slidingPanel.Slide2Right();
    }
    
    private void InitTitleButtons(final Activity activity)
    {
    	ImageButton btnSetting = (ImageButton) activity.findViewById(R.id.leftpanelbtn);
    	btnSetting.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(android.view.View arg0) {
				slidingPanel.Slide2Left();
				//slidingPanel.setVisibility(android.view.View.GONE);
				
			}});
    	
    	ImageButton btnPub = (ImageButton) activity.findViewById(R.id.writefeedbtn);
    	btnPub.setOnClickListener(new OnClickListener()
    	{

			@Override
			public void onClick(android.view.View v) {
				if(!slidingPanel.Slide2Right())
				titleBarListener.OnTitleBarButtonClick(Const.VIEW_MAIN, 1, Const.SNS_RENREN);
			}
    		
    	});
    	
    	ImageButton btnRefresh = (ImageButton) activity.findViewById(R.id.refreshbtn);
//    	btnRefresh.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(android.view.View arg0) {
//				launchGuide();
//			}});
    	btnRefresh.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(android.view.View v) {
				//slidingPanel.Slide2Right();
				if(!slidingPanel.Slide2Right())
				{
					int index = mPager.getCurrentItem();
					//String snsName = Const.SNSGROUPS[index];
					//final ListView lv = (SnsFeedListFragment)mPager.ge ().getListView();
					SnsFeedListFragment lstFrag = (SnsFeedListFragment) ((FragmentActivity)activity).getSupportFragmentManager().findFragmentByTag(((SnsAdapter)mAdapter).getTitle(index));
					
					if(lstFrag != null)
					{
						final ListView lv = (ListView) lstFrag.getListView();
						
						lv.post(new  Runnable(){
		
							@Override
							public void run() {
								lv.setSelection(0);						
							}});
					}	
				}
			}});
    		  	
    }
    
    	
	private void launchGuide() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this.zActivity,FHGuideActivity.class);
		this.zActivity.startActivity(intent);
		
	}
    
	private void InitLeftPanelView(Activity activity) {
		
		leftPanelView = new LeftPanelView(snsEventListener);
		leftPanelView.InitContent(activity, contentViewListener);
		leftPanelView.LoadView(null);
	}
    

	public class SnsAdapter extends FragmentStatePagerAdapter implements TitleProvider
	{
		FragmentManager fm;
		//Bundle framentBundle;
		
		
		public SnsAdapter(FragmentManager fm) {
			super(fm);
			this.fm = fm;
			//framentBundle = new Bundle();
		}


		@Override
		public int getItemPosition(Object object) {
			//return super.getItemPosition(object);
			int status = PagerAdapter.POSITION_NONE;
			
			return status;
		}




		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			SnsFeedListFragment snsFeedListFragment = SnsFeedListFragment.newInstance(LoadData.getCharSequenceArrayList(Const.SNS_SIGN_ON).get(position).toString());
			
			try{
				fm.beginTransaction().add(snsFeedListFragment, LoadData.getCharSequenceArrayList(Const.SNS_SIGN_ON).get(position).toString().toUpperCase()).commit();
			}
			catch(Exception e)
			{
				Log.i(TAG, "Error + "+e.toString());
			}
			
			return snsFeedListFragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return LoadData.getCharSequenceArrayList(Const.SNS_SIGN_ON).size();
			//return Const.SNSGROUPS.length;
		}

		
		@Override
		public String getTitle(int position) {
			// TODO Auto-generated method stub
			//return MainUIView.CONTENT[position % MainUIView.CONTENT.length].toUpperCase();
			ArrayList<CharSequence> titles = LoadData.getCharSequenceArrayList(Const.SNS_SIGN_ON);
			
			return titles.get(position%titles.size()).toString().toUpperCase();
		}


		@Override
		public int getTitleCount() {
			// TODO Auto-generated method stub
			return LoadData.getCharSequenceArrayList(Const.SNS_SIGN_ON).size();
		}
		
	}
	
	
	public static class SnsFeedListFragment extends ListFragment
	{
		 //private android.view.View v;
		 private String snsName;
		 private PullToRefreshListView lstViewFeedPreview;
		 private int iCountScrollEvent;
		 private boolean bScrolling;
		 private int iPrevScrollState;
		 
//		 @Override
//		public ListView getListView() {
//			// TODO Auto-generated method stub
//			//return super.getListView();
//			 return (ListView) v;
//		}

		static SnsFeedListFragment newInstance(String sns)
		 {
			 SnsFeedListFragment snsFeedListFragment = new SnsFeedListFragment();
			 
			 Bundle args = new Bundle();
			 args.putString(Const.SNS, sns);
			 snsFeedListFragment.setArguments(args);
			 return snsFeedListFragment;
		 }

		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
			snsName = getArguments() != null ? getArguments().getString("sns"): null;
		}

		@Override
		public android.view.View onCreateView(LayoutInflater inflater,
				ViewGroup container, Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			//return super.onCreateView(inflater, container, savedInstanceState);
			android.view.View v = inflater.inflate(R.layout.fh_feed_list, container, false);
			
			return v;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
			final SnsUtil snsUtil = PubSub.zSnsOrg.GetSnsInstance(this.snsName);
			this.setListAdapter(snsUtil.getFeedAdapter());
			snsUtil.RefreshAdapter();
			lstViewFeedPreview = (PullToRefreshListView) this.getListView();
			
			lstViewFeedPreview.setOnRefreshListener(new OnRefreshListener() {
				
				@Override
				public void onRefresh() {
					// FlurryUtil
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
					FlurryUtil.logEvent(TAG + ":SnsFeedListFragment:onRefresh", snsName + "," +sdf.format(new Date()));
					new GetDataTask().execute();
				}
			});
			
			iCountScrollEvent = 0;
			bScrolling = false;
			iPrevScrollState = OnScrollListener.SCROLL_STATE_IDLE;
			lstViewFeedPreview.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					if ((iPrevScrollState == OnScrollListener.SCROLL_STATE_IDLE
							&& scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
							|| (iPrevScrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL 
									&& scrollState == OnScrollListener.SCROLL_STATE_FLING)) {
						bScrolling = true;
					} else {
						bScrolling = false;
					}
					iPrevScrollState = scrollState;
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					boolean loadOlder = firstVisibleItem + visibleItemCount >= totalItemCount;
					//boolean loadNewer = firstVisibleItem == 0;

					if(loadOlder && bScrolling) {
						//still some bug but only trigger 2 times at the bottom now.
			            //fGetAdapterFeedPreview() += visibleItemCount; // or any other amount
			            //fGetAdapterFeedPreview().notifyDataSetChanged();
						iCountScrollEvent++;
			        	Toast.makeText(zActivity, "Loading More Feeds",
			        			Toast.LENGTH_LONG).show();
			        	//PubSub.zFeedOrg.fGet10MoreNewsFeed(snsName);
			        	PubSub.zSnsOrg.GetSnsInstance(snsName).RefreshAdapter10MoreFeed();
			        	bScrolling = false;
			        }				
				}
			});
		}
		
		private class GetDataTask extends AsyncTask<Void, Void, String[]> {

	        @Override
	        protected String[] doInBackground(Void... params) {
	            // Simulates a background job.
	        	String[] mStrings = {
	                    "aa", "bb"};
				
				try {
					PubSub.zSnsOrg.GetSnsInstance(snsName).fGetNewsFeed(zActivity);
					//sleep a while to wait for async news feed getting return
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	            return mStrings;
	        }

	        @Override
	        protected void onPostExecute(String[] result) {
	            //mListItems.addFirst("Added after refresh...");
	        	Toast.makeText(zActivity, "Refresh Complete " +snsName, 
						Toast.LENGTH_SHORT).show();
	        	PubSub.zSnsOrg.GetSnsInstance(snsName).RefreshAdapter();
	            // Call onRefreshComplete when the list has been refreshed.
	        	try{
	            ((PullToRefreshListView) getListView()).onRefreshComplete();
	        	}
	        	catch(Exception e)
	        	{
	        		Log.i(TAG, "Main UI View "+ e.getMessage());
	        	}

	            super.onPostExecute(result);
	        }
	    }
		 
	}

	public void DialogCallBack(Intent data) {
		// TODO Auto-generated method stub
		this.leftPanelView.DialogCallBack(data);
	}
	
}

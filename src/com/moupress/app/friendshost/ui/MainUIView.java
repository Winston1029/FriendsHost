package com.moupress.app.friendshost.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;


import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.PubSub;
import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.sns.Listener.SnsEventListener;
import com.moupress.app.friendshost.ui.listeners.DetailViewListener;
import com.moupress.app.friendshost.ui.listeners.TitleBarListener;
import com.moupress.app.friendshost.uicomponent.SlidingPanel;
import com.moupress.app.friendshost.uicomponent.PullToRefreshListView;
import com.moupress.app.friendshost.uicomponent.PullToRefreshListView.OnRefreshListener;
import com.moupress.app.friendshost.uicomponent.TabPageIndicator;
import com.moupress.app.friendshost.uicomponent.SlidingPanel.PanelSlidingListener;
import com.moupress.app.friendshost.uicomponent.interfaces.TitleProvider;



/**
 * @author Li Ji
 *
 */
public class MainUIView extends View{
	

	private ViewPager mPager;
	private TabPageIndicator mIndicator;
	private FragmentPagerAdapter mAdapter;
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
		this.DetailLayoutId = R.layout.fh_main_ui;
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
		}
	}

	@Override
	public void InitDetail(Activity activity, DetailViewListener detailViewListener) {
		// TODO Auto-generated method stub
		super.InitDetail(activity, detailViewListener);
		
		if(DetailLayoutId != -1)
			activity.setContentView(DetailLayoutId);
		
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
		
	}



	@Override
	public void LoadView(Bundle loadData) {
		
		//Set Adapter for View Pages on the MainUI
		this.LoadData = loadData;
		mPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mPager);
		
	}
	
	public FragmentPagerAdapter getAdapter() {
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
    
    
    private void InitTitleButtons(Activity activity)
    {
    	Button btnSetting = (Button) activity.findViewById(R.id.leftpanelbtn);
    	btnSetting.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(android.view.View arg0) {
				// TODO Auto-generated method stub
				slidingPanel.Slide2Left();
				//slidingPanel.setVisibility(android.view.View.GONE);
			}});
    	
    	Button btnPub = (Button) activity.findViewById(R.id.writefeedbtn);
    	btnPub.setOnClickListener(new OnClickListener()
    	{

			@Override
			public void onClick(android.view.View v) {
				// TODO Auto-generated method stub
				slidingPanel.Slide2Right();
			}
    		
    	});
    	
    	Button btnRefresh = (Button) activity.findViewById(R.id.refreshbtn);
    	btnRefresh.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(android.view.View v) {
				// TODO Auto-generated method stub
				slidingPanel.Slide2Right();
			}});	
    }
    
	private void InitLeftPanelView(Activity activity) {
		
		leftPanelView = new LeftPanelView(snsEventListener);
		leftPanelView.InitDetail(activity, detailViewListener);
		
	}
    

	public class SnsAdapter extends FragmentPagerAdapter implements TitleProvider
	{
		public SnsAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			//snsFeedListFragment = SnsFeedListFragment.newInstance(LoadData.getCharSequenceArrayList(Const.SNS_SIGN_ON).get(position).toString());
			return SnsFeedListFragment.newInstance(LoadData.getCharSequenceArrayList(Const.SNS_SIGN_ON).get(position).toString());
			//return snsFeedListFragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return LoadData.getCharSequenceArrayList(Const.SNS_SIGN_ON).size();
		}

		@Override
		public String getTitle(int position) {
			// TODO Auto-generated method stub
			//return MainUIView.CONTENT[position % MainUIView.CONTENT.length].toUpperCase();
			ArrayList<CharSequence> titles = LoadData.getCharSequenceArrayList(Const.SNS_SIGN_ON);
			
			
			return titles.get(position%titles.size()).toString().toUpperCase();
		}
		
//		public SnsFeedListFragment getSnsFeedListFragment() {
//			return snsFeedListFragment;
//		}
	}
	
	
	public static class SnsFeedListFragment extends ListFragment
	{
		 private String snsName;
		 private PullToRefreshListView lstViewFeedPreview;
		 private int iCountScrollEvent;
		 private boolean bScrolling;
		 private int iPrevScrollState;
		 
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
			this.setListAdapter(PubSub.zSnsOrg.GetSnsInstance(this.snsName).getFeedAdapter());
			PubSub.zSnsOrg.GetSnsInstance(snsName).RefreshAdapter();
			PubSub.setSNSDisplayed(snsName);
			lstViewFeedPreview = (PullToRefreshListView) this.getListView();
			lstViewFeedPreview.setOnRefreshListener(new OnRefreshListener() {
				
				@Override
				public void onRefresh() {
					// TODO Auto-generated method stub
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
	            ((PullToRefreshListView) getListView()).onRefreshComplete();

	            super.onPostExecute(result);
	        }
	    }
		 
	}


	
}

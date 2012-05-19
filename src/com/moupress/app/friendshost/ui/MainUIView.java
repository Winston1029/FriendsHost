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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;

import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.PubSub;
import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.ui.listeners.DetailViewListener;
import com.moupress.app.friendshost.ui.listeners.TitleBarListener;
import com.moupress.app.friendshost.uicomponent.PullToRefreshListView;
import com.moupress.app.friendshost.uicomponent.PullToRefreshListView.OnRefreshListener;
import com.moupress.app.friendshost.uicomponent.TabPageIndicator;
import com.moupress.app.friendshost.uicomponent.interfaces.TitleProvider;



/**
 * @author Li Ji
 *
 */
public class MainUIView extends View{
	
	private static final String[] CONTENT = new String[] { "Facebook", "Renren", "Weibo", "Twitter"};

	private ViewPager mPager;
	private TabPageIndicator mIndicator;
	private FragmentPagerAdapter mAdapter;
	
	
	
	public  MainUIView()
	{
		this.TitleLayoutId = R.layout.fh_title_bar;
		this.DetailLayoutId = R.layout.fh_main_ui;
	}

	
	
	@Override
	public void InitTitle(Activity activity, TitleBarListener titleBarListener) {
		super.InitTitle(activity, titleBarListener);
		activity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	}


	private static Activity zActivity;

	@Override
	public void InitDetail(Activity activity, DetailViewListener detailViewListener) {
		// TODO Auto-generated method stub
		super.InitDetail(activity, detailViewListener);
		
		if(DetailLayoutId != -1)
			activity.setContentView(DetailLayoutId);
		
		if(TitleLayoutId != -1)
			activity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, this.TitleLayoutId);
		
		if(activity instanceof FragmentActivity) 
			mAdapter = new SnsAdapter(((FragmentActivity)activity).getSupportFragmentManager());
		
		mPager = (ViewPager) activity.findViewById(R.id.snsPager);
		mIndicator = (TabPageIndicator) activity.findViewById(R.id.snsTabIndicator);
		
		zActivity = activity;
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
			return MainUIView.CONTENT.length;
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
		 String snsName;
		 private PullToRefreshListView lstViewFeedPreview;
		 private static int iPrevScrollStatus;
		 private static int iLstViewScrollAction;
		 private static int iCntOnScrollEvents;
		 private static boolean bScrolled;
		 
		 static SnsFeedListFragment newInstance(String sns)
		 {
			 SnsFeedListFragment snsFeedListFragment = new SnsFeedListFragment();
			 
			 Bundle args = new Bundle();
			 args.putString(Const.SNS, sns);
			 PubSub.setSNSDisplayed(sns);
			 snsFeedListFragment.setArguments(args);
			 bScrolled = false;
			 iCntOnScrollEvents = 0;
			 iPrevScrollStatus = OnScrollListener.SCROLL_STATE_IDLE;
			 iLstViewScrollAction = Const.LISTVIEW_SCROLL_AT_TOP;
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
			
			lstViewFeedPreview = (PullToRefreshListView) this.getListView();
			lstViewFeedPreview.setOnRefreshListener(new OnRefreshListener() {
				
				@Override
				public void onRefresh() {
					// TODO Auto-generated method stub
					new GetDataTask().execute();
					
				}
			});
			
			lstViewFeedPreview.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
//					if (iPrevScrollStatus == OnScrollListener.SCROLL_STATE_FLING
//							&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
//						bScrolled = true;
//						iLstViewScrollAction = Const.LISTVIEW_SCROLL_TO_TOP;
//					} else if (iPrevScrollStatus == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
//							&& scrollState == OnScrollListener.SCROLL_STATE_FLING) {
//						bScrolled = true;
//						iLstViewScrollAction = Const.LISTVIEW_SCROLL_AT_TOP;
//					} else {
//						bScrolled = false;
//					}
//					iPrevScrollStatus = scrollState;
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					boolean loadOlder = firstVisibleItem + visibleItemCount >= totalItemCount;
					boolean loadNewer = firstVisibleItem == 0;

//					if (bScrolled) {
//						System.out.println("Scrolled");
//					}
//					if (loadNewer && iLstViewScrollAction == Const.LISTVIEW_SCROLL_AT_TOP) {
//						iCntOnScrollEvents++;
//						Toast.makeText(zActivity, "At Top "+iCntOnScrollEvents, 
//								Toast.LENGTH_SHORT).show();
//					}
//					if (loadNewer && bScrolled) {
//						iCntOnScrollEvents++;
//						Toast.makeText(zActivity, "Reach Top "+iCntOnScrollEvents, 
//								Toast.LENGTH_SHORT).show();
//					}
//					if (loadOlder && iLstViewScrollAction == Const.LISTVIEW_SCROLL_AT_TOP) {
//						iCntOnScrollEvents++;
//						Toast.makeText(zActivity, "At Bottom "+iCntOnScrollEvents, 
//								Toast.LENGTH_SHORT).show();
//					}
//			        if(loadOlder && bScrolled) {
//			            //fGetAdapterFeedPreview() += visibleItemCount; // or any other amount
//			            //fGetAdapterFeedPreview().notifyDataSetChanged();
//			        	Toast.makeText(zActivity, "Reach End "+iCntOnScrollEvents,
//			        			Toast.LENGTH_SHORT).show();
//			        }				
				}
			});
		}
		
		private class GetDataTask extends AsyncTask<Void, Void, String[]> {

	        @Override
	        protected String[] doInBackground(Void... params) {
	            // Simulates a background job.
	        	String[] mStrings = {
	                    "aa", "bb"};
	            //Thread.sleep(2000);
				PubSub.zSnsOrg.GetSnsInstance(snsName).fGetNewsFeed(zActivity);
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

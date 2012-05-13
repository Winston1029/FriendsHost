package com.moupress.app.friendshost.ui;

import java.util.ArrayList;

import android.app.Activity;
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

import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.PubSub;
import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.ui.listeners.DetailViewListener;
import com.moupress.app.friendshost.ui.listeners.TitleBarListener;
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
		
	}
	
	
	
	@Override
	public void LoadView(Bundle loadData) {
		
		//Set Adapter for View Pages on the MainUI
		this.LoadData = loadData;
		mPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mPager);
		
	}




	class SnsAdapter extends FragmentPagerAdapter implements TitleProvider
	{

		public SnsAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			return SnsFeedListFragment.newInstance(LoadData.getCharSequenceArrayList(Const.SNS_SIGN_ON).get(position).toString());
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
	}
	
	
	public static class SnsFeedListFragment extends ListFragment
	{
		 String snsName;
		 
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
		}
		 
	}
}

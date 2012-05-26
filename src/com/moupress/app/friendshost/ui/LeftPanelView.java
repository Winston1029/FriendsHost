package com.moupress.app.friendshost.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.PubSub;
import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.sns.Listener.SnsEventListener;
import com.moupress.app.friendshost.ui.listeners.ContentViewListener;
import com.moupress.app.friendshost.ui.listeners.TitleBarListener;

public class LeftPanelView extends View{
	
	private ExpandableListView settingExpLstV;
	private ExpandableListView feedBksExpLstV;
	
	private SnsEventListener snsEventListener;
	
	private SnsAccntLstAdapter snsAccntLstAdapter;
	
	private ExpandableListAdapter settingExpandableListAdapter;
	private ExpandableListAdapter feedBksExpandableListAdapter;
	
	public LeftPanelView(SnsEventListener snsEventListener) {
		this.snsEventListener = snsEventListener;
		
	}

	@Override
	public void RefreshView()
	{
		if(this.snsAccntLstAdapter != null)
		{
			this.snsAccntLstAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void InitContent(Activity activity,
			ContentViewListener detailViewListener) {
		// TODO Auto-generated method stub
		super.InitContent(activity, detailViewListener);
		
		this.snsAccntLstAdapter = new SnsAccntLstAdapter(activity);
		
		//Setting Expander List View
		settingExpLstV = (ExpandableListView) activity.findViewById(R.id.settingExpandLst);
		settingExpandableListAdapter = new ExpandableListAdapter(activity, Const.SETTING_BASIC);
		settingExpLstV.setAdapter(settingExpandableListAdapter);
		
		//Feedbacks Expander List View
		feedBksExpLstV = (ExpandableListView) activity.findViewById(R.id.feedbksExpandLst);
		feedBksExpandableListAdapter = new ExpandableListAdapter(activity, Const.SETTING_FEEDBACKS);
		feedBksExpLstV.setAdapter(feedBksExpandableListAdapter);
	}


	@Override
	public void InitTitle(Activity activity, TitleBarListener titleBarListener) {
		// TODO Auto-generated method stub
		super.InitTitle(activity, titleBarListener);
	}


	@Override
	protected void LoadView(Bundle loadData) {
		// TODO Auto-generated method stub
		//super.LoadView(loadData);
	}
	
	private class ExpandableListAdapter extends BaseExpandableListAdapter
	{
		private Activity activity;
		private LayoutInflater viewInflator;
		private String section;
		public ExpandableListAdapter(Activity activity, String section)
		{
			this.activity = activity;
			this.section = section;
			
			 viewInflator = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			
			if(this.section.equals(Const.SETTING_BASIC))
			{
				if(groupPosition == 0)
				{
					//return Const.SNSGROUPS[childPosition];
					return Const.SNSGROUPS;
				}
				else
				{
					return null;
				}
			}
			return null;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			
			if(this.section.equals(Const.SETTING_BASIC))
			{
				if(groupPosition == 0)
				{
					return childPosition;
				}
				else
				{
					return 0;
				}
			}
			
			return 0;
		}

		@Override
		public android.view.View getChildView(int groupPosition,
				int childPosition, boolean isLastChild,
				android.view.View convertView, ViewGroup parent) {
			
			if(this.section.equals(Const.SETTING_BASIC))
			{
				if(groupPosition == 0)
				{
					if(convertView == null)
						convertView = viewInflator.inflate(R.layout.fh_login_account_list, null);
					
					//TextView tv = (TextView) convertView.findViewById(R.id.snsLogin);
					//tv.setText(Const.SNSGROUPS[childPosition]);
					ListView listView = (ListView) convertView.findViewById(R.id.snsAccountsLst);
					listView.setAdapter(snsAccntLstAdapter);
					
					return convertView;
				}
				else
				{
					return null;
				}
				
			}
			return null;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			
			if(this.section.equals(Const.SETTING_BASIC))
			{
				if(groupPosition == 0)
				{
					//return Const.SNSGROUPS.length;
					return 1;
				}
				else
				{
					return 0;
				}
			}
			return 0;
		}

		@Override
		public Object getGroup(int groupPosition) {
			
			if(this.section.equals(Const.SETTING_BASIC))
			{
				return Const.SETTING_BASIC_GROUPS[groupPosition];
			}
			else if(this.section.equals(Const.SETTING_FEEDBACKS))
			{
				return Const.SETTING_FEEDBACKS_GROUPS[groupPosition];
			}
			
			return null;
			
		}

		@Override
		public int getGroupCount() {
			
			if(this.section.equals(Const.SETTING_BASIC))
			{
				return Const.SETTING_BASIC_GROUPS.length;
			}
			else if(this.section.equals(Const.SETTING_FEEDBACKS))
			{
				return Const.SETTING_FEEDBACKS_GROUPS.length;
			}
			
			return 0;
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public android.view.View getGroupView(int groupPosition,
				boolean isExpanded, android.view.View convertView,
				ViewGroup parent) {

			if(convertView == null)
        	{
        		convertView= viewInflator.inflate(R.layout.fh_setting_groups, null);
        	}
			
			TextView textView = (TextView) convertView.findViewById(R.id.groupTv);
            textView.setText(getGroup(groupPosition).toString());
            //ListView listView = (ListView) convertView.findViewById(R.id.snsAccountsLst);
            
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
	
	private class SnsAccntLstAdapter extends BaseAdapter
	{
		private LayoutInflater viewInflator;
		
		public SnsAccntLstAdapter(Activity activity)
		{
			viewInflator = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Const.SNSGROUPS.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return Const.SNSGROUPS[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public android.view.View getView(final int position,
				android.view.View convertView, ViewGroup parent) {
			
			
			if(convertView == null)
        	{
        		convertView= viewInflator.inflate(R.layout.fh_login_account_item, null);
        	}
			
			TextView textView = (TextView) convertView.findViewById(R.id.snsLogin);
            textView.setText(Const.SNSGROUPS[position]);
            //ListView listView = (ListView) convertView.findViewById(R.id.snsAccountsLst);
            final ImageView imgView = (ImageView) convertView.findViewById(R.id.snsStatusInd);
            
            setSnsStatus(imgView, PubSub.zSnsOrg.GetSnsInstance(Const.SNSGROUPS[position]).isSelected());
            
            convertView.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(android.view.View view) {
					
					PubSub.zSnsOrg.GetSnsInstance(Const.SNSGROUPS[position]).ToggleSelectSNS(snsEventListener);
					
				}});
            
			return convertView;
		}


		private void setSnsStatus(ImageView imgView, boolean selected) {
			
			if(selected)
			{
				imgView.setImageResource(R.drawable.chk_option);
			}
			else
			{
				imgView.setImageResource(R.drawable.unchk_option);
			}
		}
		
	};
	
	
	
	

}

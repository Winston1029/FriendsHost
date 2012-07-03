package com.moupress.app.friendshost.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.PubSub;
import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.activity.FHDialogActivity;
import com.moupress.app.friendshost.sns.Listener.SnsEventListener;
import com.moupress.app.friendshost.ui.listeners.ContentViewListener;
import com.moupress.app.friendshost.ui.listeners.TitleBarListener;
import com.moupress.app.friendshost.util.Pref;

public class LeftPanelView extends View{
	
	//private ExpandableListView settingExpLstV;
	//private ExpandableListView feedBksExpLstV;
	private Activity zActivity;
	
	private ListView acntLstV;
	private ListView settingLstV;
	private ListView feedBksLstV;
	
	private SnsEventListener snsEventListener;
	private LstAdapter snsAccntLstAdapter;
	private LstAdapter settingLstAdapter;
	private LstAdapter feedBksLstAdapter;
	
	//private ExpandableListAdapter settingExpandableListAdapter;
	//private ExpandableListAdapter feedBksExpandableListAdapter;
	
	
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
		this.zActivity = activity;
		
		this.snsAccntLstAdapter = new LstAdapter(activity,Const.SNSGROUPS, Const.SETTING_ACNT);
		this.settingLstAdapter = new LstAdapter(activity,Const.SETTING_BASIC_GROUPS, Const.SETTING_BASIC);
		this.feedBksLstAdapter = new LstAdapter(activity,Const.SETTING_FEEDBACKS_GROUPS, Const.SETTING_FEEDBACKS);
		
		int index = Pref.getMyIntPref(zActivity.getApplicationContext(), Const.SETTING_BASIC+"_UPT_FREQ");
		if(index <0) index = 1;
		if(index >= 0 && index < Const.SETTING_UPT_FREQ_BTN_TEXT.length)
		{
			this.settingLstAdapter.SetDtlText(new String[]{Const.SETTING_UPT_FREQ_BTN_TEXT[index]});
		}
		
		this.acntLstV = (ListView) activity.findViewById(R.id.signOnLst);
		this.acntLstV.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent,
					android.view.View view, int position, long id) {
				
				PubSub.zSnsOrg.GetSnsInstance(Const.SNSGROUPS[position]).ToggleSelectSNS(snsEventListener);
				
			}});
		
		this.settingLstV = (ListView) activity.findViewById(R.id.settingLst);
		this.settingLstV.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent,
					android.view.View view, int position, long id) {
				
				LaunchDialog(Const.SETTING_BASIC,Const.SETTING_BASIC_GROUPS[position]);
				
			}});
		
		this.feedBksLstV = (ListView) activity.findViewById(R.id.feedBksLst);
		this.feedBksLstV.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent,
					android.view.View view, int position, long id) {
				LaunchDialog(Const.SETTING_FEEDBACKS,Const.SETTING_FEEDBACKS_GROUPS[position]);
			}});
		 
		//Setting Expander List View
		//settingLstV = (ListView) activity.findViewById(R.id.settingExpandLst);
		//settingListAdapter = new ExpandableListAdapter(activity, Const.SETTING_BASIC);
		//settingExpLstV.setAdapter(settingExpandableListAdapter);
		
		//Feedbacks Expander List View
		//feedBksExpLstV = (ExpandableListView) activity.findViewById(R.id.feedbksExpandLst);
		//feedBksExpandableListAdapter = new ExpandableListAdapter(activity, Const.SETTING_FEEDBACKS);
		//feedBksExpLstV.setAdapter(feedBksExpandableListAdapter);
	}


	@Override
	public void InitTitle(Activity activity, TitleBarListener titleBarListener) {
		// TODO Auto-generated method stub
		super.InitTitle(activity, titleBarListener);
	}


	@Override
	public void LoadView(Bundle loadData) {
		// TODO Auto-generated method stub
		//super.LoadView(loadData);
		this.acntLstV.setAdapter(snsAccntLstAdapter);
		this.settingLstV.setAdapter(settingLstAdapter);
		this.feedBksLstV.setAdapter(feedBksLstAdapter);
	}
	
	
	private class LstAdapter extends BaseAdapter
	{
		private LayoutInflater viewInflator;
		private String[] displayArray;
		private String grpName;
		private String[] dtlText;
		
		public LstAdapter(Activity activity, String[] displayArray, String grpName)
		{
			viewInflator = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.displayArray = displayArray;
			this.grpName = grpName;
		}
		

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			//return Const.SNSGROUPS.length;
			return this.displayArray.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			//return Const.SNSGROUPS[position];
			return displayArray[position];
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
            //textView.setText(Const.SNSGROUPS[position]);
			textView.setText(displayArray[position]);
            
            //ListView listView = (ListView) convertView.findViewById(R.id.snsAccountsLst);
			final ImageView imgView = (ImageView) convertView.findViewById(R.id.snsStatusInd);
			final TextView dtlView = (TextView) convertView.findViewById(R.id.txtDetail);
			if(this.grpName.equals(Const.SETTING_ACNT))
			{
				//setSnsStatus(imgView, PubSub.zSnsOrg.GetSnsInstance(Const.SNSGROUPS[position]).isSelected());
				setSnsStatus(imgView, PubSub.zSnsOrg.GetSnsInstance(displayArray[position]).isSelected());
			}
			
			final ImageView logView = (ImageView) convertView.findViewById(R.id.snsLogo);
			
			
			if(this.grpName.equals(Const.SETTING_ACNT))
			{
				logView.setImageResource(PubSub.zSnsOrg.GetSnsInstance(displayArray[position]).GetLogImg());
			}
			else if(this.grpName.equals(Const.SETTING_BASIC) && ((String)this.getItem(position)).contains(Const.SETTING_BASIC_GROUPS[0]))
			{
				logView.setImageResource(R.drawable.fh_update_time);
				imgView.setVisibility(android.view.View.GONE);
				dtlView.setVisibility(android.view.View.VISIBLE);
				
				if(this.dtlText != null && dtlText.length > position && dtlText[position].length() > 0)
				dtlView.setText(dtlText[position]);
			}
			else if(this.grpName.equals(Const.SETTING_FEEDBACKS) && ((String)this.getItem(position)).equals(Const.SETTING_FEEDBACKS_GROUPS[1]))
			{
				logView.setImageResource(R.drawable.fh_rate);
			}
			else if(this.grpName.equals(Const.SETTING_FEEDBACKS) && ((String)this.getItem(position)).equals(Const.SETTING_FEEDBACKS_GROUPS[2]))
			{
				logView.setImageResource(R.drawable.fh_help_icon);
			}
			else if(this.grpName.equals(Const.SETTING_FEEDBACKS) && ((String)this.getItem(position)).equals(Const.SETTING_FEEDBACKS_GROUPS[0]))
			{
				logView.setImageResource(R.drawable.fh_feedback_icon);
			}
			
			
//            convertView.setOnClickListener(new OnClickListener(){
//
//				@Override
//				public void onClick(android.view.View view) {
//					
//					//PubSub.zSnsOrg.GetSnsInstance(Const.SNSGROUPS[position]).ToggleSelectSNS(snsEventListener);
//					if(grpName.equals(Const.SETTING_ACNT))
//					{
//						PubSub.zSnsOrg.GetSnsInstance(displayArray[position]).ToggleSelectSNS(snsEventListener);
//					}
//					else if(grpName.equals(Const.SETTING_BASIC) || grpName.equals(Const.SETTING_FEEDBACKS))
//					{
//						LaunchDialog(grpName,(String)getItem(position));
//					}
//					
//				}});
			
			return convertView;
		}


		private void setSnsStatus(ImageView imgView, boolean selected) {
			
			if(selected)
			{
				imgView.setImageResource(R.drawable.fh_remove);
			}
			else
			{
				imgView.setImageResource(R.drawable.fh_add);
			}
		}
		
//		private void LaunchDialog(String grpName, String optionName)
//		{
//			int displayView = -1;
//			int themeId = -1;
//			if(grpName.equals(Const.SETTING_BASIC) && optionName.contains(Const.SETTING_BASIC_GROUPS[0]))
//			{
//				displayView = R.layout.fh_upt_req_layout;
//				themeId = android.R.style.Theme_Dialog;
//			}
//			else if (grpName.equals(Const.SETTING_FEEDBACKS) && optionName.equals(Const.SETTING_FEEDBACKS_GROUPS[1]))
//			{
//				displayView = R.layout.fh_rate_layout;
//				themeId = android.R.style.Theme_Dialog;
//			}
//			else if(grpName.equals(Const.SETTING_FEEDBACKS) && optionName.equals(Const.SETTING_FEEDBACKS_GROUPS[0]))
//			{
//				displayView = R.layout.fh_feedback_layout;
//			}
//			else if (grpName.equals(Const.SETTING_FEEDBACKS) && optionName.equals(Const.SETTING_FEEDBACKS_GROUPS[2]))
//			{
//				displayView = R.layout.fh_help_layout;
//			}
//			
//			popUpDialogActivity(displayView,optionName,themeId);
//		}
//
//
//		private void popUpDialogActivity(int displayView, String optionName, int themeId) {
//			
//			if(displayView > 0)
//			{
//				Intent intent = new Intent(zActivity,FHDialogActivity.class);
//				intent.putExtra(Const.DIALOG_VIEW_ID, displayView);
//				intent.putExtra(Const.SETTING_REQ_KEY, optionName);
//				intent.putExtra(Const.DIALOG_THEME_ID, themeId);
//				zActivity.startActivityForResult(intent, Const.CD_REQ_DIALOG);
//			}
//			else
//			{
//				Toast.makeText(zActivity, "Invalid View", 1000);
//			}
//		}
//		
		
		public void SetDtlText(String[] dtlText)
		{
			this.dtlText = dtlText;
		}
	}


	
	private void LaunchDialog(String grpName, String optionName)
	{
		int displayView = -1;
		int themeId = -1;
		if(grpName.equals(Const.SETTING_BASIC) && optionName.contains(Const.SETTING_BASIC_GROUPS[0]))
		{
			displayView = R.layout.fh_upt_req_layout;
			themeId = android.R.style.Theme_Dialog;
		}
		else if (grpName.equals(Const.SETTING_FEEDBACKS) && optionName.equals(Const.SETTING_FEEDBACKS_GROUPS[1]))
		{
			displayView = R.layout.fh_rate_layout;
			themeId = android.R.style.Theme_Dialog;
		}
		else if(grpName.equals(Const.SETTING_FEEDBACKS) && optionName.equals(Const.SETTING_FEEDBACKS_GROUPS[0]))
		{
			displayView = R.layout.fh_feedback_layout;
		}
		else if (grpName.equals(Const.SETTING_FEEDBACKS) && optionName.equals(Const.SETTING_FEEDBACKS_GROUPS[2]))
		{
			displayView = R.layout.fh_help_layout;
		}
		
		popUpDialogActivity(displayView,optionName,themeId);
	}


	private void popUpDialogActivity(int displayView, String optionName, int themeId) {
		
		if(displayView > 0)
		{
			Intent intent = new Intent(zActivity,FHDialogActivity.class);
			intent.putExtra(Const.DIALOG_VIEW_ID, displayView);
			intent.putExtra(Const.SETTING_REQ_KEY, optionName);
			intent.putExtra(Const.DIALOG_THEME_ID, themeId);
			zActivity.startActivityForResult(intent, Const.CD_REQ_DIALOG);
		}
		else
		{
			Toast.makeText(zActivity, "Invalid View", 1000);
		}
	}
	
	
	public void DialogCallBack(Intent data) {
		
		if(data.getIntExtra(Const.DIALOG_VIEW_ID, -1) == R.layout.fh_upt_req_layout)
		{
			int index = data.getIntExtra(Const.SETTING_BASIC_GROUPS[0]+"_SET", -1);
			if(index >=0)
			{
				this.settingLstAdapter.SetDtlText(new String[]{Const.SETTING_UPT_FREQ_BTN_TEXT[index]});
				this.settingLstAdapter.notifyDataSetChanged();
			}
		}
		
	};
	
//	private class ExpandableListAdapter extends BaseExpandableListAdapter
//	{
//		private Activity activity;
//		private LayoutInflater viewInflator;
//		private String section;
//		
//		public ExpandableListAdapter(Activity activity, String section)
//		{
//			this.activity = activity;
//			this.section = section;
//			
//			 viewInflator = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		}
//		
//		@Override
//		public Object getChild(int groupPosition, int childPosition) {
//			
//			if(this.section.equals(Const.SETTING_BASIC))
//			{
//				if(groupPosition == 0)
//				{
//					//return Const.SNSGROUPS[childPosition];
//					return Const.SNSGROUPS;
//				}
//				else
//				{
//					return null;
//				}
//			}
//			return null;
//		}
//
//		@Override
//		public long getChildId(int groupPosition, int childPosition) {
//			
//			if(this.section.equals(Const.SETTING_BASIC))
//			{
//				if(groupPosition == 0)
//				{
//					return childPosition;
//				}
//				else
//				{
//					return 0;
//				}
//			}
//			
//			return 0;
//		}
//
//		@Override
//		public android.view.View getChildView(int groupPosition,
//				int childPosition, boolean isLastChild,
//				android.view.View convertView, ViewGroup parent) {
//			
//			if(this.section.equals(Const.SETTING_BASIC))
//			{
//				if(groupPosition == 0)
//				{
//					if(convertView == null)
//						convertView = viewInflator.inflate(R.layout.fh_login_account_list, null);
//					
//					//TextView tv = (TextView) convertView.findViewById(R.id.snsLogin);
//					//tv.setText(Const.SNSGROUPS[childPosition]);
//					ListView listView = (ListView) convertView.findViewById(R.id.snsAccountsLst);
//					listView.setAdapter(snsAccntLstAdapter);
//					
//					return convertView;
//				}
//				else
//				{
//					return null;
//				}
//				
//			}
//			return null;
//		}
//
//		@Override
//		public int getChildrenCount(int groupPosition) {
//			
//			if(this.section.equals(Const.SETTING_BASIC))
//			{
//				if(groupPosition == 0)
//				{
//					//return Const.SNSGROUPS.length;
//					return 1;
//				}
//				else
//				{
//					return 0;
//				}
//			}
//			return 0;
//		}
//
//		@Override
//		public Object getGroup(int groupPosition) {
//			
//			if(this.section.equals(Const.SETTING_BASIC))
//			{
//				return Const.SETTING_BASIC_GROUPS[groupPosition];
//			}
//			else if(this.section.equals(Const.SETTING_FEEDBACKS))
//			{
//				return Const.SETTING_FEEDBACKS_GROUPS[groupPosition];
//			}
//			
//			return null;
//			
//		}
//
//		@Override
//		public int getGroupCount() {
//			
//			if(this.section.equals(Const.SETTING_BASIC))
//			{
//				return Const.SETTING_BASIC_GROUPS.length;
//			}
//			else if(this.section.equals(Const.SETTING_FEEDBACKS))
//			{
//				return Const.SETTING_FEEDBACKS_GROUPS.length;
//			}
//			
//			return 0;
//		}
//
//		@Override
//		public long getGroupId(int groupPosition) {
//			// TODO Auto-generated method stub
//			return groupPosition;
//		}
//
//		@Override
//		public android.view.View getGroupView(int groupPosition,
//				boolean isExpanded, android.view.View convertView,
//				ViewGroup parent) {
//
//			if(convertView == null)
//        	{
//        		convertView= viewInflator.inflate(R.layout.fh_setting_groups, null);
//        	}
//			
//			TextView textView = (TextView) convertView.findViewById(R.id.groupTv);
//            textView.setText(getGroup(groupPosition).toString());
//            //ListView listView = (ListView) convertView.findViewById(R.id.snsAccountsLst);
//            
//			return convertView;
//		}
//
//		@Override
//		public boolean hasStableIds() {
//			// TODO Auto-generated method stub
//			return false;
//		}
//
//		@Override
//		public boolean isChildSelectable(int groupPosition, int childPosition) {
//			// TODO Auto-generated method stub
//			return false;
//		}
//		
//	}
//	
	

}

package com.moupress.app.friendshost.ui;

import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.ui.listeners.ContentViewListener;
import com.moupress.app.friendshost.ui.listeners.TitleBarListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

public class HelpView extends DialogView{

	private Activity zActivity;
	
	private ExpandableListView helpExpLstV;
	private ExpandableListAdapter helpExpAdapter;
	
	private String[] helpList;
	
	public HelpView(Activity zActivity, Intent intentBack)
	{
		this.zActivity = zActivity;
		
	}

	@Override
	public void LoadView(Bundle loadData) {
		// TODO Auto-generated method stub
		helpExpLstV.setAdapter(helpExpAdapter);
	}

	@Override
	protected void RefreshView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void InitContent(Activity activity,
			ContentViewListener contentViewListener) {
		// TODO Auto-generated method stub
		//super.InitContent(activity, contentViewListener);
		helpList = activity.getResources().getStringArray(R.array.fh_help_items);
		helpExpLstV = (ExpandableListView) activity.findViewById(R.id.helpExpandLst);
		helpExpAdapter = new ExpandableListAdapter();
	}

	@Override
	public void InitTitle(Activity activity, TitleBarListener titleBarListener) {
		// TODO Auto-generated method stub
		//super.InitTitle(activity, titleBarListener);
		Button sendBtn = (Button) zActivity.findViewById(R.id.thirdbtn);
		sendBtn.setVisibility(android.view.View.INVISIBLE);
		
		Button closeBtn = (Button) zActivity.findViewById(R.id.CancelBtn);
		closeBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(android.view.View v) {
				// TODO Auto-generated method stub
				zActivity.finish();
			}});
	}

	@Override
	public int GetSetBtnTxId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public OnClickListener GetSetOnClickListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int GetTitleId() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private class ExpandableListAdapter extends BaseExpandableListAdapter{

		private LayoutInflater viewInflator;
		
		public ExpandableListAdapter()
		{
			this.viewInflator = (LayoutInflater)zActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public Object getChild(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getChildId(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getChildView(final int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			ImageView imgView = new ImageView(zActivity);
			if(groupPosition==0)
			{
				imgView.setImageResource(R.drawable.fh_help_cmt);
			}
			else if(groupPosition==1)
			{
				imgView.setImageResource(R.drawable.fh_help_pub);
			}
			
			return imgView;
		}

		@Override
		public int getChildrenCount(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return helpList[groupPosition];
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return helpList.length;
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			
			if(convertView == null)
			{
				convertView= viewInflator.inflate(R.layout.fh_help_item_layout, null);
			}
			
			TextView tvhelpItem = (TextView) convertView.findViewById(R.id.tvGroup);
			tvhelpItem.setText(helpList[groupPosition]);
			
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
		}};
	
}

package com.moupress.app.friendshost.ui;

import com.moupress.app.friendshost.PubSub;
import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.ui.listeners.ContentViewListener;
import com.moupress.app.friendshost.ui.listeners.TitleBarListener;
import com.moupress.app.friendshost.util.Mail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class FeedBackView extends DialogView{
	
	private Activity zActivity;
	private Intent intentBack;
	
	private ExpandableListView feedBksExpLstV;
	private ExpandableListAdapter feedBksExpAdapter;
	
	private String[] feedBackList;
	private String[] feedBackCntList;
	
	public FeedBackView(Activity zActivity, Intent intentBack)
	{
		this.zActivity = zActivity;
		this.intentBack = intentBack;
	}
	
	
	private void ExpandGroup(int index, boolean Expand)
	{

		for (int i=0; i< this.feedBackList.length;i++)
		{	
			if(index == i)
			{
//				if(Expand)
//				{
//					this.feedBksExpLstV.expandGroup(i);
//				}
//				else
//				{
//					this.feedBksExpLstV.collapseGroup(i);
//				}
			}
			else
			{
				this.feedBksExpLstV.collapseGroup(i);
			}
		}
		
	}
	
	
	
	@Override
	public void InitTitle(final Activity activity, TitleBarListener titleBarListener) {
		// TODO Auto-generated method stub
		//super.InitTitle(activity, titleBarListener);
		Button btnClose = (Button) activity.findViewById(R.id.CancelBtn);
		btnClose.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				activity.finish();
			}});
		
		Button btnSend = (Button) activity.findViewById(R.id.thirdbtn);
		btnSend.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Mail.sendFeedbackEmail(activity, makeEmailContent());
				activity.finish();
			}

			}
		);
	}

	private String makeEmailContent() {
		String emailCnt = null;
		EditText edtTxtBoxEmailAdrr = (EditText) zActivity.findViewById(R.id.edtTxtEmlAddr);
		
		if(edtTxtBoxEmailAdrr.getText().toString() != null)
		{
			emailCnt += "Email: "+edtTxtBoxEmailAdrr.getText().toString()+"\n";
		}
		
		for(int i=0 ; i< this.feedBackList.length;i++)
		{
			if(this.feedBackCntList.length > i && this.feedBackCntList[i] != null)
			{
				emailCnt +=  this.feedBackList[i]+"  "+this.feedBackCntList[i]+"\n";
			}
		}
		
		return emailCnt;
	}

	@Override
	public void InitContent(Activity activity,
			ContentViewListener contentViewListener) {
		// TODO Auto-generated method stub
		//super.InitContent(activity, contentViewListener);
		this.feedBksExpLstV = (ExpandableListView) activity.findViewById(R.id.feedBackExpList);
		this.feedBackList = activity.getResources().getStringArray(R.array.fh_feedback_items);
		this.feedBksExpAdapter = new ExpandableListAdapter();
		this.feedBksExpLstV.setOnGroupExpandListener(new OnGroupExpandListener(){

			@Override
			public void onGroupExpand(int indx) {
				// TODO Auto-generated method stub
			   ExpandGroup(indx,true);	
			}});
		this.feedBackCntList = new String[this.feedBackList.length];
	}


	@Override
	public void LoadView(Bundle loadData) {
		// TODO Auto-generated method stub
		this.feedBksExpLstV.setAdapter(feedBksExpAdapter);
		//this.feedBksExpLstV.expandGroup(0);
	}

	@Override
	protected void RefreshView() {
		// TODO Auto-generated method stub
		
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
	
	
	private class ExpandableListAdapter extends BaseExpandableListAdapter
	{

		private LayoutInflater viewInflator;
		
		public ExpandableListAdapter()
		{
			this.viewInflator = (LayoutInflater)zActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		@Override
		public View getChildView(final int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView == null)
			{
				convertView= viewInflator.inflate(R.layout.fh_feedback_edttx, null);
			}
			EditText edtTx = (EditText) convertView.findViewById(R.id.edtTxFeedbacks);
			//EditText edtTx = new EditText(zActivity);
			edtTx.setText(feedBackCntList[groupPosition]);
			
			edtTx.setOnFocusChangeListener(new OnFocusChangeListener(){

				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					
					if(hasFocus)
					{
						
					}
					else
					{
						feedBackCntList[groupPosition] = ((EditText)view).getText().toString();
					}
					
					if(!hasFocus && feedBksExpLstV.isGroupExpanded(groupPosition))
					{
						view.requestFocus();
					}
					
				}});
			return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return feedBackList[groupPosition];
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return feedBackList.length;
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public View getGroupView(final int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView == null)
			{
				convertView= viewInflator.inflate(R.layout.fh_feedback_item_layout, null);
			}
			
			TextView tvFbItem = (TextView) convertView.findViewById(R.id.tvGroup);
			tvFbItem.setText(feedBackList[groupPosition]);
//			RadioButton rdBtnYS = (RadioButton) convertView.findViewById(R.id.rdBtnYs);
//			rdBtnYS.setOnCheckedChangeListener(new OnCheckedChangeListener(){
//
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//					if(isChecked)
//					{
//						ExpandGroup(groupPosition, true);
//					}
//				}});
//			
//			RadioButton rdBtnNO = (RadioButton) convertView.findViewById(R.id.rdBtnNo);
//			rdBtnNO.setOnCheckedChangeListener(new OnCheckedChangeListener(){
//
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView,
//						boolean isChecked) {
//					if(isChecked)
//					{
//						ExpandGroup(groupPosition, false);
//					}
//				}});
			
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
		}}
	
}

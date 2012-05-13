package com.moupress.app.friendshost.activity;

import java.util.ArrayList;

import com.github.droidfu.widgets.WebImageView;
import com.moupress.app.friendshost.R;
import com.moupress.app.friendshost.sns.FeedEntryComment;
import com.moupress.app.friendshost.uicomponent.FeedCommentUIComponent;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LstViewCommentAdapter extends BaseAdapter {
	
	private ArrayList<FeedEntryComment> commentArrayList;
	
	private Activity zActivity;
	private int iLayoutResId;
	private LayoutInflater viewInflator;
	
	public LstViewCommentAdapter(Activity activity, int layoutResId) {
		this.zActivity = activity;
		this.iLayoutResId = layoutResId;
		commentArrayList = new ArrayList<FeedEntryComment>();
		viewInflator = zActivity.getLayoutInflater();
	}

	@Override
	public int getCount() {
		if (commentArrayList == null ) {
			return 0;
		}
		else {
			return commentArrayList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		return commentArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent) {
		
		FeedCommentUIComponent feedCommentUIComponent;
		if(convertView == null) {
			convertView = viewInflator.inflate(iLayoutResId, null);
			feedCommentUIComponent = new FeedCommentUIComponent();
			
			//comment head
			feedCommentUIComponent.setImg_userhead_detail_comment((WebImageView) convertView.findViewById(R.id.img_userhead_detail_comment));
			
			//comment text
			feedCommentUIComponent.setTxv_username_detail_comment((TextView) convertView.findViewById(R.id.txv_username_detail_comment));
			feedCommentUIComponent.setTxv_commentcreatedtime_detail_comment((TextView) convertView.findViewById(R.id.txv_commentcreatedtime_detail_comment));
			feedCommentUIComponent.setTxv_commentmsg_detail_comment((TextView) convertView.findViewById(R.id.txv_commentmsg_detail_comment));
			
			//comment reply
			feedCommentUIComponent.setImg_replytouser_detail_comment((ImageView) convertView.findViewById(R.id.img_replytouser_detail_comment));
			
			convertView.setTag(feedCommentUIComponent);
		} else {
			feedCommentUIComponent = (FeedCommentUIComponent) convertView.getTag();
		}
		
		if (commentArrayList != null && commentArrayList.size() > 0) {
			FeedEntryComment comment = commentArrayList.get(position);
			feedCommentUIComponent.loadUserHeadDetailComment(comment.getCommentedHeadUrl());
			
			feedCommentUIComponent.loadUserNameDetailComment(comment.getCommentedName());
			feedCommentUIComponent.loadCommentCreatedTimeDetailComment(comment.getCommentedTime());
			feedCommentUIComponent.loadCommentMsgDetailComment(comment.getCommentedMsg());
			
			//feedCommentUIComponent.loadUserHeadDetailComment(comment.getCommentedHeadUrl());
		}
		
		return convertView;
	}
	
	public void addItem(FeedEntryComment item) {
		commentArrayList.add(item);
	}

}

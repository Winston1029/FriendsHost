package com.moupress.app.friendshost.uicomponent;

import android.widget.ImageView;
import android.widget.TextView;
import com.github.droidfu.widgets.WebImageView;

public class FeedCommentUIComponent {

	private WebImageView img_userhead_detail_comment;
	private TextView txv_username_detail_comment;
	private TextView txv_commentmsg_detail_comment;
	private TextView txv_commentcreatedtime_detail_comment;
	private ImageView img_replytouser_detail_comment;
	public void setImg_userhead_detail_comment(
			WebImageView img_userhead_detail_comment) {
		this.img_userhead_detail_comment = img_userhead_detail_comment;
	}
	public WebImageView getImg_userhead_detail_comment() {
		return img_userhead_detail_comment;
	}
	public void setTxv_username_detail_comment(
			TextView txv_username_detail_comment) {
		this.txv_username_detail_comment = txv_username_detail_comment;
	}
	public TextView getTxv_username_detail_comment() {
		return txv_username_detail_comment;
	}
	public void setTxv_commentmsg_detail_comment(
			TextView txv_commentmsg_detail_comment) {
		this.txv_commentmsg_detail_comment = txv_commentmsg_detail_comment;
	}
	public TextView getTxv_commentmsg_detail_comment() {
		return txv_commentmsg_detail_comment;
	}
	public void setTxv_commentcreatedtime_detail_comment(
			TextView txv_commentcreatedtime_detail_comment) {
		this.txv_commentcreatedtime_detail_comment = txv_commentcreatedtime_detail_comment;
	}
	public TextView getTxv_commentcreatedtime_detail_comment() {
		return txv_commentcreatedtime_detail_comment;
	}
	public void setImg_replytouser_detail_comment(
			ImageView img_replytouser_detail_comment) {
		this.img_replytouser_detail_comment = img_replytouser_detail_comment;
	}
	public ImageView getImg_replytouser_detail_comment() {
		return img_replytouser_detail_comment;
	}
	
	
	public void loadUserHeadDetailComment(String commentedHeadUrl) {
		img_userhead_detail_comment.setImageUrl(commentedHeadUrl);
		img_userhead_detail_comment.loadImage();		
	}
	public void loadUserNameDetailComment(String commentedName) {
		txv_username_detail_comment.setText(commentedName);
	}
	public void loadCommentCreatedTimeDetailComment(String commentedTime) {
		txv_commentcreatedtime_detail_comment.setText(commentedTime);
	}
	public void loadCommentMsgDetailComment(String commentedMsg) {
		txv_commentmsg_detail_comment.setText(commentedMsg);
	}
	
	
}

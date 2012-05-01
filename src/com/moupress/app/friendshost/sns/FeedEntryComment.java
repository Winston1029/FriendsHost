package com.moupress.app.friendshost.sns;

import com.moupress.app.friendshost.Const;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class FeedEntryComment implements Parcelable {
	//for reference only
	protected String commetedfeedID;
	protected String sns;
	
	private String commentedID;
	private String commentedTime;
	private String commentedMsg;
	private String commentedUserID;
	private String commentedName;
	private String commentedHeadUrl;
	
	public FeedEntryComment(Parcel in) {
		
		Bundle bundle = Bundle.CREATOR.createFromParcel(in);
		
		this.commetedfeedID = bundle.getString(Const.COMMENTED_FEEDID);
		this.commentedID = bundle.getString(Const.COMMENTED_ID);
		this.commentedTime = bundle.getString(Const.COMMENTED_TIME);
		this.commentedMsg = bundle.getString(Const.COMMENTED_MSG);
		this.commentedUserID = bundle.getString(Const.COMMENTED_USERID);
		this.commentedName = bundle.getString(Const.COMMENTED_NAME);
		this.commentedHeadUrl = bundle.getString(Const.COMMENTED_HEADURL);
	}
	
	public FeedEntryComment() {
	}

	public void setCommetedfeedID(String commetedfeedID) {
		this.commetedfeedID = commetedfeedID;
	}
	public String getCommetedfeedID() {
		return commetedfeedID;
	}
	public void setSns(String sns) {
		this.sns = sns;
	}
	public String getSns() {
		return sns;
	}
	public void setCommentedID(String commentedID) {
		this.commentedID = commentedID;
	}
	public String getCommentedID() {
		return commentedID;
	}
	public void setCommentedTime(String commentedTime) {
		this.commentedTime = commentedTime;
	}
	public String getCommentedTime() {
		return commentedTime;
	}
	public void setCommentedMsg(String commentedMsg) {
		this.commentedMsg = commentedMsg;
	}
	public String getCommentedMsg() {
		return commentedMsg;
	}
	public void setCommentedUserID(String commentedUserID) {
		this.commentedUserID = commentedUserID;
	}
	public String getCommentedUserID() {
		return commentedUserID;
	}
	public void setCommentedName(String commentedName) {
		this.commentedName = commentedName;
	}
	public String getCommentedName() {
		return commentedName;
	}
	public void setCommentedHeadUrl(String commentedHeadUrl) {
		this.commentedHeadUrl = commentedHeadUrl;
	}
	public String getCommentedHeadUrl() {
		return commentedHeadUrl;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
	}
}

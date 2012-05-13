package com.moupress.app.friendshost.sns;

public class FeedEntryComment /*implements Parcelable*/ {
	//for reference only
	protected String commetedfeedID;
	protected String sns;
	
	private String commentedID;
	private String commentedTime;
	private String commentedMsg;
	private String commentedUserID;
	private String commentedName;
	private String commentedHeadUrl;
	
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
	
//	public FeedEntryComment(Parcel in) {
//		
//		commetedfeedID = in.readString();
//		commentedMsg = in.readString();
//		commentedUserID = in.readString();
//		commentedName = in.readString();
//		commentedHeadUrl = in.readString();
//		
////		Bundle bundle = Bundle.CREATOR.createFromParcel(in);
////		
////		this.commetedfeedID = bundle.getString(Const.COMMENTED_FEEDID);
////		this.commentedID = bundle.getString(Const.COMMENTED_ID);
////		this.commentedTime = bundle.getString(Const.COMMENTED_TIME);
////		this.commentedMsg = bundle.getString(Const.COMMENTED_MSG);
////		this.commentedUserID = bundle.getString(Const.COMMENTED_USERID);
////		this.commentedName = bundle.getString(Const.COMMENTED_NAME);
////		this.commentedHeadUrl = bundle.getString(Const.COMMENTED_HEADURL);
//	}
//
//
//	@Override
//	public int describeContents() {
//		return 0;
//	}
//	@Override
//	public void writeToParcel(Parcel out, int flags) {
//		out.writeString(commetedfeedID);
//		out.writeString(commentedTime);
//		out.writeString(commentedMsg);
//		out.writeString(commentedUserID);
//		out.writeString(commentedName);
//		out.writeString(commentedHeadUrl);
//		
////		Bundle bundle = new Bundle();
////		
////		if(commetedfeedID != null) { bundle.putString( Const.COMMENTED_FEEDID,commetedfeedID);}
////		if(commentedID != null) { bundle.putString( Const.COMMENTED_ID,commentedID);}
////		if(commentedTime != null) { bundle.putString( Const.COMMENTED_TIME,commentedTime);}
////		if(commentedMsg != null) { bundle.putString( Const.COMMENTED_MSG,commentedMsg);}
////		if(commentedUserID != null) { bundle.putString( Const.COMMENTED_USERID,commentedUserID);}
////		if(commentedName != null) { bundle.putString( Const.COMMENTED_NAME,commentedName);}
////		if(commentedHeadUrl != null) { bundle.putString( Const.COMMENTED_HEADURL,commentedHeadUrl);}
////		
////		bundle.writeToParcel(out, flags);
//	}
//	
//	public static final Parcelable.Creator<FeedEntryComment> CREATOR = new Parcelable.Creator<FeedEntryComment>(){
//
//		@Override
//		public FeedEntryComment createFromParcel(Parcel in) {
//			return new FeedEntryComment(in);
//		}
//
//		@Override
//		public FeedEntryComment[] newArray(int size) {
//			return new FeedEntryComment[size];
//		}};
}

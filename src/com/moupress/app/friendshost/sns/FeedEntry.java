package com.moupress.app.friendshost.sns;

import java.util.ArrayList;
import java.util.List;

import com.moupress.app.friendshost.Const;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class FeedEntry implements Parcelable{

	private String sHeadImg;
	
	private String sID;
	private String sName;
	private String sOwnerID;
	private String sCreatedTime;
	private String sFeedType;
	private String sMsgBody;
	private String sStory;
	private String sLink;
	//private String sStory_Tags;
	private String sPhotoPreviewLink;
	private String sPhotoPreviewName;
	private String sPhotoPreviewCaption;
	private String sPhotoPreviewDescription;
	
	private UserFriend zFriend;
	private List<FeedEntryComment> zComments;
	
	public FeedEntry() {
		setzFriend(new UserFriend());
		zComments = new ArrayList<FeedEntryComment>();
	}
	
	public FeedEntry(Parcel in)
	{
		Bundle bundle = Bundle.CREATOR.createFromParcel(in);
		
		this.sHeadImg = bundle.getString(Const.SHEADIMG);
		this.sName = bundle.getString(Const.SNAME);
		this.sOwnerID = bundle.getString(Const.SOWNERID);
		this.sCreatedTime = bundle.getString(Const.SCREATEDTIME);
		this.sFeedType = bundle.getString(Const.SFEEDTYPE);
		this.sMsgBody = bundle.getString(Const.SMSGBODY);
		this.sLink = bundle.getString(Const.SLINK);
		this.sStory = bundle.getString(Const.SSTORY);
		this.sPhotoPreviewLink = bundle.getString(Const.SPHOTOPREVIEWLINK);
		this.sPhotoPreviewName = bundle.getString(Const.SPHOTOPREVIEWNAME);
		this.sPhotoPreviewCaption = bundle.getString(Const.SPHOTOPREVIEWCAPTION);
		this.sPhotoPreviewDescription = bundle.getString(Const.SPHOTOPREVIEWDESCRIPTION);
		
		this.zFriend = UserFriend.CREATOR.createFromParcel(in);
	}
	
	public void setsHeadImg(String sHeadImg) {
		this.sHeadImg = sHeadImg;
	}
	public String getsHeadImg() {
		return sHeadImg;
	}
	public void setsName(String sName) {
		this.sName = sName;
	}
	public String getsName() {
		return sName;
	}
	public void setsCreatedTime(String sCreatedTime) {
		this.sCreatedTime = sCreatedTime;
	}
	public String getsCreatedTime() {
		return sCreatedTime;
	}
	public void setsPhotoPreviewLink(String sPhotoPreviewLink) {
		this.sPhotoPreviewLink = sPhotoPreviewLink;
	}
	public String getsPhotoPreviewLink() {
		return sPhotoPreviewLink;
	}
	public void setsMsgBody(String sMsgBody) {
		this.sMsgBody = sMsgBody;
	}
	public String getsMsgBody() {
		return sMsgBody;
	}
	public void setsPhotoPreviewDescription(String sPhotoPreviewDescription) {
		this.sPhotoPreviewDescription = sPhotoPreviewDescription;
	}
	public String getsPhotoPreviewDescription() {
		return sPhotoPreviewDescription;
	}
	public void setsPhotoPreviewName(String sPhotoPreviewName) {
		this.sPhotoPreviewName = sPhotoPreviewName;
	}
	public String getsPhotoPreviewName() {
		return sPhotoPreviewName;
	}
	public void setsPhotoPreviewCaption(String sPhotoPreviewCaption) {
		this.sPhotoPreviewCaption = sPhotoPreviewCaption;
	}
	public String getsPhotoPreviewCaption() {
		return sPhotoPreviewCaption;
	}
	public void setsStory(String sStory) {
		this.sStory = sStory;
	}
	public String getsStory() {
		return sStory;
	}
//	public void setsStory_Tags(String sStory_Tags) {
//		this.sStory_Tags = sStory_Tags;
//	}
//	public String getsStory_Tags() {
//		return sStory_Tags;
//	}
	public void setsOwnerID(String sOwnerID) {
		this.sOwnerID = sOwnerID;
	}
	public String getsOwnerID() {
		return sOwnerID;
	}

	public void setzFriend(UserFriend zFriend) {
		this.zFriend = zFriend;
	}

	public UserFriend getzFriend() {
		return zFriend;
	}

	@Override
	public int describeContents() {
		return 0;
		
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		
		Bundle bundle = new Bundle();
		
		if(sHeadImg != null) { bundle.putString( Const.SHEADIMG,sHeadImg);}
		if(sName != null) { bundle.putString( Const.SNAME,sName);}
		if(sOwnerID != null) { bundle.putString( Const.SOWNERID,sOwnerID);}
		if(sCreatedTime != null) { bundle.putString( Const.SCREATEDTIME,sCreatedTime);}
		if(sFeedType != null) { bundle.putString( Const.SFEEDTYPE,sFeedType);}
		if(sMsgBody != null) { bundle.putString( Const.SMSGBODY,sMsgBody);}
		if(sLink != null) { bundle.putString( Const.SLINK,sLink);}
		if(sStory != null) { bundle.putString( Const.SSTORY,sStory);}
		if(sPhotoPreviewLink != null) { bundle.putString( Const.SPHOTOPREVIEWLINK,sPhotoPreviewLink);}
		if(sPhotoPreviewName != null) { bundle.putString( Const.SPHOTOPREVIEWNAME,sPhotoPreviewName);}
		if(sPhotoPreviewCaption != null) { bundle.putString( Const.SPHOTOPREVIEWCAPTION,sPhotoPreviewCaption);}
		if(sPhotoPreviewDescription != null) { bundle.putString( Const.SPHOTOPREVIEWDESCRIPTION,sPhotoPreviewDescription);}
		
		bundle.writeToParcel(out, flags);
		if(this.zFriend != null) { this.zFriend.writeToParcel(out, flags);}
		//if(this.zComments != null) { this.zFriend.writeToParcel(out, flags);}
	}
	
	public void setsFeedType(String sFeedType) {
		this.sFeedType = sFeedType;
	}

	public String getsFeedType() {
		return sFeedType;
	}

	public void setsID(String sID) {
		this.sID = sID;
	}

	public String getsID() {
		return sID;
	}

	public void setzComments(List<FeedEntryComment> zComments) {
		this.zComments = zComments;
	}

	public List<FeedEntryComment> getzComments() {
		return zComments;
	}

	public void setsLink(String sLink) {
		this.sLink = sLink;
	}

	public String getsLink() {
		return sLink;
	}

	public static final Parcelable.Creator<FeedEntry> CREATOR = new Parcelable.Creator<FeedEntry>(){

		@Override
		public FeedEntry createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			return new FeedEntry(in);
		}

		@Override
		public FeedEntry[] newArray(int size) {
			// TODO Auto-generated method stub
			return new FeedEntry[size];
		}};

}
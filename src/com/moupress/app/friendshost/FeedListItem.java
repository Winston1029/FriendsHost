package com.moupress.app.friendshost;

public class FeedListItem {

	private String sHeadImg;
	
	private String sName;
	private String sCreatedTime;
	private String sMsgBody;
	private String sPhotoPreviewLink;
	private String sPhotoPreviewDescription;
	
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
	
}

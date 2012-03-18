package com.moupress.app.friendshost;

public class FeedListItem {

	private String sHeadImg;
	
	private String sName;
	private String sCreatedTime;
	private String sMsgBody;
	private String sStory;
	//private String sStory_Tags;
	private String sPhotoPreviewLink;
	private String sPhotoPreviewName;
	private String sPhotoPreviewCaption;
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
	
}

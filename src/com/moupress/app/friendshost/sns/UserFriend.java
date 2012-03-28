package com.moupress.app.friendshost.sns;

public class UserFriend {
	private String id;
	private String name;
	private String headurl;
	
	protected String sns;
	
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setHeadurl(String headurl) {
		this.headurl = headurl;
	}
	public String getHeadurl() {
		return headurl;
	}
	public void setSNS(String sns) {
		this.sns = sns;
	}
	public String getSNS() {
		return sns;
	}
}

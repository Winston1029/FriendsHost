package com.moupress.app.friendshost.sns.facebook;

public class FBHomeFeedEntryAction {
	
	private String link;
	private String name;
	
	//No Arg constructor
	public FBHomeFeedEntryAction() {}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLink() {
		return link;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	//Getter methods should be declared.  Setter methods are optional 
}

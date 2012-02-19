package com.moupress.app.friendshost.sns.facebook;

import java.util.List;

public class FBHomeFeed {
	//Collection of entries
	private List<FBHomeFeedEntry> data;

	//No Arg constructor
	public FBHomeFeed() {}
	 
	public List getData() {
		return data;
	}
	
	public void setData(List<FBHomeFeedEntry> data) {
		this.data = data;
	}
}

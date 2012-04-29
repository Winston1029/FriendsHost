package com.moupress.app.friendshost.sns.Renren;

import java.util.List;


public class RenrenFeedHome {
	//Collection of entries
	private List<RenrenFeedElementEntry> data;

	//No Arg constructor
	public RenrenFeedHome() {}
	 
	public List getData() {
		return data;
	}
	
	public void setData(List<RenrenFeedElementEntry> data) {
		this.data = data;
	}
}

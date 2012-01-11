package com.moupress.app.util.facebook;

public class FBFeedItem {
	String id;
	FeedItemFrom from;
	String message;
	String picture;
	String link;
	String source;
	String name;
	String caption;
	String description;
	String icon;
	FeedItemActions actions;
	String type;
	
	
	class FeedItemFrom {
		public String name;
		public String id;
	}
	
	class FeedItemActions {
		public String name;
		public String link;
	}
}

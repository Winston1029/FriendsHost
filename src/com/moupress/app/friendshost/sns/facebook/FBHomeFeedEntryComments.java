package com.moupress.app.friendshost.sns.facebook;

import java.util.List;
import com.moupress.app.friendshost.sns.FeedEntryComment;


public class FBHomeFeedEntryComments {

	private String count;
	private List<FBFeedEntryComment> data;
	
	public class FBFeedEntryComment extends FeedEntryComment {
		private FBHomeFeedEntryFrom from;
		
		private String id;
		private String message;
		private String created_time;
		
		public void setFrom(FBHomeFeedEntryFrom from) {
			this.from = from;
		}
		public FBHomeFeedEntryFrom getFrom() {
			return from;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getMessage() {
			return message;
		}
		public void setCreated_time(String created_time) {
			this.created_time = created_time;
		}
		public String getCreated_time() {
			return created_time;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getId() {
			return id;
		}
		
	}
	
	public void setCount(String count) {
		this.count = count;
	}
	public String getCount() {
		return count;
	}

	public void setData(List<FBFeedEntryComment> data) {
		this.data = data;
	}

	public List<FBFeedEntryComment> getData() {
		return data;
	}
	
}

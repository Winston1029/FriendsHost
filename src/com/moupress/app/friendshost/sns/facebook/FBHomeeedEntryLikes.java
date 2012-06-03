package com.moupress.app.friendshost.sns.facebook;

import java.util.List;
import com.moupress.app.friendshost.sns.FeedEntryLike;

public class FBHomeeedEntryLikes  {

	private String count;
	private List<FBFeedEntryLike> data;
	
	public class FBFeedEntryLike extends FeedEntryLike {
		private String name;
		private String id;
		public void setName(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getId() {
			return id;
		}
	}

	public void setData(List<FBFeedEntryLike> data) {
		this.data = data;
	}

	public List<FBFeedEntryLike> getData() {
		return data;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getCount() {
		return count;
	}
}

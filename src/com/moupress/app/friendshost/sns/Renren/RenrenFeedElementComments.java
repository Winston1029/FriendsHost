package com.moupress.app.friendshost.sns.Renren;

import java.util.List;
import com.moupress.app.friendshost.sns.FeedEntryComment;

public class RenrenFeedElementComments {

	private String count;
	private List<RenrenFeedElementComment> comment;
	
	public void setCount(String count) {
		this.count = count;
	}
	public String getCount() {
		return count;
	}
	
	public void setComment(List<RenrenFeedElementComment> comment) {
		this.comment = comment;
	}
	public List<RenrenFeedElementComment> getComment() {
		return comment;
	}

	public class RenrenFeedElementComment extends FeedEntryComment  {
		private String comment_id;
		private String headurl;
		private String name;
		private String text;
		private String time;
		private String uid;
		
		public void setComment_id(String comment_id) {
			this.comment_id = comment_id;
		}
		public String getComment_id() {
			return comment_id;
		}
		public void setHeadurl(String headurl) {
			this.headurl = headurl;
		}
		public String getHeadurl() {
			return headurl;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public void setText(String text) {
			this.text = text;
		}
		public String getText() {
			return text;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public String getTime() {
			return time;
		}
		public void setUid(String uid) {
			this.uid = uid;
		}
		public String getUid() {
			return uid;
		}
		
	}
	
}

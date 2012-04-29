package com.moupress.app.friendshost.sns.Renren;

import java.util.List;

import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.sns.FeedItem;
import com.moupress.app.friendshost.sns.UserFriend;

public class RenrenFeedElementEntry extends FeedItem {
	
	private String actor_id;
	private String actor_type;
	private String source_id;
	private String feed_type;
	
	private List<RenrenFeedElementAttachment> attachment;
	
	private String post_id;
	private String headurl;
	private String message;
	private String title;
	private String update_time;
	
	private RenrenFeedElementLikes likes;
	
	private String name;
	private String prefix;
	private String description;
	
	//private RenrenFriend zFriend;
	
	private RenrenFeedElementComments comments;

	public RenrenFeedElementEntry() {
	}
	
	public void setActor_id(String actor_id) {
		this.actor_id = actor_id;
	}

	public String getActor_id() {
		return actor_id;
	}

	public void setActor_type(String actor_type) {
		this.actor_type = actor_type;
	}

	public String getActor_type() {
		return actor_type;
	}

	public void setSource_id(String source_id) {
		this.source_id = source_id;
	}

	public String getSource_id() {
		return source_id;
	}

	public void setAttachment(List<RenrenFeedElementAttachment> attachment) {
		this.attachment = attachment;
	}

	public List<RenrenFeedElementAttachment> getAttachment() {
		return attachment;
	}

	public void setFeed_type(String feed_type) {
		this.feed_type = feed_type;
	}

	public String getFeed_type() {
		return feed_type;
	}

	public void setPost_id(String post_id) {
		this.post_id = post_id;
	}

	public String getPost_id() {
		return post_id;
	}

	public void setHeadurl(String headurl) {
		this.headurl = headurl;
	}

	public String getHeadurl() {
		return headurl;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setLikes(RenrenFeedElementLikes likes) {
		this.likes = likes;
	}

	public RenrenFeedElementLikes getLikes() {
		return likes;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setComments(RenrenFeedElementComments comments) {
		this.comments = comments;
	}

	public RenrenFeedElementComments getComments() {
		return comments;
	}

	
}

package com.moupress.app.friendshost.sns.Renren;

public class RenenFeedElement {

	private String id;
	private String feed_type;
	private String actor_type;
	private String name;
	private String actor_id;
	private String update_time;
	//private String headurl;
	private String message;
	private String title;
	private String link;
	private String prefix;
	private String description;
	private String feed_media_media_id;
	private String feed_media_owner_id;
	private String feed_media_owner_name;
	private String feed_media_media_link;
	private String feed_media_media_type;
	private String feed_media_src;
	
	private RenrenFriend zFriend;
	
	public RenenFeedElement () {
		zFriend = new RenrenFriend();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setFeed_type(String feed_type) {
		this.feed_type = feed_type;
	}

	public String getFeed_type() {
		return feed_type;
	}

	public void setActor_type(String actor_type) {
		this.actor_type = actor_type;
	}

	public String getActor_type() {
		return actor_type;
	}

	public void setActor_id(String actor_id) {
		this.actor_id = actor_id;
	}

	public String getActor_id() {
		return actor_id;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLink() {
		return link;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setFeed_media_media_id(String feed_media_media_id) {
		this.feed_media_media_id = feed_media_media_id;
	}

	public String getFeed_media_media_id() {
		return feed_media_media_id;
	}

	public void setFeed_media_owner_id(String feed_media_owner_id) {
		this.feed_media_owner_id = feed_media_owner_id;
	}

	public String getFeed_media_owner_id() {
		return feed_media_owner_id;
	}

	public void setFeed_media_media_link(String feed_media_media_link) {
		this.feed_media_media_link = feed_media_media_link;
	}

	public String getFeed_media_media_link() {
		return feed_media_media_link;
	}

	public void setFeed_media_media_type(String feed_media_media_type) {
		this.feed_media_media_type = feed_media_media_type;
	}

	public String getFeed_media_media_type() {
		return feed_media_media_type;
	}

	public void setFeed_media_owner_name(String feed_media_owner_name) {
		this.feed_media_owner_name = feed_media_owner_name;
	}

	public String getFeed_media_owner_name() {
		return feed_media_owner_name;
	}

	public void setFeed_media_src(String feed_media_src) {
		this.feed_media_src = feed_media_src;
	}

	public String getFeed_media_src() {
		return feed_media_src;
	}

	public void setUser(RenrenFriend user) {
		this.zFriend = user;
	}

	public RenrenFriend getFriend() {
		return zFriend;
	}
	
	
}

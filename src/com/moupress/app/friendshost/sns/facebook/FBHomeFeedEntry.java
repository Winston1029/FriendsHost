package com.moupress.app.friendshost.sns.facebook;

import java.util.List;

public class FBHomeFeedEntry {
	public final String TYPE_LINK = "link";
	public final String TYPE_PHOTO = "photo";
	public final String TYPE_STATUS = "status";
	
	private String caption;
	private String id;
	private String icon;
	private String name;
	private String message;
	private String story;
	//private String story_tags;
	private String picture;
	private String link;
	private String description;
	private String source;
	private String attribution;
	private String updated_time;
	private String created_time;
	private String type; //link, photo, status
	   
	//Nested classes for From entries
	private FBHomeFeedEntryFrom from;
	
	//Nested collection for Actions entries
	private List<FBHomeFeedEntryAction> actions;
	
	//No Arg constructor
	public FBHomeFeedEntry() {}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getCaption() {
		return caption;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIcon() {
		return icon;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getPicture() {
		return picture;
	}

	public void setAttribution(String attribution) {
		this.attribution = attribution;
	}

	public String getAttribution() {
		return attribution;
	}

	public void setUpdated_time(String updated_time) {
		this.updated_time = updated_time;
	}

	public String getUpdated_time() {
		return updated_time;
	}

	public void setCreated_time(String created_time) {
		this.created_time = created_time;
	}

	public String getCreated_time() {
		return created_time;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setFrom(FBHomeFeedEntryFrom from) {
		this.from = from;
	}

	public FBHomeFeedEntryFrom getFrom() {
		return from;
	}

	public void setActions(List<FBHomeFeedEntryAction> actions) {
		this.actions = actions;
	}

	public List<FBHomeFeedEntryAction> getActions() {
		return actions;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLink() {
		return link;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource() {
		return source;
	}

	public void setStory(String story) {
		this.story = story;
	}

	public String getStory() {
		return story;
	}

//	public void setStory_tags(String story_tags) {
//		this.story_tags = story_tags;
//	}
//
//	public String getStory_tags() {
//		return story_tags;
//	}
	
	//Getter methods should be declared.  Setter methods are optional 
}

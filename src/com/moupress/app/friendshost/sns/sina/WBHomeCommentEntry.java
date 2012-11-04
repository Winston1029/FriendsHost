package com.moupress.app.friendshost.sns.sina;

import com.moupress.app.friendshost.Const;

public class WBHomeCommentEntry {

	
	private String sns = Const.SNS_SINA;
	private String comment_id;
	private String feed_id;
	private String usr_id;
	private String usr_name;
	private String usr_hdr_url;
	private String message;
	private String create_tm;
	
	public WBHomeCommentEntry(){}

	public String getSns() {
		return sns;
	}

	public void setSns(String sns) {
		this.sns = sns;
	}

	public String getComment_id() {
		return comment_id;
	}

	public void setComment_id(String commentId) {
		comment_id = commentId;
	}

	public String getFeed_id() {
		return feed_id;
	}

	public void setFeed_id(String feedId) {
		feed_id = feedId;
	}

	public String getUsr_id() {
		return usr_id;
	}

	public void setUsr_id(String usrId) {
		usr_id = usrId;
	}

	public String getUsr_name() {
		return usr_name;
	}

	public void setUsr_name(String usrName) {
		usr_name = usrName;
	}

	public String getUsr_hdr_url() {
		return usr_hdr_url;
	}

	public void setUsr_hdr_url(String usrHdrUrl) {
		usr_hdr_url = usrHdrUrl;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCreate_tm() {
		return create_tm;
	}

	public void setCreate_tm(String createTm) {
		create_tm = createTm;
	}
	
	

}

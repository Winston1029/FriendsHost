/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.moupress.app.util.Renren;

/**
 * �?装�?�新鲜事用到的�?�数。
 * 
 * @author yong.li@opi-corp.com
 */
public class FeedParam {

	/**
	 * 新鲜事模�?�ID
	 */
	private int templateId;

	/**
	 * 用�?�进行模�?�替�?�的json数�?�串
	 */
	private String templateData;

	/**
	 * 新鲜事body的附件信�?�
	 */
	private String bodyGeneral;

	/**
	 * 用户输入的评论信�?�
	 */
	private String userMessage = "";

	/**
	 * 针对用户输入框的�??示信�?�
	 */
	private String userMessagePrompt = "";

	public String getFeedInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		if (this.templateId > 0)
			sb.append("\"template_id\": \"" + this.templateId + "\"");
		if (this.templateData != null)
			sb.append(",\"template_data\": " + this.templateData);
		if (this.bodyGeneral != null)
			sb.append(",\"body_general\": \"" + this.bodyGeneral + "\"");
		sb.append("}");
		return sb.toString();
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public String getTemplateData() {
		return templateData;
	}

	public void setTemplateData(String templateData) {
		this.templateData = templateData;
	}

	public String getBodyGeneral() {
		return bodyGeneral;
	}

	public void setBodyGeneral(String bodyGeneral) {
		this.bodyGeneral = bodyGeneral;
	}

	public String getUserMessage() {
		return userMessage;
	}

	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}

	public String getUserMessagePrompt() {
		return userMessagePrompt;
	}

	public void setUserMessagePrompt(String userMessagePrompt) {
		this.userMessagePrompt = userMessagePrompt;
	}
}

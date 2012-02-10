/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.moupress.app.util.Renren;

import com.renren.android.RenrenError;
import com.renren.android.RenrenException;
import com.renren.android.RequestParam;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * �?装feed.publishFeed接�?�的新鲜事�?�数
 * 
 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com) 2011-08-04
 */
public class FeedPublishRequestParam extends RequestParam implements Parcelable{
	
	private static final String METHOD = "feed.publishFeed";
	/**
	 * 标题长度�?制
	 */
	private static final int NAME_MAX_LENGTH = 30;

	/**
	 * 新鲜事主体内容长度�?制
	 */
	private static final int DESCRIPTION_MAX_LENGTH = 200;
	
	/**
	 * 新鲜事主体内容长度�?制（Widget）
	 */
	private static final int WIDGET_DESCRIPTION_MAX_LENGTH = 120;
		
	/**
	 * 新鲜事副标题内容长度�?制
	 */
	private static final int CAPTION_MAX_LENGTH = 20;
	
	/**
	 * 新鲜事动作模�?�文案长度�?制
	 */
	private static final int ACTION_NAME_MAX_LENGTH = 10;
	
	/**
	 * 用户自定义内容长度�?制
	 */
	private static final int MESSAGE_MAX_LENGTH = 200;
	
	/**
	 * 用户自定义内容长度�?制（WIDGET）
	 */
	private static final int WIDGET_MESSAGE_MAX_LENGTH = 140;
	
	/**
	 * 正常长度
	 */
	public static final int NORMAL_LENGTH = 0x000000;
	
	/**
	 * 标题太长
	 */
	public static final int NAME_TOO_LONG = 0x000010;
	
	/**
	 * �??述太长
	 */
	public static final int DESCRIPTION_TOO_LONG = 0x000100;
	
	/**
	 * 副标题太长
	 */
	public static final int CAPTION_TOO_LONG = 0x001000;;
	
	/**
	 * 动作标题太长
	 */
	public static final int ACTION_NAME_TOO_LONG = 0x010000;;
	
	/**
	 * 用户输入内容过长
	 */
	public static final int MESSAGE_TOO_LONG = 0x100000;
	
	/**
	 * 新鲜事标题，最多30个字符，为必须�?�数
	 */
	private String name;
	
	/**
	 * 新鲜事主体内容，最多200个字符，为必须�?�数
	 */
	private String description;
	
	/**
	 * 新鲜事标题和图片指�?�的链接，为必须�?�数
	 */
	private String url;
	
	/**
	 * 新鲜事图片地�?�，为�?�选�?�数
	 */
	private String imageUrl;
	
	/**
	 * 新鲜事副标题，最多20个字符，为�?�选�?�数
	 */
	private String caption;
	
	/**
	 * 新鲜事动作模�?�文案，最多20个字符，为�?�选�?�数
	 */
	private String actionName;
	
	/**
	 * 新鲜事动作模�?�链接，为�?�选�?�数
	 */
	private String actionLink;
	
	/**
	 * 用户输入的自定义内容，最多200个字符，为�?�选�?�数
	 */
	private String message;
	
	public FeedPublishRequestParam(Parcel in) {
		Bundle bundle = in.readBundle();
		name = bundle.getString("name");
		description = bundle.getString("description");
		url = bundle.getString("url");
		imageUrl = bundle.getString("image_url");
		caption = bundle.getString("caption");
		actionName = bundle.getString("action_name");
		actionLink = bundle.getString("action_link");
		message = bundle.getString("message");
	}
	
	public FeedPublishRequestParam(String name, String description, String url, String imageUrl, String caption,
			String actionName, String actionLink, String message) {
		this.name = name;
		this.description = description;
		this.url = url;
		this.imageUrl = imageUrl;
		this.caption = caption;
		this.actionName = actionName;
		this.actionLink = actionLink;
		this.message = message;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Bundle bundle = new Bundle();
		if(name != null) {
			bundle.putString("name", name);
		}
		if(description != null) {
			bundle.putString("description", description);
		}
		if(url != null) {
			bundle.putString("url", url);
		}
		if(imageUrl != null) {
			bundle.putString("image_url", imageUrl);
		}
		if(caption != null) {
			bundle.putString("caption", caption);
		}
		if(actionName != null) {
			bundle.putString("action_name", actionName);
		}
		if(actionLink != null) {
			bundle.putString("action_link", actionLink);
		}
		if(message != null) {
			bundle.putString("message", message);
		}
		dest.writeBundle(bundle);
	}

	public static final Parcelable.Creator<FeedPublishRequestParam> CREATOR
             = new Parcelable.Creator<FeedPublishRequestParam>() {
         public FeedPublishRequestParam createFromParcel(Parcel in) {
             return new FeedPublishRequestParam(in);
         }

         public FeedPublishRequestParam[] newArray(int size) {
             return new FeedPublishRequestParam[size];
         }
    };

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getUrl() {
		return url;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getCaption() {
		return caption;
	}

	public String getActionName() {
		return actionName;
	}

	public String getActionLink() {
		return actionLink;
	}

	public String getMessage() {
		return message;
	}

	public static Parcelable.Creator<FeedPublishRequestParam> getCreator() {
		return CREATOR;
	}    
	
	/**
	 * 将超出长度�?制的�?�数截短
	 * @return
	 */
	public void trunc() {
		if(name != null && name.length() > NAME_MAX_LENGTH) {
			name = name.substring(0, NAME_TOO_LONG);
		}
		
		if(description != null && description.length() > DESCRIPTION_MAX_LENGTH) {
			description = description.substring(0, DESCRIPTION_TOO_LONG);
		}
		
		if(caption != null && caption.length() > CAPTION_MAX_LENGTH) {
			caption = caption.substring(0, CAPTION_MAX_LENGTH);
		}
		
		if(actionName != null && actionName.length() > ACTION_NAME_MAX_LENGTH) {
			actionName = actionName.substring(0, ACTION_NAME_TOO_LONG);
		}
		
		if(message != null && message.length() > MESSAGE_MAX_LENGTH) {
			message = message.substring(0, MESSAGE_TOO_LONG);
		}
	}

	/**
	 * 检查调用API的新鲜事中�?��?�数的长度是�?�短于上�?
	 * 
	 * @return 
	 * 		Feed长度的状�?，详�?{@link FeedPublishRequestParam}中的定义
	 */
	private int checkFeed(){
		int flag = FeedPublishRequestParam.NORMAL_LENGTH;
		if(name != null && name.length() > FeedPublishRequestParam.NAME_MAX_LENGTH) {
			flag |= FeedPublishRequestParam.NAME_TOO_LONG;
		}
		
		if(description != null && 
				description.length() > FeedPublishRequestParam.DESCRIPTION_MAX_LENGTH) {
			flag |= FeedPublishRequestParam.DESCRIPTION_TOO_LONG;
		}
		
		if(caption != null && caption.length() > FeedPublishRequestParam.CAPTION_MAX_LENGTH) {
			flag |= FeedPublishRequestParam.CAPTION_TOO_LONG;
		}
		
		if(actionName != null 
				&& actionName.length() > FeedPublishRequestParam.ACTION_NAME_MAX_LENGTH) {
			flag |= FeedPublishRequestParam.ACTION_NAME_TOO_LONG;
		}
		
		if(message != null && message.length() > FeedPublishRequestParam.MESSAGE_MAX_LENGTH) {
			flag |= FeedPublishRequestParam.MESSAGE_TOO_LONG;
		}
		
		return flag;
	}
	
	
	/**
	 * 检查Widget新鲜事中�?��?�数的长度是�?�短于上�?
	 * 
	 * @return 
	 * 		Feed长度的状�?，详�?{@link FeedPublishRequestParam}中的定义
	 */
	private int checkWidgetFeed(){
		int flag = FeedPublishRequestParam.NORMAL_LENGTH;
		if(name != null && name.length() > FeedPublishRequestParam.NAME_MAX_LENGTH) {
			flag |= FeedPublishRequestParam.NAME_TOO_LONG;
		}
		
		if(description != null && 
				description.length() > FeedPublishRequestParam.WIDGET_DESCRIPTION_MAX_LENGTH) {
			flag |= FeedPublishRequestParam.DESCRIPTION_TOO_LONG;
		}
		
		if(caption != null && caption.length() > FeedPublishRequestParam.CAPTION_MAX_LENGTH) {
			flag |= FeedPublishRequestParam.CAPTION_TOO_LONG;
		}
		
		if(actionName != null 
				&& actionName.length() > FeedPublishRequestParam.ACTION_NAME_MAX_LENGTH) {
			flag |= FeedPublishRequestParam.ACTION_NAME_TOO_LONG;
		}
		
		if(message != null && message.length() > FeedPublishRequestParam.WIDGET_MESSAGE_MAX_LENGTH) {
			flag |= FeedPublishRequestParam.MESSAGE_TOO_LONG;
		}
		
		return flag;
	}

	/**
	 * 返回用于API的�?�数
	 */
	@Override
	public Bundle getParams() throws RenrenException {
		if(name == null || name.length() == 0 || description == null || 
				description.length() == 0 || url == null || url.length() == 0) {
			String errorMsg = "Required parameter could not be null.";
			throw new RenrenException(
					RenrenError.ERROR_CODE_NULL_PARAMETER, errorMsg,
					errorMsg);
		}
		
		if(checkFeed() != NORMAL_LENGTH) {
			String errorMsg = "Some parameter is illegal for feed.";
			throw new RenrenException(
					RenrenError.ERROR_CODE_ILLEGAL_PARAMETER, errorMsg,
					errorMsg);
		}
		
		Bundle params = new Bundle();
		params.putString("method", METHOD);
		params.putString("name", name);
		params.putString("description", description);
		params.putString("url", url);
		if(imageUrl != null) {
			params.putString("image", imageUrl);
		}
		if(caption != null) {
			params.putString("caption", caption);
		}
		if(actionName != null) {
			params.putString("action_name", actionName);
		}
		if(actionLink != null) {
			params.putString("action_link", actionLink);
		}
		if(message != null) {
			params.putString("message", message);
		}
		return params;
	}
	

	/**
	 * 返回用于用于Widget的�?�数
	 * @throws RenrenException
	 * @return 按照Widget�?求组织的�?�数
	 */
	public Bundle getWidgetParams() throws RenrenException {
		if(name == null || name.length() == 0 || description == null || 
				description.length() == 0 || url == null || url.length() == 0) {
			String errorMsg = "Required parameter could not be null.";
			throw new RenrenException(
					RenrenError.ERROR_CODE_NULL_PARAMETER, errorMsg,
					errorMsg);
		}
		
		if(checkWidgetFeed() != NORMAL_LENGTH) {
			String errorMsg = "Some parameter is illegal for feed.";
			throw new RenrenException(
					RenrenError.ERROR_CODE_ILLEGAL_PARAMETER, errorMsg,
					errorMsg);
		}
		
		Bundle params = new Bundle();
		params.putString("method", METHOD);
		params.putString("name", name);
		params.putString("description", description);
		params.putString("url", url);
		if(imageUrl != null) {
			params.putString("image", imageUrl);
		}
		if(caption != null) {
			params.putString("caption", caption);
		}
		if(actionName != null) {
			params.putString("action_name", actionName);
		}
		if(actionLink != null) {
			params.putString("action_link", actionLink);
		}
		if(message != null) {
			params.putString("message", message);
		}
		return params;
	}
}

/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.moupress.app.util.Renren;

import org.json.JSONException;
import org.json.JSONObject;

import com.renren.android.ResponseBean;
import com.renren.android.Util;


/**
 * �?装�?��?新鲜事�?功�?�的返回结果
 *  
 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com)
 *
 */
public class FeedPublishResponseBean extends ResponseBean {

	public static final String RESPONSE = "post_id";
	
	/**
	 * 默认的postId，若post ID为默认id，则表示没有正确获�?�post id
	 */
	public static final int DEFAULT_POST_ID = 0;
	/**
	 * 新鲜事的id 
	 */
	private int postId;
	
	public FeedPublishResponseBean(String response) {
		super(response);
		try {
			JSONObject json = new JSONObject(response);
			postId = json.getInt(RESPONSE);
		} catch(JSONException je) {
			Util.logger(je.getMessage());
			postId = DEFAULT_POST_ID;
		}
	}

	public int getPostId() {
		return postId;
	}

	@Override
	public String toString() {
		return "post_id: " + postId; 
	}
}

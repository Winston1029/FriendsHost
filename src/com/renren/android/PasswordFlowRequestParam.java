/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.android;

import android.os.Bundle;
import android.text.TextUtils;


/**
 * 
 * @author hecao (he.cao@renren-inc.com)
 * 使用用户�??密�?登陆方�?的请求对象。
 *
 */
public class PasswordFlowRequestParam extends RequestParam {
	
	/**
	 * 构造一个password flow请求对象
	 * 
	 * @param userName
	 *            用户的用户�?? 若调用输入界�?�，则此�?�数�?�为空
	 * @param password
	 *            用户的密�?，明文 若调用输入界�?�，则此�?�数�?�为空
	 */
	public PasswordFlowRequestParam (String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	/**
	 * 构造一个password flow请求对象
	 * 
	 * @param userName
	 *            用户的用户�?? 若调用输入界�?�，则此�?�数�?�为空
	 * @param password
	 *            用户的密�?，若调用输入界�?�，则此�?�数�?�为空
	 * @param permissions
	 *            需�?的�?��? �?�选�?�数
	 */
	public PasswordFlowRequestParam (String userName, String password, String[] permissions) {
		this.userName = userName;
		this.password = password;
		this.permissions = permissions;
	}
	
	/**
	 * 应用的api key
	 */
	private String apiKey;
	
	/**
	 * 应用的secret key
	 */
	private String secretKey;
	
	/**
	 * 用户的用户�??
	 */
	private String userName;
	
	/**
	 * 用户的密�?，明文
	 */
	private String password;
	
	/**
	 * 请求的�?��?
	 */
	private String[] permissions;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String[] getPermissions() {
		return permissions;
	}

	public void setPermissions(String[] permissions) {
		this.permissions = permissions;
	}

	@Override
	public Bundle getParams() throws RenrenException {
		

		checkNullParams(apiKey, secretKey, userName, password);

        Bundle parameters = new Bundle();
        parameters.putString("grant_type", "password");
        parameters.putString("username", userName);
        parameters.putString("password", password);
        parameters.putString("client_id", apiKey);
        parameters.putString("client_secret", secretKey);
        if (permissions != null && permissions.length > 0) {
            String scope = TextUtils.join(" ", permissions);
            parameters.putString("scope", scope);
        }
        
        return parameters;
	}

}

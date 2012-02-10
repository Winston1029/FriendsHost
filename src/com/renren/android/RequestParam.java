/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.android;

import android.os.Bundle;
import android.text.TextUtils;

/**
 * 
 * å¼€æ”¾å¹³å?°å?„ApiæŽ¥å?£è¯·æ±‚å?‚æ•°çš„æŠ½è±¡ç±»
 * 
 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com)
 *
 */
public abstract class RequestParam {
	
	public abstract Bundle getParams() throws RenrenException;
	
	public void checkNullParams (String... params) throws RenrenException {
		
		for (String param : params) {
			if (TextUtils.isEmpty(param)) {
				String errorMsg = "required parameter MUST NOT be null";
				throw new RenrenException(
						RenrenError.ERROR_CODE_NULL_PARAMETER, errorMsg,
						errorMsg);
			}
		}
		
	}
}

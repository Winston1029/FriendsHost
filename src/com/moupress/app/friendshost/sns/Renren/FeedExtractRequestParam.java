package com.moupress.app.friendshost.sns.Renren;


import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.renren.api.connect.android.common.RequestParam;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.exception.RenrenException;

public class FeedExtractRequestParam extends RequestParam implements Parcelable {
	
	private static final String METHOD = "feed.get";
			
	private String format;
	private String type;
	private int page;
	private String count;
	
	
	public FeedExtractRequestParam(String format, String type, int page)
	{
		this.format = format;
		this.type = type;
		this.page = page;
		this.count = "100";
	}
	
	

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
	}

	@Override
	public Bundle getParams() throws RenrenException {
		
		if(type == null || type.length() == 0)
		{
			String errorMsg = "Required parameter could not be null.";
			throw new RenrenException(
					RenrenError.ERROR_CODE_NULL_PARAMETER, errorMsg,
					errorMsg);
		}
		
		Bundle params = new Bundle();
		
		params.putString("method", METHOD);
		params.putString("format", "XML");
		params.putString("type", type);
		//params.putInt("page", page);
		params.putString("page", page+"");
		//params.putString("count", count);
		
		return params;
	}

	
}

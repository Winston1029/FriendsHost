package com.moupress.app.util.Renren;



import com.renren.android.RenrenError;
import com.renren.android.RenrenException;
import com.renren.android.RequestParam;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
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
		params.putString("format", "JSON");
		params.putString("type", type);
		params.putInt("page", page);
		//params.putString("count", count);
		
		return params;
	}

	
}

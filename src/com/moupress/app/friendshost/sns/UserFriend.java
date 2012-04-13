package com.moupress.app.friendshost.sns;

import com.moupress.app.friendshost.Const;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class UserFriend implements Parcelable{
	protected String id;
	protected String name;
	protected String headurl;
	
	protected String sns;
	
	public UserFriend()
	{}
	public UserFriend(Parcel in) {
		
		Bundle bundle = Bundle.CREATOR.createFromParcel(in);
		
		this.id = bundle.getString(Const.FID);
		this.name = bundle.getString(Const.FNAME);
		this.headurl = bundle.getString(Const.FHEADURL);
		this.sns = bundle.getString(Const.FSNS);

	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setHeadurl(String headurl) {
		this.headurl = headurl;
	}
	public String getHeadurl() {
		return headurl;
	}
	public void setSNS(String sns) {
		this.sns = sns;
	}
	public String getSNS() {
		return sns;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
		
		if(id != null) { bundle.putString(Const.FID,id);}
		if(name != null) { bundle.putString(Const.FNAME,name);}
		if(headurl != null) { bundle.putString(Const.FHEADURL,headurl);}
		if(sns != null) { bundle.putString(Const.FSNS,sns);}
		
		bundle.writeToParcel(out, flags);
	}
	
	public static final Parcelable.Creator<UserFriend> CREATOR = new Parcelable.Creator<UserFriend>(){

		@Override
		public UserFriend createFromParcel(Parcel in) {
			return new UserFriend(in);
		}

		@Override
		public UserFriend[] newArray(int size) {
			return new UserFriend[size];
		}};
}

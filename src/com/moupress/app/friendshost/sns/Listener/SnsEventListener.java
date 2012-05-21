package com.moupress.app.friendshost.sns.Listener;



/**
 * @author Li Ji
 * 
 * Listener for events that Sns is Selected/Unselected
 * It is the interface between Presenter(MainUIViews, etc) and Model(SnsOrg)
 * 
 */

public interface SnsEventListener {
	
	public void OnSnsUtilAdded(String snsName);
	public void OnSnsUtilRemoved(String snsName);
	
}

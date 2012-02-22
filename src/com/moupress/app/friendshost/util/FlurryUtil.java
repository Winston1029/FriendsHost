package com.moupress.app.friendshost.util;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.flurry.android.FlurryAgent;

public class FlurryUtil {
	//	2RB6SIPR2RL6B84S6KJS
	// MN4C6SHMKI5W6FX38BWE
	private static String FlurryAPI_Key = "2RB6SIPR2RL6B84S6KJS";
	
	public static void onStart(Context context) {
		
		FlurryAgent.setReportLocation(false);
		FlurryAgent.onStartSession(context, FlurryAPI_Key);
	}
	
	public static void onStop(Context context) {
		FlurryAgent.onEndSession(context);
	}
	
	public static void logEvent(String eventKey, String param) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(eventKey, param);
		FlurryAgent.logEvent(eventKey, map);
	}
}

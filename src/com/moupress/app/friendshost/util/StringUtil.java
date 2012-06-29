package com.moupress.app.friendshost.util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	
	private static String regex_url = "(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	private static String regex_id = "\\d+";

	public static ArrayList<String> retrieveURL(String message) {
		Matcher m = Pattern.compile(regex_url).matcher(message);
		ArrayList<String> urls = new ArrayList<String>();
		while (m.find()) {
			urls.add(m.group());
		}
		return urls;
	}
	
	public static ArrayList<String> retrieveID(String message) {
		Matcher m = Pattern.compile(regex_id).matcher(message);
		ArrayList<String> ids = new ArrayList<String>();
		while (m.find()) {
			ids.add(m.group());
		}
		return ids;
	}
}

package com.moupress.app.friendshost.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import weibo4andriod.Status;

import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.sns.FeedEntryComment;
import com.moupress.app.friendshost.sns.UserFriend;
import com.moupress.app.friendshost.sns.Renren.RenenFeedElement;
import com.moupress.app.friendshost.sns.Renren.RenrenFeedElementComments.RenrenFeedElementComment;
import com.moupress.app.friendshost.sns.Renren.RenrenFeedElementEntry;
import com.moupress.app.friendshost.sns.facebook.FBHomeFeedEntry;
import com.moupress.app.friendshost.sns.facebook.FBHomeFeedEntryAction;
import com.moupress.app.friendshost.sns.facebook.FBHomeFeedEntryComments.FBFeedEntryComment;
import com.moupress.app.friendshost.sns.facebook.FBHomeFeedEntryFrom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper {

	static final String DATABASE_NAME = "friendhost_feed.db";
    static final String NEWSFEED_TABLE_NAME = "newsfeed";
    static final int DATABASE_VERSION = 2;
    
    //query condition
    static final String ORDER_DESC = " DESC";
    static final String LIMIT = " LIMIT 10";
    
    //sns network type
    static final String SNS_FACEBOOK = "Facebook";
    static final String SNS_RENREN = "Renren";
    static final String SNS_SINA = "Sina";
    static final String SNS_TWITTER = "Twitter";

    // database tables
    static final String T_USER = "User";
    static final String T_FEED = "Feed";
    static final String T_COMMENTS = "Comments";
    static final String T_ACTIONS = "Actions";
    
    // User Columns
    static final String C_USER_ID = "id";
    static final String C_USER_SNS = "SNS";
    static final String C_USER_NAME = "name";
    static final String C_USER_HEADURL = "headurl";

    // Feed Columns
    static final String C_FEED_ID = "id";
    static final String C_FEED_FROM = "feedFrom";
    static final String C_FEED_OWNER_ID = "feed_owner_id";
    static final String C_FEED_SNS = "SNS";
    static final String C_FEED_MSG = "msg";
    static final String C_FEED_STORY = "story";
    static final String C_FEED_PIC = "pic";
    static final String C_FEED_SOURCE = "source";
    static final String C_FEED_LINK = "link";
    static final String C_FEED_NAME = "name";
    static final String C_FEED_CAPTION = "caption";
    static final String C_FEED_DESCRIPTION = "description";
    static final String C_FEED_ICON = "icon";
    static final String C_FEED_TYPE = "type";
    static final String C_FEED_ISREAD = "isread";
    static final String C_FEED_CREATED_TIME = "created_time";
    static final String C_FEED_UPDATED_TIME = "updated_time";
    
    // Comments Columns
    static final String C_COMMENTS_ID = "id";
    static final String C_COMMENTS_SNS = "SNS";
    static final String C_COMMENTS_FEEDID = "feedid";
    static final String C_COMMENTS_USERID = "comment_userid";
    static final String C_COMMENTS_USERNAME = "comment_username";
    static final String C_COMMENTS_USERHEADURL = "comment_userheadurl";
    static final String C_COMMENTS_MSG = "msg";
    static final String C_COMMENTS_CREATED_TIME = "created_time";
    
    // Action Columns
    static final String C_ACTIONS_FEEDID = "feedid";
    static final String C_ACTIONS_NAME = "name";
    static final String C_ACTIONS_LINK = "link";
    
    // Create table SQL statement
    static final String CREATE_USER_TABLE = "CREATE TABLE " + T_USER + " ("
										    + C_USER_ID + " TEXT PRIMARY KEY,"
										    + C_USER_SNS + " TEXT,"
										    + C_USER_NAME + " TEXT,"
										    + C_USER_HEADURL + " TEXT"
										    + ");";
    
    static final String CREATE_FEED_TABLE = "CREATE TABLE " + T_FEED + " ("
										    + C_FEED_ID + " TEXT PRIMARY KEY,"
										    + C_FEED_SNS + " TEXT,"
										    + C_FEED_FROM + " TEXT,"
										    + C_FEED_OWNER_ID + " TEXT,"
										    + C_FEED_MSG + " TEXT,"
										    + C_FEED_STORY + " TEXT,"
										    + C_FEED_PIC + " TEXT,"
										    + C_FEED_SOURCE + " TEXT,"
										    + C_FEED_LINK + " TEXT,"
										    + C_FEED_NAME + " TEXT,"
										    + C_FEED_CAPTION + " TEXT,"
										    + C_FEED_DESCRIPTION + " TEXT,"
										    + C_FEED_ICON + " TEXT,"
										    + C_FEED_TYPE + " TEXT,"
										    + C_FEED_ISREAD + " TEXT,"
										    + C_FEED_CREATED_TIME + " TEXT,"
										    + C_FEED_UPDATED_TIME + " TEXT"
										    + ");";
    
    static final String CREATE_COMMENTS_TABLE = "CREATE TABLE " + T_COMMENTS + " ("
										    + C_COMMENTS_ID + " TEXT PRIMARY KEY,"
										    + C_COMMENTS_SNS + " TEXT,"
										    + C_COMMENTS_FEEDID + " TEXT,"
										    + C_COMMENTS_USERID + " TEXT,"
										    + C_COMMENTS_USERNAME + " TEXT,"
										    + C_COMMENTS_USERHEADURL + " TEXT,"
										    + C_COMMENTS_MSG + " TEXT,"
										    + C_COMMENTS_CREATED_TIME + " TEXT"
										    + ");";
    
    static final String CREATE_ACTIONS_TABLE = "CREATE TABLE " + T_ACTIONS + " ("
										    + C_ACTIONS_FEEDID + " TEXT PRIMARY KEY,"
										    + C_ACTIONS_NAME + " TEXT,"
										    + C_ACTIONS_LINK + " TEXT"
										    + ");";

    private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_USER_TABLE);
			db.execSQL(CREATE_FEED_TABLE);
			db.execSQL(CREATE_COMMENTS_TABLE);
			db.execSQL(CREATE_ACTIONS_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(Const.TAG, "Upgrading database from version " + oldVersion + " to "
	                + newVersion + ", which will destroy all old data");
			/*
			 * Be Careful, this will remove all existing data user has locally
			 */
			db.execSQL("DROP TABLE IF EXISTS " + CREATE_USER_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + CREATE_FEED_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + CREATE_COMMENTS_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + CREATE_ACTIONS_TABLE);
			onCreate(db);
		}

	}

	private static DatabaseHelper zDatabaseHelper;
	private static SQLiteDatabase zSQLiteDB;

	public DBHelper(Context context) {
		zDatabaseHelper = new DatabaseHelper(context);
		if (zSQLiteDB == null) {
			zSQLiteDB = zDatabaseHelper.getWritableDatabase();
		}
	}

	public void fCleanup() {
		if (zSQLiteDB != null) {
			zSQLiteDB.close();
			zSQLiteDB = null;
		}
	}
	
	// insert feed for facebook
	public long fInsertFeed(FBHomeFeedEntry entry) {
		// check if exist
		long ret = 0;
		if (fIfItemExist(entry.getId(), SNS_FACEBOOK, T_FEED)) {
			return ret;
		}
		ContentValues values  = new ContentValues();

		values.put(C_FEED_SNS, SNS_FACEBOOK);
		values.put(C_FEED_ID, entry.getId());
		values.put(C_FEED_MSG, entry.getMessage());
		values.put(C_FEED_STORY, entry.getStory());
		//values.put(C_FEED_STORYTAG, entry.getStory_tags());
		values.put(C_FEED_FROM, entry.getFrom().getName());
		values.put(C_FEED_OWNER_ID, entry.getFrom().getId());
		values.put(C_FEED_PIC, entry.getPicture());
		values.put(C_FEED_SOURCE, entry.getSource());
		values.put(C_FEED_LINK, entry.getLink());
		values.put(C_FEED_NAME, entry.getName());
		values.put(C_FEED_CAPTION, entry.getCaption());
		values.put(C_FEED_DESCRIPTION, entry.getDescription());
		values.put(C_FEED_ICON, entry.getIcon());
		values.put(C_FEED_TYPE, entry.getType());
		values.put(C_FEED_ISREAD, "0");
		values.put(C_FEED_CREATED_TIME, entry.getCreated_time());
		values.put(C_FEED_UPDATED_TIME, entry.getUpdated_time());
		
		ret = zSQLiteDB.insert(T_FEED, null, values);
		
		//fInsertUser(entry.getFrom());
		//fInsertActions(entry.getActions());
		return ret;
	}
	
	// insert feed for Sina
	public long fInsertFeed(Status status) {
		long ret = 0;
		
		if (fIfItemExist(status.getId() + "", SNS_SINA, T_FEED)) {
			return ret;
		}
		
		ContentValues values  = new ContentValues();
		values.put(C_FEED_SNS, SNS_SINA);
		values.put(C_FEED_ISREAD, "0");
		values.put(C_FEED_ID, status.getId() + "");
		values.put(C_FEED_FROM, status.getUser().getName());
		values.put(C_FEED_OWNER_ID, status.getUser().getId());
		values.put(C_FEED_MSG, status.getText());
		values.put(C_FEED_PIC, status.getThumbnail_pic());
		values.put(C_FEED_LINK, status.getOriginal_pic());
		values.put(C_FEED_UPDATED_TIME, status.getCreatedAt().toGMTString());
		values.put(C_FEED_CREATED_TIME, status.getCreatedAt().toGMTString());
		//values.put(C_FEED_FROM, status.getSource());
		
		ret = zSQLiteDB.insert(T_FEED, null, values);
		
		return ret;
	}
	
	// insert feed for Renren
	public long fInsertFeed(RenrenFeedElementEntry entry) {
		long ret = 0;
		
		if (fIfItemExist(entry.getPost_id(), SNS_RENREN, T_FEED)) {
			return ret;
		}
		
		ContentValues values  = new ContentValues();
		values.put(C_FEED_SNS, SNS_RENREN);
		values.put(C_FEED_ID, entry.getPost_id());
		values.put(C_FEED_TYPE, entry.getsFeedType());
		if (entry.getFeed_type().equals("10")) { //prefix and message are the same if feedtype is "更新状态的新鲜事"
			values.put(C_FEED_MSG, entry.getPrefix());
		} else {
			if (entry.getMessage() != null ) {
				values.put(C_FEED_MSG, entry.getPrefix() + ": " + entry.getMessage());	
			} else {
				values.put(C_FEED_MSG, entry.getPrefix());
			}
		}
		values.put(C_FEED_FROM, entry.getName());
		values.put(C_FEED_OWNER_ID, entry.getActor_id());
		values.put(C_FEED_ISREAD, "0");
		values.put(C_FEED_CREATED_TIME, entry.getsCreatedTime());
		values.put(C_FEED_UPDATED_TIME, entry.getUpdate_time());
		if (entry.getDescription() != null && !entry.getDescription().equals("null") ) {
			values.put(C_FEED_STORY, entry.getTitle() + "\n" + entry.getDescription());	
		} else {
			values.put(C_FEED_STORY, entry.getTitle());
		}
		
		// if media_type = blog the URL is need to construct in a special way
		//String media_type = entry.getFeed_media_media_type();
		//String link = entry.getLink();
		if (entry.getAttachment().size() > 0 ) {
			String media_type = entry.getAttachment().get(0).getMedia_type();
			String link = entry.getAttachment().get(0).getHref();
			
			if (media_type != null && media_type.equals("blog")) {
				//String actualBlog_Url = "http://blog.renren.com/blog/" + entry.getFeed_media_owner_id() + "/" + entry.getFeed_media_media_id();
				String actualBlog_Url = "http://blog.renren.com/blog/" + entry.getAttachment().get(0).getOwner_id() + "/" + entry.getAttachment().get(0).getMedia_id();
				values.put(C_FEED_LINK, actualBlog_Url);
			} else {
				values.put(C_FEED_LINK, link);
			}
			
			//values.put(C_FEED_PIC, entry.getFeed_media_src());
			values.put(C_FEED_PIC, entry.getAttachment().get(0).getSrc());
		}
		
		ret = zSQLiteDB.insert(T_FEED, null, values);
		
		return ret;
	}
	
	//Twitter
	public long fInsertFeed(twitter4j.Status status) {
		long ret = 0;
		
		if (fIfItemExist(String.valueOf(status.getId()), SNS_TWITTER, T_FEED)) {
			return ret;
		}
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"); 
		
		ContentValues values  = new ContentValues();
		values.put(C_FEED_SNS, SNS_TWITTER);
		values.put(C_FEED_ISREAD, "0");
		values.put(C_FEED_ID, status.getId() + "");
		values.put(C_FEED_FROM, status.getUser().getName());
		values.put(C_FEED_OWNER_ID, status.getUser().getId());
		values.put(C_FEED_MSG, status.getText());
		//values.put(C_FEED_PIC, status.GET);
		values.put(C_FEED_UPDATED_TIME, df.format(status.getCreatedAt()));
		values.put(C_FEED_CREATED_TIME, df.format(status.getCreatedAt()));
		
		ret = zSQLiteDB.insert(T_FEED, null, values);
		return ret;
	}
	
	private boolean fIfItemExist(String feedid, String sns, String table) {
		String item = fGetItemByID(feedid, sns, table);
		if (item != null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public long fInsertFriend(UserFriend friend) {
		long ret = 0;
		if (fIfItemExist(friend.getId(), friend.getSNS(), T_USER)) {
			return ret;
		}
		
		ContentValues values  = new ContentValues();
		
		values.put(C_USER_ID, friend.getId());
		values.put(C_USER_NAME, friend.getName());
		values.put(C_USER_HEADURL, friend.getHeadurl());
		values.put(C_USER_SNS, friend.getSNS());
		
		ret = zSQLiteDB.insert(T_USER, null, values);
		
		return ret;
	}
	
	public void fInsertActions(List<FBHomeFeedEntryAction> list) {
		ContentValues values  = new ContentValues();
	}
	
	public long fInsertComments(FBFeedEntryComment comment) {
		long ret = 0;
		if (fIfItemExist(comment.getId(), comment.getSns(), T_COMMENTS)) {
			return ret;
		}
		
		ContentValues values  = new ContentValues();
		values.put(C_COMMENTS_SNS, comment.getSns());
		values.put(C_COMMENTS_ID, comment.getId());
		values.put(C_COMMENTS_FEEDID, comment.getCommetedfeedID());
		values.put(C_COMMENTS_USERID, comment.getFrom().getId());
		values.put(C_COMMENTS_USERNAME, comment.getFrom().getName());
		values.put(C_COMMENTS_USERHEADURL, comment.getFrom().getHeadurl());
		values.put(C_COMMENTS_MSG, comment.getMessage());
		values.put(C_COMMENTS_CREATED_TIME, comment.getCreated_time());
		
		ret = zSQLiteDB.insert(T_COMMENTS, null, values);
		
		return ret;
	}
	
	public long fInsertComments(RenrenFeedElementComment comment) {
		long ret = 0;
		if (fIfItemExist(comment.getComment_id(), comment.getSns(), T_COMMENTS)) {
			return ret;
		}
		
		ContentValues values  = new ContentValues();
		values.put(C_COMMENTS_SNS, comment.getSns());
		values.put(C_COMMENTS_ID, comment.getComment_id());
		values.put(C_COMMENTS_FEEDID, comment.getCommetedfeedID());
		values.put(C_COMMENTS_USERID, comment.getUid());
		values.put(C_COMMENTS_USERNAME, comment.getName());
		values.put(C_COMMENTS_USERHEADURL, comment.getHeadurl());
		values.put(C_COMMENTS_MSG, comment.getText());
		values.put(C_COMMENTS_CREATED_TIME, comment.getTime());
		
		ret = zSQLiteDB.insert(T_COMMENTS, null, values);
		
		return ret;
	}
	
	public static void fUpdateFeedRead() {
		
	}
	
	public String[] fGetFeedSummary(String sns) {
		String[] columns = new String[] {C_FEED_FROM, C_FEED_MSG};
		String where = C_FEED_ISREAD + " = ? and " 
						+ C_FEED_SNS + " = ? ";
		String[] selectionArgs = new String[] {"0", sns};
		Cursor cursor = null;
		String[] result = null;
		try {
			cursor = zSQLiteDB.query(T_FEED, columns, where, selectionArgs, null, null, C_FEED_CREATED_TIME + ORDER_DESC);
			int numRows = cursor.getCount();
			result = new String[numRows];
			cursor.moveToFirst();
			for (int i = 0; i < numRows; ++i) {
				result[i] = cursor.getString(0) +" : "+ cursor.getString(1);
				cursor.moveToNext();
			}
		} catch (SQLException e) {
			Log.v(Const.TAG, "Get all birthday failed.", e);
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return result;
	}
	
	/**
	 * To be refactor
	 * @param sns
	 * @return
	 */
	public String[][] fGetFeedPreview(String sns) {
		String[] columns = new String[] {C_FEED_ID,
										 C_FEED_FROM, C_FEED_OWNER_ID, C_FEED_CREATED_TIME, 
										 C_FEED_TYPE, C_FEED_MSG, C_FEED_STORY, C_FEED_LINK,
										 C_FEED_PIC, C_FEED_NAME, C_FEED_CAPTION, C_FEED_DESCRIPTION};
		String where = C_FEED_ISREAD + " = ? and " 
						+ C_FEED_SNS + " = ?"; //and "
						//+ C_FEED_UPDATED_TIME + " > ?";
						//+ C_FEED_TYPE + " in (\"status\", \"picture\", \"link\")";
		String[] selectionArgs = new String[] {"0", sns};
		Cursor cursor = null;
		String[][] result = null;
		try {
			cursor = zSQLiteDB.query(T_FEED, columns, where, selectionArgs, 
					null, null, C_FEED_UPDATED_TIME + ORDER_DESC + LIMIT);
			int numRows = cursor.getCount();
			result = new String[numRows][columns.length];
			cursor.moveToFirst();
			for (int i = 0; i < numRows; ++i) {
				for (int j = 0; j < columns.length; ++j) {
					result[i][j] = cursor.getString(j);
				}
				cursor.moveToNext();
			}
		} catch (SQLException e) {
			Log.v(Const.TAG, "Get all birthday failed.", e);
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return result;
	}
	
	public String[][] fGetFeedByID(String sns, String feedid) {
		String[] columns = new String[] {C_FEED_ID,
				 C_FEED_FROM, C_FEED_OWNER_ID, C_FEED_CREATED_TIME, 
				 C_FEED_TYPE, C_FEED_MSG, C_FEED_STORY, C_FEED_LINK,
				 C_FEED_PIC, C_FEED_NAME, C_FEED_CAPTION, C_FEED_DESCRIPTION};
		String where = C_FEED_ID + " = ? and " 
						+ C_FEED_SNS + " = ?"; //and "
		//+ C_FEED_TYPE + " in (\"status\", \"picture\", \"link\")";
		String[] selectionArgs = new String[] {feedid, sns};
		Cursor cursor = null;
		String[][] result = null;
		try {
			cursor = zSQLiteDB.query(T_FEED, columns, where, selectionArgs, null, null, C_FEED_UPDATED_TIME + ORDER_DESC);
			int numRows = cursor.getCount();
			result = new String[numRows][columns.length];
			cursor.moveToFirst();
			for (int i = 0; i < numRows; ++i) {
				for (int j = 0; j < columns.length; ++j) {
					result[i][j] = cursor.getString(j);
				}
				cursor.moveToNext();
			}
		} catch (SQLException e) {
			Log.v(Const.TAG, "Get all birthday failed.", e);
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return result;
	}
	
	public String fGetItemByID (String feedid, String sns, String table) {
		String[] columns = new String[] {C_FEED_FROM, C_FEED_MSG};
		String where = C_FEED_SNS + " = ? and " 
						+ C_FEED_ID + " = ?";
		String[] selectionArgs = new String[] {sns, feedid};
		Cursor cursor = null;
		String result = null;
		
		try {
			cursor = zSQLiteDB.query(table, null, where, selectionArgs, null, null, null);
			cursor.moveToFirst();
			if (cursor.getCount() > 0 ) {
				result = cursor.getString(0) +" : "+ cursor.getString(1);
			}
//			int numRows = cursor.getCount();
//			result = new String[numRows];
//			for (int i = 0; i < numRows; ++i) {
//				result[i] = cursor.getString(0) +" : "+ cursor.getString(1);
//				cursor.moveToNext();
//			}
		} catch (SQLException e) {
			Log.v(Const.TAG, "Get all birthday failed.", e);
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return result;
	}
	
	public void fPurgeFeed() {
		
	}

	public String[][] fGetFeedOwner(String sns, String ownerid) {
		String[] columns = new String[] {C_USER_NAME, C_USER_HEADURL};
		String where = C_USER_SNS + " = ? and " 
						+ C_USER_ID + " = ?";
		String[] selectionArgs = new String[] {sns, ownerid};
		Cursor cursor = null;
		String[][] result = null;
		
		try {
			cursor = zSQLiteDB.query(T_USER, columns, where, selectionArgs, null, null, null);
			int numRows = cursor.getCount();
			result = new String[numRows][columns.length];
			cursor.moveToFirst();
			for (int i = 0; i < numRows; ++i) {
				for (int j = 0; j < columns.length; ++j) {
					result[i][j] = cursor.getString(j);
				}
				cursor.moveToNext();
			}
		} catch (SQLException e) {
			Log.v(Const.TAG, "Get all birthday failed.", e);
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return result;
	}
	
	public String[][] fGetFeedComments(String sns, String feedid) {
		String[] columns = new String[] {C_COMMENTS_ID, 
										 C_COMMENTS_USERID, C_COMMENTS_USERNAME, C_COMMENTS_USERHEADURL, 
										 C_COMMENTS_MSG, C_COMMENTS_CREATED_TIME};
		String where = C_COMMENTS_SNS + " = ? and " 
						+ C_COMMENTS_FEEDID + " = ?";
		String[] selectionArgs = new String[] {sns, feedid};
		Cursor cursor = null;
		String[][] result = null;
		
		try {
			cursor = zSQLiteDB.query(T_COMMENTS, columns, where, selectionArgs, null, null, null);
			int numRows = cursor.getCount();
			result = new String[numRows][columns.length];
			cursor.moveToFirst();
			for (int i = 0; i < numRows; ++i) {
				for (int j = 0; j < columns.length; ++j) {
					result[i][j] = cursor.getString(j);
				}
				cursor.moveToNext();
			}
		} catch (SQLException e) {
			Log.v(Const.TAG, "Get all birthday failed.", e);
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		
		return result;
	}


}

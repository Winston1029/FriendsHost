package com.moupress.app.friendshost.util;

import java.util.ArrayList;
import java.util.List;

import com.moupress.app.friendshost.Const;
import com.moupress.app.friendshost.sns.Renren.RenenFeedElement;
import com.moupress.app.friendshost.sns.facebook.FBHomeFeedEntry;
import com.moupress.app.friendshost.sns.facebook.FBHomeFeedEntryAction;
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
    static final String C_USER_NAME = "name";

    // Feed Columns
    static final String C_FEED_ID = "id";
    static final String C_FEED_FROM = "feedFrom";
    static final String C_FEED_SNS = "SNS";
    static final String C_FEED_MSG = "msg";
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
    static final String C_COMMENTS_FEEDID = "feedid";
    static final String C_COMMENTS_FROM = "commentFrom";
    static final String C_COMMENTS_MSG = "msg";
    static final String C_COMMENTS_CREATED_TIME = "created_time";
    
    // Action Columns
    static final String C_ACTIONS_FEEDID = "feedid";
    static final String C_ACTIONS_NAME = "name";
    static final String C_ACTIONS_LINK = "link";
    
    // Create table SQL statement
    static final String CREATE_USER_TABLE = "CREATE TABLE " + T_USER + " ("
										    + C_USER_ID + " TEXT PRIMARY KEY,"
										    + C_USER_NAME + " TEXT"
										    + ");";
    
    static final String CREATE_FEED_TABLE = "CREATE TABLE " + T_FEED + " ("
										    + C_FEED_ID + " TEXT PRIMARY KEY,"
										    + C_FEED_SNS + " TEXT,"
										    + C_FEED_FROM + " TEXT,"
										    + C_FEED_MSG + " TEXT,"
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
										    + C_COMMENTS_FEEDID + " TEXT,"
										    + C_COMMENTS_FROM + " TEXT,"
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
	
	public long fInsertFeed(FBHomeFeedEntry entry) {
		// check if exist
		long ret = 0;
		if (fIfFeedExist(entry.getId(), SNS_FACEBOOK)) {
			return ret;
		}
		ContentValues values  = new ContentValues();

		values.put(C_FEED_SNS, SNS_FACEBOOK);
		values.put(C_FEED_ID, entry.getId());
		values.put(C_FEED_MSG, entry.getMessage());
		values.put(C_FEED_FROM, entry.getFrom().getName());
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
		fInsertComments();
		return ret;
	}
	
	public long fInsertFeed(RenenFeedElement entry) {
		long ret = 0;
		
		if (fIfFeedExist(entry.getId(), SNS_RENREN)) {
			return ret;
		}
		
		ContentValues values  = new ContentValues();
		values.put(C_FEED_SNS, SNS_RENREN);
		values.put(C_FEED_ID, entry.getId());
		values.put(C_FEED_MSG, entry.getMessage());
		values.put(C_FEED_FROM, entry.getName());
		values.put(C_FEED_ISREAD, "0");
		values.put(C_FEED_UPDATED_TIME, entry.getUpdate_time());
		
		ret = zSQLiteDB.insert(T_FEED, null, values);
		
		return ret;
	}
	
	private boolean fIfFeedExist(String feedid, String sns) {
		String feed = fGetFeedByID(feedid, sns);
		if (feed != null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static void fInsertUser(FBHomeFeedEntryFrom fbHomeFeedEntryFrom) {
		ContentValues values  = new ContentValues();
	}
	
	public static void fInsertActions(List<FBHomeFeedEntryAction> list) {
		ContentValues values  = new ContentValues();
	}
	
	public static void fInsertComments() {
		
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
		String[] columns = new String[] {C_FEED_FROM, C_FEED_CREATED_TIME, C_FEED_MSG, C_FEED_PIC, C_FEED_DESCRIPTION};
		String where = C_FEED_ISREAD + " = ? and " 
						+ C_FEED_SNS + " = ? ";
		String[] selectionArgs = new String[] {"0", sns};
		Cursor cursor = null;
		String[][] result = null;
		try {
			cursor = zSQLiteDB.query(T_FEED, columns, where, selectionArgs, null, null, C_FEED_CREATED_TIME + ORDER_DESC);
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
	
	public String fGetFeedByID (String feedid, String sns) {
		String[] columns = new String[] {C_FEED_FROM, C_FEED_MSG};
		String where = C_FEED_ISREAD + " = ? and " 
						+ C_FEED_SNS + " = ? and " 
						+ C_FEED_ID + " = ?";
		String[] selectionArgs = new String[] {"0", sns, feedid};
		Cursor cursor = null;
		String result = null;
		
		try {
			cursor = zSQLiteDB.query(T_FEED, columns, where, selectionArgs, null, null, C_FEED_CREATED_TIME);
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
		if (sns.equals(SNS_RENREN)) {
			if (result != null) {
				System.out.println(result);
			}
		}
		return result;
	}
	
	public void fPurgeFeed() {
		
	}

	

}

package org.ralapanawa.mobile.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ralapanawa.mobile.entity.Water_Level;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler {
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "contactsManager";

	private SimpleDateFormat dateFormat;

	private Date date;
	private static Context context;

	public DatabaseHandler(Context context) {
		this.context = context;
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		date = new Date();
	}
	
	private final static String CAREAT_REST="CREATE TABLE ";

	private final static String CreateWATER_LEVEL = "CREATE TABLE " + Water_Level.TABLE_NAME + " ("
			+ Water_Level.TANK_ID + " varchar(10) NOT NULL,"
			+ Water_Level.WATER_LEVEL_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + Water_Level.EMP_ID
			+ " varchar(10) ," + Water_Level.DATE
			+ "  DATETIME  DEFAULT CURRENT_DATETIME ," + Water_Level.DEPTH_FT
			+ " varchar(30) NOT NULL," + Water_Level.CAPACITY
			+ " varchar(30) ," + Water_Level.SLUIPLB + " varchar(30) ,"
			+ Water_Level.SLUIPRB + " varchar(30)," + Water_Level.REMARKS
			+ " varchar(30) ," + Water_Level.GPS + " varchar(30) )";

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase database) {
			database.execSQL(CreateWATER_LEVEL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(DatabaseHandler.class.getName(),
					"Upgrading database from version " + oldVersion + " to "
							+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + Water_Level.TABLE_NAME);
			onCreate(db);
		}
	}

	public void addWaterLevel(Water_Level water_level) throws ParseException {
		String insert_sql = "INSERT INTO "+Water_Level.TABLE_NAME
				+ "("+Water_Level.TANK_ID+","+Water_Level.WATER_LEVEL_ID+","+Water_Level.EMP_ID+","+Water_Level.DATE+","+Water_Level.DEPTH_FT+","+Water_Level.CAPACITY+","+Water_Level.SLUIPLB+","+Water_Level.SLUIPRB+","+Water_Level.REMARKS+","+Water_Level.GPS+")VALUES	("
				+ "\""
				+ water_level.getTankID()
				+ "\""
				+ ","
				+ (getWaterLevels().size() + 1)
				+ ","
				+ null
				+ ","
				+ "(SELECT datetime('now'))"
				+ ","
				+ "\""
				+ water_level.getDepth_ft()
				+ "\""
				+ ","
				+ null
				+ ","
				+ null
				+ "," + null + "," + null + "," + null + ")";

		//ContentValues values = new ContentValues();
		//values.put(Water_Level.WATER_LEVEL_ID, (getWaterLevels().size() + 1));
		//values.put(Water_Level.TANK_ID, water_level.getTankID());
		//values.put(Water_Level.DEPTH_FT, water_level.getDepth_ft());

		// Inserting Row
		mDb.execSQL(insert_sql);

	}

	public List<Water_Level> getWaterLevels() throws ParseException {

		Cursor cursor = mDb.query(Water_Level.TABLE_NAME, new String[] {
				Water_Level.TANK_ID, Water_Level.WATER_LEVEL_ID,
				Water_Level.EMP_ID, Water_Level.DATE, Water_Level.DEPTH_FT,
				Water_Level.CAPACITY, Water_Level.SLUIPLB, Water_Level.SLUIPRB,
				Water_Level.REMARKS, Water_Level.GPS }, null, null, null, null,
				null);

		List<Water_Level> waLs = new ArrayList<Water_Level>();

		Log.i("DB", cursor.getCount() + "");

		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {

			Water_Level water_level = new Water_Level(
					cursor.getString(cursor.getColumnIndex(Water_Level.TANK_ID)),
					cursor.getString(cursor
							.getColumnIndex(Water_Level.WATER_LEVEL_ID)),
					cursor.getString(cursor.getColumnIndex(Water_Level.EMP_ID)),
					dateFormat.parse(cursor.getString(cursor
							.getColumnIndex(Water_Level.DATE))),
					cursor.getString(cursor
							.getColumnIndex(Water_Level.DEPTH_FT)),
					cursor.getString(cursor
							.getColumnIndex(Water_Level.CAPACITY)),
					cursor.getString(cursor.getColumnIndex(Water_Level.SLUIPLB)),
					cursor.getString(cursor.getColumnIndex(Water_Level.SLUIPRB)),
					cursor.getString(cursor.getColumnIndex(Water_Level.REMARKS)),
					cursor.getString(cursor.getColumnIndex(Water_Level.GPS)));

			waLs.add(water_level);
		}
		// return contact
		return waLs;
	}

	public Water_Level getWaterLevel(String tankid) throws ParseException {
		Water_Level wtr = null;
		List<Water_Level> wls = getWaterLevels();
		Log.i("DB + size", wls.get(0).getTankID() + "");
		for (Water_Level water_Level : wls) {
			//Log.i("DB-TNKID", water_Level.getTankID() + "");
			//Log.i("DB-ST", tankid+ "");

			if (water_Level.getTankID().equals(tankid)) {
				wtr = water_Level;
			}
		}

		return wtr;
	}

	public DatabaseHandler open() throws SQLException {
		mDbHelper = new DatabaseHelper(context);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}
}

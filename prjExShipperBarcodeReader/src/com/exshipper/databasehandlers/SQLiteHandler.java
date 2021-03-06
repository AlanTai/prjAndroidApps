package com.exshipper.databasehandlers;

import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHandler extends SQLiteOpenHelper{
	private static final String DATABASE_NAME = "exshipperDB";
	private static final int DATABASE_VERSION = 1;

	public SQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public long insertDataToDB(SQLiteTableTemplate argTable) throws Exception{
		return 0;
		
	}
	
	public int deleteDataFromDB(){
		return 0;
	}
	
	public int updateDataFromDB(){
		return 0;
	}
	
	
	//
	public JSONObject getAllDataFromDB(String argTableName) throws Exception{
		SQLiteDatabase db = getReadableDatabase();
		JSONObject jsonObjQueryResult = null;
		String strQueryCmd = null;
		Cursor cursor = null;
		
		try{
			cursor = db.rawQuery(strQueryCmd, null);
			if(cursor != null && cursor.moveToFirst()){
				jsonObjQueryResult = new JSONObject();
				do{
					for(int i = 0; i<cursor.getColumnCount(); i++){
						jsonObjQueryResult.put(cursor.getColumnName(i), cursor.getString(i));
					}
				}
				while(cursor.moveToNext());
			}
		}
		catch(Exception ex){
			throw new Exception("Fail to get all data from DB; Error Message: "+ex.getMessage());
		}
		finally{
			if(cursor != null){
				cursor.close();
			}
			if(db != null){
				db.close();
			}
		}
		
		return jsonObjQueryResult;
		
	}

}

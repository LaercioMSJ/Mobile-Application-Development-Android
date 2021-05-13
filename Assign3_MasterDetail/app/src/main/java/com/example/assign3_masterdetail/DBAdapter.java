package com.example.assign3_masterdetail;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.util.Log;

public class DBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_RATING ="rating";
    public static final String TAG = "DBAdapter";
    private static final String DATABASE_NAME = "MyDB";
    private static final String DATABASE_TABLE = "countries";
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_CREATE =
            "create table countries(_id integer primary key autoincrement,"
                    + "country text not null,rating text not null);";
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME,null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db)
        {
            try{
                db.execSQL(DATABASE_CREATE);
            }catch(SQLException e){
                e.printStackTrace();
            }
        }//end method onCreate

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG,"Upgrade database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }//end method onUpgrade
    }

    //open the database
    public void open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
    }

    //close the database
    public void close()
    {
        DBHelper.close();
    }

    //insert a country into the database
    public long insertCountry(String country,String rating)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_COUNTRY, country);
        initialValues.put(KEY_RATING, rating);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //delete a particular country
    public boolean deleteCountry(String countryName)
    {
        return db.delete(DATABASE_TABLE,KEY_COUNTRY + "=\"" + countryName + "\"",null) >0;
    }

    //retrieve all the countries
    public Cursor getAllCountry()
    {
        return db.query(DATABASE_TABLE, new String[]{KEY_ROWID,KEY_COUNTRY,
                KEY_RATING},null,null,null,null,null);
    }

    //retrieve a single country
    public Cursor getCountry(String countryName) throws SQLException
    {
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                KEY_COUNTRY,KEY_RATING},KEY_COUNTRY + "=\"" + countryName + "\"",null,null,null,null,null);
        if(mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //updates a country
    public boolean updateCountry(long rowId,String country,String rating)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_COUNTRY, country);
        contentValues.put(KEY_RATING, rating);
        return db.update(DATABASE_TABLE, contentValues, KEY_ROWID + "=" + rowId,null) >0;
    }
}//end class DBAdapter

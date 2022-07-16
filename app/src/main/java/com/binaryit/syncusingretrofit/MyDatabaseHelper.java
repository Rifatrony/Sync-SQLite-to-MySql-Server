package com.binaryit.syncusingretrofit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;

    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE = "CREATE TABLE "+DbContains.TABLE_NAME+"( "+DbContains.ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ""+DbContains.NAME+" VARCHAR(255), "+DbContains.NUMBER+" VARCHAR(255), "+DbContains.AGE+" VARCHAR(255)); ";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " +DbContains.TABLE_NAME;

    private static final String SELECT_ALL = "SELECT * FROM " + DbContains.TABLE_NAME;



    public MyDatabaseHelper(Context context) {
        super(context, DbContains.DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void saveToLocalDatabase(String name, String number, String age){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DbContains.NAME, name);
        contentValues.put(DbContains.NUMBER, number);
        contentValues.put(DbContains.AGE, age);

        long success = sqLiteDatabase.insert(DbContains.TABLE_NAME, null, contentValues);

        if (success == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Successfully Added", Toast.LENGTH_SHORT).show();
        }

    }

    public Cursor ReadDataFromLocalStorage(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(SELECT_ALL, null);
        return cursor;
    }

    public void deleteFromLocal(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(DbContains.TABLE_NAME, null, null);
    }

}

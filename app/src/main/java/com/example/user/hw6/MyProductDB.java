package com.example.user.hw6;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 2017/12/13.
 */

public class MyProductDB extends SQLiteOpenHelper {
    private static final String database = "myPDdata.db";
    private static final int version = 1;

    public MyProductDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }

    public MyProductDB(Context context){
        this(context,database,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE myTable(_id integer primary key autoincrement,"+"shop text no null,"+"title text no null,"+"price integer no null,"+"description text no null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXSIST myTable");
        onCreate(db);
    }
}

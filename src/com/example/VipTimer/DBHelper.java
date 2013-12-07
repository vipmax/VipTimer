package com.example.VipTimer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created with IntelliJ IDEA.
 * User: vipmax
 * Date: 29.11.13
 * Time: 19:36
 * To change this template use File | Settings | File Templates.
 */
public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        // конструктор суперкласса

        super(context, "myDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create table myWorkoutDate(id integer primary key autoincrement, date text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i2) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

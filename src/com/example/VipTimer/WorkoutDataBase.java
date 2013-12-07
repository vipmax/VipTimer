package com.example.VipTimer;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: vipmax
 * Date: 29.11.13
 * Time: 21:47
 * To change this template use File | Settings | File Templates.
 */
public class WorkoutDataBase extends MyActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workoutdate);


        LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout);

        LayoutInflater ltInflater = getLayoutInflater();

        DBHelper dbhelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        Cursor c = db.query("myWorkoutDate", null, null, null, null, null, null);
        if(c.moveToFirst()){
            Toast.makeText(this,"Ваши тренировки",Toast.LENGTH_SHORT).show();
        int n = c.getCount();

        if(c!=null && n >0 ){

        do {
            View item = ltInflater.inflate(R.layout.item, linLayout, false);
            TextView tvName = (TextView) item.findViewById(R.id.IDText);
            TextView tvPosition = (TextView) item.findViewById(R.id.DateText);
            tvName.setText("ID : " + c.getString(c.getColumnIndex("id")));
            tvPosition.setText(" DATE : " + c.getString(c.getColumnIndex("date")));
            item.getLayoutParams().width = 500;
            linLayout.addView(item);

        } while (c.moveToNext());
        }
      }
       else {
            Toast.makeText(this,"Нет тренировок",Toast.LENGTH_SHORT).show();
        }
    }
}
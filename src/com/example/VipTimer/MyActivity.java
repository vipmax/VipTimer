package com.example.VipTimer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import 	android.media.MediaPlayer;
import android.content.DialogInterface.OnClickListener;


public class MyActivity extends Activity implements View.OnClickListener {

    Timer myTimer;
    Button startButton;
    public  static TextView textViewTimer;
    int timeInSeconds = 00, timeInMinutes = 40;
    public  static Handler h;
    static boolean activityFirstIsRunning = true;
    MyTimer timer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        startButton = (Button) findViewById(R.id.startButton);
        textViewTimer = (TextView) findViewById(R.id.textView);
        startButton.setOnClickListener(this);
        h = new Handler();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startButton:
                if (startButton.getText().equals("Stop")) {
                    startButton.setText("Start");
                    timer.stop();
                }
                else if (startButton.getText().equals("Start")) {
                        startButton.setText("Stop");
                        timer = new MyTimer(0,40);
                        timer.start();
                     }
                break;
        }
    }

    public void deleteTimer(Timer myTimer){
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.relax);
        mediaPlayer.start();
        myTimer.cancel();
        Log.e("MyTag", "time is off");
        timeInMinutes=40;
        timeInSeconds=00;
        h.post(new Runnable() {
            @Override
            public void run() {
                startButton.setText("Start");
                textViewTimer.setText(String.valueOf(40) + ":" + String.format("%02d", 00));
            }
        });

    }

    private static long back_pressed;



    public void puorOffInDataBase()  {
        DBHelper dbhelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Date date = new Date(System.currentTimeMillis());
        String customerDate = (1900 + date.getYear()) + "-" + (1 + date.getMonth()) + "-" + date.getDate() + " " + String.format("%02d", date.getHours()) + ":" + String.format("%02d", date.getMinutes()) + ":" + String.format("%02d", date.getSeconds());
        cv.put("date",customerDate);
        db.insert("myWorkoutDate", null, cv);
        dbhelper.close();
    }

    public void logDB(){
        DBHelper dbhelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        Cursor c = db.query("myWorkoutDate", null, null, null, null, null, null);



        c.moveToFirst();

         do{
        Log.d("myTag", "ID : " + c.getString(c.getColumnIndex("id")) + " Date :  " + c.getString(c.getColumnIndex("date")));
        } while(c.moveToNext());

        dbhelper.close();



    }

    public Cursor getCursorDBIndex(int index){
        DBHelper dbhelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        Cursor c = db.query("myWorkoutDate", null, null, null, null, null, null);
          if(c.moveToFirst()){

            c.move(index);
            Log.d("myTag", "ID : " + c.getString(c.getColumnIndex("id")) + " Date :  " + c.getString(c.getColumnIndex("date")));
            return c;
          } else return null;
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub

        menu.add("Хронология тренировок");
        menu.add("Выход");


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if(item.getTitle().equals("Хронология тренировок")){
            Intent intent1 = new Intent(this, WorkoutDataBase.class);
            startActivity(intent1);
        }
        if (item.getTitle().equals("Выход")){
            Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onBackPressed() {

        if (activityFirstIsRunning==false){
            super.onBackPressed();
        }
       else {
            if(myTimer!=null)
            Toast.makeText(this,"Тренировка продолжается",Toast.LENGTH_SHORT).show();
            moveTaskToBack(true);
        }



    }

    @Override
    public void onStart() {
        super.onStart();
        activityFirstIsRunning = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        activityFirstIsRunning = false;
    }


}


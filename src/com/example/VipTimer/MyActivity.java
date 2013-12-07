package com.example.VipTimer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import 	android.media.MediaPlayer;
import android.content.DialogInterface.OnClickListener;


public class MyActivity extends Activity implements View.OnClickListener {

    Timer myTimer;
    Button startButton;
    TextView textViewTimer,textViewDate;
    int timeInSeconds = 00, timeInMinutes = 40;
    Handler h;
    static boolean activityFirstIsRunning = true;


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
                    showDialog(1);



                }
                else if (startButton.getText().equals("Start")) {
                    puorOffInDataBase();
                    logDB();
                    Toast toast = Toast.makeText(getApplicationContext(), "Have a nice workout!", Toast.LENGTH_SHORT);
                    toast.show();
                    startButton.setText("Stop");
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.comeon);
                    mediaPlayer.start();
                    mediaPlayer=null;
                    myTimer = new Timer();  //!!! The timer doesn't have any built-in methods for pausing. You can cancel the timer when you want to "pause" and make a new one when you want to "resume

                    myTimer.schedule(new TimerTask() {
                        public void run() {



                            h.post(new Runnable() {  // использую Handler, привязанный к UI-Thread
                                @Override
                                public void run() {
                                    timeInSeconds--;
                                    if (timeInSeconds < 0) {
                                        timeInMinutes--;
                                        timeInSeconds = 59;
                                    }

                                    if((timeInMinutes==35||timeInMinutes==30||timeInMinutes==25||timeInMinutes==20||timeInMinutes==15||timeInMinutes==10||timeInMinutes==5||timeInMinutes==4||timeInMinutes==3||timeInMinutes==2||timeInMinutes==1)&&timeInSeconds==1){
                                        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.speedup);
                                        mediaPlayer.start();//
                                    }
//
                                    if(timeInMinutes==-1 && timeInSeconds==59){
                                        deleteTimer(myTimer);
                                        h.post(new Runnable() {  // использую Handler, привязанный к UI-Thread
                                            @Override
                                            public void run() {
                                                Toast toaster = Toast.makeText(getApplicationContext(), "Workout completed!", Toast.LENGTH_SHORT);
                                                toaster.show();
                                            }
                                        });
                                    }


                                    textViewTimer.setText(String.valueOf(timeInMinutes) + ":" + String.format("%02d", timeInSeconds));
                                }
                            });

                        }
                    }, 0, 1000);


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
        db.insert("myWorkoutDate", null,cv);
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

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 1) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            // заголовок
            adb.setTitle("Выход");
            // сообщение
            adb.setMessage("Завершить тренировку?");
            // иконка
            adb.setIcon(android.R.drawable.ic_dialog_info);
            // кнопка положительного ответа
            adb.setPositiveButton("Да", myClickListener);
            // кнопка отрицательного ответа
            adb.setNegativeButton("Нет", myClickListener);


            return adb.create();
        }
        return super.onCreateDialog(id);
    }

    OnClickListener myClickListener = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                // положительная кнопка
                case Dialog.BUTTON_POSITIVE:
                    startButton.setText("Start");
                    deleteTimer(myTimer);

                    break;
                // негаитвная кнопка
                case Dialog.BUTTON_NEGATIVE:

                    break;

            }
        }
    };

}


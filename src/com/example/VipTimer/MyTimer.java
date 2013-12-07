package com.example.VipTimer;

import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: vipmax
 * Date: 07.12.13
 * Time: 18:09
 * To change this template use File | Settings | File Templates.
 */
public class MyTimer  {


    Timer timer;
    int min;
    int sec;


    public MyTimer(int sec, int min) {
        this.sec = sec;
        this.min = min;
        timer =null;
    }

    public void start() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                sec--;
                if (sec < 0) {
                    sec = 59;
                    min--;
                }
                if (min < 0) {
                    timer.cancel();

                }
                MyActivity.h.post(new Runnable() {
                    @Override
                    public void run() {
                        MyActivity.textViewTimer.setText(String.valueOf(min) + ":" + String.format("%02d", sec));
                    }
                });


            }
        }, 0, 1000);


    }

    public void stop(){
        timer.cancel();
    }




}


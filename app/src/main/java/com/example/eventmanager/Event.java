package com.example.eventmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static com.example.eventmanager.MainActivity.getAppContext;

public class Event {

    private  String id;
    private String name;
    private String note;
    private String date_time;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }

    public String getDate_Time() {
        return date_time;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setDate_Time(String date_time) {
        this.date_time = date_time;
    }


    public Event(){
    }

    public Event(String id,String name, int day, int month, int year, int hour, int min) {

        this.id = id;
        this.name = name;
        /*Calendar calendar = Calendar.getInstance();
        calendar.set(year,month-1,day,hour,min,0);
        this.date = calendar.getTime();*/
        String new_date_time=(year+"-"+month+"-"+day+" "+hour+":"+min);
        this.date_time=new_date_time;
        Log.i("DATE",this.date_time);
        //createAlarm(calendar,name);
    }

    public Event(String id,String name, int day, int month, int year, int hour, int min, String note) {

        this.id=id;
        this.name = name;
        this.note = note;
        /*Calendar calendar = Calendar.getInstance();
        calendar.set(year,month-1,day,hour,min,0);*/
        String new_date_time=(year+"-"+month+"-"+day+" "+hour+":"+min);
        this.date_time=new_date_time;
        Log.i("DATE",this.date_time);
        //createAlarm(calendar,name);
    }

    /*public void createAlarm(Calendar calendar,String event){
        Intent intent = new Intent(getAppContext(),Alarm.class);
        intent.putExtra("Event",event);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getAppContext(),
                0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getAppContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
    }*/


}
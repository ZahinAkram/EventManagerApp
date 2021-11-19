package com.example.eventmanager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class EventCreateActivity extends AppCompatActivity {

    EditText eventName;
    EditText eventNote;
    Button timePickerDialogButton;
    Button datePickerDialogButton;
    Button mFirebasebtn;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        eventName = (EditText) findViewById(R.id.eventName);
        eventNote = (EditText) findViewById(R.id.eventNote);
        mFirebasebtn = (Button) findViewById(R.id.firebase_btn);
        timePickerDialogButton = (Button)findViewById(R.id.timeButton);
        datePickerDialogButton = (Button)findViewById(R.id.dateButton);
        mFirebasebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEvent();
            }
        });
        Calendar now = Calendar.getInstance();
        int hour = now.get(java.util.Calendar.HOUR_OF_DAY);
        int minute = now.get(java.util.Calendar.MINUTE);
        int year = now.get(java.util.Calendar.YEAR);
        int month = now.get(java.util.Calendar.MONTH);
        int day = now.get(java.util.Calendar.DAY_OF_MONTH);
        timePickerDialogButton.setText(hour+":"+minute);
        datePickerDialogButton.setText(day+"/"+month+"/"+year);
        this.showDatePickerDialog();
        this.showTimePickerDialog();


    }

    private void addEvent(){

        String name = eventName.getText().toString().trim();
        String date = datePickerDialogButton.getText().toString().trim();
        String time = timePickerDialogButton.getText().toString().trim();
        String note = eventNote.getText().toString().trim();

        String[] arr = date.split("/");

        int day = Integer.parseInt(arr[0]);
        int month = Integer.parseInt(arr[1]);
        int year = Integer.parseInt(arr[2]);

        arr = time.split(":");

        int hour = Integer.parseInt(arr[0]);
        int min = Integer.parseInt(arr[1]);



        if(!TextUtils.isEmpty(name)){

            String id = mDatabase.push().getKey();

            Event event;
            if (note.isEmpty()) {
                event = new Event(id,name, day, month, year, hour, min);

            } else {
                event = new Event(id,name, day, month, year, hour, min, note);
            }

            mDatabase.child(id).setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(EventCreateActivity.this, "Event Created.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(EventCreateActivity.this, "Error.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(this,"You should enter an event name", Toast.LENGTH_LONG).show();
        }
    }

    public void toEventListActivity(View view){
        Intent intent = new Intent(getApplicationContext(),EventListActivity.class);
        startActivity(intent);

    }

    private void showTimePickerDialog()
    {
        timePickerDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new OnTimeSetListener instance. This listener will be invoked when user click ok button in TimePickerDialog.
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        StringBuffer strBuf = new StringBuffer();
                        strBuf.append(hour);
                        strBuf.append(":");
                        strBuf.append(minute);
                        timePickerDialogButton.setText(strBuf.toString());
                    }
                };

                // Whether show time in 24 hour format or not.
                boolean is24Hour = false;

                String time = timePickerDialogButton.getText().toString().trim();
                String[] arr = time.split(":");
                int hour = Integer.parseInt(arr[0]);
                int minute = Integer.parseInt(arr[1]);
                TimePickerDialog timePickerDialog = new TimePickerDialog(EventCreateActivity.this, android.R.style.Theme_Holo_Light_Dialog, onTimeSetListener, hour, minute, is24Hour);
                timePickerDialog.setTitle("Please select time.");
                timePickerDialog.show();
            }
        });
    }

    private void showDatePickerDialog()
    {
        datePickerDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new OnDateSetListener instance. This listener will be invoked when user click ok button in DatePickerDialog.
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        StringBuffer strBuf = new StringBuffer();
                        strBuf.append(dayOfMonth);
                        strBuf.append("/");
                        strBuf.append(month+1);
                        strBuf.append("/");
                        strBuf.append(year);

                        datePickerDialogButton.setText(strBuf.toString());
                    }
                };

                String date = datePickerDialogButton.getText().toString().trim();
                String[] arr = date.split("/");
                int day = Integer.parseInt(arr[0]);
                int month = Integer.parseInt(arr[1]);
                int year = Integer.parseInt(arr[2]);

                // Create the new DatePickerDialog instance.
                DatePickerDialog datePickerDialog = new DatePickerDialog(EventCreateActivity.this, onDateSetListener, year, month, day);
                // Set dialog icon and title.
                datePickerDialog.setTitle("Please select date");

                // Popup the dialog.
                datePickerDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuLogout:

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));

                break;
        }

        return true;
    }

}

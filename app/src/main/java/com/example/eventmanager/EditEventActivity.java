package com.example.eventmanager;

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
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;


public class EditEventActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private ListView mEventList;
    private EventAdapter mAdapter;

    private ArrayList<Event> eventsList = new ArrayList<>();
    private ArrayList<String> mKeys = new ArrayList<>();

    //EditText editEventDate;
    //EditText editEventTime;
    EditText editEventName;
    EditText editEventNote;
    Button timePickerDialogButton;
    Button datePickerDialogButton;

    private Button editButton;
    private Button deleteButton;

    private String eventID;
    private String eventDate; //Za-to get date


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        eventID = intent.getStringExtra("eventID");

        //Extra for showing event date
       /* eventDate = intent.getStringExtra("eventDate");

        Log.i("eventDate: ",eventDate);

        String[] arr = eventDate.split("/");

        int day1 = Integer.parseInt(arr[0]);
        int month1 = Integer.parseInt(arr[1]);
        int year1 = Integer.parseInt(arr[2]);

        arr = eventDate.split(":");

        int hour1 = Integer.parseInt(arr[3]);
        int minute1 = Integer.parseInt(arr[4]);*/

        //Extra Ends here so just delete upto here

        mDatabase  = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        //editEventDate = (EditText) findViewById(R.id.editDate);
        //editEventTime = (EditText) findViewById(R.id.editTime);
        editEventName = (EditText) findViewById(R.id.editName);
        editEventNote = (EditText) findViewById(R.id.editNote);
        timePickerDialogButton = (Button)findViewById(R.id.timeButton);
        datePickerDialogButton = (Button)findViewById(R.id.dateButton);
        editButton = (Button) findViewById(R.id.editButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);


        editEventName.setText(intent.getStringExtra("eventName"));
        editEventNote.setText(intent.getStringExtra("eventNote"));
        //editEventDate.setText(intent.getStringExtra("eventDate"));
        //editEventTime.setText(intent.getStringExtra("eventDate"));
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


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editEvent(eventID);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent(eventID);
            }
        });

    }

    private void editEvent(String eventID){

        String name = editEventName.getText().toString().trim();
        //String date = editEventDate.getText().toString().trim();
        //String time = editEventTime.getText().toString().trim();
        String date = datePickerDialogButton.getText().toString().trim();
        String time = timePickerDialogButton.getText().toString().trim();
        String note = editEventNote.getText().toString().trim();

        String[] arr = date.split("/");

        int day = Integer.parseInt(arr[0]);
        int month = Integer.parseInt(arr[1]);
        int year = Integer.parseInt(arr[2]);

        arr = time.split(":");

        int hour = Integer.parseInt(arr[0]);
        int min = Integer.parseInt(arr[1]);



        if(!TextUtils.isEmpty(name)){

            Event event;
            if (note.isEmpty()) {
                event = new Event(eventID,name, day, month, year, hour, min);

            } else {
                event = new Event(eventID,name, day, month, year, hour, min, note);
            }

            mDatabase.child(eventID).setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(EditEventActivity.this, "Event Edited.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),EventListActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(EditEventActivity.this, "Error.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(this,"You should enter an event name", Toast.LENGTH_LONG).show();
        }

    }

    private void deleteEvent(String eventID){

        mDatabase.child(eventID).removeValue();
        Toast.makeText(EditEventActivity.this,"Event Deleted", Toast.LENGTH_LONG).show();
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditEventActivity.this, android.R.style.Theme_Holo_Light_Dialog, onTimeSetListener, hour, minute, is24Hour);
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditEventActivity.this, onDateSetListener, year, month, day);
                // Set dialog icon and title.
                datePickerDialog.setTitle("Please select date");

                // Popup the dialog.
                datePickerDialog.show();
            }
        });
    }

}


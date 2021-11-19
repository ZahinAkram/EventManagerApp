package com.example.eventmanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.security.Key;
import java.security.Timestamp;
import java.util.Calendar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class TodayEventActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private Query query;
    private Query query2;
    private ListView mEventList;
    private EventAdapter mAdapter;
    ProgressBar progressBar;
    TextView textView;

    private ArrayList<Event> eventsList;
    private ArrayList<String> mKeys = new ArrayList<>();

    public void toEventCreateActivity(View view){
        Intent intent = new Intent(getApplicationContext(),EventCreateActivity.class);
        startActivity(intent);
    }

    public void toEventListActivity(View view){
        Intent intent = new Intent(getApplicationContext(),EventListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_event);

        mDatabase  = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        Calendar now = Calendar.getInstance();
        //int hour = now.get(Calendar.HOUR_OF_DAY);
        //int min = now.get(Calendar.MINUTE);
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int day = now.get(Calendar.DAY_OF_MONTH);

        query=mDatabase.orderByChild("date_Time").startAt(year+"-"+month+"-"+day+" "+0+":"+0).endAt(year+"-"+month+"-"+day+" "+23+":"+59);


        textView = (TextView) findViewById(R.id.textView2);
        mEventList = (ListView) findViewById(R.id.listViewEvents);
        eventsList = new ArrayList<>();

        mEventList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = eventsList.get(position);
                Intent intent = new Intent(getApplicationContext(), EditEventActivity.class);

                intent.putExtra("eventID",event.getId());
                intent.putExtra("eventName",event.getName());
                intent.putExtra("eventDate",event.getDate_Time());
                intent.putExtra("eventNote",event.getNote());
                startActivity(intent);

                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                eventsList.clear();
                for(DataSnapshot eventSnapshot: dataSnapshot.getChildren()){

                    Event event = eventSnapshot.getValue(Event.class);
                    eventsList.add(event);
                }
                mAdapter = new EventAdapter(TodayEventActivity.this,eventsList);
                mEventList.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

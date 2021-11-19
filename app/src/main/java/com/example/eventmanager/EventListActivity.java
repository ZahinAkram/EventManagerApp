package com.example.eventmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;



public class EventListActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ListView mEventList;
    private EventAdapter mAdapter;

    private ArrayList<Event> eventsList;
    private ArrayList<String> mKeys = new ArrayList<>();

    public void toEventCreateActivity(View view){
        Intent intent = new Intent(getApplicationContext(),EventCreateActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase  = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mEventList = (ListView) findViewById(R.id.listViewEvents);
        eventsList = new ArrayList<>();

        mEventList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                /*Event event = eventsList.get(position);
                Intent intent = new Intent(getApplicationContext(), EditEventActivity.class);

                intent.putExtra("eventID",event.getId());
                intent.putExtra("eventName",event.getName());
                intent.putExtra("eventDate",event.getDate());
                intent.putExtra("eventNote",event.getNote());
                startActivity(intent);*/

                new AlertDialog.Builder(EventListActivity.this)
                        .setTitle("What do you want to do?")
                        .setMessage("Do you want to Delete or Event this Event?")
                        .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        Event event = eventsList.get(position);
                                        Intent intent = new Intent(getApplicationContext(), EditEventActivity.class);

                                        intent.putExtra("eventID",event.getId());
                                        intent.putExtra("eventName",event.getName());
                                        intent.putExtra("eventDate",event.getDate_Time());
                                        intent.putExtra("eventNote",event.getNote());
                                        startActivity(intent);

                                    }
                                }
                        )
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        Event event = eventsList.get(position);
                                        mDatabase.child(event.getId()).removeValue();
                                        Toast.makeText(EventListActivity.this,"Event Deleted", Toast.LENGTH_LONG).show();

                                    }
                                }
                        )
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                eventsList.clear();
                for(DataSnapshot eventSnapshot: dataSnapshot.getChildren()){

                    Event event = eventSnapshot.getValue(Event.class);
                    eventsList.add(event);
                }
                mAdapter = new EventAdapter(EventListActivity.this,eventsList);
                mEventList.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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


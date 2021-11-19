package com.example.eventmanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


public class EventAdapter extends ArrayAdapter<Event> {

    private Activity context;
    private List<Event> eventList = new ArrayList<>();

    public EventAdapter(Activity context, ArrayList<Event> list) {
        super(context, R.layout.event_list_item , list);
        this.context = context;
        this.eventList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem= inflater.inflate(R.layout.event_list_item,null,true);

        TextView name = (TextView) listViewItem.findViewById(R.id.textView_name);
        TextView date = (TextView) listViewItem.findViewById(R.id.textView_date);
        TextView note = (TextView) listViewItem.findViewById(R.id.textView_note);

        Event currentEvent = eventList.get(position);

        name.setText(currentEvent.getName());
        //date.setText(currentEvent.getDate().toString());
        note.setText(currentEvent.getNote());
        date.setText(currentEvent.getDate_Time());

        //ImageView image = (ImageView)listItem.findViewById(R.id.imageView_poster);
        //image.setImageResource(currentMovie.getmImageDrawable());

        return listViewItem;
    }
}


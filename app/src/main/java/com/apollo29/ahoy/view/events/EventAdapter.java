package com.apollo29.ahoy.view.events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo29.ahoy.R;
import com.apollo29.ahoy.comm.event.Event;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<Event> events;

    public EventAdapter(List<Event> events) {
        this.events = events;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    @NonNull
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View cardView = inflater.inflate(R.layout.events_item_view, parent, false);

        // Return a new holder instance
        return new EventAdapter.ViewHolder(cardView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(EventAdapter.ViewHolder viewHolder, int position) {
        Event event = events.get(position);

        viewHolder.eventTitle.setText(event.title);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return this.events.size();
    }

    public void updateEvents(List<Event> events){
        this.events = events;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView eventTitle;


        public ViewHolder(View itemView) {
            super(itemView);

            this.eventTitle = itemView.findViewById(R.id.event_title);
        }
    }
}

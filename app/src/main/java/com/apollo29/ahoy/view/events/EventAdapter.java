package com.apollo29.ahoy.view.events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo29.ahoy.R;
import com.apollo29.ahoy.comm.event.Event;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<Event> events;
    private final OnItemClickListener clickListener;
    private final OnDownloadClickListener downloadClickListener;

    public EventAdapter(List<Event> events, OnItemClickListener clickListener, OnDownloadClickListener downloadClickListener) {
        this.events = events;
        this.clickListener = clickListener;
        this.downloadClickListener = downloadClickListener;
    }

    @Override
    @NonNull
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View cardView = inflater.inflate(R.layout.events_item_view, parent, false);
        return new EventAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(EventAdapter.ViewHolder viewHolder, int position) {
        Event event = events.get(position);

        viewHolder.eventTitle.setText(event.title);
        viewHolder.eventDate.setText(EventUtil.getDefaultDateString(event));
        viewHolder.download.setVisibility(View.GONE);

        if (EventUtil.isFuture(event)){
            viewHolder.eventIcon.setImageResource(R.drawable.ic_event_future);
        }
        else if (EventUtil.isCurrent(event)){
            viewHolder.eventIcon.setImageResource(R.drawable.ic_event_current);
            viewHolder.download.setVisibility(View.VISIBLE);
        }
        else if (EventUtil.isDoneOrCurrent(event)){
            viewHolder.eventIcon.setImageResource(R.drawable.ic_event_done);
            viewHolder.download.setVisibility(View.VISIBLE);
        }
        else if (EventUtil.isArchived(event)){
            viewHolder.eventIcon.setImageResource(R.drawable.ic_event_archive);
        }
    }

    @Override
    public int getItemCount() {
        return this.events.size();
    }

    public void updateEvents(List<Event> events){
        this.events = events;
        notifyDataSetChanged();
    }

    @Nullable
    public Event getItem(int position){
        return events.get(position);
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    public interface OnDownloadClickListener {
        void onClick(View view, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView eventTitle;
        public TextView eventDate;
        public ImageView eventIcon;
        public View download;
        public View clickableArea;

        public ViewHolder(View itemView) {
            super(itemView);

            this.eventTitle = itemView.findViewById(R.id.event_title);
            this.eventDate = itemView.findViewById(R.id.event_date);
            this.eventIcon = itemView.findViewById(R.id.event_icon);
            this.download = itemView.findViewById(R.id.download_clickable_area);
            this.download.setOnClickListener(view -> downloadClickListener.onClick(view, getAdapterPosition()));
            this.clickableArea = itemView.findViewById(R.id.list_item_clickable_area);
            this.clickableArea.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition());
        }
    }
}

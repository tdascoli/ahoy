package com.apollo29.ahoy.view.events.guests;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo29.ahoy.R;
import com.apollo29.ahoy.comm.queue.Queue;

import java.util.List;

public class GuestAdapter extends RecyclerView.Adapter<GuestAdapter.ViewHolder> {

    private List<Queue> guests;

    public GuestAdapter(List<Queue> guests) {
        this.guests = guests;
    }

    @Override
    @NonNull
    public GuestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View cardView = inflater.inflate(R.layout.guests_item_view, parent, false);
        return new GuestAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(GuestAdapter.ViewHolder viewHolder, int position) {
        Queue guest = guests.get(position);

        String guestName = viewHolder.itemView.getContext().getString(R.string.events_guests_guest_name, guest.firstname, guest.lastname);
        viewHolder.guestName.setText(guestName);
        viewHolder.guestEmail.setText(guest.email);
    }

    @Override
    public int getItemCount() {
        return this.guests.size();
    }

    public void update(List<Queue> guests){
        this.guests = guests;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView guestName;
        public TextView guestEmail;

        public ViewHolder(View itemView) {
            super(itemView);

            this.guestName = itemView.findViewById(R.id.guest_name);
            this.guestEmail = itemView.findViewById(R.id.guest_email);
        }
    }
}

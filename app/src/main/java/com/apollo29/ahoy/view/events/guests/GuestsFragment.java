package com.apollo29.ahoy.view.events.guests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.apollo29.ahoy.R;
import com.apollo29.ahoy.view.events.EventUtil;
import com.apollo29.ahoy.view.events.EventsViewModel;
import com.apollo29.ahoy.view.events.register.RegisterManuallyFragmentArgs;

public class GuestsFragment extends Fragment {
    @Nullable
    private GuestAdapter adapter;
    private EventsViewModel viewModel;
    private int eventId = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);
        return inflater.inflate(R.layout.guests_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getArguments()!=null){
            eventId = RegisterManuallyFragmentArgs.fromBundle(getArguments()).getEventId();
        }

        TextView title = view.findViewById(R.id.guests_title);
        TextView subtitle = view.findViewById(R.id.guests_subtitle);
        viewModel.loadEvent(eventId).observe(getViewLifecycleOwner(), event -> {
            if (!event.isEmpty()){
                title.setText(getString(R.string.events_guests_title, event.title));
                subtitle.setText(getString(R.string.events_guests_subtitle, EventUtil.getDefaultDateString(event)));
            }
        });

        RecyclerView list =  view.findViewById(R.id.guests_list);
        viewModel.guests(eventId).observe(getViewLifecycleOwner(), guests -> {
            if (adapter==null) {
                adapter = new GuestAdapter(guests);
                list.setAdapter(adapter);
            }
            else {
                adapter.update(guests);
            }
        });

        SwipeRefreshLayout refreshView =  view.findViewById(R.id.refresh_view);
        refreshView.setOnRefreshListener(() ->
                viewModel.refreshData(eventId).observe(getViewLifecycleOwner(), refreshView::setRefreshing));
    }
}

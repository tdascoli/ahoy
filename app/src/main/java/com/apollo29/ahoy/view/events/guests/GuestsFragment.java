package com.apollo29.ahoy.view.events.guests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo29.ahoy.R;
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
        return inflater.inflate(R.layout.events_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getArguments()!=null){
            eventId = RegisterManuallyFragmentArgs.fromBundle(getArguments()).getEventId();
        }

        RecyclerView list =  view.findViewById(R.id.events_list);
        viewModel.guests(eventId).observe(getViewLifecycleOwner(), guests -> {
            if (adapter==null) {
                adapter = new GuestAdapter(guests);
                list.setAdapter(adapter);
            }
            else {
                adapter.update(guests);
            }
        });
    }
}

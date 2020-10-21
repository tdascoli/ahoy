package com.apollo29.ahoy.view.events;

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

public class EventsFragment extends Fragment {
    @Nullable
    private EventAdapter adapter;
    private EventsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);
        return inflater.inflate(R.layout.events_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView eventList =  view.findViewById(R.id.events_list);
        viewModel.events().observe(getViewLifecycleOwner(), events -> {
            if (adapter==null) {
                adapter = new EventAdapter(events);
                eventList.setAdapter(adapter);
            }
            else {
                adapter.updateEvents(events);
            }
        });
    }
}
